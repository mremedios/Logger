package logger

import java.time.LocalDateTime

case class LogMessage(component: String, date: LocalDateTime, level: String, message: String)
