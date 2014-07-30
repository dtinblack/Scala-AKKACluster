// Listener.scala
//
// 

package com.example.piakka

import akka.actor.{Actor, ActorLogging, ActorRef}
import scala.concurrent.duration._

object Listener {

  case class PiApproximation(pi: Double, duration: Duration)
  case class WorkerJoined(worker: ActorRef)
  
}  

class Listener extends Actor with ActorLogging {

     import Listener._

     def receive = {
     
         case PiApproximation(pi, duration ) =>
           //   println("Message recieved from the master")
              println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t\t%s"
              .format(pi, duration))
              context.system.shutdown()
              
         case WorkerJoined(worker) => log.info("Added Worker {} to the pool", worker)     
      }
      
      
}