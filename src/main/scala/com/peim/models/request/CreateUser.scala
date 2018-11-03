package com.peim.models.request

case class CreateUser(
    name: String,
    groupId: Int,
    email: String,
    phone: Option[String]
)
