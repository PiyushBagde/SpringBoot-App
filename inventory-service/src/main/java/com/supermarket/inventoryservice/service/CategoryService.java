package com.supermarket.inventoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermarket.inventoryservice.exception.ResourceNotFoundException;
import com.supermarket.inventoryservice.model.Category;
import com.supermarket.inventoryservice.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public Category addCategory(Category category) {
		return categoryRepository.save(category);
	}
	
	
	public Category getCategoryById(int category_id) {
		Category Category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found with Id: "+category_id));
		return Category;
	}
	
	public List<Category> getAllCategories(){
		return categoryRepository.findAll(); 
	}
	
	public Category getCategoryByName(String categoryName) {
//		Optional<Category> C = categoryRepository.findByName(categoryName);
//		if(!C.isPresent()) {
//			throw new ResourceNotFoundException("Category not found with name "+ categoryName);
//		}
//		return (Category) C.get();
		return categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));
   
	}
	

	public Category updateCategory(int category_id, String newCategory) {
		Category existingCategory = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found with Id: "+category_id));
		existingCategory.setCategoryName(newCategory);
		return categoryRepository.save(existingCategory);
	}
	
	public void deleteCategory(int category_id) {
		if(!categoryRepository.existsById(category_id)) {
			throw new ResourceNotFoundException("Category not found with Id :"+category_id);
		}
		categoryRepository.deleteById(category_id);
	}
}
