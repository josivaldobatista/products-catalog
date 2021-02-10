package com.jfb.catalogproducts.services;

import java.util.List;

import com.jfb.catalogproducts.entities.Category;
import com.jfb.catalogproducts.repositories.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository repository;
  
  @Transactional(readOnly = true)
  public List<Category> findAll() {
    return repository.findAll();
  }
}
