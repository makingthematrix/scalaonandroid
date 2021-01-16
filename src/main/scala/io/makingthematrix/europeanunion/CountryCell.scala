/*
 * Copyright (c) 2016, 2020, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *   * Neither the name of Gluon, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Based on https://github.com/gluonhq/gluon-samples/tree/master/fiftystates
 */

package io.makingthematrix.europeanunion

import com.gluonhq.charm.glisten.control.CharmListCell
import com.gluonhq.charm.glisten.control.ListTile
import io.makingthematrix.europeanunion.model.Country
import javafx.geometry.Insets
import javafx.scene.image.ImageView
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii}
import javafx.scene.paint.Color

object CountryCell {
  def apply(): CountryCell = new CountryCell(new ListTile(), new ImageView())

  val ScotlandBackgroundFill = new BackgroundFill(Color.BEIGE, new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0))
}

final class CountryCell(tile: ListTile, imageView: ImageView) extends CharmListCell[Country] {
  imageView.setFitHeight(15)
  imageView.setFitWidth(25)
  tile.setPrimaryGraphic(imageView)
  setText(null)

  override def updateItem(item: Country, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    (Option(item), empty) match {
      case (Some(state), false) =>
        tile.textProperty.setAll(state.toString)
        tile.setWrapText(true)
        Country.getImage(item.flag).foreach(imageView.setImage)
        setGraphic(tile)
        if (state == Country.Scotland) tile.setBackground(new Background(CountryCell.ScotlandBackgroundFill))
      case _ =>
        setGraphic(null)
    }
  }
}