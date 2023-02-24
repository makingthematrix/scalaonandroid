package fxcalculator.logic.expressions

import fxcalculator.logic.Dictionary
import fxcalculator.logic.expressions.Error.EvaluationError

final case class NativeFunction(name: String, argNames: Seq[String], f: Seq[Double] => Double) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] =
    Left(EvaluationError(s"Unable to evaluate a native function $name"))
  override def declaration: String = Expression.functionDeclaration(name, argNames)
  override def textForm: String = s"$declaration = [NativeFunction]"

object NativeFunction:
  inline def f1(name: String, argName: String, f: Double => Double): NativeFunction =
    NativeFunction(name, Seq(argName), args => f(args.head))
  inline def f1(name: String, f: Double => Double): NativeFunction = f1(name, "x", f)
  inline def f2(name: String, argName1: String, argName2: String, f: (Double, Double) => Double): NativeFunction =
    NativeFunction(name, Seq(argName1, argName2), args => f(args(0), args(1)))
  inline def f2(name: String, f: (Double, Double) => Double): NativeFunction = f2(name, "x", "y", f)