package calculator.replcalc.expressions

import Error.EvaluationError
import calculator.replcalc.{Dictionary, Parser}

/**
 * Variable
 * 
 * A variable is any string that is a valid name according to the dictionary rules. The logic here relies on
 * the order of parsing stages - the same string might be a name of a function, but then it should be parsed by
 * `Function` and we should never reach `Variable`. (On the other hand, if the string is a number, it will not
 * meet the criteria for a valid name and it will be passed further down, to the `Constant` expression type).
 * 
 * When a variable is evaluated, it looks into the dictionary for an expression associated with it.
 * If found, that expression is being evaluated and its result returned.
 * 
 * @param name A valid name of a variable
 */
final case class Variable(name: String) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] =
    dict
      .get(name)
      .map(_.run(dict))
      .getOrElse(Left(EvaluationError(s"Variable not found: $name")))
  
object Variable extends Parseable[Variable]:
  override def parse(parser: Parser, line: String): ParsedExpr[Variable] =
    if !Dictionary.isValidName(line, true) then 
      ParsedExpr.empty
    else if !parser.dictionary.contains(line) then
      ParsedExpr.error(s"Variable not found: $line")
    else
      ParsedExpr(Variable(line))
