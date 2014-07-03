// WorkerSpec.scala
//
// Messages used by the Actors

package com.example.piakka

import akka.actor.{ActorSystem, Props, ActorPath}
import akka.testkit.{TestKit, ImplicitSender}
import org.scalatest.matchers.MustMatchers
import org.scalatest.WordSpecLike
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.duration._

class WorkerSpec extends TestKit(ActorSystem("WorkerSpec"))
      with ImplicitSender
      with WordSpecLike
      with BeforeAndAfterAll
      with MustMatchers {
      
  override def afterAll() {
  
     system.shutdown()
     
  }
  
  def worker(name: String) = system.actorOf(Props(
      new TestWorker(ActorPath.fromString(
    "akka://%s/user/%s".format(system.name, name))))) 
    
  "Worker" should {
     "work" in {
     
     val master = system.actorOf(Props[Master], "master")
     
     val w1 = worker("master")
     val w2 = worker("master")
     val w3 = worker("master") 
          
       master ! "Hithere"
       master ! "Guys"
       master ! "So"
       master ! "What's"
       master ! "Up?"
     
       expectMsgAllOf("Hithere", "Guys", "So", "What's", "Up?")
     
   
     }
    }
 }                      