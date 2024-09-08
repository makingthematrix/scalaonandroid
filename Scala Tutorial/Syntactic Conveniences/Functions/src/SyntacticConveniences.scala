object SyntacticConveniences {
  def function(xs: Array[Int], ys: Array[Int]): Array[(Int, Int)] = {
    for {
      x <- xs if x % 2 == 0
      y <- ys
    } /*complete the function*/
  }

  def functionDesugared(xs: Array[Int], ys: Array[Int]): Array[(Int, Int)] = {
    xs.filter { x =>
      x % 2 == 0
    }.flatMap { x =>
      ys.map { y =>
        (x, y)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println("function output:")
    println(function(Array(1, 2, 3, 4), Array(1, 2, 3)).deep.mkString("\n"))
  }
}