import { Channel, invoke } from '@tauri-apps/api/core'


export type NcnnRecognizeEvent={
  text:string
}
export async function NcnnInitRecognizer()
{
  await invoke("plugin:sherpa-ncnn|ncnn_init_recognizer");
}
export async function NcnnInitMicrophone() {
  await invoke("plugin:sherpa-ncnn|ncnn_init_microphone");
}
export async function NcnnStartRecognize(onTextReceived:(response:NcnnRecognizeEvent)=>void) {
  const ncnnRecognizeChannel=new Channel<NcnnRecognizeEvent>();
  ncnnRecognizeChannel.onmessage=onTextReceived;
  await invoke("plugin:sherpa-ncnn|ncnn_start_recognize",{channel:ncnnRecognizeChannel})
}

export async function NcnnStopRecognize() {
  await invoke("plugin:sherpa-ncnn|ncnn_stop_recognize");
}