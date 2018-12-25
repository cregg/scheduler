package sched.services

import java.net.URI

import com.redis.RedisClient

import scala.util.{Properties, Try}

/**
  * Created by cleclair on 2018-08-24.
  */
object RedisService {

  val redis = Properties.envOrNone("REDIS_URL") match {
    case Some(redisUrl) =>
      val redisUri = new URI(redisUrl)
      val host = redisUri.getHost
      val port = redisUri.getPort
      val secret = Try(redisUri.getUserInfo.split(":",2).last).toOption
      new RedisClient(host, port, secret = secret)
    case _ => new RedisClient("localhost", 6379)
  }
}
