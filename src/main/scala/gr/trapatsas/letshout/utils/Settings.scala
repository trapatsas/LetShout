package gr.trapatsas.letshout.utils

import com.typesafe.config.{Config, ConfigFactory}

object Settings {

  val config: Config = ConfigFactory.load()

  val getServerPort: Int = config.getInt("server.port")
  val getServerInterface: String = config.getString("server.interface")
  val getServerVersion: Double = config.getDouble("server.version")

  val getTwitterConsumerKey: String = config.getString("twitter.consumer.key")
  val getTwitterConsumerSecret: String = config.getString("twitter.consumer.secret")
  val getTwitterAccessKey: String = config.getString("twitter.access.key")
  val getTwitterAccessSecret: String = config.getString("twitter.access.secret")

  val getTwitterTimeout: Int = config.getInt("timeout.twitter")

}