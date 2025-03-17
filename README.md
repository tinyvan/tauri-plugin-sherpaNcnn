# Tauri Plugin sherpaNcnn
A tauri plugin (now android) for sherpaNcnn.

The plugin is built by TinyWang.
Notice: Model files must be configured manually!!!

# Complilation Step
Environment:
`Linux` `Android_NDK` `cmake`
```sh
git clone https://github.com/tinyvan/sherpa-ncnn-modified
```
sherpa-ncnn-modified is forked from [\[k2-fsa/sherpa-ncnn\](\[text\](https://github.com/k2-fsa/sherpa-ncnn))](https://github.com/k2-fsa/sherpa-ncnn) and 
some of its code has been modified to make it embedded in the project.

Next,you can run one of the following scripts in Linux
!!!Please set the environment variable ANDROID_NDK before you run this script
!!!Please set the environment variable ANDROID_NDK before you run this script
```sh
build-aarch64-linux-gnu.sh
build-android-arm64-v8a-with-vulkan.sh
build-android-arm64-v8a.sh
build-android-armv7-eabi.sh
build-android-x86-64.sh
build-android-x86.sh
```
After build you will get shared library file in `build-..../install/lib` folder,and just copy them to the `jniLibs` in the android code library of this tauri plugin.(You'd better follow the instructions in [k2-fsa/sherpa-ncnn](<https://github.com/k2-fsa/sherpa-ncnn))](https://github.com/k2-fsa/sherpa-ncnn)>))
## 代码库功能函数实现情况表
## Library Function Implementation Status Table

| 函数 Function Name | Android | iOS | Windows | macOS | Linux |
|---|---|---|---|---|---|
| `NcnnInitRecognizer` | ✅ | ❌ | ❌ | ❌ | ❌ |
| `NcnnInitRecognizerFromAsset` | ✅ | ❌ | ❌ | ❌ | ❌ |
| `NcnnInitMicrophone`  | ✅ | ❌ | ❌ | ❌ | ❌ |
| `NcnnStartRecognize` x| ✅ | ❌ | ❌ | ❌ | ❌ |
| `NcnnStopRecognize`  | ✅ | ❌ | ❌ | ❌ | ❌ |

**说明/Notes:**

*   ✅  : 已实现 / Implemented
*   ❌  : 未实现 / Not Implemented
