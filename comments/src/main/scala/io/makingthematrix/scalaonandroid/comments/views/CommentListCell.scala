package io.makingthematrix.scalaonandroid.comments.views

import com.gluonhq.charm.glisten.control.{Alert, ListTile}
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon
import io.makingthematrix.scalaonandroid.comments.model.Comment
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Button, ButtonType, ListCell}
import javafx.scene.layout.VBox

case class CommentListCell() extends ListCell[Comment] {
  private val tile = new ListTile
  private var comment: Comment = _

  private val button = MaterialDesignIcon.DELETE.button(_ => showDialog(comment))
  tile.setSecondaryGraphic(new VBox(button))

  override def updateItem(
    item:  Comment,
    empty: Boolean
  ): Unit = {
    super.updateItem(item, empty)
    comment = item
    if (!empty && item != null) {
      tile.textProperty().setAll(item.author, item.content)
      setGraphic(tile)
    } else {
      setGraphic(null)
    }
  }

  def showDialog(item: Comment): Unit = {
    val alert = new Alert(AlertType.CONFIRMATION)
    alert.setTitleText("Confirm Deletion")
    alert.setContentText("This commentwil be deleted permanently.\nDo you want to continue?")

    val yes = new Button("yes, delete permanently")
    yes.setOnAction(_ => {
      alert.setResult(ButtonType.YES)
      alert.hide()
    })
    yes.setDefaultButton(true)

    val no = new Button("No")
    no.setCancelButton(true)
    no.setOnAction(_ => {
      alert.setResult(ButtonType.NO)
      alert.hide()
    })

    alert.getButtons.setAll(yes, no)

    import scala.jdk.javaapi.OptionConverters._
    val result: Option[ButtonType] = toScala(alert.showAndWait())

    if (result.contains(ButtonType.YES)) {
      listViewProperty.get.getItems.remove(item)
    }

  }

}
