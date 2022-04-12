package com.socialnetworkpresence

import com.socialnetworkpresence.Models.SocialNetwork._
import com.socialnetworkpresence.Models._
import com.socialnetworkpresence.dummy
import munit.CatsEffectSuite

class SocialInfoSpec extends CatsEffectSuite {

  val socialInfo = SocialInfo.impl(dummy.snApi).unsafeRunSync()

  test("Get total connections from response") {

    val expectedJohn   = Connection(2, 1)
    val expectedPeter  = Connection(3, 0)
    val expectedGeorge = Connection(2, 1)
    val expectedHarry  = Connection(0, 0)
    val expectedAnna   = Connection(1, 2)

    for {
      _ <- assertIO(socialInfo.getTotalConnections(dummy.personJohn), expectedJohn)
      _ <- assertIO(socialInfo.getTotalConnections(dummy.personPeter), expectedPeter)
      _ <- assertIO(socialInfo.getTotalConnections(dummy.personGeorge), expectedGeorge)
      _ <- assertIO(socialInfo.getTotalConnections(dummy.personHarry), expectedHarry)
      _ <- assertIO(socialInfo.getTotalConnections(dummy.personAnna), expectedAnna)
    } yield ()
  }

  test("Get users wituout connections") {
    assertIO(socialInfo.getUsersWithoutConections(Facebook), 1)
  }

}
