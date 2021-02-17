package com.jfb.productscatalog.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jfb.productscatalog.entities.Category;

public class CategoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank(message = "Campo obrigatório")
  @Size( min = 3, max = 20, message = "Nome deve ter entre 3 e 20 caracteres")
  private String name;

  public CategoryDTO() {
  }

  public CategoryDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public CategoryDTO(Category entity) {
    id = entity.getId();
    name = entity.getName();
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

}
