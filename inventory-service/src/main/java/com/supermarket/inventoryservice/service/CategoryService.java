package com.supermarket.inventoryservice.service;

import com.supermarket.inventoryservice.exception.ResourceNotFoundException;
import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }


    public Category getCategoryById(int category_id) {
        return categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found with Id: " + category_id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));

    }

    public Category updateCategory(int category_id, String newCategory) {
        Category existingCategory = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found with Id: " + category_id));
        existingCategory.setCategoryName(newCategory);
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(int category_id) {
        if (!categoryRepository.existsById(category_id)) {
            throw new ResourceNotFoundException("Category not found with Id :" + category_id);
        }
        categoryRepository.deleteById(category_id);
    }

    public List<String> getAllCategoryName() {
        List<Category> categoryList = categoryRepository.findAll();
        // return sorted list of category name
        return categoryList.stream().map(Category::getCategoryName).sorted().toList();
    }
}

