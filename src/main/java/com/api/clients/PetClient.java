package com.api.clients;

import com.api.config.ConfigManager;
import com.api.models.Pet;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PetClient {

    public Response createPet(Pet pet) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .header("User-Agent", "RestAssured")
                .header("Authorization", "Bearer " + ConfigManager.API_KEY)
                .baseUri(ConfigManager.BASE_URL)
                .body(pet)
                .when()
                .post("/pet");
    }

    public Response getPetById(Long id) {
        return RestAssured.given()
                .header("User-Agent", "RestAssured")
                .header("Authorization", "Bearer " + ConfigManager.API_KEY)
                .baseUri(ConfigManager.BASE_URL)
                .when()
                .get("/pet/" + id);
    }
}
