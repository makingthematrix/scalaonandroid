
## Defining Alternatives With Sealed Traits

If we look at the introductory picture, we see that musical symbols
can be either *notes* or *rests* (but nothing else).

So, we want to introduce the concept of *symbol* as something
that can be embodied by a fixed set of alternatives: a note or rest.
We can express this in Scala using a *sealed trait* definition:

     sealed trait Symbol
     case class Note(name: String, duration: String, octave: Int) extends Symbol
     case class Rest(duration: String) extends Symbol

A sealed trait definition introduces a new type (here, `Symbol`), but no
constructor for it. Constructors are defined by alternatives that
*extend* the sealed trait:

    val symbol1: Symbol = Note("C", "Quarter", 3)
    val symbol2: Symbol = Rest("Whole")

### Pattern Matching

Since the `Symbol` type has no members, we can not do anything
useful when we manipulate one. We need a way to distinguish between
the different cases of symbols. *Pattern matching* allows us
to do so:

      def symbolDuration(symbol: Symbol): String =
        symbol match {
          case Note(name, duration, octave) => duration
          case Rest(duration) => duration
        }

The above `match` expression first checks if the given `Symbol` is a
`Note`, and if it is the case, it extracts its fields (`name`, `duration`,
and `octave`) and evaluates the expression at the right of the arrow.
Otherwise, it checks if the given `Symbol` is a `Rest`, and if it
is the case, it extracts its `duration` field and evaluates the
expression at the right of the arrow.

When we write `case Rest(duration) => …`, we say that `Rest(…)` is a
*constructor pattern*: it matches all the values of type `Rest`
that have been constructed with arguments matching the pattern `duration`.

The pattern `duration` is called a *variable pattern*. It matches
any value and *binds* its name (here, `duration`) to this value.

More generally, an expression of the form

      e match {
        case p1 => e1
        case p2 => e2
        …
        case pn => en
      }

matches the value of the selector `e` with the patterns
`p1`, …, `pn` in the order in which they are written.

The whole match expression is rewritten to the right-hand side of the first
case where the pattern matches the selector `e`.

References to pattern variables are replaced by the corresponding
parts in the selector.

### Exhaustivity

Having defined `Symbol` as a sealed trait gives us the guarantee that
the possible case of symbols is fixed. The compiler can leverage this
knowledge to warn us if we write code that does not handle *all*
the cases:
```scala
def unexhaustive(): Unit = {
  sealed trait Symbol
case class Note(name: String, duration: String, octave: Int) extends Symbol
case class Rest(duration: String)                            extends Symbol

def nonExhaustiveDuration(symbol: Symbol): String =
  symbol match {
    case Rest(duration) => duration
  }
}
```
We can try to run the above code to see how the compiler informs us that
we don’t handle all the cases in `nonExhaustiveDuration`.

### Equals

It is worth noting that, since the purpose of case classes is to
aggregate values, comparing case class instances compares their values.

## Exercise

Complete the `Rest` class declaration so that it extends `Symbol`. 
Complete the `caseClassEquals` and `symbolDuration` method definitions.

<div class="hint">In the method <code>caseClassEquals</code>, check if case class instances are equal.</div>

