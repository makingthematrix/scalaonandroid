object StandardLibrary{
  val cond: (Int, Int) => Boolean = /*insert the correct condition for evaluating if the list is sorted*/
  def insert(x: Int, xs: List[Int]): List[Int] =
    xs match {
      case List() => x :: // TODO
      case y :: ys =>
        if (cond(x, y)) x :: y :: ys
        else y :: insert(x, ys)
    }

  def main(args: Array[String]): Unit = {
    println(insert(4, 1::8::Nil))
  }
}