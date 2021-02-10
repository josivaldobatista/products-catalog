package com.jfb.catalogproducts.services;

import java.util.List;
import java.util.stream.Collectors;

import com.jfb.catalogproducts.dto.CategoryDTO;
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
  public List<CategoryDTO> findAll() {
    List<Category> list = repository.findAll();
    return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
  }
}
