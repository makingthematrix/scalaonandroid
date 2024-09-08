object ImperativeProgramming{
  def factorial(n: Int): Int = {
    var result = /*insert the initial resul value*/
    var i      = /*insert the initial i value*/
    while (i <= n) {
      result = result * i
      i = i + /*insert the i step*/
    }
    result
  }

  def main(args: Array[String]): Unit = {
    println(factorial(5))
    println(factorial(11))
    println(factorial(15))
  }
}
