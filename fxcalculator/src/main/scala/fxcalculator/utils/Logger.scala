package fxcalculator.utils

import java.util.logging.{Logger as JLogger, *}

object Logger:
  private class Logger[T](mainClass: Class[T], tag: String, maxLines: Int = 10):
    private val logger: JLogger = {
      LogManager.getLogManager.readConfiguration(mainClass.getResourceAsStream("logging.properties"))
      JLogger.getLogger(mainClass.getName)
    }

    def log(level: Level, str: String): Unit =
      str.split("\n").take(maxLines).foreach { line => logger.log(level, s"$tag: $line") }

  private var instance: Option[Logger[_]] = None

  def init[T](mainClass: Class[T], tag: String, maxLines: Int = 10): Unit =
    instance = Some(new Logger(mainClass, tag, maxLines))

  def log(level: Level, str: String): Unit = instance.foreach(_.log(level, str))
  def error(str: String): Unit = log(Level.SEVERE, str)
  def warn(str: String): Unit = log(Level.WARNING, str)
  def info(str: String): Unit = log(Level.INFO, str)

  def whereAmI(): Unit = info(WhereAmI.whereAmI)

  def error(t: Throwable): Unit =
    error(t.getMessage)
    error(WhereAmI.whereAmI(t))
