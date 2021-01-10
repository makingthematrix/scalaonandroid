### How to cook an Android app in Scala

This is just a quick and dirty guide. Yesterday I stirred together a few things and - to my surprise - they actually worked, so today I decided to write down how I did it before I forget. The following recipe only proves that it is possible to have a working, but a very simple Android app written entirely in Scala 2.13. All it displays is a simple window with an image and a button. It does not communicate with a server. It does not store anything in a local database. It does not open a websocket and it does not use Google Play Services or any of the alternatives. It does not access camera, microphone, etc. To be honest, it does not request any OS permissions at all. Before we can say that yes, writing an Android app in Scala is possible, all those things need to be checked and they should either work well or we should have good workarounds. For now, all we have is just a dummy UI.



#### Ingredients

1. You need a good computer. Haha, but for real. I have Lenovo Ideapad Y700 - Intel Core i7, 8GB RAM, and an SSD hard drive, and the compilation of that dummy app took me over two minutes and ate all the RAM.

2. You need Linux. It looks like at least for now only the Linux version of GraalVM can build Android apps. And that's what we will be using.

3. Download GraalVM for Linux, for Java 11 from here: [https://github.com/graalvm/graalvm-ce-builds/releases](https://github.com/graalvm/graalvm-ce-builds/releases)

4. Add this to your `~/.bash_profile`:

   ```
   export GRAALVM_HOME=<path to GraalVM home directory>
   export JAVA_HOME=$GRAALVM_HOME
   export PATH=$GRAALVM_HOME/bin:$PATH
   ```

5. When you type in `java -version` it should display something like this now:

   ```
   > java -version
   openjdk version "11.0.9" 2020-10-20
   OpenJDK Runtime Environment GraalVM CE 20.3.0 (build 11.0.9+10-jvmci-20.3-b06)
   OpenJDK 64-Bit Server VM GraalVM CE 20.3.0 (build 11.0.9+10-jvmci-20.3-b06, mixed mode, sharing)
   ```

6. I'm not entirely sure if this is needed for what we're going to do or will the compilation do it for us, but in case it doesn't, manually download GraalVM's `native-image`:

   ```
   gu install native-image
   ```

   `gu` should be available now in your console because of `$GRAALVM_HOME/bin` in your `PATH`.
Also, read this and install whatever you need: https://www.graalvm.org/reference-manual/native-image/#prerequisites

7. You will need `adb` , "Android Debug Bridge", to connect to your Android device and install the app on it: https://www.fosslinux.com/25170/how-to-install-and-setup-adb-tools-on-linux.htm .
   Oh, and if it's not clear yet, you will need an Android device ;)

8. Make sure your `gcc` is at least version 6. It turned out my Linux distro had gcc 5.4 - a bit of a surprise since I experimented with C recently and was pretty sure I have all the modern stuff. But then I remembered I use clang, not gcc. You can try follow these steps: https://tuxamito.com/wiki/index.php/Installing_newer_GCC_versions_in_Ubuntu .
   On top of that, you will need some specific C libraries (like GTK) to build the native image and it varies from one computer to another, so I can't tell you exactly what to do. But it shouldn't be a big problem. Just follow error messages saying that you lack something and google how to install them. In my case this was the list:
```
  libasound2-dev (for pkgConfig alsa)
  libavcodec-dev (for pkgConfig libavcodec)
  libavformat-dev (for pkgConfig libavformat)
  libavutil-dev (for pkgConfig libavutil)
  libfreetype6-dev (for pkgConfig freetype2)
  libgl-dev (for pkgConfig gl)
  libglib2.0-dev (for pkgConfig gmodule-no-export-2.0)
  libglib2.0-dev (for pkgConfig gthread-2.0)
  libgtk-3-dev (for pkgConfig gtk+-x11-3.0)
  libpango1.0-dev (for pkgConfig pangoft2)
  libx11-dev (for pkgConfig x11)
  libxtst-dev (for pkgConfig xtst)
```
   On the other hand, it looks like you don't need to install Android SDK manually. GraalVM will do it for you. (But you can use your own installation if you want to) 



#### Recipe

1. We will create the app with the help of Gluon Mobile - a platform which uses JavaFX to build Java client apps. It looks like this will be **the** way to build Android apps with GraalVM in foreseeable future (https://github.com/oracle/graal/issues/632#issuecomment-643816258). No standard Android widgets - JavaFX instead. It has a lot of interesting implications but that's a story for another day.  https://gluonhq.com/products/mobile/ .
   You don't need to download anything directly from Gluon Mobile. Maven will do it for you. So, yes, we're using Maven, not SBT, because we need Maven plugins which don't have their SBT equivalents. Install `mvn` if you don't have it yet.
   By the way, here's a much more detailed explanation how to write a client app with Gluon (in Java): https://docs.gluonhq.com/client/0.1.31/#_overview . 
   A big advantage of this approach is that - in theory at least - we should be able to use a lot of the same stuff for writing apps for Android, iOS, and desktop clients. In my company we have separate teams for that, writing different versions of the same app in different technologies, which is costly and means that we can't help each other as much as we could. On the other hand, we tried to use the same tech stack in the past, and there were reasons why we decided to abandon this approach... Okay, this is also a topic for another day.  

2.  Now, download this dummy app! I based in on HelloGluon from Gluon samples: https://gluonhq.com/developers/samples/ . 
   In `pom.xml` you will find a list of plugins and dependencies the app uses (well, d'oh, this is the place where we put them, after all). I'd like to say a few words about them.

   * Scala 2.13.4. Not much to say here apart from that I'm very happy I see this version number here.

   * JavaFX 15, the UI widgets library. https://openjfx.io/ . 

   * Gluon Glisten. I'm not exactly sure yet what's the relation between this and JavaFX. Something something user interface something. https://docs.gluonhq.com/#_glisten_apis . 

   * Then comes a bunch of dependencies that I think are there to enable the app to interact with Android in a number of ways. I decided to keep them, just to know that they exist, but the dummy app only uses "display" and "util" from the list. (it's also possible that I don't understand something here - when I tried to remove some of them, compilation failed).

   * scala-maven-plugin  (https://github.com/davidB/scala-maven-plugin) - Thank you, David!

   * maven-compiler-plugin - I don't know much about it, tbh.

   * javafx-maven-plugin - to compile JavaFX with Maven, I guess?...

   * And finally client-maven-plugin for Gluon.
     Here you can see an argument for `native-image`: `--report-unsupported-elements-at-runtime`. Without it, compiling Scala ends with an error:

     ```
     Error: Unsupported type java.lang.invoke.MemberName is reachable: All methods from java.lang.invoke should have been replaced during image building.
     ```

     The issue is known and it should be fixed eventually: https://github.com/oracle/graal/issues/2761 . 

   * In the profiles section, I keep both the desktop and Android profile. This allows for running the app on the computer without the need to have my Android phone around all the time.

3. First try to build the app with `mvn client:build` and solve the problems along the way. If it works, `mvn client:run` will run the desktop version of the dummy app. You will see a window with the title "Hello, Gluon Mobile!",  a picture of this weird Java mascot, and a button with a magnifying glass icon. Clicking on it does not really let you search for anything, however, but will print "log something with Scala" to the console.

4. When that works, you can try to compile an Android APK:

   ```
   mvn -Pandroid client:build client:package
   ```

    This again may at first produce a list of errors. Be brave. When it finishes with success, in `target/client/aarch64-android/gvm` you will find the compiled app: `DummyApp.apk`. 
   Connect your Android phone to the computer with an USB cable, give the computer permission to send files to the phone, and type `adb devices` to check if your phone is recognized. It should display something like this in the console:

   ```
   > adb devices
   List of devices attached
   16b3a5c8	device
   ```

   Then, `adb install DummyApp.apk` should install the dummy app on the phone and a moment later you should be able to see it on your phone's main screen. When you click on its icon it should open approximately the same screen as its desktop version. 

   Installation might not work for a number of reasons, one of the most popular being that your Android simply does not allow installing apps this way. Go to Settings, find "Developers options", and there enable "USB debugging" and "Install via USB". If you can't find "Developers options" anyway then it might mean you need to do something funny to make it appear, like tapping the field with your Android OS version number ten times. 

   If everything works and you see the dummy app's screen on your phone, type `adb logcat | grep GraalCompiled` to see the log output of the dummy app. Now if you click on the button with the magnifying glass icon, you should see "log something from Scala" printed to the console. (I'm sure there are plugins for that for IntelliJ or maybe even VS Code - after all, there is one for Android Studio)

5. One more thing. When you start the dummy app, you will see a note in the front of the main screen, saying that this is an app built with the free version of Gluon Mobile. Any app built with the free version of Gluon Mobile will have this note. I think it's fair. If Gluon, together with JavaFX and GraalVM, lets me write Android apps in modern Scala, with an alternative solution to standard Android widgets library, and support me in the fight with the chaos created by constantly changing Android SDK, I'm all for paying them for a license - or just keep displaying this note if I don't plan to make money with the app I'm writing. 



#### Thank you

I hope you managed to build the app :) I know it's a lot, but when you have it working you can just build on top of it. Although, as I already mentioned, there are still a lot of questions which needs answering.  I think it's a good sign that I was able to put this dummy app together on January 1, 2021. I can now use the rest of the year for researching and writing more about how we can write Scala on Android... or if we can't, then why. But I'm reasonably optimistic. I know the road ahead is bumpy, but people at GraalVM, Gluon, and JavaFX, are doing amazing job. I hope it will all pay off. 
