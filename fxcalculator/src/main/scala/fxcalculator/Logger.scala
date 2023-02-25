package fxcalculator

import java.util.logging.{Logger as JLogger, *}

object Logger:
  private lazy val logger: JLogger = {
    LogManager.getLogManager.readConfiguration(classOf[Logger.type].getResourceAsStream("logging.properties"))
    JLogger.getLogger(classOf[Logger.type].getName)
  }

  def log(level: Level, str: String): Unit = logger.log(level, str)

  inline def error(str: String): Unit = log(Level.SEVERE, str)
  inline def warn(str: String): Unit = log(Level.WARNING, str)
  inline def info(str: String): Unit = log(Level.INFO, str)