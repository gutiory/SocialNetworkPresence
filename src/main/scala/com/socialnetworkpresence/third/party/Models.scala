package com.socialnetworkpresence.third.party

import com.socialnetworkpresence.Models._

object Models {

  final case class ResponseGraph(sn: SocialNetwork, people: List[Person], relationships: List[Relationship])

  final case class Relationship(`type`: String, startNode: Person, endNode: Person)

}
