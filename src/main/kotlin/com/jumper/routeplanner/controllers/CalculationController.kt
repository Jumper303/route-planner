package com.jumper.routeplanner.controllers

import com.jumper.routeplanner.components.ConfigProperties
import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
public class CalculationController {
    val API_KEY: String? = System.getenv("GOOGLE_API_KEY")

    @Autowired
    var configProp: ConfigProperties? = null

    @PostMapping("/calculateroute")
    fun calculateRoute(
        @RequestParam("origin") origin: String?,
        @RequestParam("destinations") destinations: String?
    ): ModelAndView? {
        val modelAndView = ModelAndView()
        modelAndView.addObject("origin", origin)
        modelAndView.addObject("routePlan", basicRouteCalculator(origin, destinations))
        modelAndView.viewName = "route_plan_page"
        return modelAndView
    }

    private fun basicRouteCalculator(origin: String?, destinations: String?) : String {
        return getOptimalRoute(origin!!, destinations?.split("|")!!.toMutableList()).toString()
    }

    private fun getOptimalRoute(origin: String, destinations: MutableList<String>): MutableList<String> {
        val clonedDestinations: MutableList<String> = mutableListOf()
        clonedDestinations.addAll(destinations)
        val route: MutableList<String> = mutableListOf()
        var newOrigin: String = origin
        route.add(newOrigin)

        while (route.size < destinations.size + 1) {
            if (clonedDestinations.size == 1) {
                route.add(clonedDestinations[0])
                break
            }
            val distances = getDistances(newOrigin, clonedDestinations)
            val nearestLocation = clonedDestinations[distances.indexOf(distances.minOrNull())]
            route.add(nearestLocation)
            clonedDestinations.remove(nearestLocation)
            newOrigin = nearestLocation
        }
        return route
    }

    private fun getDistances(origin: String, destinations: List<String>): Array<Int> {
        val response = RestAssured.given()
            .log()
            .ifValidationFails(LogDetail.URI, true)
            .contentType(ContentType.JSON)
            .queryParam("units", "metric")
            .queryParam("origins", origin)
            .queryParam("destinations", destinations.joinToString("|"))
            .queryParam("key", API_KEY)
            .get(configProp?.getConfigValue("distanceApiUrl"))
            .then()
            .extract()
            .response()
        return response.jsonPath().getList<Int>("rows.elements.distance.value[0]").toTypedArray()
    }
}