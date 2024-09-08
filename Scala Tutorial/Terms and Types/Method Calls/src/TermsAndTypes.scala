object TermsAndTypes {

  def upperCaseMethod(): String = {
    "Hello, Scala!".//Invoke a method here making all of the letters uppercase
  }

  def absMethod(): Int = {
    -42.//invoke a method here to return an absolute value of the number
  }

  def main(args: Array[String]): Unit = {
    println(upperCaseMethod())
    println(absMethod())
  }

}