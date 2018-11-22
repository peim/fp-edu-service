package com.peim.models

case class GroupsTree(
    id: Int,
    name: String,
    `type`: GroupType,
    parentId: Int,
    childrens: Seq[GroupsTree]
)
