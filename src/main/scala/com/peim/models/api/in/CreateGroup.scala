package com.peim.models.api.in

import com.peim.models.GroupType

case class CreateGroup(
    name: String,
    `type`: GroupType,
    parentId: Int
)
