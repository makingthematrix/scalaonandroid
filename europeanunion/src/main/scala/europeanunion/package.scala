import javafx.beans.value.{ChangeListener, ObservableValue}

import java.util.function.Consumer
import scala.jdk.FunctionWrappers.AsJavaConsumer

package object europeanunion:
  final class ChangeListener4S[T](private val onNewValue: Consumer[T]) extends ChangeListener[T]:
    override def changed(observable: ObservableValue[_ <: T], oldValue: T, newValue: T): Unit = onNewValue.accept(newValue)

  object ChangeListener4S:
    def apply[T](onNewValue: T => Unit): ChangeListener[T] = new ChangeListener4S[T](AsJavaConsumer(onNewValue))
