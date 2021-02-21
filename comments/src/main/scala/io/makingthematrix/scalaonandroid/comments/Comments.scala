package io.makingthematrix.scalaonandroid.comments

import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.visual.Swatch
import io.makingthematrix.scalaonandroid.comments.views.AppViewManager
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

object Comments {
  def main(args: Array[String]): Unit = {
    //In scala we need to pass the class of the comments class, not of the companion object (which ends with $)
    javafx.application.Application.launch(classOf[Comments], args: _*)
  }
}

class Comments extends MobileApplication {
  override def init(): Unit = {
    AppViewManager.registerViews(this)
  }

  override def postInit(scene: Scene): Unit = {
    AppViewManager.registerDrawer(this)
    Swatch.BLUE.assignTo(scene)
    scene.getWindow
      .asInstanceOf[Stage].getIcons.add(new Image(this.getClass.getResourceAsStream("/icon.png")))
  }
}
