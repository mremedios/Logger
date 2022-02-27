package logger

import LogLevel._

object Log {

  def getDividedLogger(
      name: String,
      debugPrinter: Printer,
      infoPrinter: Printer,
      errorPrinter: Printer
  ): Logger = {
    val formatter = FormatterBuilder().withLevel().build

    val debugHandler =
      new Handler(
        debugPrinter,
        formatter,
        LogLevel.all,
        LogLevel.debug
      )

    val infoHandler =
      new Handler(
        infoPrinter,
        formatter,
        LogLevel.info,
        LogLevel.info
      )

    val errorHandler =
      new Handler(
        errorPrinter,
        formatter,
        LogLevel.warning,
        LogLevel.off
      )

    new Logger(name, List(debugHandler, infoHandler, errorHandler))
  }

  def getFileDividedLogger(name: String, pathPrefix: String): Logger = {
    getDividedLogger(
      name,
      new FilePrinter(pathPrefix + ".debug.log"),
      new FilePrinter(pathPrefix + ".info.log"),
      new FilePrinter(pathPrefix + ".error.log")
    )
  }

  // $COVERAGE-OFF$Interaction with the console
  def getStreamDividedLogger(name: String): Logger = {
    getDividedLogger(
      name,
      new NullPrinter(),
      new StreamPrinter(System.out),
      new StreamPrinter(System.err)
    )
  }

  def getSimpleLogger(name: String): Logger = {
    val handler = HandlerBuilder(new StreamPrinter(System.out))
      .withDate()
      .withLevel()
      .build()

    new Logger(name, List(handler))
  }

  def getExtendedLogger(name: String): Logger = {
    val consoleHandler = HandlerBuilder(new StreamPrinter(System.out))
      .withLevel()
      .withDate()
      .setMinLevel(all)
      .build()

    val fileHandler = HandlerBuilder(new FilePrinter("error.log"))
      .withLevel()
      .withDate()
      .setMinLevel(warning)
      .build()

    new Logger(name, List(consoleHandler, fileHandler))
  }
  // $COVERAGE-ON$

}
