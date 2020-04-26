package fibonacci_multi_workers

import colossus.core.InitContext
import colossus.protocols.http.Initializer
import colossus.protocols.memcache.Memcache
import redis_client.RedisRequestHandler

class FibonacciInitializer (serverContext: InitContext) extends Initializer(serverContext) {

  val cache = Memcache.client("localhost", 11211)

  override def onConnect: RequestHandlerFactory = serverContext => new FibonacciHandler(serverContext,cache)
}
