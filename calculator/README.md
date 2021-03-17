##Calculator

This is a simple, but actually practical app for both Android and desktop. It's built on top of the [HelloFXML](https://github.com/makingthematrix/scalaonandroid/tree/main/HelloFXML) example. GUI is based on [a JavaFX tutorial](https://www.youtube.com/watch?v=p5ifU9kkp6g) by Kody Simpson and consists of the main view made in Scene Builder and a history dialog which pops up after clicking a button. The logic is written by me from scratch.

Below I will try to explain how it's all done, but before that I'd like to emphasise that my work here is only about putting together puzzle pieces done by other people:the GraalVM team, the Gluon Mobile team, all contributors to JavaFX, and many more. Even the logic behind parsing and evaluating the expression you can type in the calculator was possible only because thanks to Martin Odersky and the whole Scala team it's so easy to reason about these things in my favourite language.

A screenshot: [link]

The Android APK: [link]

### The main view

The main view is a pretty typical calculator with a field displaying the expression and buttons for numbers, operators, and a few special commands. The layout is stored in `src/main/resources/calculator/main.fxml`. To create it, I used Scene Builder and roughly followed [these two tutorial videos by Kody Simpson](https://youtu.be/p5ifU9kkp6g).
The main element of the view (they are called "nodes" in JavaFX) is a `BorderPane` - a rectangle divided into five regions: top, left, right, bottom, and center. To each region we can add another node. In our case, we use the top region for a `Label` which will display the expression we want to evaluate (and after the evaluation, its result which may become the basis for another expression), and we use the center for a `GridPane`. Other regions won't be used and they will be invisible.
The `GridPane` we use is a grid with 4 columns and 5 rows. In each entry, except one, we place a `Button`. We make those button spread out to take all the space in the entry, and we give each of them an id, and an action: when the button is clicked, the `onEvaluate` method in `MainController` will be called.

Look into the `HelloFXML` example for a short discussion of how FXML views interact with controllers. Basically, each FXML view is connected to a controller class in Scala. The controller contains fields that correspond to FXML nodes with the same ids as the names of the fields, and methods which will be called from the view if we link them to specific actions the user can execute on the nodes. 
While methods are only called in response to actions of the user, the correspondence of fields and nodes goes both ways. For example, if I used a `TextField` instead of a `Label` to display the expression, the user would be able to write in that text field and I would be able to read that input in the Scala code by accessing the corresponding field. But that would make checking if the expression is valid more difficult as the user would have much more freedom to blunder. Instead, the field is a read-only `Label` and changes to its contents is done through clicking buttons. This way, when the user clicks a button, I can check in the Scala code if adding the corresponding number or an operator to the expression is possible before I do it, and if yes, I update the field with the new expression, and that automatically displays the new expression in the view.
The same things happen after the calculator evaluates the expression and returns a result. I use the same `Label` to display the expression and the result. When I obtain the result, I just call the `setText` method on the label and that automatically updates the text displayed on the screen.

### The history dialog

But there's one more thing. Every time the user hits the `=` sign and the expression gets evaluated, it's added to the history list. If the user hits the "History" button, a dialog appears with the history of evaluated expressions. The user can then tap on one of them - the dialog will close and the result of that expression will appear on the screen, at the end of the current expression.
Cool, huh? The calculator doesn't support parentheses but this way as close as it gets.
In the original tutorial video the history dialog is implemented as a "stage" - a view of the same type as the main one. But this does not work well in mobile applications, for reasons I don't want to explore in this example. Instead, I decided to use a `Dialog` class from Gluon's `charm-glisten` library and fill it with a `ListView`. This is also done with Scene Builder, even though the list view is the only node in the whole FXML file `history.fxml`, because I wanted to present how to access a controller from other parts of the code. The main controller in the calculator is only accessed from the view, but otherwise   

2. `pom.xml` differences in comparison to HelloFXML
3. How JavaFX supports the MVC pattern (or maybe MVP?)
4. The reflection issue and how to go around it
5. The Android app icon and other settings
6. what next
