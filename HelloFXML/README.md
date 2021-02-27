
# HelloFXML

A simple Hello World application with Java 11+, JavaFX 15+ (with FXML), Scala 2.13, and GraalVM.
The code is based on the Gluon sample HelloFXML written in Java. You can read about it [here](https://docs.gluonhq.com/client/#_hellofxml_sample).
I rewrote it in Scala, and removed the localisation "bundle" as it proved to require some additional research to make it work, and I wanted to keep this example as simple as possible.
One significant change I made was to add `GridPane` to `hello.fxml`. Before making this example, I worked on a more complex one which used `GridPane` and its compilation was failing with weird errors. I thought `GridPane` could be the reason of that, but HelloFXML shows it's not.

You can run the app on the desktop with:

    mvn javafx:run

Snd then create and run a desktop native image:

    mvn client:build client:run

If all works, create an Android APK and install it on a connected device with adb:

    mvn -Pandroid client:build client:package
    adb install <path to apk>
