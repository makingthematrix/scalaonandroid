package fxcalculator

/**
 * The main loop
 *
 * Hello, world! I start each source file of the project with a few words about what is going on in here.
 * In my usual work I rely on Jira or GitHub - here you can go also through the list of tickets in
 * GitHub Projects, as well as the list of PRs and commits, to see the whole process of how this project
 * came to be, one feature, bugfix, and refactorization at the time.
 *
 * Here's the link: https://github.com/makingthematrix/replcalc
 *
 * But I decided that this is a bit of a special case, as it's not only about the final products, but also
 * about me explaining how I work, so I thought to use comments to talk about it.
 *
 * The main file is about creating a very simple REPL - a Read-Eval-Print Loop. I don't use any external
 * library for that so I decided to do it the simplest way possible: just a while loop with a variable check
 * as the exit condition. There are only two commands in the REPL and one of them is ":exit" which set the
 * exit variable to "true" and that ends the program.
 * In the rest of the code I use almost only collection methods, "for/yield", and recursion - all more idiomatic
 * constructs in Scala. Here I decided to go with "while" because in this special case it's really straightforward
 * to use it, and I value simplicity and readability more than consistency. (Consistency is of course also important,
 * as it often helps with the other two, but I believe there are exceptions).
 */

import fxcalculator.logic.{Dictionary, Evaluator, Parser, ParserCreator, Preprocessor}
import fxcalculator.logic.expressions.{Assignment, Constant, ConstantAssignment, Error, Expression, FunctionAssignment, NativeFunction}

import scala.util.chaining.*
import scala.io.StdIn.readLine

object REPL:
  def main(args: Array[String]): Unit =
    val parser = ParserCreator.createParser(true, true)
    var exit = false
    while !exit do
      print("> ")
      readLine().trim match
        case ":exit"              => exit = true
        case ":list"              => list(parser)
        case line if line.isEmpty =>
        case line                 => run(parser, line)

  private def list(parser: Parser): Unit =
    parser.assignments.foreach(entry => println(entry.textForm))

  private def run(parser: Parser, text: String): Unit =
    val evInfo = Evaluator.evaluate(parser, text)
    if evInfo.assignments.nonEmpty then
      val lines = evInfo.assignments.map(_._2)
      parser.store(lines)
      lines.foreach { line => println(s"You created a new assignment: $line") }
    evInfo.result match
      case result: Double => println(result)
      case error: Error => println(error.toString)
      case _ =>
