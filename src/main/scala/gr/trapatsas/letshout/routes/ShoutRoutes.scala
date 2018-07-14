package gr.trapatsas.letshout.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.caching.scaladsl.Cache
import akka.http.scaladsl.model.{HttpMethods, StatusCodes, Uri}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.CachingDirectives.{alwaysCache, routeCache}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import akka.pattern.ask
import akka.util.Timeout
import gr.trapatsas.letshout.services.TwitterActor.Shout
import gr.trapatsas.letshout.services.{TwitterError, UserTweets}
import gr.trapatsas.letshout.utils.{JsonSupport, Settings}

import scala.concurrent.duration._

trait ShoutRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[ShoutRoutes])
  lazy val myCache: Cache[Uri, RouteResult] = routeCache[Uri]

  lazy val simpleKeyer: PartialFunction[RequestContext, Uri] = {
    case r: RequestContext if r.request.method == HttpMethods.GET => r.request.uri
  }

  def twitterActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout: Timeout = Timeout((Settings.getTwitterTimeout + 1).seconds)

  //#routes
  lazy val restRoutes: Route =
    pathPrefix("shouts") {
      parameters('handle.as[String], 'tweets.as[Int] ? 1) { (handle, tweets) =>
        alwaysCache(myCache, simpleKeyer) { // Caching happens here
          onSuccess(twitterActor ? Shout(handle, tweets)) {
            case response: UserTweets =>
              complete((StatusCodes.OK, response))
            case e: TwitterError =>
              complete(StatusCodes.InternalServerError, e.error)
            case _ =>
              log.error("An error occurred :(")
              complete(StatusCodes.InternalServerError, "An error occurred :(")
          }
        }
      }
    }
  //#routes

}
