package io.makingthematrix.fiftystates

import javafx.application.Platform
import javafx.event.{ActionEvent, EventHandler}

object Utils {

  def actionEventHandler(handler: ActionEvent => Unit): EventHandler[ActionEvent] =
    (e: ActionEvent) => Platform.runLater(() => handler(e))

  @inline def returning[T](t: => T)(body: T => Any): T = {
    body(t)
    t
  }
}
