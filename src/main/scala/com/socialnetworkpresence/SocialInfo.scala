package com.socialnetworkpresence

import cats.Monad
import cats.syntax.all._

import com.socialnetworkpresence.Models._
import com.socialnetworkpresence.third.party.Models._
import com.socialnetworkpresence.third.party.SNGraphApi

trait SocialInfo[F[_]] {

  def getTotalConnections(person: Person): F[Connection]

  def getUsersWithoutConections(sn: SocialNetwork): F[Int]

}

object SocialInfo {

  def impl[F[_]: Monad](snApi: SNGraphApi[F]): SocialInfo[F] = new SocialInfo[F] {

    override def getTotalConnections(person: Person): F[Connection] =
      for {
        facebookConns <- snApi.getGraph(SocialNetwork.Facebook).map(getConnections(person, _))
        twitterConns  <- snApi.getGraph(SocialNetwork.Twitter).map(getConnections(person, _))
      } yield (facebookConns + twitterConns)

    override def getUsersWithoutConections(sn: SocialNetwork): F[Int] =
      snApi.getGraph(sn).map(firstConnsMap(_).filter(_._2.isEmpty).size)

    private def getConnections(person: Person, response: ResponseGraph): Connection =
      if (response.people.contains(person) === false) Connection(0, 0)
      else {
        val firsts       = firstConnsMap(response)
        val personFirsts = firsts.getOrElse(person, Nil)
        val seconds = personFirsts
          .flatMap(first => firsts.getOrElse(first, Nil))
          .filterNot(_ === person)
          .filterNot(personFirsts.contains(_))
          .toSet
        Connection(personFirsts.size, seconds.size)
      }

    private def firstConnsMap(response: ResponseGraph): Map[Person, List[Person]] = response.people
      .map(p =>
        (
          p,
          response.relationships.collect {
            case rel if rel.startNode === p || rel.endNode === p =>
              if (rel.startNode === p) rel.endNode
              else rel.startNode
          }
        )
      )
      .toMap

  }

}
