// PiCalc.scala
//
// Estimating Pi ( using Leibniz's Formula )  written in Scala

package com.example.picalc

import scala.annotation.tailrec

object PiCalc {
      
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
   
      
  def main(args: Array[String]) {
  
    val initial = System.currentTimeMillis 
    
    val pi = calculatePi(start = 0, numberOfElements = 10000)
        
    println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t\t%s"
             .format(pi, (System.currentTimeMillis - initial)))
             
    
  }
}