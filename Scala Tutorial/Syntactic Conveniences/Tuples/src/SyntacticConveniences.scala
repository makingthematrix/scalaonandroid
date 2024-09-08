import org.scalatest.{FlatSpec, Matchers}

object SyntacticConveniences extends FlatSpec with Matchers {
  def pair(i: Int, s: String): (Int, String) = /*create the tuple here*/

  def main(args: Array[String]): Unit = {
    val is: (Int, String) = pair(42, "foo")

    is match {
      case (i, s) =>
        println(i)
        println(s)
    }

    val (i, s) = is
    println(i)
    println(s)

    println(is._1)
    println(is._2)
  }
}