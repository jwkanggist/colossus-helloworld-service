package helloworld_server

import colossus.core.ServerContext
import colossus.protocols.http.HttpMethod.Get
import colossus.protocols.http.UrlParsing.{/, Root, on}
import colossus.protocols.http.{Http, RequestHandler}
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler

class HelloRequestHandler(context: ServerContext) extends RequestHandler(context) {

  //  Attaching each connections to your service’s actual work
  // A new request handler is attached to every connection and does all of the actual request processing.
  override def handle: PartialHandler[Http] = {
    // The handle partial function is where request processing actually happens.
    // In it, incoming HttpRequest objects are mapped to HttpResponse.
    case request @ Get on Root / "hello" => {
      /*
        Callbacks are the concurrency mechanism Colossus provides to do non-blocking operations.
        They are similar to Scala Futures in their use,
        but their execution is entirely single-threaded and managed by the Worker.
       */
      Callback.successful(request.ok("Hello World!"))
    }
  }

}