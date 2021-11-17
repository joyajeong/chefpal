package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import retrofit2.http.QueryMap;

public interface SpoonacularClient {

    String API_BASE_URL = "https://api.spoonacular.com";
    String apiKey = "8099b1e129144cb686d2d3cb989cc486";

    //GET request to search recipes based on certain queries
    @GET("/recipes/complexSearch")
    Call<RecipeSearchResult> recipeResults(
            @Query("query") String query,
            @Query("cuisine") String cuisine,
            @Query("diet") String diet,
            @Query("intolerances") String intolerances,
            @Query("instructionsRequired") boolean instructionsRequired,
            @Query("maxReadyTime") int maxReadyTime,
            @Query("sort") String sort,
            @Query("apiKey") String apiKey
            );

    //GET request to get information using recipe ID
    @GET("/recipes/{id}/information")
    Call<RecipeInformationResult> recipeInformationResults(
            @Path("id") int recipeId,
            @Query("includeNutrition") Boolean inclNutrition,
            @Query("apiKey") String apiKey

    );

}
