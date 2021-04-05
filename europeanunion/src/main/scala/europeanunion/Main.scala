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
package europeanunion

import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.geometry.Dimension2D
import javafx.scene.Scene

import scala.jdk.FunctionConverters._

object Main {
    def main(args: Array[String]): Unit = javafx.application.Application.launch(classOf[Main], args: _*)
}

final class Main extends MobileApplication {
  override def init(): Unit = addViewFactory(MobileApplication.HOME_VIEW, () => BasicView())

  override def postInit(scene: Scene): Unit = {
    Swatch.BLUE.assignTo(scene)
    scene.getStylesheets.add(getClass.getResource("style.css").toExternalForm)
    if (Platform.isDesktop) {
      val dimension2D =
        DisplayService.create.map(((ds: DisplayService) => ds.getDefaultDimensions).asJava).orElse(new Dimension2D(640, 480))
      scene.getWindow.setWidth(dimension2D.getWidth)
      scene.getWindow.setHeight(dimension2D.getHeight)
    }
  }
}
