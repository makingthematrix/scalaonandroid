### How to cook an Android app in Scala 2.13

This is a repo for examples, small tutorials, and some chaotic notes on how to write Android apps with GraalVM, Gluon Mobile, JavaFX, and Scala 2.13. As you can imagine, even puttnig this whole tech stack together is not an easy task. But I managed to do this pretty quickly for something that looks so complex, so I decided, why the hell not, I want to share with you how to do it. My idea is to test all the usual functionalities an Android app should have:
 * UI widgets
 * Push notifications (Google Play Services? MicroG? Something else?)
 * Web sockets
 * Access to the camera and microphone
 * Android's KeyStore (e.g. for storing encrypted passwords)
 * Intents from other apps (e.g. for file sharing)
 * The app lifecycle, esp. how to manage going to the background and waking up, receiving an event while in the background, etc.
 * Permissions
 * Execution contexts handling
If any of them don't work and there are no easy alternatives, it means the whole tech stack is not mature yet. But from what I saw so far I'm carefully optimistic. Especially the fact that so many people work on so many parts of all of this, and my work consists mainly of putting it all together, gives me hope. 

My goal for further future (overly optimistic, of course) is to write a TRON game where players could log in to a server, set up a board, play a game with each other (or maybe even three or four people together) and store the history of their games on the backend. For that I would either use [FXGL](https://github.com/AlmasB/FXGL) or [libGDX](https://libgdx.badlogicgames.com/). But that's in a galaxy far away.

#### So, are you ready to try?

1. [What do you need](https://github.com/makingthematrix/scalaonandroid/wiki/What-do-you-need)
2. [How to compile and run an example](https://github.com/makingthematrix/scalaonandroid/wiki/How-to-compile-and-run-an-example)
2.1. [Dummy App](https://github.com/makingthematrix/scalaonandroid/wiki/HelloGluon-example-(aka-DummyApp)) example
2.2. [European Union](https://github.com/makingthematrix/scalaonandroid/wiki/European-Union-(and-Scotland)-example) example
