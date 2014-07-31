// Worker.scala
//
//

package com.example.piakka

import akka.actor.{Actor, ActorRef, ActorPath, ActorLogging}
import scala.annotation.tailrec
import akka.pattern.pipe 
import scala.concurrent.Future

object Worker {
    case class WorkerCreated(worker: ActorRef)
    case class SendWork(worker: ActorRef)
    case class CalculationFinished(workerSender: ActorRef, calculation: Double)    
}


class Worker(masterLocation: ActorPath) extends Actor with ActorLogging {
  
  import Master._
  import Worker._

  // Find the location of the Master 
   
  val master = context.actorFor(masterLocation)
  
  // Notify the Master that we're alive
  
  override def preStart() = master ! WorkerCreated(self)
  
  def calculatePi(start: Int, numberOfElements: Int) : Double = {
       @tailrec
       def calculatePiFor(start: Int, limit: Int, acc: Double, count: Int) : Double =
         count match {
         case x if x == limit => acc
         case _ => calculatePiFor(start + 1, limit, acc + 4.0 * (1 - (start % 2) * 2) / (2 * start + 1), 
                   count + 1)
       }
       calculatePiFor(start, numberOfElements , 0.0, 0)
   }
   
  
   implicit val ec = context.dispatcher

   def doCalculation(workSender: ActorRef, start: Int, numberOfElements: Int  ): Unit = {
     Future {

        CalculationFinished( workSender, calculatePi(start, numberOfElements) )
      } pipeTo self
    }
  
  def busy: Receive = {

    case WorkerIsReady => log.info("Received a message to do work but I'm busy")
    
    case CalculationFinished(worker, result) => master ! Result(worker, result)

    context.become(idle)
     
  }

  def idle: Receive = {

    case WorkerIsReady => master ! SendWork(self)
                                              
    case Calculate(worker, Work(start, numberOfElements )) => 
                                           doCalculation(worker, start, numberOfElements)
                                           context.become(busy)
  }

  def receive = idle
}

























