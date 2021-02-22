package io.makingthematrix.scalaonandroid.comments

object Launcher {
  def main(args: Array[String]): Unit = {
    //In scala we need to pass the class of the comments class, not of the companion object (which ends with $)
    javafx.application.Application.launch(classOf[Comments], args: _*)
  }
}
