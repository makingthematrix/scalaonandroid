
# HelloFXML2

This is a variant of [HelloFXML](https://github.com/makingthematrix/scalaonandroid/tree/main/HelloFXML) which tests a fix to the problem of Scala dependencies being incompatible with Java 9+. - the suffix `_2.13` is treated as integral part of the library module name which results in a warning during the compilation, and then a failure in runtime when the app tries to access the library code. Here you can read more about it: https://github.com/makingthematrix/scala-suffix
The app uses a small Scala library for event streams, `wire-signals_2.13:0.3.1`, to test that the fix works. Newer versions of `wire-signals` are already fixed on the library side, but `0.3.1` would crash the app if used with the fix. And the fix is just that `scala-suffix-maven-plugin` is added to the list of plugins and it modifies the library's jar file in the local Maven repository before the compilation, adding a line to its manifest: `Automatic-Module-Name: wire-signals`.


You can run the app on the desktop with:

    mvn javafx:run

And then create and run a desktop native image:

    mvn client:build client:run

If all works, create an Android APK and install it on a connected device with adb:

    mvn -Pandroid client:build client:package
    adb install <path to apk>
