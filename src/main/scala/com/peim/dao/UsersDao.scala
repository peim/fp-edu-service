//package com.peim.dao
//
//import com.peim.models.api.in.{CreateUser, UpdateUser}
//import com.peim.models.tables.UserEntity
//import doobie.util.query.Query0
//import doobie.util.update.Update0
//
//trait UsersDao {
//
//  def create(user: CreateUser): Update0
//
//  def update(user: UpdateUser): Update0
//
//  def find(id: Int): Query0[UserEntity]
//
//  def findByGroup(groupId: Int): Query0[UserEntity]
//
//  def list(skip: Int, take: Int): Query0[UserEntity]
//
//}
