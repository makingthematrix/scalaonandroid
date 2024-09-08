
## Enumerations

Our above definition of the `Note` type allows users to create instances
with invalid names and durations:

      val invalidNote = Note("not a name", "not a duration", 3)

It is generally a bad idea to work with a model that makes it possible
to reach invalid states. In our case, we want to restrict the space
of the possible note names and durations to a set of fixed alternatives.

In the case of note names, the alternatives are either `A`, `B`, `C`,
`D`, `E`, `F` or `G`. We can express the fact that note names are
a fixed set of alternatives by using a sealed trait, but in contrast to
the previous example, alternatives are not case classes because they
aggregate no information:

      sealed trait NoteName
      case object A extends NoteName
      case object B extends NoteName
      case object C extends NoteName
      …
      case object G extends NoteName

### Algebraic Data Types

Data types defined with sealed trait and case classes are called
*algebraic data types*. An algebraic data type definition can
be thought of as a set of possible values.

Algebraic data types are a powerful way to structure information.

If a concept of your program’s domain can be formulated in terms of
an *is* relationship, you will express it with a sealed trait:

"A symbol *is* either a note *or* a rest."

      sealed trait Symbol
      case class Note(…) extends Symbol
      case class Rest(…) extends Symbol

On the other hand, if a concept of your program’s domain can be
formulated in terms of an *has* relationship, you will express it
with a case class:

"A note *has* a name, a duration, *and* an octave number."

      case class Note(name: String, duration: String, octave: Int) extends Symbol

## Exercise

Consider the following algebraic data type that models note durations.
Complete the implementation of the function `fractionOfWhole`, which
takes a duration as a parameter and returns the corresponding fraction
of the `Whole` duration.
