object SyntacticConveniences{
  def greet(name: String): String =
    /*insert the correct prefix here*/"Hello, $name!"

  def greetLouder(name: String): String =
    s"Hello, /*introduce the dynamic value here and make it uppercase*/!"

  def main(args: Array[String]): Unit = {
    println(greet("Scala"))
    println(greet("Functional Programming"))
    println(greetLouder("Scala"))
  }
}