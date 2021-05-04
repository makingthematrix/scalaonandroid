import com.wire.signals.{DispatchQueue, EventContext, EventStream, Signal, Subscription}
import javafx.application.Platform

import scala.util.chaining.scalaUtilChainingOps

package object hellofxml {

  final class UiDispatchQueue extends DispatchQueue {
    override def execute(runnable: Runnable): Unit = Platform.runLater(runnable)
  }

  private var _ui: Option[DispatchQueue] = None
  implicit lazy val Ui: DispatchQueue = _ui match {
    case Some(ui) => ui
    case None     => new UiDispatchQueue().tap(setUi)
  }

  def setUi(ui: DispatchQueue): Unit = this._ui = Some(ui)

  implicit final class RichSignal[V](val signal: Signal[V]) extends AnyVal {
    def onUi(subscriber: V => Unit)(implicit context: EventContext = EventContext.Global): Subscription =
      signal.on(Ui)(subscriber)(context)
  }

  implicit final class RichEventStream[E](val stream: EventStream[E]) extends AnyVal {
    def onUi(subscriber: E => Unit)(implicit context: EventContext = EventContext.Global): Subscription =
      stream.on(Ui)(subscriber)(context)
  }
}
