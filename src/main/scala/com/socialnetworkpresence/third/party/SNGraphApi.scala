package com.socialnetworkpresence.third.party

import com.socialnetworkpresence.Models._
import com.socialnetworkpresence.third.party.Models._
import org.http4s.client.Client

trait SNGraphApi[F[_]] {

  def getGraph(sn: SocialNetwork): F[ResponseGraph]

}

object SNGraphApi {

  def impl[F[_]](client: Client[F]) = new SNGraphApi[F] {

    override def getGraph(sn: SocialNetwork): F[ResponseGraph] = ???

  }

}
