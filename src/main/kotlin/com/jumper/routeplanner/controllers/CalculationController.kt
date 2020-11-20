package com.jumper.routeplanner.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDateTime

@Controller
public class CalculationController {

    @PostMapping("/calculateroute")
    fun calculateRoute(
        @RequestParam("origin") origin: String?,
        @RequestParam("destinations") destinations: String?
    ): ModelAndView? {
        val modelAndView = ModelAndView()
        modelAndView.addObject("origin", origin)
        modelAndView.addObject("routePlan", dummyRouteCalculator(origin, destinations))
        modelAndView.viewName = "route_plan_page"
        return modelAndView
    }

    private fun dummyRouteCalculator(origin: String?, destinations: String?) : String {
        return "this is the road to hell ${LocalDateTime.now()}"
    }
}