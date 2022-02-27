package logger

import scoverage.Platform.FileWriter

import java.io.{OutputStream, PrintStream}
import java.nio.file.Path
import scala.util.Using

trait Printer {
  def print(message: String): Unit
}

case class NullPrinter() extends Printer {
  def print(message: String): Unit = {}
}

case class StreamPrinter(out: OutputStream) extends Printer {
  def printStream: PrintStream = new PrintStream(out)
  def print(message: String): Unit = printStream.print(message)
}

case class FilePrinter(filename: String) extends Printer {
  {
    val dir = Path.of(filename).getParent
    if (dir != null) {
      dir.toFile.mkdirs()
    }
  }

  def print(message: String): Unit = {
    Using(new FileWriter(filename, true)) { out => out.write(message) }
  }

}
