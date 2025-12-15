package com.api.tests;

import com.api.clients.PetClient;
import com.api.models.Category;
import com.api.models.Pet;
import com.api.models.Tag;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import com.api.config.ConfigManager;


import java.util.Arrays;
import java.util.List;

public class PetTests {

    private PetClient client;

    @BeforeClass
    public void setup() {
        client = new PetClient();
    }

    // Positive Test
    @Test
    public void testCreatePetSuccess() {
        Category category = new Category(1L, "Dogs");
        List<String> photos = Arrays.asList("https://example.com/photo1.jpg");
        List<Tag> tags = Arrays.asList(new Tag(1L, "friendly"));

        Pet pet = new Pet(123L, category, "Buddy", photos, tags, "available");

        Response res = client.createPet(pet);
        System.out.println(res.asString());

        Assert.assertEquals(res.getStatusCode(), 200);
        Assert.assertEquals(res.jsonPath().getString("name"), "Buddy");
        Assert.assertEquals(res.jsonPath().getString("status"), "available");
    }

    @Test
    public void testGetPetByIdSuccess() {
        Long petId = 20L;

        Response res = client.getPetById(petId);
        System.out.println(res.asString());

        Assert.assertEquals(res.getStatusCode(), 200);
        Assert.assertEquals(res.jsonPath().getLong("id"), petId);

    }


    // Negative Test
    @Test
    public void testCreatePetMissingName() {
        Category category = new Category(1L, "Dogs");
        List<String> photos = Arrays.asList("https://example.com/photo1.jpg");
        List<Tag> tags = Arrays.asList(new Tag(1L, "friendly"));

        Pet pet = new Pet(999L, category, null, photos, tags, "available");

        Response res = client.createPet(pet);
        System.out.println("Negative Missing Name:\n" + res.asString());

        Assert.assertEquals(res.getStatusCode(), 200,
                "API accepts missing name — so 200 is expected");
    }

    @Test
    public void testCreatePetInvalidJson() {
        String invalidBody = "{ \"id\": 1, \"name\": \"Buddy\" "; // Kurang tanda }

        Response res = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ConfigManager.API_KEY)
                .baseUri(ConfigManager.BASE_URL)
                .body(invalidBody)
                .post("/pet");

        System.out.println("Negative Invalid JSON:\n" + res.asString());

        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 500,
                "Expected 400 or 500 for invalid JSON");
    }


    // Boundary Test
    @Test
    public void testCreatePetBoundaryLongName() {
        Category category = new Category(1L, "Dogs");
        List<String> photos = Arrays.asList("https://example.com/photo.jpg");
        List<Tag> tags = Arrays.asList(new Tag(1L, "big-name"));

        // Generate name panjang (300 chars)
        char[] chars = new char[300];
        Arrays.fill(chars, 'A');
        String longName = new String(chars);

        Pet pet = new Pet(500L, category, longName, photos, tags, "available");

        Response res = client.createPet(pet);
        System.out.println("Boundary Long Name:\n" + res.asString());

        // PetStore biasanya menerima, tapi boundary test harus cek
        Assert.assertTrue(
                res.getStatusCode() == 200 || res.getStatusCode() == 400,
                "Should return 200 (allowed) or 400 (reject long name)"
        );
        System.out.println(res.getStatusCode());
    }

    @Test
    public void testGetPetBoundaryNegativeId() {
        Long invalidId = -1L;

        Response res = client.getPetById(invalidId);
        System.out.println("Boundary Negative ID:\n" + res.asString());

        Assert.assertTrue(
                res.getStatusCode() == 400 || res.getStatusCode() == 404,
                "Expected 400 or 404 for negative ID"
        );
    }


}

