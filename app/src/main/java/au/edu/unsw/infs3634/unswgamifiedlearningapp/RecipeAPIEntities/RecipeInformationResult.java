package au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "recipes")
public class RecipeInformationResult {

    private static String TAG = "RecipeInformationResult";

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("imageType")
    @Expose
    private String imageType;

    @SerializedName("servings")
    @Expose
    private Integer servings;

    @SerializedName("readyInMinutes")
    @Expose
    private Integer readyInMinutes;

    @SerializedName("license")
    @Expose
    private String license;

    @SerializedName("sourceName")
    @Expose
    private String sourceName;

    @SerializedName("sourceUrl")
    @Expose
    private String sourceUrl;

    @SerializedName("spoonacularSourceUrl")
    @Expose
    private String spoonacularSourceUrl;

    @SerializedName("aggregateLikes")
    @Expose
    private Integer aggregateLikes;

    @SerializedName("healthScore")
    @Expose
    private Double healthScore;

    @SerializedName("spoonacularScore")
    @Expose
    private Double spoonacularScore;

    @SerializedName("pricePerServing")
    @Expose
    private Double pricePerServing;

    @SerializedName("analyzedInstructions")
    @Expose
    private List<Object> analyzedInstructions = null;

    @SerializedName("cheap")
    @Expose
    private Boolean cheap;

    @SerializedName("creditsText")
    @Expose
    private String creditsText;

    @SerializedName("cuisines")
    @Expose
    private List<Object> cuisines = null;

    @SerializedName("dairyFree")
    @Expose
    private Boolean dairyFree;

    @SerializedName("diets")
    @Expose
    private List<Object> diets = null;

    @SerializedName("gaps")
    @Expose
    private String gaps;

    @SerializedName("glutenFree")
    @Expose
    private Boolean glutenFree;

    @SerializedName("instructions")
    @Expose
    private String instructions;

    @SerializedName("ketogenic")
    @Expose
    private Boolean ketogenic;

    @SerializedName("lowFodmap")
    @Expose
    private Boolean lowFodmap;

    @SerializedName("occasions")
    @Expose
    private List<Object> occasions = null;

    @SerializedName("sustainable")
    @Expose
    private Boolean sustainable;

    @SerializedName("vegan")
    @Expose
    private Boolean vegan;

    @SerializedName("vegetarian")
    @Expose
    private Boolean vegetarian;

    @SerializedName("veryHealthy")
    @Expose
    private Boolean veryHealthy;

    @SerializedName("veryPopular")
    @Expose
    private Boolean veryPopular;

    @SerializedName("whole30")
    @Expose
    private Boolean whole30;

    @SerializedName("weightWatcherSmartPoints")
    @Expose
    private Integer weightWatcherSmartPoints;

    @SerializedName("dishTypes")
    @Expose
    private List<String> dishTypes = null;

    @SerializedName("extendedIngredients")
    @Expose
    private List<ExtendedIngredient> extendedIngredients = null;

    @SerializedName("summary")
    @Expose
    private String summary;

