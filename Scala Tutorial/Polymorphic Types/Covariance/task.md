
## Covariance 

There's another interaction between subtyping and type parameters we
need to consider.

Consider the following type modeling a field that contains an animal:

      trait Field[A] {
        def get: A // returns the animal that lives in this field
      }

Given:

      Zebra <: Mammal

Would the following be true?

      Field[Zebra] <: Field[Mammal]

Intuitively, this makes sense: a field containing a zebra is a special case of a field
containing an arbitrary mammal.

We call the types for which this relationship holds *covariant*
because their subtyping relationship varies with the type parameter.

Does covariance make sense for all types, not just for `Field`?

#### Arrays

For perspective, let's look at arrays in Java (and C#).

Reminder:

 - An array of `T` elements is written as `T[]` in Java.
 - In Scala, we use the parameterized type syntax `Array[T]` to refer to the same type.

Arrays in Java are covariant, so one would have:

      Zebra[] <: Mammal[]

However, covariant array typing causes problems.

To see why, consider the Java code below:

      Zebra[] zebras = new Zebra[]{ new Zebra() }  // Array containing 1 `Zebra`
      Mammal[] mammals = zebras      // Allowed because arrays are covariant in Java
      mammals[0] = new Giraffe()     // Allowed because a `Giraffe` is a subtype of `Mammal`
      Zebra zebra = zebras[0]        // Get the first `Zebra` … which is actually a `Giraffe`!

It looks like we assigned a `Giraffe` to a
variable of type `Zebra` in the last line!

What went wrong?

#### The Liskov Substitution Principle 

The following principle stated by Barbara Liskov tells us when a
type can be a subtype of another.

If `A <: B`, then everything one can to do with a value of type `B` one should also
be able to do with a value of type `A`.

The problematic array example would be written in Scala as follows:

      val zebras: Array[Zebra] = Array(new Zebra)
      val mammals: Array[Mammal] = zebras
      mammals(0) = new Giraffe
      val zebra: Zebra = zebras(0)

If you try to compile this example, you will get a compile error at line 2:

      type mismatch;
        found   : Array[Zebra]
        required: Array[Mammal]

## Variance 

We have seen that some types should be covariant, whereas
others should not.

Roughly speaking, a type that accepts mutations of its elements should
not be covariant.

Meanwhile, immutable types can be covariant if some conditions
on methods are met.

## Definition of Variance

Say `C[T]` is a parameterized type, and `A` and `B` are types such that `A <: B`.

In general, there are *three* possible relationships between `C[A]` and `C[B]`:

 - `C[A] <: C[B]`, `C` is *covariant*;
 - `C[A] >: C[B]`, `C` is *contravariant*;
 - neither `C[A]` nor `C[B]` is a subtype of the other, `C` is *nonvariant*.

Scala lets you declare the variance of a type by annotating the type parameter:

 - `class C[+A] { … }`, `C` is *covariant*;
 - `class C[-A] { … }`, `C` is *contravariant*;
 - `class C[A] { … }`, `C` is *nonvariant*.

### Typing Rules for Functions 

Generally, we have the following rule for subtyping between function types:

If `A2 <: A1` and `B1 <: B2`, then

`A1 => B1  <:  A2 => B2`

So functions are *contravariant* in their argument type(s) and
*covariant* in their result type.

This leads to the following revised definition of the `Function1` trait:

      trait Function1[-T, +U] {
        def apply(x: T): U
      }

### Contravariance Example 

Consider the following type modeling a veterinary:

      trait Vet[A] {
        def treat(a: A): Unit // Treats an animal of type `A`
      }

In such a case, intuitively, it makes sense to have `Vet[Mammal] <: Vet[Zebra]` because
a vet that can treat any mammal is able to treat a zebra in particular. This is
an example of a contravariant type.

### Variance Checks 

In the array example, we have seen that the combination of covariance with
certain operations is unsound.

In the case of `Array`, the problematic combination is:
 - the covariant type parameter `T` which appears in the parameter position of the method `update`.

The Scala compiler will check that there are no problematic combinations when
compiling a class with variance annotations.

Roughly speaking,

 - *covariant* type parameters can only appear in method results.
 - *contravariant* type parameters can only appear in method parameters.
 - *invariant* type parameters can appear anywhere.

The precise rules are a bit more involved; fortunately, the Scala compiler performs them for us.

#### Variance-Checking the Function Trait 

Let's have a look at `Function1` again:

      trait Function1[-T, +U] {
        def apply(x: T): U
      }

Here,

 - `T` is contravariant and appears only as a method parameter type
 - `U` is covariant and appears only as a method result type

So the method checks out OK.

## Making Classes Covariant 

Sometimes, we have to put in a bit of work to make a class covariant.

Consider adding a `prepend` method to `Stream`, which prepends a given
element, yielding a new stream.

A first implementation of `prepend` could look like this:

      trait Stream[+T] {
        def prepend(elem: T): Stream[T] = Stream.cons(elem, this)
      }

But that does not work!

Why does the above code not type-check?

`prepend` fails variance checking.

Indeed, the compiler is right to throw out `Stream` with `prepend`
because it violates the Liskov Substitution Principle.

Here's something one can do with the stream `mammals` of type `Stream[Mammal]`:

      mammals.prepend(new Giraffe)

However, the same operation on the list `zebras` of type
`Stream[Zebra]` would lead to a type error:

      zebras.prepend(new Giraffe)
                     ^ type mismatch
                     required: Zebra
                     found: Giraffe

So, `Stream[Zebra]` cannot be a subtype of `Stream[Mammal]`.

But `prepend` is a natural method to have on immutable lists!

How can we make it variance-correct?

We can use a *lower bound*:
```
      def prepend [U >: T](elem: U): Stream[U] = 
```
```
      Stream.cons(elem, this)
```
This does pass variance checks because:

 - covariant type parameters may appear in the lower bounds of method type parameters;
 - contravariant type parameters may appear in the upper bounds of a method.

## Exercise

Complete the class definition with covariance and `animal` as a parameter.