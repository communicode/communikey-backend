/*
  * Copyright (C) 2016 communicode AG
 */
package de.communicode.communikey.controller;

import de.communicode.communikey.domain.Password;
import de.communicode.communikey.form.EditPasswordForm;
import de.communicode.communikey.form.NewPasswordForm;
import de.communicode.communikey.repository.PasswordRepository;
import de.communicode.communikey.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * The controller for all password endpoints.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Controller
public class PasswordController {

  @Autowired
  private PasswordRepository passwordRepository;
  @Autowired
  private PasswordService passwordService;

  /**
   * Endpoint to list all {@link Password} entities.
   *
   * @param model the model holding the attributes
   * @return the string to the endpoint
   */
  @GetMapping(value = "/passwords")
  String listPasswords(Model model) {
    model.addAttribute("passwords", passwordRepository.findAll());
    return "passwords";
  }

  /**
   * Endpoint to delete a {@link Password} entity.
   *
   * @param id the ID of the {@link Password} to delete
   * @return the string to the endpoint redirection
   */
  @GetMapping(value = "passwords/{id}/delete")
  String deletePassword(@PathVariable long id) {
    passwordRepository.delete(id);
    return "redirect:/passwords";
  }

  /**
   * Endpoint to the form to edit a {@link Password} entity.
   *
   * <p>
   *   This is used to process the data entered by the user into the form.
   * </p>
   *
   * @param id the ID of the {@link Password} to edit
   * @param model the model holding the attributes
   * @return the string to the endpoint
   */
  @GetMapping(value = "/passwords/{id}/edit")
  String editPasswordForm(@PathVariable long id, Model model) {
    Password password = passwordRepository.findOne(id);
    EditPasswordForm editPasswordForm = new EditPasswordForm();
    editPasswordForm.setValue(password.getValue());
    model.addAttribute("password", password);
    model.addAttribute("editPasswordForm", editPasswordForm);
    return "/passwords-edit";
  }

  /**
   * Endpoint to edit a {@link Password} entity.
   *
   * @param id the ID of the {@link Password} to edit
   * @param editPasswordForm the {@link EditPasswordForm} as named {@link Model} attribute
   * @return the string to the endpoint redirection
   */
  @PostMapping(value = "/passwords/{id}/edit")
  String editPassword(@PathVariable long id, @ModelAttribute("editPasswordForm") EditPasswordForm editPasswordForm) {
    Password password = passwordRepository.findOne(id);
    password.setValue(editPasswordForm.getValue());
    passwordService.modifyPasswordValue(passwordRepository.findOne(id), editPasswordForm.getValue());
    passwordRepository.save(password);
    return "redirect:/passwords";
  }

  /**
   * Endpoint to the form to create a new {@link Password} entity.
   *
   * <p>
   *   This is used to process the data entered by the user into the form.
   * </p>
   *
   * @param newPassword the new {@link Password} entity
   * @param model the {@link Model} holding the attributes
   * @return the string to the endpoint
   */
  @GetMapping(value = "/passwords/new")
  String newPasswordForm(Password newPassword, Model model) {
    model.addAttribute("password", newPassword);
    model.addAttribute("newPasswordForm", new NewPasswordForm());
    return "/passwords-new";
  }

  /**
   * Endpoint to create a new {@link Password} entity.
   *
   * @param newPasswordForm the {@link NewPasswordForm} as named {@link Model} attribute
   * @return the string to the endpoint redirection
   */
  @PostMapping(value = "/passwords/new")
  String newPassword(@ModelAttribute("newPasswordForm") NewPasswordForm newPasswordForm) {
    Password password = new Password(newPasswordForm.getValue());
    passwordRepository.save(password);
    return "redirect:/passwords";
  }
}