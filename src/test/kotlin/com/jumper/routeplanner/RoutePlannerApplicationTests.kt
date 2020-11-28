package com.jumper.routeplanner

import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RoutePlannerApplicationTests {
    val API_KEY: String? = System.getenv("GOOGLE_API_KEY")
    val API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json"

    @Test
    fun dummyTest() {
        Assert.assertTrue(true)
    }

    //@Test
    fun shouldReturnOptimalRoute() {
        var origin = "Washington,DC"
        val destinations = mutableListOf("Pittsburgh", "New+York+City,NY", "Philadelphia")
        val route = getOptimalRoute(origin, destinations)
        Assert.assertTrue(route == listOf("Washington,DC", "Philadelphia", "New+York+City,NY", "Pittsburgh"))
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
            .get(API_URL)
            .then()
            .extract()
            .response()
        return response.jsonPath().getList<Int>("rows.elements.distance.value[0]").toTypedArray()
    }
}
