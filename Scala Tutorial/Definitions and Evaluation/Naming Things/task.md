
## Naming Things

Consider the following program, which computes the area of a disc
whose radius is `10`:

    3.14159 * 10 * 10

To make complex expressions more readable, we can give meaningful names to
intermediate expressions:

    val radius = 10
    val pi = 3.14159

    pi * radius * radius

Besides making the last expression more readable, it also allows us to
not repeat the actual value of the radius.

## Evaluation

A name is evaluated by replacing it with the right-hand side of its definition.

### Example

Here are the evaluation steps of the above expression:

    pi * radius * radius
    3.14159 * radius * radius
    3.14159 * 10 * radius
    31.4159 * radius
    31.4159 * 10
    314.159

## Methods

Definitions can have parameters. For instance, consider the program in the code editor.

## Exercise
Let’s define a method that computes the area of a disc, given its radius (`x`).
