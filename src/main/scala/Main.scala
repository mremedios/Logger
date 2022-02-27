package logger

object Main {
  def main(args: Array[String]): Unit = {
    val x = HandlerBuilder(printer = new NullPrinter, minLevel = LogLevel.off)
  }
}
