object ClassesVsCaseClasses {
  val c3 = Note("C", "Quarter", 3)
  val d = Note("D", "Quarter", 3)
  val c4 = c3.copy(octave = 4)

  def main(args: Array[String]): Unit = {
    println(c3.toString)
    println(c3.equals(d))
    println(c4.toString)
  }
}