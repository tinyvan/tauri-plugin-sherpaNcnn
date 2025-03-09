use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
  app: &AppHandle<R>,
  _api: PluginApi<R, C>,
) -> crate::Result<SherpaNcnn<R>> {
  Ok(SherpaNcnn(app.clone()))
}

pub struct SherpaNcnn<R: Runtime>(AppHandle<R>);

impl<R: Runtime> SherpaNcnn<R> {

}
