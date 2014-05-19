// PiApp.scala

package com.example.picluster

object PiApp {

  def main(args: Array[String]): Unit = {
    // starting 2 frontend nodes and 3 backend nodes
    PiFrontend.main(Seq("2551").toArray)
    PiBackend.main(Seq("2552").toArray)
    PiBackend.main(Array.empty)
    PiBackend.main(Array.empty)
    PiFrontend.main(Array.empty)
  }

}
