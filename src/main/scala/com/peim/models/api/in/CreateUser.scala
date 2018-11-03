package com.peim.models.api.in

case class CreateUser(
    name: String,
    groupId: Int,
    email: String,
    phone: Option[String]
)
