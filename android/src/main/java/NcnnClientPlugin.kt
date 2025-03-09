package com.android.tinywangsherpaNcnn

import android.Manifest
import android.app.Activity
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.Permission
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Channel
import app.tauri.plugin.Invoke
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin

@InvokeArg
class NcnnInitRecognizerArg{
    lateinit var modelPath:String
}

@InvokeArg
class NccnStartRecognizeArg{
    lateinit var channel: Channel
}

@TauriPlugin(
    permissions = [
        Permission(strings = [Manifest.permission.RECORD_AUDIO])
    ]
)
class NcnnClientPlugin(private val activity: Activity):Plugin(activity) {
    private val ncnnClient = NcnnClient(activity)

    @Command
    fun ncnnInitRecognizer(invoke: Invoke){
        val args=invoke.parseArgs(NcnnInitRecognizerArg::class.java)
        ncnnClient.initRecognizer(args.modelPath)
        invoke.resolve()
    }
    @Command
    fun ncnnInitRecognizerFromAsset(invoke: Invoke){
        val args=invoke.parseArgs(NcnnInitRecognizerArg::class.java)
        ncnnClient.initRecognizerFromAsset(args.modelPath)
        invoke.resolve()
    }
    @Command
    fun ncnnInitMicrophone(invoke: Invoke){
        ncnnClient.initMicrophone()
        invoke.resolve()
    }
    @Command
    fun ncnnStartRecognize(invoke: Invoke){
        val args=invoke.parseArgs(NccnStartRecognizeArg::class.java)
        ncnnClient.start { text ->
            val jsObject = JSObject()
            jsObject.put("text", text)
            args.channel.send(jsObject)
        }
        invoke.resolve()
    }
    @Command
    fun ncnnStopRecognize(invoke: Invoke){
        ncnnClient.stop()
        invoke.resolve()
    }
}