    public RecipeInformationResult(Integer id, String title, String image, String imageType, Integer servings,
                                   Integer readyInMinutes, String license, String sourceName, String sourceUrl,
                                   String spoonacularSourceUrl, Integer aggregateLikes, Double spoonacularScore,
                                   Double pricePerServing, List<Object> analyzedInstructions, Boolean cheap,
                                   String creditsText, List<Object> cuisines, Boolean dairyFree, List<Object> diets,
                                   String gaps, Boolean glutenFree, String instructions, Boolean ketogenic,
                                   Boolean lowFodmap, List<Object> occasions, Boolean sustainable, Boolean vegan,
                                   Boolean vegetarian, Boolean veryHealthy, Boolean veryPopular, Boolean whole30,
                                   Integer weightWatcherSmartPoints, List<String> dishTypes, List<ExtendedIngredient>
                                   extendedIngredients, String summary) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.imageType = imageType;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.license = license;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.spoonacularSourceUrl = spoonacularSourceUrl;
        this.aggregateLikes = aggregateLikes;
        this.spoonacularScore = spoonacularScore;
        this.pricePerServing = pricePerServing;
        this.analyzedInstructions = analyzedInstructions;
        this.cheap = cheap;
        this.creditsText = creditsText;
        this.cuisines = cuisines;
        this.dairyFree = dairyFree;
        this.diets = diets;
        this.gaps = gaps;
        this.glutenFree = glutenFree;
        this.instructions = instructions;
        this.ketogenic = ketogenic;
        this.lowFodmap = lowFodmap;
        this.occasions = occasions;
        this.sustainable = sustainable;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.veryHealthy = veryHealthy;
        this.veryPopular = veryPopular;
        this.whole30 = whole30;
        this.weightWatcherSmartPoints = weightWatcherSmartPoints;
        this.dishTypes = dishTypes;
        this.extendedIngredients = extendedIngredients;
        this.summary = summary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }

    public void setSpoonacularSourceUrl(String spoonacularSourceUrl) {
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }

    public Integer getAggregateLikes() {
        return aggregateLikes;
    }

    public void setAggregateLikes(Integer aggregateLikes) {
        this.aggregateLikes = aggregateLikes;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }

    public Double getSpoonacularScore() {
        return spoonacularScore;
    }

    public void setSpoonacularScore(Double spoonacularScore) {
        this.spoonacularScore = spoonacularScore;
    }

    public Double getPricePerServing() {
        return pricePerServing;
    }

    public void setPricePerServing(Double pricePerServing) {
        this.pricePerServing = pricePerServing;
    }

    public List<Object> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public void setAnalyzedInstructions(List<Object> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    public Boolean getCheap() {
        return cheap;
    }

    public void setCheap(Boolean cheap) {
        this.cheap = cheap;
    }

    public String getCreditsText() {
        return creditsText;
    }

    public void setCreditsText(String creditsText) {
        this.creditsText = creditsText;
    }

    public List<Object> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<Object> cuisines) {
        this.cuisines = cuisines;
    }

    public Boolean getDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(Boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public List<Object> getDiets() {
        return diets;
    }

    public void setDiets(List<Object> diets) {
        this.diets = diets;
    }

    public String getGaps() {
        return gaps;
    }

    public void setGaps(String gaps) {
        this.gaps = gaps;
    }

    public Boolean getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Boolean getKetogenic() {
        return ketogenic;
    }

    public void setKetogenic(Boolean ketogenic) {
        this.ketogenic = ketogenic;
    }

    public Boolean getLowFodmap() {
        return lowFodmap;
    }

    public void setLowFodmap(Boolean lowFodmap) {
        this.lowFodmap = lowFodmap;
    }

    public List<Object> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<Object> occasions) {
        this.occasions = occasions;
    }

    public Boolean getSustainable() {
        return sustainable;
    }

    public void setSustainable(Boolean sustainable) {
        this.sustainable = sustainable;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public Boolean getVeryHealthy() {
        return veryHealthy;
    }

    public void setVeryHealthy(Boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public Boolean getVeryPopular() {
        return veryPopular;
    }

    public void setVeryPopular(Boolean veryPopular) {
        this.veryPopular = veryPopular;
    }

    public Boolean getWhole30() {
        return whole30;
    }

    public void setWhole30(Boolean whole30) {
        this.whole30 = whole30;
    }

    public Integer getWeightWatcherSmartPoints() {
        return weightWatcherSmartPoints;
    }

    public void setWeightWatcherSmartPoints(Integer weightWatcherSmartPoints) {
        this.weightWatcherSmartPoints = weightWatcherSmartPoints;
    }

    public List<String> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(List<String> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public List<ExtendedIngredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<ExtendedIngredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

//    public static RecipeInformationResult getRecipeById(Integer id) {
//        List<RecipeInformationResult> recipes = RecipeLevelsListActivity.recipes;
//        Log.d(TAG, "Number of recipes: " + recipes.size());
//
//        for (RecipeInformationResult r : recipes) {
//            Log.d(TAG, "Recipe id: " + r.getId());
//
//            if (r.getId().equals(id)) {
//                Log.d(TAG, "Match found");
//                return r;
//            }
//        }
//        Log.d(TAG, "end of getRecipeById() no match found :(");
//        return null;
//    }

}