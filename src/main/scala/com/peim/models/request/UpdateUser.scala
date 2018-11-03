package com.peim.models.request

case class UpdateUser(
    id: Int,
    name: Option[String],
    groupId: Option[Int],
    email: Option[String],
    phone: Option[String]
)
