package logger

import LogLevel.LogLevel

case class HandlerBuilder private(printer: Printer,
                                   formatter: FormatterBuilder = FormatterBuilder(),
                                   minLevel: LogLevel = LogLevel.warning,
                                   maxLevel: LogLevel = LogLevel.off
                                 ) {

  def withLevel(): HandlerBuilder = copy(formatter = formatter.withLevel())

  def withDate(): HandlerBuilder = copy(formatter = formatter.withDate())

  def withComponent(): HandlerBuilder = copy(formatter = formatter.withComponent())

  def setMinLevel(min: LogLevel): HandlerBuilder = copy(minLevel = min)

  def setMaxLevel(max: LogLevel): HandlerBuilder = copy(maxLevel = max)

  def build(): Handler = {
    new Handler(
      printer,
      formatter.build,
      minLevel,
      maxLevel
    )
  }
}

object HandlerBuilder {
  def apply(printer: Printer): HandlerBuilder =
    new HandlerBuilder(printer)
}

