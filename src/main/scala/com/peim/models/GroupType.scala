package com.peim.models

sealed trait GroupType
case object Public  extends GroupType
case object Private extends GroupType

object GroupType {

  def fromString(s: String): GroupType =
    s match {
      case "public"  => Public
      case "private" => Private
      case other     => throw new RuntimeException(s"unexpected group type $other")
    }

  def toString(`type`: GroupType): String = `type` match {
    case Public  => "public"
    case Private => "private"
  }

}
