import org.scalatest.{FlatSpec, Matchers}

object FunctionalLoops extends FlatSpec with Matchers {
 def factorial(n: Int): Int =
  if (/*insert the corner case condition for the factorial evaluation*/) 1
  else //insert the common case factorial expression here

  def main(args: Array[String]): Unit = {
    println(factorial(5))
  }
}