package com.jfb.catalogproducts.tests.repositories;

import java.util.Optional;

import com.jfb.productscatalog.entities.Product;
import com.jfb.productscatalog.repositories.ProductRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@SpringBootApplication
public class ProductRepositoryTests {
  
  @Autowired
  private ProductRepository repository;

  long existingId;

  @BeforeEach
  void setUp() throws Exception {
    existingId = 1L;
  }

  @Test
  public void deleteShouldDeleteObjectWhenIdExists() {
    repository.deleteById(existingId);

    Optional<Product> result = repository.findById(existingId);

    Assertions.assertFalse(result.isPresent());
  }
}
