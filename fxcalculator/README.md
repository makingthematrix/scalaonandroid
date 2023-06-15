## Fx Calculator

This is a simple, but an actually practical app for both Android and desktop. It's built with Java 17, JavaFX 20 (with FXML), Scala 3.3.0-RC2, and GraalVM 22.1.0. GUI is based on [a JavaFX tutorial](https://www.youtube.com/watch?v=p5ifU9kkp6g) by Kody Simpson and consists of the main view made in Scene Builder and an Advanced Editor dialog that pops up after clicking a button.

Below I will try to explain how it's all done, but before that, I'd like to emphasise that my work here is only about putting together puzzle pieces done by other people - the GraalVM team, the Gluon Mobile team, all contributors to JavaFX, and many more. Even the logic behind parsing and evaluating the expression you can type in the calculator was possible only because thanks to Martin Odersky and the whole Scala team it's so easy to reason about these things in my favourite language.

A screenshot: [link](https://drive.google.com/file/d/1RePEsqXzn4lAnzthDefZYHWtzRmOEy5N/view?usp=sharing)

The Android APK: [link](https://github.com/makingthematrix/scalaonandroid/raw/main/fxcalculator/Fx%20Calculator.apk)

(after you download the APK file, you can install it on your Android device with [`adb`](https://www.xda-developers.com/install-adb-windows-macos-linux/))

### The main view

The main view is a pretty typical calculator with a field displaying the expression and buttons for numbers, operators, and a few special commands. The layout is stored in `src/main/resources/calculator/main.fxml`. To create it, I used Scene Builder and roughly followed [these two tutorial videos](https://youtu.be/p5ifU9kkp6g). The main element of the view (they are called "nodes" in JavaFX) is a `BorderPane` - a rectangle divided into five regions: top, left, right, bottom, and center. To each region, I can add another node. In this case, I use the top region for a `Label` that will display the expression I want to evaluate (and after the evaluation, its result which may become the basis for another expression), and I use the center for a `GridPane`. Other regions won't be used and they will be invisible. The `GridPane` I use is a grid with 4 columns and 6 rows. In each entry I place a `Button`. I make those buttons spread out to take all the space in the entry, and we give each of them an id, and action: when the button is clicked, the `onEvaluate` method in `MainController` will be called.

Look into [the HelloFXML 3 example](https://github.com/makingthematrix/scalaonandroid/tree/main/HelloFXML3) for a short discussion of how FXML views interact with controllers. Each FXML view is connected to a controller class in Scala. The controller contains fields that correspond to FXML nodes with the same ids as the names of the fields and methods which will be called from the view if I link them to specific actions the user can execute on the nodes.  While methods are only called in response to the actions of the user, the correspondence of fields and nodes goes both ways. For example, if I used a `TextField` instead of a `Label`to display the expression, the user would be able to write in that text field and I would be able to read that input in the Scala code by accessing the corresponding field. But that would make checking if the expression is valid more difficult as the user would have much more freedom to blunder. Instead, the field is a read-only `Label`, and changes to its contents are done through clicking buttons. This way, when the user clicks a button, I can check in the Scala code if adding the corresponding number or an operator to the expression is possible before I do it, and if yes, I update the field with the new expression, and that automatically displays the new expression in the view. The same things happen after the calculator evaluates the expression and returns a result. I use the same`Label` to display the expression and the result. When I obtain the result, I just call the `setText` method on the label and that automatically updates the text displayed on the screen.

### The Advanced Editor dialog

The Advanced Editor allows you to type on the Android's (or laptop's) keyboard instead of clicking on buttons. In this mode, you have access to a bunch of hardcoded functions and constants. You can write new ones, e.g. `f(x) = x + 1` creates the function `f` which takes one argument `x` and returns `x+1`. These new functions are stored in a JSON file, so when you shut down Fx Calculator, you will not lose them. However, there are some limitations: A function can call another function but there are no loops or recursion. They can't be overloaded, and if you want to change the definition of a function, you need to delete it first, and then write it again - and that only if the function is not called by another. In the end, this is just a calculator, not a programming language interpreter.

### Back to JavaFX details

In the original tutorial video, the history dialog is implemented as a "stage" - a view of the same type as the main one. But this does not work well in mobile applications, for reasons I don't want to explore in this example. Instead, I decided to use a `Dialog` class from Gluon's `charm-glisten` library and fill it with a `ListView`. This is also done with Scene Builder, even though the list view is the only node in the whole FXML file `history.fxml`, because I wanted to present how to access a controller from other parts of the code. The main controller in the calculator is only accessed from the view, but not from another class. If we for example use event streams to carry information across the app, nothing may ever have to access the controller - instead, the controller can subscribe to event streams and react to new events by making changes in the view. But if there is a need to access the controller more directly, this is how we can do it:

```scala
object AdvancedEditor:
  private val loader = new FXMLLoader(classOf[AdvancedEditor].getResource("advancededitor.fxml"))
  private val root: Node = loader.load[Node]()
    
 ...

final class AdvancedEditor:
  import AdvancedEditor.root
    
  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("Advanced Editor"))
    d.setContent(root)
    ...
  }
  ...
  
  def run(dictionary: Dictionary): String =
    this.dictionary = Some(dictionary)
    dialog.showAndWait().toScala.getOrElse("")
```

### Differences in `pom.xml` in comparison to HelloFXML3

If you compare the `pom.xml` file in the Calculator example with the one in the basic [HelloFXML3](https://github.com/makingthematrix/scalaonandroid/tree/main/HelloFXML3), you'll notice that most of the new stuff is Gluon dependencies. They belong to two groups: 

1. `charm-glisten` 

`charm-glisten` adds sparkles to our GUI. We need it mainly because we want our `Main` class to extend `MobileApplication` - a subclass of JavaFX's Application which takes care of a lot of initial configuration of a mobile app and lets us take advantage of other charm-glisten features, such as `Swatch which` assigns one of default colour palettes to the Calculator's main view. To be honest, I could write the Calculator app in plain JavaFX and it would still run on Android, and for an app so simple the difference wouldn't be big. But I want you to think of this example as a stepping stone to more complex programs where using Gluon libraries would be necessary, so let's start using them already. 

2. `attach`

`attach` is a library that lets us communicate with the underlying platform, i.e. Android in our case. In the Calculator app, we need it for one purpose only: we want to know the screen resolution so we can make our GUI full-screen. For that, we need libraries display and util, but they, in turn, require us to include `storage` and `lifecycle`.

Apart from Gluon libraries, we also need to inform` client-maven-plugin` about our two controllers which JavaFX needs to be able to access through reflection. [See here](https://stackoverflow.com/questions/63527596/how-to-solve-fxml-loading-exceptions-in-compiled-javafx-project-using-gluonhq-cl) for a longer discussion about this topic.

And then there is [mUnit](https://scalameta.org/munit), a small unit test library for Scala. Highly recommended.

### The Android app icon and other settings

During the `mvn -Pandroid client:package` task, you might see the following message:

Default Android manifest generated in `<path to project>/target/client/aarch64-android/gensrc/android/AndroidManifest.xml`.

Consider copying it to `<path to project>/src/android/AndroidManifest.xml` before performing any modification.

`AndroidManifest.xml` is a file describing the app to the Android operating system. It says what activity should be called when the app is launched, what service class handles notifications coming through Google Play Services, what permissions the app needs from Android (the internet connection, access to the storage, to the file system ...) and many other things. The manifest file generated by default by Gluon's `client-maven-plugin` will look something like this:

```xml
<manifest xmlns:android='http://schemas.android.com/apk/res/android' package='scalaonandroid.calculator' android:versionCode='1' android:versionName='1.0'>

  <supports-screens android:xlargeScreens="true"/>

  <uses-permission android:name="android.permission.INTERNET"/>

  <application android:label='Scala Calculator' android:icon="@mipmap/ic_launcher">
    <activity android:name='com.gluonhq.helloandroid.MainActivity' 
    		  android:configChanges="orientation|keyboardHidden">
     <intent-filter>
       <category android:name='android.intent.category.LAUNCHER'/>
       <action android:name='android.intent.action.MAIN'/>
      </intent-filter>
    </activity>
	<activity android:name='com.gluonhq.helloandroid.PermissionRequestActivity'/>
  </application>
</manifest>
```

If you want to customize it, first copy` AndroidManifest.xml` to `<path to project>/src/android`. Then `client-maven-plugin` will use your version of the file instead of generating the default one. For the Calculator example it's not necessary, but you'd probably want to do it for any more complex project. Here we are only interested in the part that tells us what is the app icon: `android:icon="@mipmap/ic_launcher"`. (In fact, for Calculator even the INTERNET permission listed in the default manifest file is not necessary... and what's that `com.gluonhq.helloandroid.MainActivity`? Can we change it?)

You might have noticed that there are two resource folders in the Calculator project: `src/android/res and src/main/resources/calculator`. In the first, you will find "mipmap" subfolders. They contain versions of the app icon used by the app in different resolutions. They can be accessed through the `@mipmap` alias in the manifest file. If we store a PNG file there, it will be used as the app icon instead of the default Gluon icon. And basically, this is all we use this first resource folder for. It's done this way because the "mipmap" icons need to be accessed by Android. All other drawables, which are used inside the app, should be stored in the main resources folder, i.e. `src/main/resources/calculator` and its subfolders. 
