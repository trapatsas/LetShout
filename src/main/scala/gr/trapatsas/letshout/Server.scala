package gr.trapatsas.letshout

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import gr.trapatsas.letshout.routes.ShoutRoutes
import gr.trapatsas.letshout.services.TwitterActor
import gr.trapatsas.letshout.utils.Settings

import scala.concurrent.Await
import scala.concurrent.duration.Duration


object Server extends App with ShoutRoutes {

  // set up ActorSystem and other dependencies here
  implicit val system: ActorSystem = ActorSystem("shoutAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val twitterClient: TwitterRestClient = new TwitterRestClient(
    ConsumerToken(key = Settings.getTwitterConsumerKey, secret = Settings.getTwitterConsumerSecret),
    AccessToken(key = Settings.getTwitterAccessKey, secret = Settings.getTwitterAccessSecret)
  )

  val twitterActor: ActorRef = system.actorOf(Props(new TwitterActor), "twitterActor")

  lazy val routes: Route = restRoutes

  //#http-server
  Http().bindAndHandle(routes, Settings.getServerInterface, Settings.getServerPort)

  println(s"Visit server at http://${Settings.getServerInterface}:${Settings.getServerPort}/")

  Await.result(system.whenTerminated, Duration.Inf)
  //#http-server
}
