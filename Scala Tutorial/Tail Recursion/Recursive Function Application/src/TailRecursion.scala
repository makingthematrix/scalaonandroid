import scala.annotation.tailrec

object TailRecursion{

  def factorial(n: Int): Int = {
    @//you need to complete the tail recursion annotation here
    def iter(x: Int, result: Int): Int =
      if (x == 0) result
      else iter(x - 1, result * x)

    iter(n, 1)
  }


  def main(args: Array[String]): Unit = {
    println(factorial(5))
  }
}