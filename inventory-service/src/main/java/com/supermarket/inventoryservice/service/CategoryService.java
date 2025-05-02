package com.supermarket.inventoryservice.service;

import com.supermarket.inventoryservice.exception.OperationFailedException;
import com.supermarket.inventoryservice.exception.ResourceAlreadyExistsException;
import com.supermarket.inventoryservice.exception.ResourceNotFoundException;
import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Category addCategory(Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        if (categoryRepository.findByCategoryName(category.getCategoryName()).isPresent()) {
            System.out.println("existing category found");
            throw new ResourceAlreadyExistsException("Category with name '" + category.getCategoryName() + "' already exists.");
        }
        System.out.println("existing category not found");
        if (category.getCategoryName() == null || category.getCategoryName().isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        try {
            return categoryRepository.save(category);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to add category: " + category.getCategoryName());
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while adding category: " + category.getCategoryName());
        }
    }


    public Category getCategoryById(int category_id) {
        return categoryRepository.findById(category_id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with Id: " + category_id));
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        // if list empty
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("No categories found.");
        }
        return categories;
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + categoryName));
    }

    public Category updateCategoryName(int category_id, String newCategoryName) {
        if (newCategoryName == null || newCategoryName.isBlank()) {
            throw new IllegalArgumentException("New category name cannot be empty.");
        }
        Category existingCategory = categoryRepository.findById(category_id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update. Category not found with Id: " + category_id));

        categoryRepository.findByCategoryName(newCategoryName).ifPresent(conflictCategory -> {
            if (conflictCategory.getCategoryId() != category_id) {
                throw new OperationFailedException("Cannot update category. Name '" + newCategoryName + "' is already used by another category (ID: " + conflictCategory.getCategoryId() + ")");
            }
        });

        existingCategory.setCategoryName(newCategoryName);
        try {
            return categoryRepository.save(existingCategory);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to update category with ID: " + category_id);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while updating category with ID: " + category_id);
        }
    }

    @Transactional
    public void deleteCategory(int categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Cannot delete. Category not found with Id: " + categoryId);
        }
        try {
            categoryRepository.deleteById(categoryId);
        } catch (DataIntegrityViolationException e) { // Catch constraint violation specifically
            throw new OperationFailedException("Cannot delete category with ID: " + categoryId + ". It might be associated with existing products.");
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to delete category with ID: " + categoryId);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred while deleting category with ID: " + categoryId);
        }
    }

    public List<String> getAllCategoryName() {
        List<Category> categoryList = categoryRepository.findAll();
        if (categoryList.isEmpty()) {
            return List.of(); // Return empty list

        }
        // return sorted list of category name
        return categoryList.stream()
                .map(Category::getCategoryName)
                .sorted()
                .collect(Collectors.toList());
    }
}

