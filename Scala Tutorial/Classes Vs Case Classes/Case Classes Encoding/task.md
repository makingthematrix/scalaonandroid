## Case Classes Encoding

We saw the main differences between classes and case classes.

It turns out that case classes are just a special case of classes,
whose purpose is to aggregate several values into a single value.

The Scala language provides explicit support for this use case
because it is very common in practice.

So, when we define a case class, the Scala compiler defines a class
enhanced with some more methods and a companion object.

For instance, look at the following case class definition:

      case class Note(name: String, duration: String, octave: Int)

It expands to the following class definition:

      class Note(_name: String, _duration: String, _octave: Int) extends Serializable {
    
        // Constructor parameters are promoted to members
        val name = _name
        val duration = _duration
        val octave = _octave
    
        // Equality redefinition
        override def equals(other: Any): Boolean = other match {
          case that: Note =>
            (that canEqual this) &&
              name == that.name &&
              duration == that.duration &&
              octave == that.octave
          case _ => false
        }
    
        def canEqual(other: Any): Boolean = other.isInstanceOf[Note]
    
        // Java hashCode redefinition according to equality
        override def hashCode(): Int = {
          val state = Seq(name, duration, octave)
          state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
        }
    
        // toString redefinition to return the value of an instance instead of its memory addres
        override def toString = s"Note($name,$duration,$octave)"
    
        // Create a copy of a case class, with potentially modified field values
        def copy(name: String = name, duration: String = duration, octave: Int = octave): Note =
          new Note(name, duration, octave)
    
      }
    
      object Note {
    
        // Constructor that allows the omission of the `new` keyword
        def apply(name: String, duration: String, octave: Int): Note =
          new Note(name, duration, octave)
    
        // Extractor for pattern matching
        def unapply(note: Note): Option[(String, String, Int)] =
          if (note eq null) None
          else Some((note.name, note.duration, note.octave))
    
      }
