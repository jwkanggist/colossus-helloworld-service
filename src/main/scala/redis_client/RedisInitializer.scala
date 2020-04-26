package redis_client

import colossus.core.InitContext
import colossus.protocols.http.Initializer
import colossus.protocols.redis.Redis


class RedisInitializer (serverContext: InitContext) extends Initializer(serverContext) {

  /*
    여러개의 connection-handler가 하나의 client를 사용

    Here, we create a ServiceClient using the redis protocol in the service’s Initializer.
    This means that one connection to Redis is opened per Worker,
    and all connections handled by that worker will use this client.

    Again, since everything here is per-worker and hence single-threaded,
    there are no issues with many request handlers using the same redis client.
   */
  val redisClient = Redis.client("localhost", 6379)

  override def onConnect: RequestHandlerFactory = serverContext => new RedisRequestHandler(serverContext,redisClient)

}
