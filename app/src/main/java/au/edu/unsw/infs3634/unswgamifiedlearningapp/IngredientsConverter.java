package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientsConverter {
    @TypeConverter
    public static List<ExtendedIngredient> fromString(String value) {
        Type listType = new TypeToken<List<ExtendedIngredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<ExtendedIngredient> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
