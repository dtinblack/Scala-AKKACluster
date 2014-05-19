// PiFrontEnd.scala

package com.example.picluster

import language.postfixOps
import scala.concurrent.duration._
 
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.RootActorPath
import akka.actor.Terminated
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.Member
import akka.cluster.MemberStatus
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import java.util.concurrent.atomic.AtomicInteger

// Frontend

class PiFrontend extends Actor {
 
  var backends = IndexedSeq.empty[ActorRef]
  var jobCounter = 0
 
  def receive = {
    case job: Job if backends.isEmpty =>
      sender ! JobFailed("Service unavailable, try again later", job)
 
    case job: Job =>
      jobCounter += 1
      backends(jobCounter % backends.size) forward job
 
    case BackendRegistration if !backends.contains(sender) =>
      context watch sender
      backends = backends :+ sender
 
    case Terminated(a) ⇒
      backends = backends.filterNot(_ == a)
  }
} 

// Frontend

object PiFrontend {
  def main(args: Array[String]): Unit = {
    // Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
    val frontend = system.actorOf(Props[PiFrontend], name = "frontend")

    val counter = new AtomicInteger
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 2.seconds) {
      implicit val timeout = Timeout(5 seconds)
      (frontend ? Job("hello-" + counter.incrementAndGet())) onSuccess {
        case result => println(result)
      }
    }

  }
}       

