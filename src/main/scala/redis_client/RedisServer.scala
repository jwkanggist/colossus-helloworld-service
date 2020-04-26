package redis_client

import akka.actor.ActorSystem
import colossus.core.IOSystem
import colossus.protocols.http.{HttpServer, Initializer, RequestHandler}

// https://tumblr.github.io/colossus/0.11.1-RC1/quickstart.html

object RedisServer extends App {

  implicit val actorSystem = ActorSystem()
  implicit val ioSystem    = IOSystem()

  // #redis-client
  HttpServer.start("example-server", 9000) { initContext =>
    new RedisInitializer(initContext)
  }
  // #redis-client
}
