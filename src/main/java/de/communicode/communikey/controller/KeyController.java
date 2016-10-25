/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.controller;

import static de.communicode.communikey.config.CommunikeyConstants.ENDPOINT_KEYS;
import static de.communicode.communikey.config.CommunikeyConstants.REQUEST_KEY_DELETE;
import static de.communicode.communikey.config.CommunikeyConstants.REQUEST_KEY_EDIT;
import static de.communicode.communikey.config.CommunikeyConstants.REQUEST_KEY_NEW;
import static de.communicode.communikey.config.CommunikeyConstants.TEMPLATE_KEY_EDIT;
import static de.communicode.communikey.config.CommunikeyConstants.TEMPLATE_KEY_NEW;
import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.form.EditKeyForm;
import de.communicode.communikey.form.NewKeyForm;
import de.communicode.communikey.repository.KeyRepository;
import de.communicode.communikey.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static de.communicode.communikey.util.CommunikeyConstantsUtil.asRedirect;

/**
 * The controller for all key endpoints.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@Controller
public class KeyController {

    private final KeyRepository keyRepository;
    private final KeyService keyService;

    @Autowired
    public KeyController(KeyService keyService, KeyRepository keyRepository) {
        this.keyService = requireNonNull(keyService, "keyService must not be null!");
        this.keyRepository = requireNonNull(keyRepository, "keyService must not be null!");
    }

    /**
     * Endpoint to list all {@link Key} entities.
     *
     * @param model the model holding the attributes
     * @return the string to the endpoint
     */
    @GetMapping(value = ENDPOINT_KEYS)
    String listKeys(Model model) {
        model.addAttribute("keys", keyRepository.findAll());
        return ENDPOINT_KEYS;
    }

    /**
     * Endpoint to delete a {@link Key} entity.
     *
     * @param id the ID of the {@link Key} to delete
     * @return the string to the endpoint redirection
     */
    @GetMapping(value = REQUEST_KEY_DELETE)
    String deleteKey(@PathVariable long id) {
        keyRepository.delete(id);
        return asRedirect(ENDPOINT_KEYS);
    }

    /**
     * Endpoint to the form to edit a {@link Key} entity.
     *
     * <p>
     *   This is used to process the data entered by the user into the form.
     * </p>
     *
     * @param id the ID of the {@link Key} to edit
     * @param model the model holding the attributes
     * @return the string to the endpoint
     */
    @GetMapping(value = REQUEST_KEY_EDIT)
    String editKeyForm(@PathVariable long id, Model model) {
        if (keyRepository.findOne(id) == null) {
            return asRedirect(ENDPOINT_KEYS);
        } else {
            Key key = keyRepository.findOne(id);
            EditKeyForm editKeyForm = new EditKeyForm();
            editKeyForm.setValue(key.getValue());
            model.addAttribute("key", key);
            model.addAttribute("editKeyForm", editKeyForm);
            return TEMPLATE_KEY_EDIT;
        }
    }

    /**
     * Endpoint to edit a {@link Key} entity.
     *
     * @param id the ID of the {@link Key} to edit
     * @param editKeyForm the {@link EditKeyForm} as named {@link Model} attribute
     * @return the string to the endpoint redirection
     */
    @PostMapping(value = REQUEST_KEY_EDIT)
    String editKey(@PathVariable long id, @ModelAttribute("editKeyForm") EditKeyForm editKeyForm) {
        Key key = keyRepository.findOne(id);
        key.setValue(editKeyForm.getValue());
        keyService.modifyKeyValue(keyRepository.findOne(id), editKeyForm.getValue());
        keyRepository.save(key);
        return asRedirect(ENDPOINT_KEYS);
    }

    /**
     * Endpoint to the form to create a new {@link Key} entity.
     *
     * <p>
     *   This is used to process the data entered by the user into the form.
     * </p>
     *
     * @param newKey the new {@link Key} entity
     * @param model the {@link Model} holding the attributes
     * @return the string to the endpoint
     */
    @GetMapping(value = REQUEST_KEY_NEW)
    String newKeyForm(Key newKey, Model model) {
        model.addAttribute("key", newKey);
        model.addAttribute("newKeyForm", new NewKeyForm());
        return TEMPLATE_KEY_NEW;
    }

    /**
     * Endpoint to create a new {@link Key} entity.
     *
     * @param newKeyForm the {@link NewKeyForm} as named {@link Model} attribute
     * @return the string to the endpoint redirection
     */
    @PostMapping(value = REQUEST_KEY_NEW)
    String newKey(@ModelAttribute("newKeyForm") NewKeyForm newKeyForm) {
        Key key = new Key(newKeyForm.getValue());
        keyRepository.save(key);
        return asRedirect(ENDPOINT_KEYS);
    }
}