// Master.scala
//
// Messages used by the Actors

package com.example.piakka

import akka.actor.{Actor, ActorRef, ActorLogging, Terminated}
import scala.collection.mutable.{Map, Queue}
import scala.concurrent.duration._
import scala.math.abs


object Master {

   case class WorkToBeDone( work: Any )
   case object WorkIsReady
   case object NoWorkToBeDone
   
   case class Work(start: Int, numberOfElements: Int)
   case class Calculate(worker: ActorRef, calculation: Work) 
   case class Result(worker: ActorRef, value: Double)



}

class Master ( listener: ActorRef ) extends Actor with ActorLogging {

    import Listener._
    import Master._
    import Worker._
       
    var pi: Double = _
    
    val start: Long = System.currentTimeMillis
   
   // Step length of the calculation
   
   val numberOfElements: Int = 1000  
   
   var startElement: Int = 0
   
   var previousPi: Double = 0.0
   
   // Used to calculate the accuracy of the value of Pi
   
   val error: Double = 0.0000000001
   
   // Work Queue
   
   val workQueue = Queue.empty[Work]
      
   // On startup create packages of work
   
   override def preStart() = {
   
       var i = 0
       
       // initial value
              
       for ( i <- 0 to 10 ) {
       
       workQueue += Work(startElement, numberOfElements)
       
       startElement += numberOfElements
       
       }
       
       log.info("Work packages generated: {}", workQueue)
   
   }
   
    
   
  def receive = {
  
                        
     case WorkerCreated(worker) => listener ! WorkerJoined(worker)
                                    worker ! WorkIsReady 
                                    
     case SendWork(worker) =>  worker ! Calculate(worker, workQueue.dequeue()) 
                        
     case Result(worker, value) => pi += value                
                                if( abs( previousPi - pi ) <= error ) {
                                        log.info("Value of Pi found after:" + startElement)
                                        listener ! PiApproximation(pi, duration = (System.currentTimeMillis - start).millis)
                                        context.stop(self)
                                    } else {
                                       previousPi = pi
                                       // add another package of work to the workQueue
                                       startElement += numberOfElements
                                       workQueue += Work(startElement, numberOfElements) 
                                       self ! SendWork(worker) 
                                   }                    
                                                                 
  }
  
 

} // End of Class Master


