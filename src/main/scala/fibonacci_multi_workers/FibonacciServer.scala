package fibonacci_multi_workers

import akka.actor.ActorSystem
import colossus.core.IOSystem
import colossus.metrics.MetricSystem
import colossus.protocols.http.HttpServer

// https://tumblr.github.io/colossus/0.11.1-RC1/quickstart.html

object FibonacciServer extends App {

  implicit val actorSystem = ActorSystem()
  implicit val io          = IOSystem("io-system", workerCount = Some(1), MetricSystem("io-system"))
  implicit val executionContext = actorSystem.dispatcher

  HttpServer.start("example-server", 9000) { initContext =>
    new FibonacciInitializer(initContext)
  }
  // #fibonacci
}