import org.scalatest.{FlatSpec, Matchers}

object StructuringInformation extends FlatSpec with Matchers {

  sealed trait Symbol
  case class Note(name: String, duration: String, octave: Int) extends Symbol
  case class Rest(duration: String) //extend the sealed trait Symbol here

  def symbolDuration(symbol: Symbol): String =
    symbol match {
    case Note(name, duration, octave) => //we need to return a similar to the Rest field
    case Rest(duration) => duration
  }

  def caseClassEquals(note: Note, otherNote: Note): Boolean = {
    //return a result of equality check here
  }

  def main(args: Array[String]): Unit = {
    val c3      = Note("C", "Quarter", 3)
    val otherC3 = Note("C", "Quarter", 3)
    val f3      = Note("F", "Quarter", 3)
    val wholeRest = Rest("Whole")

    println("c3 equals to otherC3: " + caseClassEquals(c3, otherC3))
    println("The duration of the c3 is: " + symbolDuration(c3))
    println("c3 equals to f3: " + caseClassEquals(c3, f3))
    println("The duration of the wholeRest is: " + symbolDuration(wholeRest))
  }
}