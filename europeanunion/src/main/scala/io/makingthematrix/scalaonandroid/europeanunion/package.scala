package io.makingthematrix.scalaonandroid

import javafx.beans.value.{ChangeListener, ObservableValue}

import java.util.function.Consumer
import scala.jdk.FunctionWrappers.AsJavaConsumer

package object europeanunion {

  @inline def returning[T](t: T)(body: T => Any): T = {
    body(t)
    t
  }

  final class ChangeListener4S[T](onNewValue: Consumer[T]) extends ChangeListener[T] {
    override def changed(observable: ObservableValue[_ <: T], oldValue: T, newValue: T): Unit = onNewValue.accept(newValue)
  }

  object ChangeListener4S {
    def apply[T](onNewValue: T => Unit): ChangeListener[T] = new ChangeListener4S[T](AsJavaConsumer(onNewValue))
  }

}
