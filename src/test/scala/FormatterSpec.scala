package logger

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.{Clock, Instant, ZoneId}

class FormatterSpec extends AnyFlatSpec with Matchers with MockFactory {

  def testFormatter(formatter: Formatter, expectedMessage: String): Unit = {
    val message = formatter.makeMessage("message", LogLevel.warning, "component")
    message shouldEqual expectedMessage
  }

  "Formatter" should "print message" in {
    testFormatter(FormatterBuilder().build, "message")
  }

  "Formatter" should "print message and level" in {
    testFormatter(FormatterBuilder().withLevel().build,
      "[warning] message")
  }

  "Formatter" should "print level, component and message" in {
    testFormatter(FormatterBuilder().withLevel().withComponent().build,
      "[warning] [component] message")
  }

  "Formatter" should "print component, date, level and message" in {
    val clockMock = mock[Clock]
    (clockMock.instant _).expects().returns(Instant.parse("2021-11-20T15:15:30.00Z"))
    (clockMock.getZone _).expects().returns(ZoneId.of("Z"))
    testFormatter(FormatterBuilder(clock = clockMock).withComponent().withDate().withLevel().build,
      "[component] [2021/11/20 15:15:30] [warning] message")
  }

}
