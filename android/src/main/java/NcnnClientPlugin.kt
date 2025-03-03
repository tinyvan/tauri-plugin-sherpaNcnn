package com.android.tinywangsherpaNcnn

import android.Manifest
import android.app.Activity
import android.util.Log
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.Permission
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Channel
import app.tauri.plugin.Invoke
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin

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
    private val NcnnClient = NcnnClient(activity)
    @Command
    fun NcnnInitRecognizer(invoke: Invoke){
        val assetManager = activity.assets
        val filelist=assetManager.list("")
        for (file in filelist!!){
            Log.i("test", "NcnnInitRecognizer: $file")
        }
        NcnnClient.initRecognizer()
        invoke.resolve()
    }
    @Command
    fun NcnnInitMicrophone(invoke: Invoke){
        NcnnClient.initMicrophone()
        invoke.resolve()
    }
    @Command
    fun NcnnStartRecognize(invoke: Invoke){
        val args=invoke.parseArgs(NccnStartRecognizeArg::class.java)
        NcnnClient.start { text ->
            val jsObject = JSObject()
            jsObject.put("text", text)
            args.channel.send(jsObject)
        }
        invoke.resolve()
    }
    @Command
    fun NcnnStopRecognize(invoke: Invoke){
        NcnnClient.stop()
        invoke.resolve()
    }
}