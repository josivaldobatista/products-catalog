package com.jfb.productscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.jfb.productscatalog.dto.CategoryDTO;
import com.jfb.productscatalog.entities.Category;
import com.jfb.productscatalog.repositories.CategoryRepository;
import com.jfb.productscatalog.services.exceptions.DatabaseException;
import com.jfb.productscatalog.services.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository repository;

  @Transactional(readOnly = true)
  public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
    Page<Category> list = repository.findAll(pageRequest);
    return list.map(x -> new CategoryDTO(x));
  }

  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id) {
    Optional<Category> obj = repository.findById(id);
    Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    return new CategoryDTO(entity);
  }

  @Transactional
  public CategoryDTO insert(CategoryDTO dto) {
    Category entity = new Category();
    entity.setName(dto.getName());
    entity = repository.save(entity);
    return new CategoryDTO(entity);
  }

  @Transactional
  public CategoryDTO update(Long id, CategoryDTO dto) {
    try {
      Category entity = repository.getOne(id);
      entity.setName(dto.getName());
      entity = repository.save(entity);
      return new CategoryDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("O Id " + id + " não encontrado");
    }
  }

  public void delete(Long id) {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("O Id " + id + " não encontrado");
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Violação de integridade");
    }
  }

}
