package com.jfb.productscatalog.services;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.jfb.productscatalog.dto.CategoryDTO;
import com.jfb.productscatalog.dto.ProductDTO;
import com.jfb.productscatalog.dto.UriDTO;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

  @Autowired
  private ProductRepository repository;

  @Autowired
  private CategoryRepository categoryRepository;

  // @Autowired
  // private S3Service s3Service;

  // Resolvendo o problema n + 1 com a query abaixo de Products e Category
  // (ESTUDO).
  @Transactional(readOnly = true)
  public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
    Page<Product> page = repository.findAll(pageRequest);
    repository.findProductsCategories(page.stream().collect(Collectors.toList()));
    return page.map(x -> new ProductDTO(x, x.getCategories()));
  }

  @Transactional(readOnly = true)
  public Page<ProductDTO> find(Long categoryId, String name, PageRequest pageRequest) {
    List<Category> categories = (categoryId == 0) 
      ? null : Arrays.asList(categoryRepository.getOne(categoryId));
    Page<Product> list = repository.find(categories, name, pageRequest);
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

  /***
  public UriDTO uploadFile(MultipartFile file) {
    URL url = s3Service.uploadFile(file);
    return new UriDTO(url.toString());
  }
   */

}
