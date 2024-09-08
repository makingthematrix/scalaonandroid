object TermsAndTypes{

  def toHexStringMethod(): String = {
    16.//invoke the toHexString method here
  }

  def containsMethod(): Boolean = {
    (0 to 10)./*invoke the contains method here*/(10)
  }
  def dropMethod(): String = {
    "foo"./*invoke the drop method here*/(1)
  }

  def takeMethod(): String = {
    "bar"./*invoke the take method here*/(2)
  }

  def main(args: Array[String]): Unit = {
    println(toHexStringMethod())
    println(containsMethod())
    println(dropMethod())
    println(takeMethod())
  }
}