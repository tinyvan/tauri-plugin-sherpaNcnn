#![cfg(mobile)]
use tauri::{
  plugin::{Builder, TauriPlugin},
  Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::SherpaNcnn;
#[cfg(mobile)]
use mobile::SherpaNcnn;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the sherpa-ncnn APIs.
pub trait SherpaNcnnExt<R: Runtime> {
  fn sherpa_ncnn(&self) -> &SherpaNcnn<R>;
}

impl<R: Runtime, T: Manager<R>> crate::SherpaNcnnExt<R> for T {
  fn sherpa_ncnn(&self) -> &SherpaNcnn<R> {
    self.state::<SherpaNcnn<R>>().inner()
  }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
  Builder::new("sherpa-ncnn")
    .invoke_handler(tauri::generate_handler![
      commands::ncnn_init_recognizer,
      commands::ncnn_init_recognizer_from_asset,
      commands::ncnn_init_microphone,
      commands::ncnn_start_recognize,
      commands::ncnn_stop_recognize
      ])
    .setup(|app, api| {
      #[cfg(mobile)]
      let sherpa_ncnn = mobile::init(app, api)?;
      #[cfg(desktop)]
      let sherpa_ncnn = desktop::init(app, api)?;
      app.manage(sherpa_ncnn);
      Ok(())
    })
    .build()
}
