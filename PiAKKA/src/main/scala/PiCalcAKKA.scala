// PiCalcAKKA.scala
//
// Estimating Pi written AKKA and Scala

package com.example.piakka

import akka.actor.{Actor, Props, ActorSystem, ActorLogging}






object PiCalcAKKA {
      
  def main(args: Array[String]) {
  
  import CalculatePi._
  import Master._
  
  println("Started ...")
  
  // create an AKKA System
  
  val system = ActorSystem("PiAKKASystem")
  
  val calculatePi = system.actorOf(Props[CalculatePi], name = "calculatePi")

  // create the master

  val master = system.actorOf(Props(new Master(
      nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000, listener = calculatePi)),
      name = "master")
 
  // start the calculation

  master ! Calculate

  }

}