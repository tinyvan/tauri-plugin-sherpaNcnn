import { Channel, invoke } from '@tauri-apps/api/core'


export type NcnnRecognizeEvent={
  text:string
}
export async function ncnnInitRecognizer(modelPath:string)
{
  await invoke("plugin:sherpa-ncnn|ncnn_init_recognizer",{modelPath:modelPath});
}
export async function ncnnInitRecognizerFromAsset(modelPath:string) {
  await invoke("plugin:sherpa-ncnn|ncnn_init_recognizer_from_asset",{modelPath:modelPath});
}

export async function ncnnInitMicrophone() {
  await invoke("plugin:sherpa-ncnn|ncnn_init_microphone");
}
export async function ncnnStartRecognize(onTextReceived:(response:NcnnRecognizeEvent)=>void) {
  const ncnnRecognizeChannel=new Channel<NcnnRecognizeEvent>();
  ncnnRecognizeChannel.onmessage=onTextReceived;
  await invoke("plugin:sherpa-ncnn|ncnn_start_recognize",{channel:ncnnRecognizeChannel})
}

export async function ncnnStopRecognize() {
  await invoke("plugin:sherpa-ncnn|ncnn_stop_recognize");
}