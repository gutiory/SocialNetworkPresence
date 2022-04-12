package com.socialnetworkpresence

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration

import cats.data.OptionT
import cats.effect.Temporal
import cats.syntax.all._

import com.socialnetworkpresence.Models._
import com.socialnetworkpresence.third.party.Models._
import com.socialnetworkpresence.third.party.SNGraphApi
import io.chrisdavenport.mules._

trait SocialInfo[F[_]] {

  def getTotalConnections(person: Person): F[Connection]

  def getUsersWithoutConections(sn: SocialNetwork): F[Int]

}

object SocialInfo {

  def impl[F[_]: Temporal](snApi: SNGraphApi[F]): F[SocialInfo[F]] =
    MemoryCache
      .ofConcurrentHashMap[F, SocialNetwork, ResponseGraph](
        TimeSpec.fromDuration(Duration(20, TimeUnit.SECONDS))
      )
      .map { cache =>
        new SocialInfo[F] {

          override def getTotalConnections(person: Person): F[Connection] =
            for {
              facebookConns <- getCachedGraph(SocialNetwork.Facebook).map(getConnections(person, _))
              twitterConns  <- getCachedGraph(SocialNetwork.Twitter).map(getConnections(person, _))
            } yield (facebookConns + twitterConns)

          override def getUsersWithoutConections(sn: SocialNetwork): F[Int] =
            getCachedGraph(sn).map(firstConnsMap(_).filter(_._2.isEmpty).size)

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

          private def getCachedGraph(sn: SocialNetwork): F[ResponseGraph] =
            OptionT(cache.lookup(SocialNetwork.Facebook)).getOrElseF(snApi.getGraph(sn))
        }

      }

}
