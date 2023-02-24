package fxcalculator

import java.util.logging.{Logger => JLogger, *}
import scala.util.chaining.scalaUtilChainingOps

object Logger:
  private var logger: Option[JLogger] = None

  private def instance(): JLogger = logger match
    case Some(l) => l
    case None =>
      LogManager.getLogManager.readConfiguration(classOf[fxcalculator.Logger.type].getResourceAsStream("logging.properties"))
      JLogger.getLogger(classOf[fxcalculator.Logger.type].getName).tap { l => logger = Some(l) }

  def log(level: Level, str: String): Unit = instance().log(level, str)

  inline def error(str: String): Unit = log(Level.SEVERE, str)
  inline def warn(str: String): Unit = log(Level.WARNING, str)
  inline def info(str: String): Unit = log(Level.INFO, str)