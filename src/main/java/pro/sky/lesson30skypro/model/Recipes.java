package pro.sky.lesson30skypro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recipes {
    /**
     * Идентфикатор рецепта
     */
    private int id;
    /**
     * Наименование рецепта
     */
    @NotBlank(message = "Обязательно введите имя рецепта")
    private String nameRecipe;
    /**
     * Время готовки
     */
    @Positive
    private int cookingTime;
    @NotBlank
    private String measureUnitTime;
    @NotEmpty
    private List<Ingredients> ingredients;
    @NotEmpty
    private List<String> stepsCooking;
}
