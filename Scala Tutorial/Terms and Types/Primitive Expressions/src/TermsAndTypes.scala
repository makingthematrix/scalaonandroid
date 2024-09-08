object TermsAndTypes{

  def sayHello(): String = {
    "Hello, " ++ "Scala!"
  }

  def sumTheNumbers(): Int = {
    1 + 3
  }

  def main(args: Array[String]): Unit = {
    println(sayHello())
    println(sumTheNumbers())
  }
}
