<script setup lang="ts">
import { ref } from "vue";
import { invoke } from "@tauri-apps/api/core";
import { ncnnInitMicrophone,ncnnInitRecognizer,ncnnInitRecognizerFromAsset,ncnnStartRecognize,ncnnStopRecognize } from "tauri-plugin-sherpa-ncnn-api";
const init=()=>{
  ncnnInitMicrophone().then(()=>{
    console.log("MicroSuccess")
  })
  ncnnInitRecognizerFromAsset("sherpa-ncnn-streaming-zipformer-bilingual-zh-en-2023-02-13").then(()=>{
    console.log("RecogInitSuncess")
  })
}
const begin=()=>{
  ncnnStartRecognize((resp)=>{
    console.log("Rsp.",resp.text)
  }).then(()=>{
    console.log("RecogStartSuccess")
  })
}
const stop=()=>{
  ncnnStopRecognize().then(()=>{
    console.log("RecogStopSuccess")
  })
}

</script>


<template>
  <main class="container">
    <button @click="init">
      Init
    </button>
    <button @click="begin">
      Begin
    </button>
    <button @click="stop">
      Stop
    </button>
    
    </main>
</template>

