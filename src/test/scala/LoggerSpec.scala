package logger

import LogLevel.LogLevel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.io.ByteArrayOutputStream
import java.nio.file.Files
import scala.io.Source.fromFile
import scala.util.Using


class LoggerSpec extends AnyFlatSpec with Matchers {


  def testLevel(minLevel: LogLevel, maxLevel: LogLevel, expected: String): Unit = {
    val stream = new ByteArrayOutputStream
    val handler = new Handler(
      StreamPrinter(stream),
      FormatterBuilder().withLevel().build,
      minLevel,
      maxLevel
    )
    val myLogger = new Logger("MyLogger", List(handler))

    myLogger.trace("trace")
    myLogger.debug("debug")
    myLogger.info("info")
    myLogger.warning("warning")
    myLogger.error("error")
    myLogger.fatal("fatal")

    new String(stream.toByteArray) shouldEqual expected
  }

  "Logger" should "print all logs into given stream" in {
    val expected =
      """[trace] trace
        |[debug] debug
        |[info] info
        |[warning] warning
        |[error] error
        |[fatal] fatal
        |""".stripMargin

    testLevel(LogLevel.all, LogLevel.off, expected)
  }

  "Logger" should "filter logs" in {
    val expected =
      """[debug] debug
        |[info] info
        |[warning] warning
        |[error] error
        |""".stripMargin
    testLevel(LogLevel.debug, LogLevel.error, expected)
  }

  "Logger" should "implement Null Object" in {
    val handler = new Handler(
      NullPrinter(),
      FormatterBuilder().withLevel().build,
      LogLevel.all,
      LogLevel.off
    )
    val myLogger = new Logger("MyLogger", List(handler))

    myLogger.trace("trace")
  }

  "Logger" should "print logs into file" in {
    val filename = Files.createTempFile("test", "").toString
    val handler = new Handler(
      FilePrinter(filename),
      FormatterBuilder().withLevel().build,
      LogLevel.all,
      LogLevel.off
    )
    val myLogger = new Logger("MyLogger", List(handler))

    myLogger.trace("trace")

    val expected =
      """[trace] trace
        |""".stripMargin

    Using(fromFile(filename)) { in =>
      val log = in.getLines().mkString
      log shouldEqual expected
    }
  }

  "Logger" should "have file implementation with level divided logs" in {
    val prefix = Files.createTempDirectory("testdir").toString

    val myLogger = Log.getFileDividedLogger("LoggerName", prefix)

    myLogger.trace("trace")
    myLogger.debug("debug")
    myLogger.info("info")
    myLogger.warning("warning1")
    myLogger.warning("warning2")
    myLogger.error("error")
    myLogger.fatal("fatal")

    val expected = List(
      """[trace] trace
        |[debug] debug
        |""".stripMargin,

      """[info] info
        |""".stripMargin,

      """[warning] warning1
        |[warning] warning2
        |[error] error
        |[fatal] fatal
        |""".stripMargin)

    val filenames = List(
      prefix + ".debug.log",
      prefix + ".info.log",
      prefix + ".error.log",
    )

    for (i <- 0 until 3) {
      Using(fromFile(filenames(i))) { in =>
        val log = in.getLines().mkString
        log shouldEqual expected(i)
      }
    }
  }

  "HandlerBuilder" should "create valid handlers to logger" in {

    new MockPrinter() ex

    val stream = new ByteArrayOutputStream
    val handler1 = HandlerBuilder(StreamPrinter(stream))
      .withComponent()
      .withLevel()
      .setMinLevel(LogLevel.all)
      .setMaxLevel(LogLevel.off)
      .build()

    val handler2 = HandlerBuilder(NullPrinter()).build()

    val handler3 = HandlerBuilder(FilePrinter("filename"))
      .withDate()
      .setMinLevel(LogLevel.off)
      .build()

    val myLogger = new Logger("MyLogger", List(handler1, handler2, handler3))

    myLogger.trace("trace")
    myLogger.debug("debug")
    myLogger.info("info")
    myLogger.warning("warning")
    myLogger.error("error1")
    myLogger.error("error2")
    myLogger.fatal("fatal")

    val expected =
      """[MyLogger] [trace] trace
        |[MyLogger] [debug] debug
        |[MyLogger] [info] info
        |[MyLogger] [warning] warning
        |[MyLogger] [error] error1
        |[MyLogger] [error] error2
        |[MyLogger] [fatal] fatal
        |""".stripMargin

    new String(stream.toByteArray) shouldEqual expected
  }
}

