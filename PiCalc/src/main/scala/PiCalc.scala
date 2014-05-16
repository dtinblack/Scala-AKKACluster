// PiCalc.scala
//
// Estimating Pi ( using Leibniz's Formula )  written in Scala

package com.example.picalc

import scala.annotation.tailrec

object PiCalc {
   
  def calculatePi(numberOfElements: Int) : Double = {
     @tailrec
     def calculatePiFor(start: Int, limit: Int, acc: Double) : Double =
       start match {
         case x if x == limit => acc
         case _ => calculatePiFor(start + 1, limit, acc + 4.0 * (1 - (start % 2) * 2) / (2 * start + 1))
       }
     calculatePiFor(0, numberOfElements , 0.0)
   }
   
  def main(args: Array[String]) {
  
    val start = System.currentTimeMillis 
    
    val numberOfElements = 100000
      
    val pi = calculatePi(numberOfElements)
        
    println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t\t%s"
             .format(pi, (System.currentTimeMillis - start)))

  }
}