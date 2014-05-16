// PiAKKA.scala
//
// Estimating Pi written AKKA and Scala

package com.example.piakka

import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._
import scala.annotation.tailrec

object PiAKKA extends App {                                  // Bootstrap the application

calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000)
 
class Listener extends Actor {

     def receive = {
     
         case PiApproximation(pi, duration ) =>
         println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t\t%s"
             .format(pi, duration))
         context.system.shutdown()
      }
}         
     
class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef ) extends Actor {

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

class Worker extends Actor {

   def receive = { 
       case Work(start, nrOfElements ) => 

          sender ! Result(calculatePiFor(start, nrOfElements)) // preform work
    }  
        
}

def calculatePiFor(start: Int, nrOfElements: Int ): Double = {

//    println("start: %s number of elements %s".format(start, nrOfElements))

/*    
    var acc = 0.0;
    
    for( i <- start until ( start + nrOfElements ) )
        acc += 4.0 * (1 - ( i % 2 ) * 2 ) / ( 2 * i + 1 )
    
    acc
*/    



     @tailrec
     def calculatePi(start: Int, limit: Int, acc: Double) : Double =
       start match {
         case x if x == limit => acc
         case _ => calculatePi(start + 1, limit, acc + 4.0 * (1 - (start % 2) * 2) / (2 * start + 1))
       }

    calculatePi(start, start + nrOfElements, 0.0)
  
       
}

 
def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {
    // Create an Akka system
    val system = ActorSystem("PiSystem")
 
    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")
 
    // create the master
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, nrOfMessages, nrOfElements, listener)),
      name = "master")
 
    // start the calculation
    master ! Calculate
 
  }
  

 } // End of extended App

