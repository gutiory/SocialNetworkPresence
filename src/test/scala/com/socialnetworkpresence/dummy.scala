package com.socialnetworkpresence

import cats.effect.IO

import com.socialnetworkpresence.Models.SocialNetwork._
import com.socialnetworkpresence.Models._
import com.socialnetworkpresence.third.party.Models._
import com.socialnetworkpresence.third.party.SNGraphApi

object dummy {

  val personJohn = Person("John")

  val personHarry = Person("Harry")

  val personPeter = Person("Peter")

  val personGeorge = Person("George")

  val personAnna = Person("Anna")

  val people = List(personJohn, personHarry, personPeter, personGeorge, personAnna)

  val relationships = List(
    Relationship("HasConnection", personJohn, personPeter),
    Relationship("HasConnection", personJohn, personGeorge),
    Relationship("HasConnection", personPeter, personGeorge),
    Relationship("HasConnection", personPeter, personAnna)
  )

  val facebookResponse = ResponseGraph(Facebook, people, relationships)

  val twitterResponse = ResponseGraph(Twitter, Nil, Nil)

  val snApi = new SNGraphApi[IO]() {

    override def getGraph(sn: SocialNetwork): IO[ResponseGraph] = sn match {
      case Twitter  => IO.delay(twitterResponse)
      case Facebook => IO.delay(facebookResponse)
    }

  }

}
