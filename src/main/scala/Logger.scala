package logger

import LogLevel.LogLevel

class Logger(val name: String, val handlers: List[Handler]) {

  def log(level: LogLevel, message: String): Unit = {
    handlers.foreach(_.log(level, message, name))
  }

  def trace(message: String): Unit = log(LogLevel.trace, message)
  def debug(message: String): Unit = log(LogLevel.debug, message)
  def info(message: String): Unit = log(LogLevel.info, message)
  def warning(message: String): Unit = log(LogLevel.warning, message)
  def error(message: String): Unit = log(LogLevel.error, message)
  def fatal(message: String): Unit = log(LogLevel.fatal, message)
}
