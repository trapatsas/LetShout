package gr.trapatsas.letshout.services

import akka.actor.{Actor, ActorLogging}
import com.danielasfregola.twitter4s.TwitterRestClient
import gr.trapatsas.letshout.utils.Settings

import scala.concurrent.Await
import scala.concurrent.duration._

final case class UserTweets(user: String, tweets: Seq[UserTweet], version: Double)

final case class UserTweet(tweet: String, createdAt: String)

final case class TwitterError(error: String)

object TwitterActor {

  case class Shout(handle: String, lastN: Int)

}

class TwitterActor()(implicit twitterClient: TwitterRestClient) extends Actor with ActorLogging {

  import TwitterActor._

  override def receive: Receive = {

    case Shout(user, n) =>
      tweetsToShouts(user, n)
  }

  protected def tweetsToShouts(user: String, n: Int): Unit = {
    try {
      val result: Seq[UserTweet] = Await.result(
        twitterClient.userTimelineForUser(screen_name = user, count = n),
        Settings.getTwitterTimeout.seconds
      ).data.map(t => UserTweet(s"${t.text.toUpperCase}!", t.created_at.toString))
      val response = UserTweets(user, result, Settings.getServerVersion)
      sender() ! response
    } catch {
      case e: Exception =>
        log.error(e, e.getLocalizedMessage)
        sender() ! TwitterError(e.getLocalizedMessage)
    }
  }
}
