#![cfg(mobile)]
use tauri::ipc::Channel;
use tauri::{AppHandle, command, Runtime};


use crate::error::Result;
use crate::SherpaNcnnExt;

#[command]
pub(crate) async fn ncnn_init_recognizer<R:Runtime>(
    app:AppHandle<R>,
    model_path:String
)->Result<()>
{
    #[cfg(mobile)]
    app.sherpa_ncnn().ncnn_init_recognizer(model_path).await.map_err(|e| e.into())
}

#[command]
pub(crate) async fn ncnn_init_recognizer_from_asset<R:Runtime>(
    app:AppHandle<R>,
    model_path:String
)->Result<()>
{
    app.sherpa_ncnn().ncnn_init_recognizer_from_asset(model_path).await.map_err(|e|e.into())
}

#[command]
pub(crate) async fn ncnn_init_microphone<R:Runtime>(
    app:AppHandle<R>,
)->Result<()>
{
    app.sherpa_ncnn().ncnn_init_microphone().await.map_err(|e|e.into())
} 

#[command]
pub(crate) async fn ncnn_start_recognize<R:Runtime>(
    app:AppHandle<R>,
    channel:Channel
)->Result<()>
{
    app.sherpa_ncnn().ncnn_start_recognize(channel).await.map_err(|e|e.into())
}

#[command]
pub(crate) async fn ncnn_stop_recognize<R:Runtime>(
    app:AppHandle<R>,
)->Result<()>
{
    app.sherpa_ncnn().ncnn_stop_recognize().await.map_err(|e|e.into())
}