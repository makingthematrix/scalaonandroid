
## Operators Are Methods

Actually, operators are just methods with symbolic names:

        3 + 2 == 3.+(2)

The *infix syntax* allows you to omit the dot and the parentheses.
<br/><br/>
The infix syntax can also be used with regular methods:

        1.to(10) == 1 to 10

Any method with a parameter can be used like an infix operator.

## Values and Types 

Expressions have a *value* and a *type*. The evaluation model
defines how to get a value out of an expression. Types classify values.
<br/><br/>

Both `0` and `1` are numbers, their type is `Int`.
<br/><br/>
`"foo"` and `"bar"` are text, their type is `String`.

## Static Typing 

The Scala compiler statically checks that you don’t combine incompatible
expressions.
<br/>
<br/>
Try filling the blank in the code editor with values whose type is
different from `Int` and see the result.

## Exercise 

Complete the range expression with an appropriate value.
