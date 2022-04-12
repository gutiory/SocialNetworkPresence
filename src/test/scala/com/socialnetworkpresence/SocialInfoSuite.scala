package com.socialnetworkpresence

import cats.effect.IO

import com.socialnetworkpresence.Models.SocialNetwork._
import com.socialnetworkpresence.Models._
import com.socialnetworkpresence.third.party.Models._
import com.socialnetworkpresence.third.party.SNGraphApi
import munit.CatsEffectSuite

class SocialInfoSpec extends CatsEffectSuite {

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

  val socialInfo = SocialInfo.impl(snApi).unsafeRunSync()

  test("Get total connections from response") {

    val expectedJohn   = Connection(2, 1)
    val expectedPeter  = Connection(3, 0)
    val expectedGeorge = Connection(2, 1)
    val expectedHarry  = Connection(0, 0)
    val expectedAnna   = Connection(1, 2)

    for {
      _ <- assertIO(socialInfo.getTotalConnections(personJohn), expectedJohn)
      _ <- assertIO(socialInfo.getTotalConnections(personPeter), expectedPeter)
      _ <- assertIO(socialInfo.getTotalConnections(personGeorge), expectedGeorge)
      _ <- assertIO(socialInfo.getTotalConnections(personHarry), expectedHarry)
      _ <- assertIO(socialInfo.getTotalConnections(personAnna), expectedAnna)
    } yield ()
  }

  test("Get users wituout connections") {
    assertIO(socialInfo.getUsersWithoutConections(Facebook), 1)
  }

}
