const COMMANDS: &[&str] = &[
  "ncnn_init_recognizer",
  "ncnn_init_microphone",
  "ncnn_start_recognize",
  "ncnn_stop_recognize",
  "ncnn_init_recognizer_from_asset",];

fn main() {
  tauri_plugin::Builder::new(COMMANDS)
    .android_path("android")
    .ios_path("ios")
    .build();
}
