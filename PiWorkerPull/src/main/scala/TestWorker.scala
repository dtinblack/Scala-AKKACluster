// TestWorker.scala
//

package com.example.piakka

import akka.actor.{ActorPath, ActorRef}
import scala.concurrent.Future
import akka.pattern.pipe 

class TestWorker(masterLocation: ActorPath) extends Worker(masterLocation) {

  import Worker._

  implicit val ec = context.dispatcher
  
  def doWork(workSender: ActorRef, msg: Any): Unit = {
    Future {
        workSender ! msg
        WorkComplete("done")
      } pipeTo self
  }
} 