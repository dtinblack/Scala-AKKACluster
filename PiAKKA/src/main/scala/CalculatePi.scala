// CalculatePi.scala
//
// 

package com.example.piakka

import akka.actor.{Actor}
import scala.concurrent.duration.Duration


object CalculatePi {
   case class PiApproximation(pi: Double, duration: Duration)
}  

class CalculatePi extends Actor {

   import CalculatePi._
   
   def receive = {
   
      case PiApproximation(pi, duration) =>
       println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t\t%s"
             .format(pi, duration))
                 
      context.system.shutdown()
   
   }
}   


