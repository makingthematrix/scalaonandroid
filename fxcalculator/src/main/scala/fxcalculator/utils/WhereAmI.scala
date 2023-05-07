package fxcalculator.utils

import java.io.{PrintWriter, StringWriter}

object WhereAmI:
  final private[utils] class WhereAmI extends Exception("Where am I?")

  inline def whereAmI: String = whereAmI(new WhereAmI, None)

  inline def whereAmI(maxLines: Int): String = whereAmI(new WhereAmI, Some(maxLines))

  inline def whereAmI(throwable: Throwable): String = whereAmI(throwable, None)

  inline def whereAmI(throwable: Throwable, maxLines: Int): String = whereAmI(throwable, Some(maxLines))

  private def whereAmI(throwable: Throwable, maxLines: Option[Int]): String =
    val result = new StringWriter
    throwable.printStackTrace(new PrintWriter(result))
    maxLines match
      case None    => result.toString
      case Some(n) => result.toString.split('\n').take(n).mkString("\n")