// Worker.scala
//
// Messages used by the Actors

package com.example.piakka

import akka.actor.Actor
import scala.annotation.tailrec

object Worker {

//  case object Work

   case class Work(start: Int, nrOfElements: Int )

}




class Worker extends Actor {

   import Master._
   
 
     
   private def calculatePiFor(start: Int, nrOfElements: Int): Double = {

    var acc = 0.0;
    
    println("Start: " + start )
    
    for( i <- start until ( start + nrOfElements ) )
        acc += 4.0 * (1 - ( i % 2 ) * 2 ) / ( 2 * i + 1 )
    acc
   
   }
   


/*

      @tailrec
     private def calculatePiFor(start: Int, limit: Int, acc: Double) : Double =
       start match {
         case x if x == limit => acc
         case _ => println("Start: " + start + " Limit: " + limit + start)
                   calculatePiFor(start + 1, limit, acc + 4.0 * (1 - (start % 2) * 2) / (2 * start + 1))
                   
    } 
   
*/

   def receive = { 
       case Work(start, nrOfElements ) => 
          sender ! Result(calculatePiFor(start, nrOfElements)) // preform work
    }      

}




