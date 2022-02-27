package logger

import LogLevel.LogLevel

class Handler(
    val printer: Printer,
    val formatter: Formatter,
    val minLevel: LogLevel = LogLevel.getMinLogLevel,
    val maxLevel: LogLevel = LogLevel.off
) {

  def log(level: LogLevel, message: String, component: String): Unit = {
    if (minLevel <= level && level <= maxLevel) {
      printer.print(formatter.makeMessage(message, level, component) + System.lineSeparator())
    }
  }
}
