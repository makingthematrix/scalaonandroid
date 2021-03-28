package comments.views

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

  private def id(presenterClass: Class[_ <: GluonPresenter[_]]): String =
    presenterClass.getSimpleName.toUpperCase(Locale.ROOT).replace("PRESENTER", "")

  private def view(title: String,
                   presenterClass: Class[_ <: GluonPresenter[_]],
                   menuIcon: MaterialDesignIcon,
                   flags: Flag*
                  ) = {
    registry.createView(id(presenterClass), title, presenterClass, menuIcon, flags: _*)
  }

  def registerViews(app: MobileApplication): Unit = {
    registry.getViews.asScala.foreach(v => {
      v.registerView(app)
    })
  }

  def registerDrawer(app: MobileApplication): Unit = {
    val header = new NavigationDrawer.Header(
      "Gluon Mobile",
      "The Comments App",
      new Avatar(21, new Image(classOf[AppViewManager].getResourceAsStream("/icon.png")))
    )
    Utils.buildDrawer(app.getDrawer, header, registry.getViews)
  }

}

class AppViewManager
