// Master.scala
//
// 

package com.example.picalc

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import scala.concurrent.duration._
import scala.math.abs

object Master {

   case object Calculate
   case class Work(start: Int, numberOfElements: Int)
   case class Result(value: Double)
   
}

class Master (listener: ActorRef ) extends Actor with ActorLogging {

   import Master._
   import Listener._

   var pi: Double = _
   val start: Long = System.currentTimeMillis
   
   // Step length of the calculation
   
   val numberOfElements: Int = 1000  
   
   var startElement: Int = 0
   
   var previousPi: Double = 0.0
   
   // Used to calculate the accuracy of the value of Pi
   
   val error: Double = 0.0000000001
   
   val worker = context.actorOf(Props[Worker], name = "worker")
   
   def receive = {
   
      case Calculate => worker ! Work(startElement, numberOfElements)
      
      case Result(value) => pi += value  
                            if( abs( previousPi - pi ) <= error ) {
                              log.info("Value of Pi found after:" + startElement)
                              listener ! PiApproximation(pi, duration = (System.currentTimeMillis - start).millis)
                              context.stop(self)
                             } else {
                              previousPi = pi
                              startElement += numberOfElements
                              worker ! Work( startElement, numberOfElements )
                             }  
   }

}

