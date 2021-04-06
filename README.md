### Scala on Android

This is a repo for examples, small tutorials, and some chaotic notes on how to write Android apps with GraalVM, Gluon Mobile, JavaFX, and Scala 2.13. As you can imagine, even putting this whole tech stack together requires some effort. But I managed to do this pretty quickly for something that looks so complex, so I decided, why the hell not, I want to share with you how to do it. My idea is to test all the usual functionalities an Android app should have:
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

If you're like me and before you start anything you need to know its history and context down to the times fish crawled out of the primordial sea - [here's an overview of history of Scala development on Android and its current status as of 2021](https://makingthematrix.wordpress.com/2021/03/17/scala-on-android/). 

1. [What do you need](https://github.com/makingthematrix/scalaonandroid/wiki/What-do-you-need)
2. [How to compile and run an example](https://github.com/makingthematrix/scalaonandroid/wiki/How-to-compile-and-run-an-example)
3. [HelloGluon](https://github.com/makingthematrix/scalaonandroid/wiki/HelloGluon-example-(aka-DummyApp)) 
4. [European Union](https://github.com/makingthematrix/scalaonandroid/wiki/European-Union-(and-Scotland)-example) 
5. [HelloFXML](https://github.com/makingthematrix/scalaonandroid/wiki/HelloFXML-example) 
6. [Calculator](https://github.com/makingthematrix/scalaonandroid/tree/main/calculator)

#### Discord

There is a #scala-android channel on the official Scala discord: https://discord.gg/UuDawpq7 
And also an #android channel on the "Learning Scala" discord: https://discord.gg/XHMt6Yq4

In both cases, these are channels about all things connected to Scala on Android. I write mostly about the GraalVM stack, but if you're interested in React Native+Scala.js approach, you're more than welcome (actually I need someone to write about that :) ) or if you're into the sbt-android or gradle-scala plugins, or maybe you don't even care that much about Scala but you're interested in one of the other puzzle pieces we can use: GraalVM, Gluon Mobile, Slinky... come and share your ideas :)
