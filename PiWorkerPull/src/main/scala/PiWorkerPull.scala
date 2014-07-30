// PiWorkerPull.scala
//
// Estimating Pi written AKKA and Scala

package com.example.piakka

import akka.actor.{Actor, Props, ActorSystem, ActorLogging, ActorPath}

object PiWorkerPull {
      
  def main(args: Array[String]) {
  
  import Master._
  
  // Create AKKA System
    
  val system = ActorSystem("PiWorkerPull")
  
  // Create the listener to print result and shutdown system
  
  val listener = system.actorOf(Props[Listener], name = "listener")
  
  // Create the Master
  
  val master = system.actorOf(Props( new Master(listener) ), name = "master")
  
  // Create Workers - in this case 4 
  
  def worker(name: String) = system.actorOf(Props(
      new Worker(ActorPath.fromString(
    "akka://%s/user/%s".format(system.name, name))))) 
  
    val worker1 = worker("master")
    val worker2 = worker("master")
    val worker3 = worker("master")
    val worker4 = worker("master")
    val worker5 = worker("master")
    val worker6 = worker("master")
        


  }


}



















