package fxcalculator.logic.expressions

/**
 * Error
 * 
 * There are three error types, each for one main part of the expression processing: pre-processing, parsing, and
 * evaluation. Note that each of these three may involve calling the other two for nested expressions, so in the end
 * I can't assume what exact type of an error I got when I want to print it in the REPL - even if I got an error from
 * the parser, it might still be an error of evaluation of a variable used in the parsed expression (variables are
 * evaluated eagerly). I treat them all as error of the `Error` enum type, and I use the overloaded `toString` 
 * to display more specific information to the user.
 */

enum Error(val msg: String, private val title: String):
  case ParsingError(override val msg: String)      extends Error(msg, "Parsing error")
  case EvaluationError(override val msg: String)   extends Error(msg, "Evaluation error")
  case PreprocessorError(override val msg: String) extends Error(msg, "Preprocessor error")

  override def toString: String = s"$title: $msg"
