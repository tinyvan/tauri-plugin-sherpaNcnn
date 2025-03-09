use serde::{Deserialize, Serialize};
use tauri::ipc::Channel;


#[derive(Serialize)]
#[serde(rename_all="camelCase")]
pub struct NcnnStartRecognizePayload{
  pub channel:Channel
}
#[derive(Serialize, Deserialize)]
#[serde(rename_all="camelCase")]
pub struct PathPayload{
  pub model_path:String
}
