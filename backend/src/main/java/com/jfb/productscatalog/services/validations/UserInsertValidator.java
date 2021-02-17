package com.jfb.productscatalog.services.validations;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.jfb.productscatalog.dto.UserInsertDTO;
import com.jfb.productscatalog.entities.User;
import com.jfb.productscatalog.repositories.UserRepository;
import com.jfb.productscatalog.resources.exceptions.FieldMessage;

import org.springframework.beans.factory.annotation.Autowired;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

  @Autowired
  private UserRepository repository;

  @Override
  public void initialize(UserInsertValid ann) {
  }

  @Override
  public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

    List<FieldMessage> list = new ArrayList<>();

    User user = repository.findByEmail(dto.getEmail());

    if (user != null) {
      list.add(new FieldMessage("email", "Email existente na base de dados"));
    }

    for (FieldMessage e : list) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
          .addConstraintViolation();
    }
    return list.isEmpty();
  }
}