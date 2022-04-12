package com.socialnetworkpresence

import cats.effect.IO

import com.socialnetworkpresence.dummy
import com.socialnetworkpresence.http.Routes
import io.circe.Json
import io.circe.syntax._
import munit.Http4sHttpRoutesSuite
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._
import org.http4s.syntax.all._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.server.http4s.Http4sServerOptions

class RoutesSuite extends Http4sHttpRoutesSuite {

  val interpreter: Http4sServerInterpreter[IO] = Http4sServerInterpreter[IO](Http4sServerOptions.default[IO, IO])

  val socialInfo = SocialInfo.impl(dummy.snApi).unsafeRunSync()

  override val routes: HttpRoutes[IO] = Routes[IO](socialInfo, interpreter)

  test(GET(uri"connections" / "John")).alias("Get all John's connections") { response =>
    val expected = Json.obj("firstDegree" := 2, "secondDegree" := 1)
    assertEquals(response.status, Status.Ok)
    assertIO(response.as[Json], expected)
  }

  test(GET(uri"no-connections" / "facebook")).alias("Get number of people without connections") { response =>
    println(response.status.code) // scalafix:ok
    assertEquals(response.status, Status.Ok)
    assertIO(response.as[Int], 1)
  }

}
