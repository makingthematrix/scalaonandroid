
## Traits

In Scala, a class can only have one superclass.

But what if a class has several natural supertypes to which it conforms
or from which it wants to inherit code?

In such case, you could use `trait`s.

A trait is declared like an abstract class, just with `trait` instead of
`abstract class`:

      trait Planar {
        def height: Int
        def width: Int
        def surface = height * width
      }

Classes, objects and traits can inherit from at most one class but
arbitrary many traits:

      class Square extends Shape with Planar with Movable …

On the other hand, traits cannot have (value) parameters, only classes can.

## Scala's Class Hierarchy 

<img src="./scala_type_hierarchy.png" style="max-width: 100%" />

### Top Types

At the top of the type hierarchy we find:

 - `Any`
   - The base type of all types
   - Methods: `==`, `!=`, `equals`, `hashCode`, `toString`
 - `AnyRef`
   - The base type of all reference types
   - Alias of `java.lang.Object`
 - `AnyVal`
   - The base type of all primitive types

### Bottom Type

`Nothing` is at the bottom of Scala's type hierarchy. It is a subtype
of every other type.

There is no value of type `Nothing`.

Why is that useful?

 - It signals abnormal termination.
 - It may be an element type of empty collections.

### The Null Type 

Every reference class type also has `null` as a value.

The type of `null` is `Null`.

`Null` is a subtype of every class that inherits from `Object`; it is
incompatible with the subtypes of `AnyVal`.

      val x = null         // x: Null
      val y: String = null // y: String
      val z: Int = null    // error: type mismatch

## Exercise 

The following `Reducer` abstract class defines how to
reduce a list of values into a single value by starting
with an initial value and combining it with each element
of the list.
Complete the implementation of the objects `Product` and `Sum`, which 
inherit from `Reducer`, so that the first `println` statement prints `24` 
and the second one – `10`.
