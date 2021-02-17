package com.jfb.productscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jfb.productscatalog.entities.User;

public class UserDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank(message = "Campo obrigatório")
  @Size( min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
  private String firstName;

  @NotBlank(message = "Campo obrigatório")
  @Size( min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
  private String lastName;

  @Email(message = "Email inválido")
  private String email;

  Set<RoleDTO> roles = new HashSet<>();

  public UserDTO() {
  }

  public UserDTO(Long id, String firstName, String lastName, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public UserDTO(User entity) {
    id = entity.getId();
    firstName = entity.getFirstName();
    lastName = entity.getLastName();
    email = entity.getEmail();
    entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<RoleDTO> getRoles() {
    return this.roles;
  }

}
