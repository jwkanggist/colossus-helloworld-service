package helloworld_server

import colossus.core.InitContext
import colossus.protocols.http.Initializer

class HelloInitializer(context: InitContext) extends Initializer(context) {

  // Hook environment and setup logic into a worker

  /*
    The Initializer is a long-lived object that is created once per Worker
    and manages the service’s environment within the worker.

    Because Workers are single-threaded,
    Initializers provide a place to share resources among all connections handled by the worker.

    In particular this is often where outgoing connections to external services can be opened.
   */
  override def onConnect: RequestHandlerFactory = context => new HelloRequestHandler(context)

}
