package calculator.logic.expressions

import calculator.logic.{Dictionary, Parser}

/**
 * Unary minus
 * 
 * Parsing of a unary minus looks very simple but it's because a lot of heavy lifting was done at earlier stages,
 * before the parser reached this expression type. The preprocessor removed whitespaces and parentheses, and
 * the `AddSubstract` expression type made sure that binary '-' is out of question. Now all we need to check is
 * if the first character in the line is a minus. If yes, we have a `UnaryMinus` expression type.
 * 
 * @param innerExpr The expression under the unary minus. Its evaluation result will be negated.
 */
final case class UnaryMinus(innerExpr: Expression) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] = 
    innerExpr.run(dict).map(-_)
  
object UnaryMinus extends Parseable[UnaryMinus]:
  override def parse(parser: Parser, line: String): ParsedExpr[UnaryMinus] =
    if line.length <= 1 || line.head != '-' then
      ParsedExpr.empty
    else
      parser
        .parse(line.substring(1))
        .happyPath(expr => ParsedExpr(UnaryMinus(expr)))
