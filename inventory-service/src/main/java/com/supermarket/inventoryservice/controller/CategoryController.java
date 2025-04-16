package com.supermarket.inventoryservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.service.CategoryService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/invent")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/addCategory")
	public String addCategory(@RequestBody Category category) {
		categoryService.addCategory(category);
		return "Category added successfully";
	}
	
	@GetMapping("/getCategoryById/{category_id}")
	public Category getCategoryById(@PathVariable int category_id) {
		Category C = categoryService.getCategoryById(category_id);
		return C;
	}
	
	@GetMapping("/getAllCategory")
	public List<Category> getAllCategory(){
		List<Category> categoryList = categoryService.getAllCategories();
		return categoryList;
	}
	
	@GetMapping("/getCategoryByName/{categoryName}")
	public Category getCategoryByName(@PathVariable String categoryName) {
		return categoryService.getCategoryByName(categoryName);
	}
	
	@PutMapping("/updateCategory/{id}")
	public Category updateCategory(@PathVariable int id, @RequestParam String newCategory) {
		return categoryService.updateCategory(id, newCategory);
	}
	
	@DeleteMapping("/deleteCategory/{category_id}")
	public String deleteCategory(@PathVariable int category_id) {
		categoryService.deleteCategory(category_id);
		return "Category deleted successfully";
	}
	
	
}
