package com.peim.models.request

import com.peim.models.GroupType

case class CreateGroup(
    name: String,
    `type`: GroupType,
    parentId: Int
)
