package com.jfb.productscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.jfb.productscatalog.entities.Category;
import com.jfb.productscatalog.entities.Product;

public class ProductDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank(message = "Campo obrigatório")
  @Size( min = 5, max = 50, message = "Nome deve ter entre 5 e 50 caracteres")
  private String name;

  @NotBlank(message = "Campo obrigatório")
  private String description;

  @Positive(message = "Preço deve ser o valor positivo")
  private Double price;

  private String imgUrl;

  @PastOrPresent(message = "A data d produto não pode ser futura")
  private Instant date;

  private List<CategoryDTO> categories = new ArrayList<>();

  public ProductDTO() {
  }

  public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imgUrl = imgUrl;
    this.date = date;
  }

  public ProductDTO(Product entity) {
    id = entity.getId();
    name = entity.getName();
    description = entity.getDescription();
    price = entity.getPrice();
    imgUrl = entity.getImgUrl();
    date = entity.getDate();
  }

  public ProductDTO(Product entity, Set<Category> categories) {
    this(entity);
    categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return this.price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getImgUrl() {
    return this.imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public Instant getDate() {
    return this.date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public List<CategoryDTO> getCategories() {
    return this.categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }

}
