# ClearBluetooth
Simple app that will remove all bluetooth pairings with a push of a button or from command line using `adb`
<br><br/>
## Building and installation for Android 11 or lower
Application can be installed by building it in Android Studio or from command line

Building from command line. This will build and install the application to connected device
```bash
./gradlew installDebug
> BUILD SUCCESSFUL
```

Build application without installing
```bash
./gradlew assembleDebug
> BUILD SUCCESSFUL
```

Both command will build application. Built `.apk` is located in
```bash
app/build/outputs/apk/debug/app-debug.apk
```
<br><br/>
## Building and installation for Android 12
Download the `apk` from the releases or build it by using ide or gradle
```bash
./gradlew assembleDebug
> BUILD SUCCESSFUL
```
Android 12 has stricter permission control. Either install with `-g` flag:
```bash
adb install -g path/to/apk
```
- `-g` will allow all the run time permissions

### `If installed without -g user has to allow bluetooth permissions. The permission is asked only once on the initial launch of the app. If denied, uninstall the application and reinstall to have permissions asked again`
<br><br/>
## Possible problems

```bash
Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain
Caused by: java.lang.ClassNotFoundException: org.gradle.wrapper.GradleWrapperMain
```

Run
```bash
gradle wrapper
```
<br><br/>
## Using the application
The application has two activities:
- MainActivity
- UnpairWithoutUI

`MainActivity` is the one that has the UI and opens up when application is launched from the phone. `UnpairWithoutUI` will
run without UI and close after pairings are removed.

To clear the bluetooth pairings from the command line. Install the application and run the `UnpairWithoutUI`
```bash
adb shell am start -n com.automationdev.clearbluetooth/.UnpairWithoutUI
```

To enable/disable devices's bluetooth connection
```bash
adb shell am start -n com.automationdev.clearbluetooth/.ToggleBluetooth -e bluetooth enable/disable
```
<br><br/>
## Logging
When running unpairing from command line the application will output how many devices were paired and how many the app was able to unpair.
These prints can be found from log. Grep with the name of the activity `W UnpairWithoutUI`
```bash
adb logcat | grep "W UnpairWithoutUI"
> ... W UnpairWithoutUI: There is currently 7 paired bluetooth devices.
> ... W UnpairWithoutUI: 7 of 7 devices un-paired.
```
<br><br/>
## Release
Create a Github release and use `v` at the beginning of tag (for example `v1.0`). `.apk` is added to the release artefacts automatically.