object LexicalScopes {
  def scopeRules(): Unit = {
    val x = 0

    def /*declare a name for the function here*/(y: Int) = y + 1

    val result = {
      val x = /*use the name you declared previously*/(3)
      x * x
    } + x

    println(result)
  }

  def main(args: Array[String]): Unit = {
    scopeRules()
  }
}