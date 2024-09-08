object TermsAndTypes {

  def staticTyping(): Range.Inclusive =
    1 to //insert the upper boundary of the range here, it should be of an appropriate type

  def main(args: Array[String]): Unit = {
    println(staticTyping())
  }
}