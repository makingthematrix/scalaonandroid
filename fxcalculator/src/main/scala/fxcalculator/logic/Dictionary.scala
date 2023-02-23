package fxcalculator.logic

/**
 * Dictionary
 *
 * The dictionary holds variables and functions in their already parsed forms, i.e. as expressions with associated names.
 * It's basically a wrapper over a map of String -> Expression, but with some additional functionality:
 * 1. Before adding a new entry, the dictionary checks if the name is valid. The name has to start with a letter or
 *    an underscore (or '$' but only if it's a special variable), all next characters have to be letters, numbers, or
 *    underscores, and at least one character has to be NOT an underscore.
 * 2. It can create special expressions with unique names. This is used by the preprocessor for turning nested
 *    expressions inside parentheses into special variables.
 * 3. It can create its own copy updated with additional entries. This is used to first parse function assignments
 *    and then to evaluate functions - each function can contain other functions and each time we go a step down,
 *    we need to add arguments as variables to the dictionary we use, but when we go up again, we shouldn't be able
 *    to use those variables anymore.
 */

import fxcalculator.logic.expressions.*

final class Dictionary(private var dict: Map[String, Expression] = Map.empty):
  import Dictionary.{isValidName, specialValuesCounter}

  def canAssign(name: String): Boolean =
    dict.get(name) match
      case Some(_ : Assignment)      => true
      case None if isValidName(name) => true
      case _                         => false

  def add(name: String, expr: Expression, canBeSpecial: Boolean = false): Boolean =
    dict.get(name) match
      case Some(_ : Assignment) =>
        dict += name -> expr
        true
      case None if isValidName(name, canBeSpecial) =>
        dict += name -> expr
        true
      case _ =>
        false

  def addSpecial(expr: Expression): String =
    specialValuesCounter += 1
    val name = s"$$$specialValuesCounter"
    dict += name -> expr
    name
        
  inline def addNativeFunction(name: String, argNames: Seq[String], f: Seq[Double] => Double): Boolean = 
    add(name, NativeFunction(name, argNames, f))
  
  inline def get(name: String): Option[Expression] = dict.get(name)

  inline def contains(name: String): Boolean = dict.contains(name)

  inline def expressions: Map[String, Expression] = dict.filter(_._1.head != '$')

  inline def specials: Map[String, Expression] = dict.filter(_._1.head == '$')

  def copy(updates: Map[String, Expression]): Dictionary = Dictionary(dict ++ updates)

  def list: Seq[Expression] =
    expressions
      .toSeq
      .sortBy(_._1)
      .map(_._2)

  def list[T <: Expression](exType: Class[T]): Seq[T] =
    expressions
      .collect { case (name, expr) if expr.getClass == exType => (name, expr.asInstanceOf[T]) }
      .toSeq
      .sortBy(_._1)
      .map(_._2)

object Dictionary:
  private var specialValuesCounter: Long = 0L

  def isValidName(name: String, canBeSpecial: Boolean = false): Boolean =
    name.nonEmpty &&
      name.exists(_ != '_') &&
      (name.head.isLetter || name.head == '_' || (canBeSpecial && name.head == '$')) &&
      name.substring(1).forall(ch => ch.isLetterOrDigit || ch == '_')
