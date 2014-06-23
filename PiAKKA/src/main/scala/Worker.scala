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




