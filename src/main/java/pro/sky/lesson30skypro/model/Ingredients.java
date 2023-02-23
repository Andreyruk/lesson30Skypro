package pro.sky.lesson30skypro.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ingredients {
    /**
     * Идентфикатор ингредиента
     */
    private int id;
    /**
     * Наименование ингредиента
     */
    @NotBlank(message = "Обязательно введите имя ингредиента")
    private String nameIngredient;
    @Positive
    private int quantity;
    @NotBlank
    private String measureUnit;
}
