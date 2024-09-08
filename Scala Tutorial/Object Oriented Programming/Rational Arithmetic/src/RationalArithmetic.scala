import org.scalatest.{FlatSpec, Matchers}

object RationalArithmetic extends FlatSpec with Matchers {
  class Rational(x: Int, y: Int) {
    private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
    private val g = gcd(x, y)
    def numer = x / g
    def denom = y / g
    def + (r: Rational) =
      new Rational(
        numer * r.denom + r.numer * denom,
        denom * r.denom
      )
    def - (r: Rational) =
      new Rational(
        numer * r.denom - r.numer * denom,
        denom * r.denom
      )
    def * (r: Rational) =
      /*create a Rational with appropriate expressions for numerator and denomenator*/
    def / (r: Rational) =
      /*create a Rational with appropriate expressions for numerator and denomenator*/
  }

  def main(args: Array[String]): Unit = {
    val firstRational = new Rational(3, 4)
    val secondRational = new Rational(11, 13)

    println((firstRational * secondRational).numer)
    println((firstRational * secondRational).denom)
    println((firstRational / secondRational).numer)
    println((firstRational / secondRational).denom)
  }
}