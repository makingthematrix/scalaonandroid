import org.scalatest.{FlatSpec, Matchers}

object LazyEvaluation extends FlatSpec with Matchers {
  val builder = new StringBuilder
  val x      = { builder += 'x'; 1 }
  /*complete the declaration for y to be executed lazy*/val y = { builder += 'y'; 2 }
  def z      = { builder += 'z'; 3 }

  def main(args: Array[String]): Unit = {

    println(z + y + x + z + y + x)

    println(builder.result())
  }

}