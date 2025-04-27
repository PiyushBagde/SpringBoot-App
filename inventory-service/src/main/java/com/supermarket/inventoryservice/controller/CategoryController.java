package com.supermarket.inventoryservice.controller;

import com.supermarket.inventoryservice.exception.ResourceAlreadyExistsException;
import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/invent")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // routes for admin
    @PostMapping("/admin/addCategory")
    public String addCategory(@RequestBody Category category) throws ResourceAlreadyExistsException {
        Category addedCategory =  categoryService.addCategory(category);
        return addedCategory.getCategoryName() +" Category added successfully";
    }

    @GetMapping("/admin/getAllCategory")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/customer/getAllCategoryName")
    public List<String> getAllCategoryName() {
        return categoryService.getAllCategoryName();
    }

    @DeleteMapping("/admin/deleteCategory/{category_id}")
    public String deleteCategory(@PathVariable int category_id) {
        categoryService.deleteCategory(category_id);
        return "Category deleted successfully";
    }


    // routes for biller and customer
    @GetMapping("/getCategoryById/{category_id}")
    public Category getCategoryById(@PathVariable int category_id) {
        return categoryService.getCategoryById(category_id);
    }


    @GetMapping("/admin/getCategoryByName/{categoryName}")
    public Category getCategoryByName(@PathVariable String categoryName) {
        return categoryService.getCategoryByName(categoryName);
    }

    @PutMapping("/admin/updateCategory/{id}")
    public Category updateCategory(@PathVariable int id, @RequestParam String newCategory) {
        return categoryService.updateCategory(id, newCategory);
    }


}