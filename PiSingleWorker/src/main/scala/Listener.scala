// Listener.scala
//
// 

package com.example.picalc

import akka.actor.{Actor, ActorLogging}
import scala.concurrent.duration._

object Listener {

  case class PiApproximation(pi: Double, duration: Duration)
  
}  

class Listener extends Actor with ActorLogging {

     import Listener._

     def receive = {
     
         case PiApproximation(pi, duration ) =>
         println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s"
             .format(pi, duration))
         context.system.shutdown()
      }
}