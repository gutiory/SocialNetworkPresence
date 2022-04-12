package com.socialnetworkpresence.http

import com.socialnetworkpresence.Models.Person
import com.socialnetworkpresence.SocialInfo
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter

object Routes {

  def apply[F[_]](S: SocialInfo[F], interpreter: Http4sServerInterpreter[F]): HttpRoutes[F] = {

    val noConnections = Endpoints.noConnections.serverLogicSuccess(sn => S.getUsersWithoutConections(sn))

    val getConnections = Endpoints.getConnections.serverLogicSuccess(name => S.getTotalConnections(Person(name)))

    interpreter.toRoutes(List(noConnections, getConnections))
  }

}
