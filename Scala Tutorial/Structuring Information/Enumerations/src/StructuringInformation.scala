import org.scalatest.{FlatSpec, Matchers}

object StructuringInformation extends FlatSpec with Matchers {
  sealed trait Duration
  case object Whole   extends Duration
  case object Half    extends Duration
  case object Quarter extends Duration

  def fractionOfWhole(duration: Duration): Double =
    duration match {
      case Whole   => 1.0
      case Half    => //return half of a whole
      /*this should be one of the casesq, for a quarter duration*/ => 0.25
    }

  def main(args: Array[String]): Unit = {
    println(fractionOfWhole(Quarter))
    println(fractionOfWhole(Half))
    println(fractionOfWhole(Whole))
  }
}