// PiSingleWorker.scala
//
// Estimating Pi ( using Leibniz's Formula )  written in Scala

package com.example.picalc

import akka.actor.{ActorSystem, Props}


object PiSingleWorker {
            
  def main(args: Array[String]) {
  
      import Master._
  
      // Create AKKA System
  
      val system = ActorSystem("PiSingleWorker")
  
      // Create the listener, which will print the result and shutdown
  
      val listener = system.actorOf(Props[Listener], name = "listener")
      
      // Create the master 
      
      val master = system.actorOf(Props( new Master(listener) ), name = "master")
      
      // start the calculation
  
      master ! Calculate

  }
  
} // End of PiSingleWorker