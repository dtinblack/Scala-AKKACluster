// PiMessages.scala

package com.example.picluster

final case class Job(text: String)
final case class Result(text: String)
final case class JobFailed(reason: String, job: Job)
final case object BackendRegistration


