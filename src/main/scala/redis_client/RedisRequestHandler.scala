package redis_client

import akka.util.ByteString
import colossus.core.ServerContext
import colossus.protocols.http.HttpMethod.Get
import colossus.protocols.http.{Http, RequestHandler}
import colossus.protocols.http.UrlParsing.{/, Root, on}
import colossus.protocols.redis.RedisClient
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler


class RedisRequestHandler(context: ServerContext, redisClient:RedisClient[Callback]) extends RequestHandler(context) {

  override def handle: PartialHandler[Http] = {
    case req @ Get on Root / "get" / key =>
      redisClient.get(ByteString(key)).map {
        case Some(data) => req.ok(data.utf8String)
        case None       => req.notFound(s"Key $key was not found")
      }

    case req @ Get on Root / "set" / key / value =>
      redisClient.set(ByteString(key), ByteString(value)).map { _ =>
        req.ok("OK")
      }
  }

}

