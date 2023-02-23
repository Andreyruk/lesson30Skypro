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
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import pro.sky.lesson30skypro.model.Ingredients;
import pro.sky.lesson30skypro.model.Recipes;
import pro.sky.lesson30skypro.services.RecipesService;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
@Tag(name = "Рецепты", description = "Работа с рецептами")
public class RecipeController {
    private final RecipesService recipesService;

    @PostMapping
    @Operation(summary = "Создание записи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись создана"),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время создания записи")
            })
    public int addRecipe(@Valid @RequestBody Recipes recipe) {
        return recipesService.addRecipe(recipe);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение записи по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись получена"),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время получения записи")
            })
    public Recipes getRecipe(@PathVariable int id) {
        return recipesService.getRecipe(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение записи всех рецептов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись получена",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))}),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время получения записи")
            })
    public Collection<Recipes> getAllRecipe() {
        return recipesService.getAllRecipe();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование записи по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись отредактирована"),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время получения записи")
            })
    public Recipes editRecipes(@PathVariable int id, @Valid @RequestBody Recipes recipes) {
        return recipesService.editRecipe(id, recipes);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление записи по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись удалена")})
//    или так
    @Parameters(value = {@Parameter(name = "id", example = "1")})
    ResponseEntity<Void> removeRecipe(@PathVariable int id) {
        recipesService.removeRecipe(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/download")
    @Operation(summary = "Скачивание рецептов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись получена"),
                    @ApiResponse(responseCode = "500", description = "Возникли ошибки во время получения записи")
            })
    public HttpEntity<byte[]> downloadRecipes() throws FileNotFoundException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        header.setContentLength(header.getContentLength());
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recipes.docx");
        return new HttpEntity<>(recipesService.downloadRecipes(), header);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
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
