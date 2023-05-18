package fxcalculator.functions

import com.gluonhq.charm.glisten.control.{CharmListCell, ListTile}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{HBox, Priority}
import javafx.scene.input.MouseEvent
import fxcalculator.utils.Resource.*

import scala.util.chaining.scalaUtilChainingOps

object FunctionCell:
  private lazy val trashIcon = new Image(stream(ClosePng))

final class FunctionCell(selectMe: FunctionEntry => Unit, deleteMe: FunctionEntry => Unit) extends CharmListCell[FunctionEntry]:
  private val tile = new ListTile().tap { t =>
    t.setWrapText(true)
    HBox.setHgrow(t, Priority.ALWAYS)
  }

  private val imageView = new ImageView().tap { view =>
    view.setFitWidth(15)
    view.setPreserveRatio(true)
    view.setImage(FunctionCell.trashIcon)
  }

  /* In Android, cells of a scrollable list can be re-used when they leave the screen.
     Instead of  just sitting there in the memory, waiting to show up again, or being destroyed and recreated,
     Android can change their data to something else and display them as those which show up on the screen while
     the original ones are scrolled out of the screen. It saves resources, but goodbye FP.
     (In short, that's why the entry's data is set here in `updateItem` instead of the constructor - it can be changed).
  */
  override def updateItem(item: FunctionEntry, empty: Boolean): Unit =
    super.updateItem(item, empty)
    if !empty then
      tile.textProperty.setAll(item.textForm)
      tile.setOnMouseClicked((_: MouseEvent) => selectMe(item))
      if item.isCustom then
        imageView.setOnMouseClicked((_: MouseEvent) => deleteMe(item))
        setGraphic(new HBox(tile, imageView))
      else
        setGraphic(tile)
