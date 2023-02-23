package pro.sky.lesson30skypro.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import pro.sky.lesson30skypro.model.Ingredients;
import pro.sky.lesson30skypro.services.IngredientService;
import pro.sky.lesson30skypro.services.RecipesService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredient")
@Tag(name = "Ингредиенты", description = "Работа с ингредиентами")
public class IngredientController {
    private final IngredientService ingredientService;
    private final RecipesService recipesService;

    @PostMapping
    @Operation(summary = "Создание записи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись создана",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))}),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время создания записи")
            })
    public int addIngredient(@Valid @RequestBody Ingredients ingredient) {
        return ingredientService.addIngredient(ingredient);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение записи по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись получена",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))}),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время получения записи")
            })
    public Ingredients getIngredient(@PathVariable int id) {
        return ingredientService.getIngredient(id);
    }

    @GetMapping
    @Operation(summary = "Получение записи всех ингредиентов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись получена",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))}),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время получения записи")
            })
    public Collection<Ingredients> getAllIngredient() {
        return ingredientService.getAllIngredient();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование записи по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись изменена",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))}),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время получения записи")
            })
    public Ingredients editIngredient(@PathVariable int id, @Valid @RequestBody Ingredients ingredient) {
        return ingredientService.editIngredients(id, ingredient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление записи по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись удалена")})
//    или так
    @Parameters(value = {@Parameter(name = "id", example = "1")})
    public ResponseEntity<Void> removeIngredients(@PathVariable int id){
        if (!recipesService.findRecipesByIngredient(id).isEmpty()) {
            throw new RuntimeException(String.format("Ингредиент с id %s используется", id));
        }
        ingredientService.removeIngredients(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions (
            MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) errors).getField();
            String errorsMessage = ((FieldError) errors).getDefaultMessage();
            errors.put(fieldName, errorsMessage);
        });
        return errors;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException notFoundException) {
        return notFoundException.getMessage();
    }
}
