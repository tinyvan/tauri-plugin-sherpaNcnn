//Copy from sherpa-ncnn
package com.android.tinywangsherpaNcnn

import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlin.concurrent.thread

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val TAG = "NcnnClient"
class NcnnClient(private val activity: Activity) {
    private val audioPermissions = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
    )
    private var useGPU:Boolean =true
    private lateinit var recognizer: SherpaNcnn
    private var audioRecorder: AudioRecord? = null
    private var recordingThread: Thread? = null
    private var isMicrophoneInitialized:Boolean = false
    private val audioSource = android.media.MediaRecorder.AudioSource.MIC
    private val sampleRateInHz = 16000
    private val channelConfig = android.media.AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = android.media.AudioFormat.ENCODING_PCM_16BIT
    @Volatile
    private var isRecording = false
    init {
        System.loadLibrary("sherpa-ncnn-jni")

    }
    fun initRecognizer(modelPath:String){
        val featConfig= getFeatureExtractorConfig(
            sampleRate = 16000.0f,
            featureDim = 80
        )
        var modelConfig= getModelConfig(useGPU = useGPU,modelDir=modelPath)
        val decoderConfig = getDecoderConfig(method = "greedy_search", numActivePaths = 4)
        val config = RecognizerConfig(
            featConfig = featConfig,
            modelConfig = modelConfig,
            decoderConfig = decoderConfig,
            enableEndpoint = true,
            rule1MinTrailingSilence = 2.0f,
            rule2MinTrailingSilence = 0.8f,
            rule3MinUtteranceLength = 20.0f,
        )
        recognizer = SherpaNcnn(
            assetManager = null,
            config = config,
        )
    }
    fun initRecognizerFromAsset(modelPath:String){
        val featConfig= getFeatureExtractorConfig(
            sampleRate = 16000.0f,
            featureDim = 80
        )
        var modelConfig= getModelConfig(useGPU = useGPU,modelDir=modelPath)
        val decoderConfig = getDecoderConfig(method = "greedy_search", numActivePaths = 4)
        val config = RecognizerConfig(
            featConfig = featConfig,
            modelConfig = modelConfig,
            decoderConfig = decoderConfig,
            enableEndpoint = true,
            rule1MinTrailingSilence = 2.0f,
            rule2MinTrailingSilence = 0.8f,
            rule3MinUtteranceLength = 20.0f,
        )
        recognizer = SherpaNcnn(
            assetManager = activity.assets,
            config = config,
        )
    }
    fun initMicrophone():Boolean{
        if(ActivityCompat.checkSelfPermission(activity,android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,audioPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
            return false
        }
        val numBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)
        Log.i(
            TAG,
            "buffer size in milliseconds: ${numBytes * 1000.0f / sampleRateInHz}"
        )

        audioRecorder = AudioRecord(
            audioSource,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            numBytes * 2 // a sample has two bytes as we are using 16-bit PCM
        )

        isMicrophoneInitialized=true
        return true
    }
    fun processSamples(callback: (text: String) -> Unit)
    {
        Log.i(TAG, "processing samples")
        val interval=0.1
        val bufferSize=(interval*sampleRateInHz).toInt()
        val buffer=ShortArray(bufferSize)
        while(isRecording)
        {
            val audioRecord=audioRecorder?.read(buffer,0,bufferSize)
            if(audioRecord!=null&&audioRecord>0)
            {

                val samples=FloatArray(audioRecord){buffer[it]/32768.0f}
                recognizer.acceptSamples(samples)
                while(recognizer.isReady())
                {
                    recognizer.decode()
                }
                val text=recognizer.text
                if(recognizer.isEndpoint())
                {
                    callback(text+"\n")
                    recognizer.reset()
                }
                else if(text.isNotBlank())
                {
                    callback(text)
                }
            }
        }
    }
    fun start(callback: (text: String) -> Unit)
    {
        if(!isRecording)
        {
            if(!isMicrophoneInitialized)
            {
                if(!initMicrophone())
                {
                    Log.e(TAG, "NCNNClient.start: microphone cannot be initialized.", )
                }
            }
            Log.i(TAG, "state: ${audioRecorder?.state}")
            audioRecorder!!.startRecording()
            isRecording=true
            recordingThread=thread(true){

                processSamples(callback)
            }
        }
    }
    fun stop()
    {
        isRecording=false
        audioRecorder.let {
            it?.stop()
            it?.release()
        }
    }
}