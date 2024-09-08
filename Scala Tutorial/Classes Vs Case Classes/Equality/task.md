
We see that creating a class instance requires the keyword `new`, whereas
this is not required for case classes.

Also, we see that the case class constructor parameters are promoted to
members, whereas this is not true for regular classes: the parameters
will remain private.

## Equality


In the example in the code editor, the same definitions of bank accounts lead to different
values, whereas the same definitions of notes lead to equal values.

As we have seen in the previous sections, stateful classes introduce a notion of *identity*
that does not exist in case classes. Indeed, the value of `BankAccount` can change over
time, whereas the value of `Note` is immutable.

In Scala, by default, comparing objects will compare their identity, but in the
case of case class instances, the equality is redefined to compare the values of
the aggregated information.

## Pattern Matching

We saw how pattern matching can be used to extract information from a case class instance:

      c3 match {
        case Note(name, duration, octave) => s"The duration of c3 is $duration"
      }

By default, pattern matching does not work with regular classes.

## Extensibility

A class can extend another class, whereas a case class can not extend
another case class (because it would not be possible to correctly
implement their equality).

## Exercise

- Complete the creation of two instances of `BankAccount`.
- Complete the creation of two instances of `c3` `Note`.
