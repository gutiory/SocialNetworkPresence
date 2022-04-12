package com.socialnetworkpresence.http

import com.socialnetworkpresence.Models._
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.codec.enumeratum._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

object Endpoints {

  val noConnections: Endpoint[Unit, SocialNetwork, Unit, Int, Any] =
    endpoint.get.in("no-connections" / path[SocialNetwork]("sn")).out(jsonBody[Int])

  val getConnections: Endpoint[Unit, String, Unit, Connection, Any] =
    endpoint.get.in("connections" / path[String]("name")).out(jsonBody[Connection])

}
