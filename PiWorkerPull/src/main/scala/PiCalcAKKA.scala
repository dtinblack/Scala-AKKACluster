// PiCalcAKKA.scala
//
// Estimating Pi written AKKA and Scala

package com.example.piakka

import akka.actor.{Actor, Props, ActorSystem, ActorLogging, ActorPath}


object PiCalcAKKA {
      
  def main(args: Array[String]) {
    
  println("Started ...")
  
  val system = ActorSystem("PiCalculation")
  
//  val calculatePi = system.actorOf(Props[CalculatePi], name = "calculatePi")
  
  def worker(name: String) = system.actorOf(Props(
      new TestWorker(ActorPath.fromString(
    "akka://%s/user/%s".format(system.name, name)))))
    
  val m = system.actorOf(Props[Master], "master")
    
  println("Creating three workers")
   
   val w1 = worker("master")
   val w2 = worker("master")
   val w3 = worker("master")
   
   
     m ! "Hello"
     m ! "fellas"
     m ! "so what is up? "
     m ! "what you doing ?"
  
  
  // shutdown the system
  
  // system.shutdown()
  

  }

}




















/*
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

*/