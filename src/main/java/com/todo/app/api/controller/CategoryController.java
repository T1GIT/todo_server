package com.todo.app.api.controller;

import com.todo.app.data.model.Category;
import com.todo.app.data.service.CategoryService;
import com.todo.app.security.auth.AuthContext;
import com.todo.app.security.auth.AuthUser;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "Category controller",
        description = "Controller to provide operations with category models")
@RequiredArgsConstructor
@RestController
@RequestMapping("/todo")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthContext authContext;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesByUser() {
        return categoryService.getOf(authContext.getUser().getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public Category addCategory(
            @RequestBody Category category) {
        return categoryService.add(authContext.getUser().getId(), category);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/category/{categoryId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeName(
            @PathVariable long categoryId,
            @RequestBody Category category) {
        categoryService.changeName(authContext.getUser().getId(), categoryId, category.getName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/category/{categoryId}")
    public void deleteCategory(
            @PathVariable long categoryId) {
        categoryService.delete(authContext.getUser().getId(), categoryId);
    }
}