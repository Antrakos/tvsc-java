package com.tvsc.web

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.springframework.boot.SpringApplication

class BasicSimulation extends Simulation {
  val context = SpringApplication.run(classOf[Web])

  val httpConf = http
    .baseURL("http://localhost:8080/api/v1") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("get all").get("/series").check(status.is(200)))
    .pause(1) // Note that Gatling has recorded real time pauses
    .exec(http("get episodes").get("/series/78901/episodes").check(status.is(200)))
    .pause(1)
    .exec(http("get watched episodes").get("/series/78901/episodes/watched").check(status.is(200)))

  setUp(scn.inject(atOnceUsers(5)).protocols(httpConf))
}