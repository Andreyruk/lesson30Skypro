package pro.sky.lesson30skypro.services;

import pro.sky.lesson30skypro.model.Ingredients;

import java.util.Collection;

public interface IngredientService {
    int addIngredient(Ingredients ingredient);

    Ingredients getIngredient(int id);

    Collection<Ingredients> getAllIngredient();

    Ingredients editIngredients(int id, Ingredients ingredient);

    void removeIngredients(int id);
}
