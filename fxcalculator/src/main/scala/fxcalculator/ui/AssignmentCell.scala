package fxcalculator.ui

import com.gluonhq.charm.glisten.control.{CharmListCell, ListTile}
import fxcalculator.logic.AssignmentEntry
import fxcalculator.utils.Resource.*
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{HBox, Priority}

import scala.util.chaining.scalaUtilChainingOps

object AssignmentCell:
  private lazy val closeIcon = new Image(stream(ClosePng))

final class AssignmentCell(selectMe: AssignmentEntry => Unit, deleteMe: AssignmentEntry => Unit) extends CharmListCell[AssignmentEntry]:
  private val tile = new ListTile().tap { t =>
    t.setWrapText(true)
    HBox.setHgrow(t, Priority.ALWAYS)
  }

  private val imageView = new ImageView().tap { view =>
    view.setFitWidth(15)
    view.setPreserveRatio(true)
    view.setImage(AssignmentCell.closeIcon)
  }

  /* In Android, cells of a scrollable list can be re-used when they leave the screen.
     Instead of  just sitting there in the memory, waiting to show up again, or being destroyed and recreated,
     Android can change their data to something else and display them as those which show up on the screen while
     the original ones are scrolled out of the screen. It saves resources, but goodbye FP.
     (In short, that's why the entry's data is set here in `updateItem` instead of the constructor - it can be changed).
  */
  override def updateItem(item: AssignmentEntry, empty: Boolean): Unit =
    super.updateItem(item, empty)
    if !empty then
      tile.textProperty.setAll(item.textForm)
      tile.setOnMouseClicked((_: MouseEvent) => selectMe(item))
      if item.isCustom then
        imageView.setOnMouseClicked((_: MouseEvent) => deleteMe(item))
        setGraphic(new HBox(tile, imageView))
      else
        setGraphic(tile)
