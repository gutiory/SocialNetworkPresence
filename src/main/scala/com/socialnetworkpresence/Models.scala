package com.socialnetworkpresence

import cats.kernel.Eq

import enumeratum.EnumEntry._
import enumeratum._
import io.circe.Codec
import io.circe.generic.semiauto._

object Models {

  final case class Person(name: String)

  object Person {

    implicit val eq: Eq[Person] = Eq.fromUniversalEquals

  }

  final case class Connection(firstDegree: Int, secondDegree: Int) {

    def +(that: Connection): Connection = Connection(that.firstDegree + firstDegree, that.secondDegree + secondDegree)

  }

  sealed trait SocialNetwork extends EnumEntry with Lowercase

  object SocialNetwork extends Enum[SocialNetwork] with CirceEnum[SocialNetwork] {

    val values = findValues

    case object Facebook extends SocialNetwork
    case object Twitter  extends SocialNetwork

    implicit val codec: Codec[SocialNetwork] = deriveCodec

  }

}
