package pro.sky.lesson30skypro.services;

import pro.sky.lesson30skypro.model.Recipes;

import java.util.Collection;
import java.util.List;

public interface RecipesService {
    int addRecipe(Recipes recipe);

    Recipes getRecipe(int id);

    Collection<Recipes> getAllRecipe();

    Recipes editRecipe(int id, Recipes recipes);

    void removeRecipe(int id);

    List<Recipes> findRecipesByIngredient(int id);

    byte[] downloadRecipes();
}
