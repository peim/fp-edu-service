package com.peim.models.api.in
import com.peim.models.GroupType

case class UpdateGroup(
    id: Int,
    name: Option[String],
    `type`: Option[GroupType],
    parentId: Option[Int]
)
