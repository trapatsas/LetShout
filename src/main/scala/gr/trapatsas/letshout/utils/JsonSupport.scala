package gr.trapatsas.letshout.utils

//#json-support
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import gr.trapatsas.letshout.services.{UserTweet, UserTweets}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val tweetsJsonFormat: RootJsonFormat[UserTweet] = jsonFormat2(UserTweet)
  implicit val userTweetsFormat: RootJsonFormat[UserTweets] = jsonFormat3(UserTweets)
}