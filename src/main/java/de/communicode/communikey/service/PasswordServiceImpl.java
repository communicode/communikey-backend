/*
  * Copyright (C) 2016 communicode AG
 */
package de.communicode.communikey.service;

import de.communicode.communikey.domain.Password;
import de.communicode.communikey.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PasswordServiceImpl implements PasswordService {

  @Autowired
  private PasswordRepository passwordRepository;

  @Override
  public Iterable<Password> getAllPasswords() {
    return passwordRepository.findAll();
  }

  @Override
  public Password getPasswordById(long id) {
    return passwordRepository.findOne(id);
  }

  @Override
  public Password getPasswordByCreationDate(Timestamp timestamp) {
    return passwordRepository.findOneByCreationTimestamp(timestamp);
  }

  @Override
  public void deletePassword(Password password) {
    passwordRepository.delete(password);
  }

  @Override
  public void modifyPasswordValue(Password password, String newValue) {
    passwordRepository.findOne(password.getId()).setValue(newValue);
  }

  @Override
  public Password savePassword(Password password) {
    return passwordRepository.save(password);
  }
}
