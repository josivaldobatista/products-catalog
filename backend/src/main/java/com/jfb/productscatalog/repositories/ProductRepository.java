package com.jfb.productscatalog.repositories;

import java.util.List;

import com.jfb.productscatalog.entities.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  // Resolvendo o problema n + 1 com a query abaixo de Products e Category.
  @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
  List<Product> findProductsCategories(List<Product> products);

}
