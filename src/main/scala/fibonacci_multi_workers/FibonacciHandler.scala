package fibonacci_multi_workers

import akka.util.ByteString
import colossus.core.ServerContext
import colossus.protocols.http.HttpMethod.Get
import colossus.protocols.http.{Http, RequestHandler}
import colossus.protocols.http.UrlParsing.{/, Long, Root, on}
import colossus.protocols.memcache.MemcacheClient
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler


import scala.concurrent.Future
class FibonacciHandler(context: ServerContext,cache: MemcacheClient[Callback]) extends RequestHandler(context) {
  import fibonacci_multi_workers.FibonacciServer.executionContext

  // #fibonacci
  def fibonacci(i: Long): Long = i match {
    case 1 | 2 => 1
    case n     => fibonacci(n - 1) + fibonacci(n - 2)
  }


  /*

  // Fibonacci 1
  override def handle: PartialHandler[Http] = {

    case req @ Get on Root / "hello" =>
      Callback.successful(req.ok("Hello World!"))

    case req @ Get on Root / "fib" / Long(n) =>
      if (n > 0) {
        Callback.successful(req.ok(fibonacci(n).toString))
      } else {
        Callback.successful(req.badRequest("number must be positive"))
      }
  }
   */


  /*
  // Fibonacci 2

//    Future를 이용해서 다른 thread에서 동작이 가능하도록함 --> 병렬처리
//
//    This will let
//    1) the calculation of the result happen in another thread, and
//    2) once the Future is complete, execution will be moved back into the Worker and
//    3) the response will be built and sent back to the client.
//
//      Of course, this works even when a connection is pipelining multiple requests at the same time on a single connection.
//      Colossus will continue to process incoming requests even while it is waiting for existing ones to complete,
//      making it easy to parallelize work.


  override def handle: PartialHandler[Http] = {

    case req@Get on Root / "hello" =>
      Callback.successful(req.ok("Hello World!"))

    // #fibonacci2
    case req@Get on Root / "fib" / Long(n) =>
      if (n > 0) {
        Callback.fromFuture(Future(fibonacci(n))).map { result =>
          req.ok(result.toString)
        }
      } else {
        Callback.successful(req.badRequest("number must be positive"))
      }
    // #fibonacci2

  }
   */

  // Fibonacci 3


//  In this case, all of the memcached interactions are still happening entirely in the worker's thread,
//  with cache misses being offloaded to another thread to keep the event loop unblocked.

  override def handle: PartialHandler[Http] = {

    case req @ Get on Root / "hello" =>
      Callback.successful(req.ok("Hello World!"))

    case req @ Get on Root / "fib" / Long(n) =>
      if (n > 0) {
        val key = ByteString(s"fib_$n")
        cache.get(key).flatMap {
          case Some(value) => Callback.successful(req.ok(value.data.utf8String)) // 캐쉬에 저장된 값이 있으면 리
          case None =>
            for {
              result   <- Callback.fromFuture(Future(fibonacci(n)))
              cacheSet <- cache.set(key, ByteString(result.toString)) // 결과를 캐쉬한다
            } yield req.ok(result.toString)
        }
      } else {
        Callback.successful(req.badRequest("number must be positive"))
      }
  }

}

