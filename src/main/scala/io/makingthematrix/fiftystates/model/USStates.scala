/*
 * Copyright (c) 2016, 2020, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of Gluon, any associated website, nor the
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
package io.makingthematrix.fiftystates.model
import com.gluonhq.attach.cache.CacheService
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.image.Image

import scala.jdk.OptionConverters._
/**
 *
 * List of US USStates
 Source: https://en.wikipedia.org/wiki/List_of_states_and_territories_of_the_United_States
 Flag images available under the Creative Commons CC0 1.0 Universal Public Domain Dedication
 *
 */
object USStates {
  private val cache = CacheService.create().toScala.map(_.getCache[String, Image]("images"))

  private final val URL_PATH = "https://upload.wikimedia.org/wikipedia/commons/thumb/"

  val statesList: ObservableList[USState] = FXCollections.observableArrayList(
    USState("Alabama", "AL", "Montgomery", 4903185, 135767, "5/5c/Flag_of_Alabama.svg/23px-Flag_of_Alabama.svg.png"),
    USState("Alaska", "AK", "Juneau", 731545, 1723337, "e/e6/Flag_of_Alaska.svg/21px-Flag_of_Alaska.svg.png"),
    USState("Arizona", "AZ", "Phoenix", 7278717, 295233, "9/9d/Flag_of_Arizona.svg/23px-Flag_of_Arizona.svg.png"),
    USState("Arkansas", "AR", "Little Rock", 3017804, 137733, "9/9d/Flag_of_Arkansas.svg/23px-Flag_of_Arkansas.svg.png"),
    USState("California", "CA", "Sacramento", 39512223, 423968, "0/01/Flag_of_California.svg/23px-Flag_of_California.svg.png"),
    USState("Colorado", "CO", "Denver", 5758736, 269602, "4/46/Flag_of_Colorado.svg/23px-Flag_of_Colorado.svg.png"),
    USState("Connecticut", "CT", "Hartford", 3565287, 14356, "9/96/Flag_of_Connecticut.svg/20px-Flag_of_Connecticut.svg.png"),
    USState("Delaware", "DE", "Dover", 973764, 6446, "c/c6/Flag_of_Delaware.svg/23px-Flag_of_Delaware.svg.png"),
    USState("Florida", "FL", "Tallahassee", 21477737, 170312, "f/f7/Flag_of_Florida.svg/23px-Flag_of_Florida.svg.png"),
    USState("Georgia", "GA", "Atlanta", 10617423, 153910, "5/54/Flag_of_Georgia_%28U.S._state%29.svg/23px-Flag_of_Georgia_%28U.S._state%29.svg.png"),
    USState("Hawaii", "HI", "Honolulu", 1415872, 28314, "e/ef/Flag_of_Hawaii.svg/23px-Flag_of_Hawaii.svg.png"),
    USState("Idaho", "ID", "Boise", 1787065, 216443, "a/a4/Flag_of_Idaho.svg/19px-Flag_of_Idaho.svg.png"),
    USState("Illinois", "IL", "Springfield", 12671821, 149997, "0/01/Flag_of_Illinois.svg/23px-Flag_of_Illinois.svg.png"),
    USState("Indiana", "IN", "Indianapolis", 6732219, 94327, "a/ac/Flag_of_Indiana.svg/23px-Flag_of_Indiana.svg.png"),
    USState("Iowa", "IA", "Des Moines", 3155070, 145746, "a/aa/Flag_of_Iowa.svg/22px-Flag_of_Iowa.svg.png"),
    USState("Kansas", "KS", "Topeka", 2913314, 213099, "d/da/Flag_of_Kansas.svg/23px-Flag_of_Kansas.svg.png"),
    USState("Kentucky", "KY", "Frankfort", 4467673, 104656, "8/8d/Flag_of_Kentucky.svg/23px-Flag_of_Kentucky.svg.png"),
    USState("Louisiana", "LA", "Baton Rouge", 4648794, 135658, "e/e0/Flag_of_Louisiana.svg/23px-Flag_of_Louisiana.svg.png"),
    USState("Maine", "ME", "Augusta", 1344212, 91634, "3/35/Flag_of_Maine.svg/23px-Flag_of_Maine.svg.png"),
    USState("Maryland", "MD", "Annapolis", 6045680, 32131, "a/a0/Flag_of_Maryland.svg/23px-Flag_of_Maryland.svg.png"),
    USState("Massachusetts", "MA", "Boston", 6892503, 27335, "f/f2/Flag_of_Massachusetts.svg/23px-Flag_of_Massachusetts.svg.png"),
    USState("Michigan", "MI", "Lansing", 9986857, 250488, "b/b5/Flag_of_Michigan.svg/23px-Flag_of_Michigan.svg.png"),
    USState("Minnesota", "MN", "St. Paul", 5639632, 225163, "b/b9/Flag_of_Minnesota.svg/23px-Flag_of_Minnesota.svg.png"),
    USState("Mississippi", "MS", "Jackson", 2976149, 125438, "4/42/Flag_of_Mississippi.svg/23px-Flag_of_Mississippi.svg.png"),
    USState("Missouri", "MO", "Jefferson City", 6137428, 180540, "5/5a/Flag_of_Missouri.svg/23px-Flag_of_Missouri.svg.png"),
    USState("Montana", "MT", "Helena", 1068778, 380832, "c/cb/Flag_of_Montana.svg/23px-Flag_of_Montana.svg.png"),
    USState("Nebraska", "NE", "Lincoln", 1934408, 200330, "4/4d/Flag_of_Nebraska.svg/23px-Flag_of_Nebraska.svg.png"),
    USState("Nevada", "NV", "Carson City", 3080156, 286380, "f/f1/Flag_of_Nevada.svg/23px-Flag_of_Nevada.svg.png"),
    USState("New Hampshire", "NH", "Concord", 1359711, 24214, "2/28/Flag_of_New_Hampshire.svg/23px-Flag_of_New_Hampshire.svg.png"),
    USState("New Jersey", "NJ", "Trenton", 8882190, 22592, "9/92/Flag_of_New_Jersey.svg/23px-Flag_of_New_Jersey.svg.png"),
    USState("New Mexico", "NM", "Santa Fe", 2096829, 314917, "c/c3/Flag_of_New_Mexico.svg/23px-Flag_of_New_Mexico.svg.png"),
    USState("New York", "NY", "Albany", 19453561, 141297, "1/1a/Flag_of_New_York.svg/23px-Flag_of_New_York.svg.png"),
    USState("North Carolina", "NC", "Raleigh", 10488084, 139391, "b/bb/Flag_of_North_Carolina.svg/23px-Flag_of_North_Carolina.svg.png"),
    USState("North Dakota", "ND", "Bismarck", 762062, 183107, "e/ee/Flag_of_North_Dakota.svg/21px-Flag_of_North_Dakota.svg.png"),
    USState("Ohio", "OH", "Columbus", 11689100, 116099, "4/4c/Flag_of_Ohio.svg/23px-Flag_of_Ohio.svg.png"),
    USState("Oklahoma", "OK", "Oklahoma City", 3956971, 181038, "6/6e/Flag_of_Oklahoma.svg/23px-Flag_of_Oklahoma.svg.png"),
    USState("Oregon", "OR", "Salem", 4217737, 254800, "b/b9/Flag_of_Oregon.svg/23px-Flag_of_Oregon.svg.png"),
    USState("Pennsylvania", "PA", "Harrisburg", 12801989, 119279, "f/f7/Flag_of_Pennsylvania.svg/23px-Flag_of_Pennsylvania.svg.png"),
    USState("Rhode Island", "RI", "Providence", 1059361, 4002, "f/f3/Flag_of_Rhode_Island.svg/18px-Flag_of_Rhode_Island.svg.png"),
    USState("South Carolina", "SC", "Columbia", 5148714, 82931, "6/69/Flag_of_South_Carolina.svg/23px-Flag_of_South_Carolina.svg.png"),
    USState("South Dakota", "SD", "Pierre", 884659, 199730, "1/1a/Flag_of_South_Dakota.svg/23px-Flag_of_South_Dakota.svg.png"),
    USState("Tennessee", "TN", "Nashville", 6829174, 109152, "9/9e/Flag_of_Tennessee.svg/23px-Flag_of_Tennessee.svg.png"),
    USState("Texas", "TX", "Austin", 28995881, 695660, "f/f7/Flag_of_Texas.svg/23px-Flag_of_Texas.svg.png"),
    USState("Utah", "UT", "Salt Lake City", 3205958, 219882, "f/f6/Flag_of_Utah.svg/23px-Flag_of_Utah.svg.png"),
    USState("Vermont", "VT", "Montpelier", 623989, 24905, "4/49/Flag_of_Vermont.svg/23px-Flag_of_Vermont.svg.png"),
    USState("Virginia", "VA", "Richmond", 8535519, 110787, "4/47/Flag_of_Virginia.svg/22px-Flag_of_Virginia.svg.png"),
    USState("Washington", "WA", "Olympia", 7614893, 184661, "5/54/Flag_of_Washington.svg/23px-Flag_of_Washington.svg.png"),
    USState("West Virginia", "WV", "Charleston", 1792147, 62755, "2/22/Flag_of_West_Virginia.svg/23px-Flag_of_West_Virginia.svg.png"),
    USState("Wisconsin", "WI", "Madison", 5822434, 169634, "2/22/Flag_of_Wisconsin.svg/23px-Flag_of_Wisconsin.svg.png"),
    USState("Wyoming", "WY", "Cheyenne", 578759, 253335, "b/bc/Flag_of_Wyoming.svg/22px-Flag_of_Wyoming.svg.png")
  )

  lazy val usState: USState = USState("United States", "US", "Washington", 328239523, 9833520, "a/a4/Flag_of_the_United_States.svg/320px-Flag_of_the_United_States.svg.png")

  def getImage(imageUrl: String): Option[Image] = (cache, URL_PATH + imageUrl) match {
    case (Some(c), url) if url.nonEmpty =>
      Option(c.get(url)).orElse {
        val cachedImage = new Image(url, true)
        c.put(url, cachedImage)

        // working with JavaFX listeners proved to be a bit tricky - I needed to create a wrapper to simplify dealing with generic types
        // TODO: Another way to solve is to use another library for pictures, e.g. Glide
        cachedImage.errorProperty().addListener(
          new ChangeListenerWrapper[java.lang.Boolean]({ newValue => if (newValue) cache.foreach(_.remove(imageUrl)) })
        )

        Some(cachedImage)
      }
    case _ => None
  }
}
