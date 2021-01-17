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

package io.makingthematrix.scalaonandroid.europeanunion

import com.gluonhq.charm.glisten.control.CharmListCell
import com.gluonhq.charm.glisten.control.ListTile
import io.makingthematrix.scalaonandroid.europeanunion.CountryCell.countryToString
import io.makingthematrix.scalaonandroid.europeanunion.model.Country
import javafx.geometry.Insets
import javafx.scene.image.ImageView
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii}
import javafx.scene.paint.Color

object CountryCell {
  def apply(): CountryCell = new CountryCell(new ListTile(), new ImageView())

  val ScotlandBackground = new Background(
    new BackgroundFill(Color.BEIGE, new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0))
  )

  val StandardBackround = new Background(
    new BackgroundFill(Color.WHITE, new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0))
  )

  def countryToString(country: Country): String = {
    import country._
    s"""
      $name ($abbr),
      Capital: $capital,
      Population (M): ${String.format("%.2f", population / 1_000_000d)},
      Area (km^2): $area,
      Density (pop/km^2): ${String.format("%.1f", density)}
    """.stripMargin
  }
}

final class CountryCell(tile: ListTile, imageView: ImageView) extends CharmListCell[Country] {
  import CountryCell._
  imageView.setFitHeight(15)
  imageView.setFitWidth(25)
  tile.setPrimaryGraphic(imageView)
  setText(null)

  /* In Android, cells of a scrollable list can be re-used when they leave the screen.
     Instead of  just sitting there in the memory, waiting to show up again, or being destroyed and recreated,
     Android can change their data to something else and display them as those which show up on the screen while
     the original ones are scrolled out of the screen. It saves resources, but goodbye FP.
     (In short, that's why the country's data is set here in `updateItem` instead of the constructor - it can be changed).
  */
  override def updateItem(item: Country, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    (Option(item), empty) match {
      case (Some(country), false) =>
        tile.textProperty.setAll(countryToString(country))
        tile.setWrapText(true)
        CountriesRepository.getImage(item.flag).foreach(imageView.setImage)
        setGraphic(tile)
        tile.setBackground(
          if (country == CountriesRepository.Scotland) ScotlandBackground
          else StandardBackround
        )
      case _ =>
        setGraphic(null)
    }
  }
}