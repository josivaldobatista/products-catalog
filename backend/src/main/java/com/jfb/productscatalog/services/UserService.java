package com.jfb.productscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.jfb.productscatalog.dto.RoleDTO;
import com.jfb.productscatalog.dto.UserDTO;
import com.jfb.productscatalog.dto.UserInsertDTO;
import com.jfb.productscatalog.dto.UserUpdateDTO;
import com.jfb.productscatalog.entities.Role;
import com.jfb.productscatalog.entities.User;
import com.jfb.productscatalog.repositories.RoleRepository;
import com.jfb.productscatalog.repositories.UserRepository;
import com.jfb.productscatalog.services.exceptions.DatabaseException;
import com.jfb.productscatalog.services.exceptions.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

  private static  Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository repository;

  @Autowired
  private RoleRepository roleRepository;

  @Transactional(readOnly = true)
  public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
    Page<User> list = repository.findAll(pageRequest);
    return list.map(x -> new UserDTO(x));
  }

  @Transactional(readOnly = true)
  public UserDTO findById(Long id) {
    Optional<User> obj = repository.findById(id);
    User entity = obj.orElseThrow(() -> new ResourceNotFoundException("User não encontrada"));
    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO insert(UserInsertDTO dto) {
    User entity = new User();
    copyDtoToEntity(dto, entity);
    entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    entity = repository.save(entity);
    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO update(Long id, UserUpdateDTO dto) {
    try {
      User entity = repository.getOne(id);
      copyDtoToEntity(dto, entity);
      entity = repository.save(entity);
      return new UserDTO(entity);
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

  private void copyDtoToEntity(UserDTO dto, User entity) {
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setEmail(dto.getEmail());

    entity.getRoles().clear();
    for (RoleDTO roleDto : dto.getRoles()) {
      Role role = roleRepository.getOne(roleDto.getId());
      entity.getRoles().add(role);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findByEmail(username);
    if (user == null) {
      logger.error("Email não encontrado: " + username);
      throw new UsernameNotFoundException("Email não encontrado");
    }
    logger.info("Email encontrado: " + username);
    return user;
  }

}
