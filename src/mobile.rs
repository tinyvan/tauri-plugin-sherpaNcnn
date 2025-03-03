use serde::de::DeserializeOwned;

use tauri::{
  ipc::Channel, plugin::{mobile::PluginInvokeError, PluginApi, PluginHandle}, AppHandle, Runtime
};
use crate::models::*;
use serde::{Serialize, Deserialize};


#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_sherpa_ncnn);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
  _app: &AppHandle<R>,
  api: PluginApi<R, C>,
) -> crate::Result<SherpaNcnn<R>> {
  #[cfg(target_os = "android")]
  let handle = api.register_android_plugin("com.android.tinywangsherpaNcnn", "NcnnClientPlugin")?;
  #[cfg(target_os = "ios")]
  let handle = api.register_ios_plugin(init_plugin_sherpa_ncnn)?;
  Ok(SherpaNcnn(handle))
}

/// Access to the sherpa-ncnn APIs.
pub struct SherpaNcnn<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> SherpaNcnn<R> {
  pub async fn ncnn_init_recognizer(&self)->Result<(),PluginInvokeError>
  {
    self.0.run_mobile_plugin("NcnnInitRecognizer", EmptyRequest)
  }
  pub async fn ncnn_init_micrphone(&self)->Result<(),PluginInvokeError>
  {
    self.0.run_mobile_plugin("NcnnInitMicrophone",EmptyRequest)
  }
  pub async fn ncnn_start_recognize(&self,channel:Channel)->Result<(),PluginInvokeError>
  {
    self.0.run_mobile_plugin("NcnnStartRecognize",NcnnStartRecognizePayload{channel})
  }
  pub async fn ncnn_stop_recognize(&self)->Result<(),PluginInvokeError>
  {
    self.0.run_mobile_plugin("NcnnStopRecognize",EmptyRequest)
  }
}
