object StructuringInformation{
  case class Note(name: String, duration: String, octave: Int)

  def setUpC3Note(): Note = {
    //the function should return a new note with name C, duration of quarter and a third octave
  }

  def main(args: Array[String]): Unit = {
    val note = setUpC3Note()
    println("Name: " + note.name)
    print("duration: " + note.duration)
    print("octave: " + note.octave)
  }
}