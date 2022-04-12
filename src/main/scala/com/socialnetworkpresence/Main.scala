package com.socialnetworkpresence

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

import com.socialnetworkpresence.http._

object Main extends IOApp {

  def run(args: List[String]) =
    Server.stream[IO].compile.drain.as(ExitCode.Success)

}
