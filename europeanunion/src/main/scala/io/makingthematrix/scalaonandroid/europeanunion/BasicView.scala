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

import com.gluonhq.charm.glisten.control._
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon
import io.makingthematrix.scalaonandroid.europeanunion.model.Density.Density
import io.makingthematrix.scalaonandroid.europeanunion.model.{Country, Density}
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{MenuItem, RadioMenuItem, ToggleGroup}
import javafx.scene.image.Image

import java.util.function.Predicate
import scala.jdk.CollectionConverters._
import scala.jdk.FunctionConverters._

object BasicView {
  import CountriesRepository._

  def apply(): BasicView = {
    val filteredList = new FilteredList(
      FXCollections.observableArrayList((euCountries :+ Scotland).asJavaCollection),
      (_: Country) => true
    )
    val listView = createListView(filteredList)
    returning(new BasicView(filteredList, listView)) { _.setCenter(listView) }
  }

  private def createListView(filteredList: FilteredList[Country]) =
    returning(new CharmListView[Country, Density](filteredList)) { view =>
      view.setCellFactory((_: CharmListView[Country, Density]) => CountryCell())
      view.setHeadersFunction((Density.getDensity _).asJava)
      view.setHeaderCellFactory((_: CharmListView[Country, Density])  => new CharmListCell[Country]() {
        private final val tile = new ListTile()

        override def updateItem(item: Country, empty: Boolean) {
          super.updateItem(item, empty)
          if (!empty) {
            val euFlag = new Avatar(23, new Image(getClass.getResourceAsStream("100px-Flag_of_Europe.svg.png")))
            tile.setPrimaryGraphic(euFlag)
            tile.textProperty.setAll(CountryCell.countryToString(EuropeanUnion))
            // tile.setWrapText(true) causes the header to expand and cover the whole screen
            // TODO: Find out how to handle it. CSS? SceneBuilder?
            setGraphic(tile)
          }
        }
      })
    }
}

final class BasicView(filteredList: FilteredList[Country], listView: CharmListView[Country, Density]) extends View {
  private var ascending = true

  override def updateAppBar(appBar: AppBar): Unit = {
    appBar.setNavIcon(MaterialDesignIcon.STAR.graphic)
    appBar.setTitleText("European Union")

    appBar.getActionItems.add(MaterialDesignIcon.SORT.button({ _ =>
      if (ascending) {
        listView.setHeaderComparator((d1: Density, d2: Density) => d1.initial - d2.initial)
        listView.setComparator((s1: Country, s2: Country) => (s1.density - s2.density).toInt)
        ascending = false
      } else {
        listView.setHeaderComparator((d1: Density, d2: Density) => d2.initial - d1.initial)
        listView.setComparator((s1: Country, s2: Country) => (s2.density - s1.density).toInt)
        ascending = true
      }
    }))

    appBar.getMenuItems.setAll(buildFilterMenu.asJavaCollection)
  }

  private def buildFilterMenu = {
    def getStatePredicate(population: Double): Predicate[Country] =
      (state: Country) => state.population >= population * 1_000_000

    val menuActionHandler: EventHandler[ActionEvent] = { e: ActionEvent =>
      val item = e.getSource.asInstanceOf[MenuItem]
      val population = item.getUserData.asInstanceOf[Double]
      filteredList.setPredicate(getStatePredicate(population))
    }

    val allStates = returning(new RadioMenuItem("All Countries")) { item =>
      item.setOnAction(menuActionHandler)
      item.setSelected(true)
    }

    val toggleGroup = returning(new ToggleGroup()) { _.getToggles.add(allStates) }

    allStates :: List(1.0, 5.0, 10.0, 20.0).map { d =>
      returning(new RadioMenuItem("Population > " + d + "M")) { item =>
        item.setUserData(d)
        item.setOnAction(menuActionHandler)
        toggleGroup.getToggles.add(item)
      }
    }
  }
}
