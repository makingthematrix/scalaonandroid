package io.makingthematrix.scalaonandroid.comments.views

import com.gluonhq.charm.glisten.afterburner.GluonPresenter
import com.gluonhq.charm.glisten.control.{TextArea, TextField}
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon
import io.makingthematrix.scalaonandroid.comments.Comments
import io.makingthematrix.scalaonandroid.comments.cloud.Service
import io.makingthematrix.scalaonandroid.comments.model.Comment
import javafx.beans.binding.Bindings
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button

import javax.inject.Inject
import scala.beans.BeanProperty

class EditionPresenter extends GluonPresenter[Comments] {
  @Inject
  @BeanProperty
  var service: Service = _

  @FXML
  @BeanProperty
  var edition: View = _

  @FXML
  @BeanProperty
  var authorText: TextField = _

  @FXML
  @BeanProperty
  var commentsText: TextArea = _

  @FXML
  @BeanProperty
  var submit: Button = _

  def initialize(): Unit = {
    edition
      .showingProperty().addListener((_, _, newValue) =>
      if (newValue) {
        val appBar = getApp.getAppBar
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(_ => getApp.getDrawer.open()))
        appBar.setTitleText("Edition")
        submit.setOpacity(1)
      }
    )

    submit
      .disableProperty().bind(
      Bindings.createBooleanBinding(
        { () =>
          authorText.textProperty().isEmpty.or(commentsText.textProperty().isEmpty).get
        },
        authorText.textProperty(),
        commentsText.textProperty()
      )
    )
  }

  @FXML
  def onCancel(event: ActionEvent): Unit = {
    authorText.setText("")
    commentsText.setText("")
    getApp.goHome
  }

  @FXML
  def onSubmit(event: ActionEvent): Unit = {
    service.addComment(Comment(authorText.getText, commentsText.getText))
    authorText.setText("")
    commentsText.setText("")
    getApp.goHome
  }
}
