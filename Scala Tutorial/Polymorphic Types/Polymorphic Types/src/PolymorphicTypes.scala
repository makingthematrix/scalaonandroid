object PolymorphicTypes {
  def size[A](xs: List[A]): Int =
    xs match {
      case Nil     /*insert what we should return in case of Nil*/
      case y :: ys /*insert what we should add for an element of the list*/ + size(ys)
    }

  def main(args: Array[String]): Unit = {
    println(size(Nil))
    println(size(List(1, 2 , 3)))
    println(size(1::3::Nil))
  }
}