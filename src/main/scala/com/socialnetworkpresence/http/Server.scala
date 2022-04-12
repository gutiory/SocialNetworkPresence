package com.socialnetworkpresence.http

import cats.effect.Async
import cats.effect.Resource
import cats.syntax.all._

import com.comcast.ip4s._
import com.socialnetworkpresence.SocialInfo
import com.socialnetworkpresence.third.party.SNGraphApi
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger
import sttp.tapir.server.http4s.Http4sServerInterpreter

object Server {

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    for {
      client        <- Stream.resource(EmberClientBuilder.default[F].build)
      snGraphApi     = SNGraphApi.impl[F](client)
      socialInfoAlg <- Stream.eval(SocialInfo.impl[F](snGraphApi))

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = Routes[F](socialInfoAlg, Http4sServerInterpreter[F]()).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- Stream.resource(
                    EmberServerBuilder
                      .default[F]
                      .withHost(ipv4"0.0.0.0")
                      .withPort(port"8080")
                      .withHttpApp(finalHttpApp)
                      .build >>
                      Resource.eval(Async[F].never)
                  )
    } yield exitCode
  }.drain

}
