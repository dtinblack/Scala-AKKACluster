// Worker.scala
//
// Messages used by the Actors

package com.example.piakka

import akka.actor.{Actor, ActorRef, ActorPath, ActorLogging}
import scala.annotation.tailrec
import akka.pattern.pipe 
import scala.concurrent.Future


object Worker {

    case class WorkerCreated(worker: ActorRef)
    case class WorkerRequestsWork(worker: ActorRef)
    case class WorkIsDone(worker: ActorRef)
    
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
  

  // This is the state we're in when we're working on something.
  // In this state we can deal with messages in a much more
  // reasonable manner
  def working: Receive = {
    // Pass... we're already working
    case WorkIsReady =>
    // Pass... we're already working
    case NoWorkToBeDone =>
    // Pass... we shouldn't even get this
    case WorkToBeDone(_) =>
      log.info("Yikes. Master told me to do work, while I'm working.")
    // Our derivation has completed its task
    
  
    
    case CalculationFinished(worker, result) => master ! Result(worker, result)
//      log.info("Work is complete.  Result {}.", result) /** prints result to the terminal **/

      // We're idle now
      context.become(idle)
     
  }

  // In this state we have no work to do.  There really are only
  // two messages that make sense while we're in this state, and
  // we deal with them specially here
  def idle: Receive = {
    // Master says there's work to be done, let's ask for it
    case WorkIsReady => 
 //           log.info("Requesting work from the Master")
                         master ! SendWork(self)
                                              
    case Calculate(worker, Work(start, numberOfElements )) => doCalculation(worker, start, numberOfElements)
 //                          sender ! Result(worker, calculatePi(start, numberOfElements))
                           context.become(working)

    // Send the work off to the implementation
    case WorkToBeDone(work) =>
      log.info("Got work {}", work)
 //     doWork(sender, work)            //*** call to do some work ***//
//      context.become(working(work))
    // We asked for it, but either someone else got it first, or
    // there's literally no work to be done
    case NoWorkToBeDone =>
  }

  def receive = idle
}

























