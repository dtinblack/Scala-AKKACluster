// PiCalc.scala
//
// Calculating Pi using Scala

package com.example.picalc

import scala.annotation.tailrec

object Pi {
   
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
    
    val numberOfElements = 1000000
      
    val pi = calculatePi(numberOfElements)
    
    println(s"\n\tpi approximation: ${pi}, took: ${System.currentTimeMillis - start} millis")

  }
}