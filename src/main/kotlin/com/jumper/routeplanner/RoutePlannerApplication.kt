package com.jumper.routeplanner

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
class RoutePlannerApplication

fun main(args: Array<String>) {
	runApplication<RoutePlannerApplication>(*args)
}
