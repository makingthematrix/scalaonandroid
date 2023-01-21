package calculator.logic.expressions

import calculator.logic.Parser

/**
 * Failure
 * 
 * The hard stop of the parsing process.
 * Every parsing stage has an option to respond with None, meaning "this line is not of my expression type".
 * And if so, then the parser passes the line to the next stage in the order. But if all stages respond that
 * the line can't be parsed to their expression type, then we have an error.
 * I handle such a case with a special expression type, `Failure`, which should be always the last stage in the parser.
 */

object Failure extends Parseable[Expression]:
  override def parse(parser: Parser, line: String): ParsedExpr[Expression] = 
    ParsedExpr.error(s"Unable to parse: $line")
