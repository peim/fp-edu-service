package com.peim.http.clients

import com.peim.errors.ServiceError
import com.peim.models.GroupsTree

trait SomeClient[F[_]] {

  def getSmth(id: String): F[Either[ServiceError, GroupsTree]]

}
