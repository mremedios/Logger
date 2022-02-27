package logger

import scala.util.Properties

object LogLevel extends Enumeration {
  type LogLevel = Value
  val all, trace, debug, info, warning, error, fatal, off = Value

  // $COVERAGE-OFF$Environment variable
  def getMinLogLevel: LogLevel = LogLevel.withName(Properties.envOrElse("LOG_LEVEL", "all"))
}
