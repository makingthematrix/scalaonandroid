
## Lazy Evaluation

The proposed `LazyList` implementation poses a serious potential performance
problem: if `tail` is called several times, the corresponding stream
will be recomputed each time.

This problem can be avoided by storing the result of the first
evaluation of `tail` and re-using the stored result instead of recomputing `tail`.

This optimization is sound, since in a purely functional language, an
expression produces the same result each time it is evaluated.

We call this scheme *lazy evaluation* (as opposed to *by-name evaluation* in
the case where everything is recomputed and *strict evaluation* for normal
parameters and `val` definitions).

### Lazy Evaluation in Scala

Haskell is a functional programming language that uses lazy evaluation by default.

Scala uses strict evaluation by default but allows lazy evaluation of value definitions
with the `lazy val` form:

      lazy val x = expr

## Exercise

Complete the `y` variable declaration to make it lazy.