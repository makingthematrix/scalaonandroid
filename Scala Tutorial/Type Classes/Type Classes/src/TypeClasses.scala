object TypeClasses {
  /**
    * Returns an integer whose sign communicates how the first parameter
    * compares to the second parameter.
    *
    * The result sign has the following meaning:
    *  - Negative if the first parameter is less than the second parameter
    *  - Positive if the first parameter is greater than the second parameter
    *  - Zero otherwise
    */
  val compareRationals: (Rational, Rational) => Int = /*complete the expression*/

  implicit val rationalOrder: Ordering[Rational] =
    (x: Rational, y: Rational) => compareRationals(x, y)

  def main(args: Array[String]): Unit = {
    val half      = new Rational(1, 2)
    val third     = new Rational(1, 3)
    val fourth    = new Rational(1, 4)
    val rationals = List(third, half, fourth)
    println(Sorting.insertionSort(rationals) == List(fourth, third, half))
  }

}