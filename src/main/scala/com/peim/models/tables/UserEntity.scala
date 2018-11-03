package com.peim.models.tables

case class UserEntity(
    id: Int,
    name: String,
    groupId: Int,
    email: String,
    phone: Option[String]
)
