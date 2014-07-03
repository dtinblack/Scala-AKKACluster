// Master.scala
//
// Messages used by the Actors

package com.example.piakka

import akka.actor.{Actor, ActorRef, ActorLogging, Terminated}
import scala.collection.mutable.{Map, Queue}


object Master {

   case class WorkToBeDone( work: Any )
   case object WorkIsReady
   case object NoWorkToBeDone

}

class Master extends Actor with ActorLogging {

    import Master._
    import Worker._

   // keep track of workers and their activity
   
   val workers = Map.empty[ActorRef, Option[Tuple2[ActorRef, Any]]]
   
   // create a queue to keep track of who is doing what
   
   val workQ = Queue.empty[Tuple2[ActorRef, Any]]
   
//   println("Master created")
   
   def notifyWorkers(): Unit = {
   
//      println("Notifying workers")
   
      if(!workQ.isEmpty) {
      
         workers.foreach {
             case ( worker, m ) if ( m.isEmpty ) => worker ! WorkIsReady
             case _ => 
         }     
      
      } 
   }
   
   
  def receive = {
  
      // Worker created - add to the list
  
      case WorkerCreated(worker) =>
            log.info("Worker created: {}", worker)
            context.watch(worker)
            workers += ( worker -> None) 
  
      // Worker wants more work
      
      case WorkerRequestsWork(worker) =>
        log.info("Worker requests work: {}", worker)
        if( workers.contains(worker)) {
           if(workQ.isEmpty)
              worker ! NoWorkToBeDone
            else if (workers(worker) == None) {
            val (workSender, work) = workQ.dequeue()
            workers += (worker -> Some(workSender -> work))
            worker.tell(WorkToBeDone(work), workSender)
            }   
        }
        
        // Worker has completed its work 
        
         
     case WorkIsDone(worker) =>
      if (!workers.contains(worker))
        log.info("Blurgh! {} said it's done work but we didn't know about him", worker)
      else
        workers += (worker -> None)

    // Worker has died - need to make sure that work is completed
    
    case Terminated(worker) =>
      if (workers.contains(worker) && workers(worker) != None) {
        log.info("Blurgh! {} died while processing {}", worker, workers(worker))
        // Send the work that it was doing back to ourselves for processing
        val (workSender, work) = workers(worker).get
        self.tell(work, workSender)
      }
      workers -= worker
     
     case work => 
          log.info("Queueing {}", work)
          workQ.enqueue(sender -> work)
          notifyWorkers()
  
  }

} // End of Class Master



/*

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

*/