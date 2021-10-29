package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SpoonacularClient {

    String API_BASE_URL = "https://api.spoonacular.com";
    @GET("/recipes/complexSearch?query=pasta&maxFat=25&number=2&apiKey=43d6739df5224359ac578587bf27a807")
    //Was Call<List<...>> but make sure to check if result is a list or just an object!!!
    Call<RecipeSearchResult> recipeResults();

}
