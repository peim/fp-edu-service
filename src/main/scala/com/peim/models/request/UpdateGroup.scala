package com.peim.models.request

import com.peim.models.GroupType

case class UpdateGroup(
    id: Int,
    name: Option[String],
    `type`: Option[GroupType],
    parentId: Option[Int]
)
