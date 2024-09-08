
This section introduces several syntactic sugars supported
by the language.

## String Interpolation

To splice values into constant `String` at runtime, you can
use *string interpolation*.


After having prefixed the string literal with `s`, you can introduce
dynamic values in it with `$`.

If you want to splice a complex expression (more than just an identifier),
surround it with braces.

## Exercise

Complete the `greetLouder` method to say "Hello, Scala" in the upper case.
