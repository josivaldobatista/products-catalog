package com.jfb.productscatalog.repositories;

import java.util.List;

import com.jfb.productscatalog.entities.Category;
import com.jfb.productscatalog.entities.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  // Resolvendo o problema n + 1 com a query abaixo de Products e Category (ESTUDO).
  @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
  List<Product> findProductsCategories(List<Product> products);

  @Query("SELECT DISTINCT obj from Product obj INNER JOIN obj.categories cats WHERE "
      + "(COALESCE(:categories) IS NULL OR cats IN :categories) AND (LOWER(obj.name) "
      + "LIKE LOWER(CONCAT('%',:name,'%')))")
  Page<Product> find(List<Category> categories, String name, Pageable pageable);

}
