package com.jfb.productscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.jfb.productscatalog.dto.CategoryDTO;
import com.jfb.productscatalog.dto.ProductDTO;
import com.jfb.productscatalog.entities.Category;
import com.jfb.productscatalog.entities.Product;
import com.jfb.productscatalog.repositories.CategoryRepository;
import com.jfb.productscatalog.repositories.ProductRepository;
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
public class ProductService {

  @Autowired
  private ProductRepository repository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
    Page<Product> list = repository.findAll(pageRequest);
    return list.map(x -> new ProductDTO(x));
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    Optional<Product> obj = repository.findById(id);
    Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Product não encontrada"));
    return new ProductDTO(entity, entity.getCategories());
  }

  @Transactional
  public ProductDTO insert(ProductDTO dto) {
    Product entity = new Product();
    copyDtoToEntity(dto, entity);
    entity = repository.save(entity);
    return new ProductDTO(entity);
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO dto) {
    try {
      Product entity = repository.getOne(id);
      copyDtoToEntity(dto, entity);
      entity = repository.save(entity);
      return new ProductDTO(entity);
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

  private void copyDtoToEntity(ProductDTO dto, Product entity) {
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setDate(dto.getDate());
    entity.setImgUrl(dto.getImgUrl());
    entity.setPrice(dto.getPrice());

    entity.getCategories().clear();
    for (CategoryDTO catDto : dto.getCategories()) {
      Category category = categoryRepository.getOne(catDto.getId());
      entity.getCategories().add(category);
    }
  }

}
