// TestPiSingleWorker.scala
//
// 

package com.example.picalc

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.testkit.{TestKit, TestActorRef, ImplicitSender}
import org.scalatest.matchers.MustMatchers
import org.scalatest.WordSpecLike

class TestPiSingleWorker extends TestKit(ActorSystem("TestPiSystem"))
  with WordSpecLike
  with MustMatchers 
  with ImplicitSender {
 
  "A Worker" must {
  
    import Master._
  
    // Creation of the TestActorRef
    val worker = TestActorRef[Worker]
 
    "receive message with result" in {
      
      // This call is synchronous. The actor receive() method will be called in the current thread
      
      worker ! Work(0, 10000)
      
      expectMsg(Result(3.1414926535900345))
    }
  }
}