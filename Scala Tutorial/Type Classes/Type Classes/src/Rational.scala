
class Rational(x: Int, y: Int) {

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
  private val g                        = gcd(x, y)

  lazy val numer: Int = x / g
  lazy val denom: Int = y / g

}
