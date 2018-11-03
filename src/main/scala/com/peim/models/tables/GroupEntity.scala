package com.peim.models.tables

import com.peim.models.GroupType

case class GroupEntity(
    id: Int,
    name: String,
    `type`: GroupType,
    parentId: Int
)
