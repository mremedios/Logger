package logger

import LogLevel.LogLevel

import java.time.format.DateTimeFormatter
import java.time.{Clock, LocalDateTime}

trait Formatter {
  def makeMessage(message: String, level: LogLevel, component: String): String
}

case class FormatterBuilder(pattern: String = "", clock: Clock = Clock.systemDefaultZone()) {

  def add(pos: Int): String = "[%" + pos + "$s] "

  def withLevel(): FormatterBuilder = copy(pattern = pattern + add(4))

  def withDate(): FormatterBuilder = copy(pattern = pattern + add(3))

  def withComponent(): FormatterBuilder = copy(pattern = pattern + add(2))

  def build: Formatter = PatternFormatter(pattern + "%1$s", clock)
}

case class PatternFormatter private (pattern: String, clock: Clock = Clock.systemDefaultZone())
    extends Formatter {

  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss")

  override def makeMessage(message: String, level: LogLevel, component: String): String = {
    String.format(
      pattern,
      message,
      component,
      LocalDateTime.now(clock).format(dateFormatter),
      level
    )
  }
}
