
# HelloFXML 3

A simple Hello World application built with Java 16, JavaFX 16 (with FXML), Scala 3, and GraalVM.
The code is based on the Gluon sample HelloFXML written in Java. You can read about it [here](https://docs.gluonhq.com/client/#_hellofxml_sample).
I rewrote it in Scala, and removed the localisation "bundle" as it proved to require some additional research to make it 
work, and I wanted to keep this example as simple as possible.

You can run the app on the desktop with:

    mvn gluonfx:run

And then create and run a desktop native image:

    mvn gluonfx:build gluonfx:nativerun

If all works, create an Android APK and install it on a connected device with adb:

    mvn -Pandroid gluonfx:build gluonfx:package
    adb install <path to apk>

The main "feature" this example is about is how we can build the GUI of an app in [Scene Builder](https://gluonhq.com/products/scene-builder/) 
and then connect it to the code written in Scala. The result of working in Scene Builder is an FXML file which should be
put in the resources folder (in this case `src/main/resources/hellofxml/hello.fxml`). The FXML file consists of one root 
"node" and any number of elements inside the node. The file can be then loaded into the code with `FXMLLoader.load[T](path)` 
where `T` is the type of the node (in this case, `AnchorPane`). The `load` method returns a reference to the root node 
which we can then put on a scene on the main stage:
```
final class HelloFXML extends Application:
  override def start(primaryStage: Stage): Unit =
    val root = FXMLLoader.load[AnchorPane](classOf[HelloFXML].getResource("hello.fxml"))
    primaryStage.setScene(new Scene(root, 400, 600))
    primaryStage.show()
```
(fortunately, the example is so small I can explain everything with the actual code)

Please also take note that the class `HelloFXML` extends a standard JavaFX `javafx.application.Application` and then 
overrides the `start` method. In the next example, where we build a calculator app for Android, we will use a specialized
`MobileApplication` class from Gluon. But using it would require drawing in many other Gluon dependencies. So, this example
is supposed to be as simple as possible - the only Gluon dependency here is `client-maven-plugin` - and this way in the
next one we will be able to talk about what exactly is needed to build a mobile app with Gluon.

The code above, together with the FXML file, displays a view with a button and a label, but we still need a way to do
something when the button is clicked. For this, we can use a controller tied to the view described in FXML. If you
look into hello.fxml, you will see that the main node has a `fx:controller` field specified, and each of the two elements
have their `fx:id`s.
```
<AnchorPane prefHeight="600.0" prefWidth="400.0" stylesheets="@style.css" xmlns="http://javafx.com/javafx/15.0.1" xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="hellofxml.HelloFXMLController">
    <Label fx:id="label" alignment="CENTER" contentDisplay="CENTER" prefHeight="66.0" prefWidth="238.0" text="Hello JavaFX" visible="false" />
    <Button fx:id="button" alignment="CENTER" contentDisplay="CENTER" layoutX="161.0" layoutY="280.0" mnemonicParsing="false" text="Click me!" />
</AnchorPane>
```

All of them can be set both in text and in Scene Builder. Now we need to create the `HelloFXMLController` class and add it
to `pom.xml` as a class on the "reflection list" of the Gluon's `client-maven-plugin`. The connection between the FXML view
and its controller in JavaFX relies on reflection which I think is a major drawback. It means that we need to maintain
lists of classes and methods we want to keep available for reflection which in more complex projects will become troublesome.
(Please look at this discussion for details [link]). In the calculator example I will propose a solution that make it
a bit easier to handle, but at least in case of controllers we are forced to register them in the `client-maven-plugin`
this way:
```
<plugin>
    <groupId>com.gluonhq</groupId>
    <artifactId>gluonfx-maven-plugin</artifactId>
    <version>${gluonfx.maven.plugin.version}</version>
    <configuration>
        <target>${gluonfx.target}</target>
        <mainClass>${main.class}</mainClass>
        <reflectionList>
            <list>hellofxml.HelloFXMLController</list>
        </reflectionList>
    </configuration>
</plugin>
```

And, finally, the controller class:
```
final class HelloFXMLController:
  @FXML private var button: Button = _
  @FXML private var label: Label = _

  def initialize(): Unit =
    button.setOnAction { (_: ActionEvent) =>
      label.setText(s"Hello from JavaFX ${System.getProperty("javafx.version")}")
      label.setVisible(!label.isVisible)
    }
```

The `@FXML` annotations tell JavaFX that the fields `button` and `label` are linked to FXML elements with ids, respectively,
`fx:button` and `fx:label`. If it was java, they would be left uninitialized (i.e. `@FXML private Button button;` and 
`@FXML private Label label`) so that JavaFX could generate the initialization code for them. In Scala, for the same reason
we have to make them `var`s and use `_`. The fact that we are not in full control of their initialization means also that
for safety reasons they shouldn't be accessed from outside the class directly, even to just read their values, and they
shouldn't be used in methods that can be called from other parts of the code before the controller is initialized.
(More about it in the calculator example). Instead we use the `initialize` method which works by convention - if it exists,
JavaFX will call it after the controller is initialized. In there, we define what will happen when the button is clicked.
There exists also another way of doing it - we could specify in the button's FXML element the name of the method that
should be called on click. I will leave it as an exercise for you ;) In both cases, we want to update the text of the label
and show it, if it's not visible yet, or hide it otherwise.

**Note**: In the moment I'm writing this (June 21, 2021) there are at least two issues with Scala 3 that need to be fixed
before we can seriously consider moving to Scala 3 on Android.
1. In `pom.xml` you can see a dependency: `org.scala-lang:scala3-library_3`. Unfortunately that suffix, `_3`, is invalid on
on Java 11. One solution would be to set `Automatic-Module-Name` in the `MANIFEST.MF` file inside the `scala3-library_3` jar,
in a similar fashion to how other Scala libraries can do it to be compatible with Java 9+. A temporary workaround is to use
a Maven plugin I develop: [scala-suffix](https://github.com/makingthematrix/scala-suffix). Under the link you can find
explanation how it works and in `pom.xml` in this project you can find an example how it can be used to add an entry to
`MANIFEST.MF` of `scala3-library_3`.
2. The second issue is more critical. **Right now code written in Scala 3 on GraalVM native image can't use lazy vals**.
And maybe not only that, but this is where the app crashes first. If you try to use a lazy val, you will get an error in
runtime similar to this one:
```
[INFO] [SUB] Exception in thread "main" scala.runtime.LazyVals$$anon$1
[INFO] [SUB] 	at scala.runtime.LazyVals$.$init$$$anonfun$3(LazyVals.scala:18)
[INFO] [SUB] 	at scala.Option.getOrElse(Option.scala:201)
[INFO] [SUB] 	at scala.runtime.LazyVals$.<clinit>(LazyVals.scala:19)
[INFO] [SUB] 	at com.oracle.svm.core.classinitialization.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:375)
[INFO] [SUB] 	at com.oracle.svm.core.classinitialization.ClassInitializationInfo.initialize(ClassInitializationInfo.java:295)
[INFO] [SUB] 	at calculator.Main.<clinit>(Main.scala:16)
[INFO] [SUB] 	at com.oracle.svm.core.classinitialization.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:375)
[INFO] [SUB] 	at com.oracle.svm.core.classinitialization.ClassInitializationInfo.initialize(ClassInitializationInfo.java:295)
```
As I understand, the reason is that the new implementation of lazy vals in Scala 3 [depends on `sun.misc.Unsafe`](https://github.com/lampepfl/dotty/blob/master/library/src/scala/runtime/LazyVals.scala)
and in the current GraalVM 21.1.0, [`sun.misc.Unsafe` is not supported](https://github.com/oracle/graal/issues/2694). 
If the fix from this link is not enough, I will open a new ticket.

And that's it. In short, Scene Builder is a very nice tool for GUI design and as we can see there's not much boilerplate
involved. its main flaw, in my opinion, lies in that JavaFX uses reflection to tie views with controllers. Fortunately,
adding complex actions can be also done from the Scala code, for example as in the `initialize` method above. Thanks to
that, we can limit the number of classes and methods which would need to register for reflection. 
