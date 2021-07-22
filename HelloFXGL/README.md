### HelloFXGL example

This is my first attempt to include FXGL, a game engine, in a mobile app. For now it... fails. But only partially.  

This example is based on [Hello Gluon](https://github.com/makingthematrix/scalaonandroid/tree/main/hellogluon) which
in turn is based on ["Hello Gluon" from Gluon Mobile samples](https://github.com/gluonhq/gluon-samples/tree/master/HelloGluon). 
It consists of the `Main` class which sets up a JavaFX app in the same way as in Hello Gluon, and then uses the main window to
display an FXGL view. In theory it should enable us to code a game in FXGL while still being able to draw JavaFX widgets
on top of it, just as this example draws the "search" button. 

Please refer to [How to compile and run an example](https://github.com/makingthematrix/scalaonandroid/wiki/How-to-compile-and-run-an-example)
for details on how to, well, compile and run this example.

The app works when it's run as a standard Java app with `mvn gluonfx:run` and as a **desktop** native app, compiled
with GraalVM Native Image (`mvn gluonfx:build gluonfx:nativerun`). In the second case we see some warnings, but they don't
seem to be serious:
```
WARN  SystemBundleService  - Failed to load: java.io.FileNotFoundException: /home/makingthematrix/workspace/scalaonandroid/HelloFXGL/system/fxgl.bundle (No such file or directory)
WARN  FXGLAssetLoaderServi - Asset "/assets/languages/english.lang" was not found!
FXGLAssetLoaderServi - Failed to load PROPERTY_MAP
WARN  LocalizationService  - menu.pressAnyKey is not localized for language ENGLISH
```

Unfortunately when we compile the example for Android (`mvn -Pandroid gluonfx:build gluonfx:package`), install it on 
a mobile device and open it, it crashes with the following error:
```
E AndroidRuntime: FATAL EXCEPTION: main
E AndroidRuntime: Process: io.makingthematrix.scalaonandroid.hellofxgl, PID: 2777
E AndroidRuntime: java.lang.UnsatisfiedLinkError: dlopen failed: cannot locate symbol "JNI_OnLoad_awt" referenced by "/data/app/io.makingthematrix.scalaonandroid.hellofxgl-yn4tm0Ly6KLJ5U46m2A6KQ==/lib/arm64/libsubstrate.so"...
E AndroidRuntime: 	at java.lang.Runtime.loadLibrary0(Runtime.java:1071)
E AndroidRuntime: 	at java.lang.Runtime.loadLibrary0(Runtime.java:1007)
E AndroidRuntime: 	at java.lang.System.loadLibrary(System.java:1667)
E AndroidRuntime: 	at com.gluonhq.helloandroid.MainActivity.surfaceCreated(MainActivity.java:104)
```

This is addressed [in an issue on the gluonfx-maven-plugin GitHub repo](https://github.com/gluonhq/gluonfx-maven-plugin/issues/349)
and the answer for now is that "there is no support yet for AWT on Android" :( Does it mean FXGL can't be used on Android at all?
I will try to learn more about it.

In the meantime, this is still a perfectly good way to write native desktop games with FXGL (*). Also, please note that
embedding FXGL makes sense for mobile apps, but on the desktop you can decide to run FXGL from the very start by specifying
the main FXGL class that extends `com.almasb.fxgl.app.GameApplication` as the main class of the app. Unfortunately, Maven 
has some problems with understanding how Scala implements its static methods in companion objects instead of directly in 
the classes. There are tricks to go around it: this example uses one of them but as far as I know it doesn't work for FXGL. 
Instead, I'd suggest to write the `Main` class in Java, make it extend `com.almasb.fxgl.app.GameApplication` instead of 
`com.gluonhq.charm.glisten.application.MobileApplication`, and call Scala code from there.

*) If you look into `pom.xml` you will see a long list of FXGL services I had to put in the `reflectionList` for `gluonfx-maven-plugin`.
[It would look better if I put them in a seperate file](https://www.graalvm.org/reference-manual/native-image/Reflection/) 
but Ialso wonder if really all of them need to be on the reflection list even if I don't use them.
