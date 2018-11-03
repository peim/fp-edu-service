package com.peim.models

sealed trait EventType
case object Info    extends EventType
case object Warning extends EventType
case object Error   extends EventType

object EventType {

  def fromString(s: String): EventType =
    s match {
      case "info"    => Info
      case "warning" => Warning
      case "error"   => Error
      case other     => throw new RuntimeException(s"unexpected event type $other")
    }

  def toString(`type`: EventType): String = `type` match {
    case Info    => "info"
    case Warning => "warning"
    case Error   => "error"
  }

}
