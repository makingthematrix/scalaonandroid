object LexicalScopes {
  def objectScopes(): Int = {
    object Foo {
      val x = 1
    }

    object Bar {
      val x = 2
    }

    object Baz {

      import Bar.x

      val y = /*insert the x variable from the Bar object here*/ + /*here you need to insert the x value from the Foo object*/
    }

    Baz.y
  }

  def main(args: Array[String]): Unit = {
    println(objectScopes())
  }
}