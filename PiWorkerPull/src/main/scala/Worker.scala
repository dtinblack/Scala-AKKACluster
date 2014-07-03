// Worker.scala
//
// Messages used by the Actors

package com.example.piakka

import akka.actor.{Actor, ActorRef, ActorPath, ActorLogging}
import scala.annotation.tailrec

object Worker {

    case class WorkerCreated(worker: ActorRef)
    case class WorkerRequestsWork(worker: ActorRef)
    case class WorkIsDone(worker: ActorRef)
    
}


abstract class Worker(masterLocation: ActorPath)
  extends Actor with ActorLogging {
  
  import Master._
  import Worker._

  // We need to know where the master is
  val master = context.actorFor(masterLocation)
  
//  println("Worker created")

  // This is how our derivations will interact with us.  It
  // allows dervations to complete work asynchronously
  case class WorkComplete(result: Any)

  // Required to be implemented for the 
  // Pi Calculation
  
  def doWork(workSender: ActorRef, work: Any): Unit

  // Notify the Master that we're alive
  override def preStart() = master ! WorkerCreated(self)

  // This is the state we're in when we're working on something.
  // In this state we can deal with messages in a much more
  // reasonable manner
  def working(work: Any): Receive = {
    // Pass... we're already working
    case WorkIsReady =>
    // Pass... we're already working
    case NoWorkToBeDone =>
    // Pass... we shouldn't even get this
    case WorkToBeDone(_) =>
      log.info("Yikes. Master told me to do work, while I'm working.")
    // Our derivation has completed its task
    case WorkComplete(result) =>
      log.info("Work is complete.  Result {}.", result)
      master ! WorkIsDone(self)
      master ! WorkerRequestsWork(self)
      // We're idle now
      context.become(idle)
  }

  // In this state we have no work to do.  There really are only
  // two messages that make sense while we're in this state, and
  // we deal with them specially here
  def idle: Receive = {
    // Master says there's work to be done, let's ask for it
    case WorkIsReady =>
      log.info("Requesting work")
      master ! WorkerRequestsWork(self)
    // Send the work off to the implementation
    case WorkToBeDone(work) =>
      log.info("Got work {}", work)
      doWork(sender, work)
      context.become(working(work))
    // We asked for it, but either someone else got it first, or
    // there's literally no work to be done
    case NoWorkToBeDone =>
  }

  def receive = idle
}



























/*

object Worker {

//  case object Work

   case class Work(start: Int, nrOfElements: Int )

}


class Worker extends Actor {

   import Master._
         
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

   def receive = { 
       case Work(start, nrOfElements ) => 
          sender ! Result(calculatePi(start, nrOfElements)) // preform work
    }      

}

*/




