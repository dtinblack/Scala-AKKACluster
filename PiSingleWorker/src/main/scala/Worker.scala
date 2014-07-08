// Worker.scala
//
// 

package com.example.picalc

import akka.actor.{Actor, ActorLogging}
import scala.annotation.tailrec

class Worker extends Actor with ActorLogging {

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
     
          case Work(start, numberOfElements ) => sender ! Result(calculatePi(start, numberOfElements))
     
     }

} 