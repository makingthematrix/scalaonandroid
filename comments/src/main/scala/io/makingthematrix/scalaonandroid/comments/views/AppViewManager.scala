package io.makingthematrix.scalaonandroid.comments.views

import com.gluonhq.charm.glisten.afterburner.AppView.Flag
import com.gluonhq.charm.glisten.afterburner.AppView.Flag._
import com.gluonhq.charm.glisten.afterburner.{AppView, AppViewRegistry, GluonPresenter, Utils}
import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.control.{Avatar, NavigationDrawer}
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon
import javafx.scene.image.Image

import java.util.Locale
import scala.jdk.CollectionConverters._

object AppViewManager {
  val registry: AppViewRegistry = new AppViewRegistry()
  val commentsView: AppView = view(
    "Comments",
    classOf[CommentsPresenter],
    MaterialDesignIcon.COMMENT,
    SHOW_IN_DRAWER,
    SKIP_VIEW_STACK,
    HOME_VIEW
  )
  val editionView: AppView =
    view("Edition", classOf[EditionPresenter], MaterialDesignIcon.EDIT, SHOW_IN_DRAWER)

  private def view(
                    title: String,
                    presenterClass: Class[_ <: GluonPresenter[_]],
                    menuIcon: MaterialDesignIcon,
                    flags: Flag*
                  ) =
    registry.createView(name(presenterClass), title, presenterClass, menuIcon, flags: _*)

  private def name(presenterClass: Class[_ <: GluonPresenter[_]]) =
    presenterClass.getSimpleName.toUpperCase(Locale.ROOT).replace("PRESENTER", "")

  def registerDrawer(app: MobileApplication): Unit =
    registry.getViews.asScala.foreach(_.registerView(app))

  def registerViews(app: MobileApplication): Unit = {
    val header = new NavigationDrawer.Header(
      "Gluon Mobile",
      "The Comments App",
      new Avatar(21, new Image(AppViewManager.getClass.getResourceAsStream("/icon.png")))
    )
    Utils.buildDrawer(app.getDrawer, header, registry.getViews)
  }

}
