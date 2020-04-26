package helloworld_server

import akka.actor.ActorSystem
import colossus.core.IOSystem
import colossus.protocols.http.HttpServer

// https://tumblr.github.io/colossus/0.11.1-RC1/quickstart.html

object HelloWorldServer extends App {
  /*
  // HelloWorld2를 실행시키면 event-loop가 돌아가기 시작한다

  An IOSystem is a collection of Workers with a thin management layer on top.

  Servers do not manage their own workers,
  but instead attach to an IOSystem and let the system do all the Worker management.

  Likewise, on its own, an IOSystem does nothing and its workers sit idle.
  Because an IOSystem is really just a set of Akka actors, it requires an ActorSystem to start.
   */

  implicit val actorSystem = ActorSystem()
  implicit val ioSystem    = IOSystem()

  /*
    This starts a Server that will listen on port 9000.
    The important part here is the third argument, which is a function of type InitContext => Initializer.
    This is a function that will be sent to every Worker in the IOSystem to initialize it,
    so every worker will call this function once.
   */
  HttpServer.start("hello-world", 9000) { context =>
    new HelloInitializer(context)
  }

}
