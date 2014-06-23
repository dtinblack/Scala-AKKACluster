// Master.scala
//
// Messages used by the Actors

package com.example.piakka

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinRouter

import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object Master {

 case object Calculate
// case object Work 
 case class Work(start: Int, nrOfElements: Int )
 case class Result(value: Double)

}

class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef ) extends Actor {

   import Master._
   import CalculatePi._

   var pi: Double = _
   var nrOfResults: Int = _
   val start: Long = System.currentTimeMillis
   
   val workerRouter = context.actorOf(
      Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")
      
   def receive = {
   
     // handle messages
   
     case Calculate => 
        for( i <- 0 until nrOfMessages ) workerRouter ! Work( i * nrOfElements, nrOfElements )
     
     case Result(value) =>
        pi += value
        nrOfResults += 1
        if( nrOfResults == nrOfMessages ) { // send the results to the listener
          listener ! PiApproximation(pi, duration = (System.currentTimeMillis - start).millis)
         // Stop this Actor and all its supervised children
         context.stop(self)
        }             
   }   

}