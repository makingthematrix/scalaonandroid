/*
 * Copyright (c) 2016, 2020, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of Gluon, any associated website, nor the
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
 */
package io.makingthematrix.fiftystates

import com.gluonhq.charm.glisten.control._
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon
import io.makingthematrix.fiftystates.model.Density.Density
import io.makingthematrix.fiftystates.model.{Density, USState, USStates}
import javafx.collections.transformation.FilteredList
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{MenuItem, RadioMenuItem, ToggleGroup}

import java.util.function.Predicate
import scala.jdk.CollectionConverters._
import scala.jdk.FunctionConverters._

object BasicView {
  def apply(): BasicView = {
    val filteredList = new FilteredList(USStates.statesList, (_: USState) => true)
    val charmListView = createCharmListView(filteredList)
    returning(new BasicView(filteredList, charmListView)) { _.setCenter(charmListView) }
  }

  private def createCharmListView(filteredList: FilteredList[USState]) =
    returning(new CharmListView[USState, Density](filteredList)) { view =>
      view.setCellFactory((_: CharmListView[USState, Density]) => USStateCell())
      view.setHeadersFunction((Density.stateToDensity _).asJava)
      view.setHeaderCellFactory((_: CharmListView[USState, Density])  => new CharmListCell[USState]() {
        private final val tile = new ListTile()
        private final val usFlag = new Avatar(16)

        tile.setPrimaryGraphic(usFlag)

        override def updateItem(item: USState, empty: Boolean) {
          super.updateItem(item, empty)
          if (!empty) {
            USStates.getImage(USStates.usState.flag).foreach(flag => usFlag.setImage(flag))
            tile.textProperty.setAll(USStates.usState.toString)
            // tile.setWrapText(true) causes the header to expand and cover the whole screen
            // TODO: Find out how to handle it. CSS? SceneBuilder?
            setGraphic(tile)
          }
        }
      })
    }
}

class BasicView(filteredList: FilteredList[USState], charmListView: CharmListView[USState, Density]) extends View {
  private var ascending = true

  override def updateAppBar(appBar: AppBar): Unit = {
    appBar.setNavIcon(MaterialDesignIcon.STAR.button)
    appBar.setTitleText("50 States")

    appBar.getActionItems.add(MaterialDesignIcon.SORT.button({ _ =>
      if (ascending) {
        charmListView.setHeaderComparator((d1: Density, d2: Density) => d1.initial - d2.initial)
        charmListView.setComparator((s1: USState, s2: USState) => (s1.density - s2.density).toInt)
        ascending = false
      } else {
        charmListView.setHeaderComparator((d1: Density, d2: Density) => d2.initial - d1.initial)
        charmListView.setComparator((s1: USState, s2: USState) => (s2.density - s1.density).toInt)
        ascending = true
      }
    }))

    appBar.getMenuItems.setAll(buildFilterMenu.asJavaCollection)
  }

  private def getStatePredicate(population: Double): Predicate[USState] =
    (state: USState) => state.population >= population * 1_000_000

  private def buildFilterMenu = {
    val menuActionHandler: EventHandler[ActionEvent] = { e: ActionEvent =>
      val item = e.getSource.asInstanceOf[MenuItem]
      val population = item.getUserData.asInstanceOf[Double]
      filteredList.setPredicate(getStatePredicate(population))
    }

    val allStates = returning(new RadioMenuItem("All States")) { item =>
      item.setOnAction(menuActionHandler)
      item.setSelected(true)
    }

    val toggleGroup = returning(new ToggleGroup()) { _.getToggles.add(allStates) }

    allStates :: List(0.5, 1.0, 2.5, 5.0).map { d =>
      returning(new RadioMenuItem("Population > " + d + "M")) { item =>
        item.setUserData(d)
        item.setOnAction(menuActionHandler)
        toggleGroup.getToggles.add(item)
      }
    }
  }
}
