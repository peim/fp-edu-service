//package com.peim.services
//
//import cats.effect.Sync
//import cats.implicits._
//import com.peim.dao.GroupsDao
//import com.peim.http.clients.SomeClient
//import com.peim.models.GroupsTree
//import com.peim.models.api.in.{CreateGroup, UpdateGroup}
//import com.peim.models.tables.GroupEntity
//import doobie.hikari.HikariTransactor
//import doobie.implicits._
//
//class GroupsService[F[_]: Sync](groupsDao: GroupsDao, someClient: SomeClient[F], transactor: HikariTransactor[F]) {
//
//  def findGroup(groupId: Int): F[Option[GroupEntity]] = {
//    groupsDao
//      .find(groupId)
//      .option
//      .transact(transactor)
//  }
//
//  def listGroups(skipOpt: Option[Int], takeOpt: Option[Int]): F[Seq[GroupEntity]] = {
//    val skip = skipOpt.getOrElse(0)
//    val take = takeOpt.getOrElse(25)
//    groupsDao
//      .list(skip, take)
//      .to[Seq]
//      .transact(transactor)
//  }
//
//  def groupsHierarchy(rootIdOpt: Option[Int]): F[Option[GroupsTree]] = {
//    val rootId = rootIdOpt.getOrElse(1)
//    groupsDao
//      .childrensRecursive(rootId)
//      .to[Seq]
//      .transact(transactor)
//      .map(buildGroupTree(rootId))
//  }
//
//  def createGroup(group: CreateGroup): F[Int] = {
//    groupsDao
//      .create(group)
//      .withUniqueGeneratedKeys[Int]("id")
//      .transact(transactor)
//  }
//
//  def updateGroup(group: UpdateGroup): F[Int] = {
//    groupsDao
//      .update(group)
//      .withUniqueGeneratedKeys[Int]("id")
//      .transact(transactor)
//  }
//
//  def buildGroupTree(rootId: Int)(groups: Seq[GroupEntity]): Option[GroupsTree] = {
//
//    def loop(root: GroupEntity): GroupsTree = {
//      GroupsTree(root.id, root.name, root.`type`, root.parentId, groups.filter(_.parentId == root.id).map(loop))
//    }
//
//    groups.find(_.id == rootId).map(loop)
//  }
//
//}
