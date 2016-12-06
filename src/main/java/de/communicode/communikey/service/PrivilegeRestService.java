/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Privilege;
import de.communicode.communikey.exception.PrivilegeConflictException;
import de.communicode.communikey.exception.PrivilegeNotFoundException;
import de.communicode.communikey.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrivilegeRestService implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public PrivilegeRestService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = requireNonNull(privilegeRepository, "privilegeRepository must not be null!");
    }

    @Override
    public Privilege create(String name) throws PrivilegeConflictException, IllegalArgumentException {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be empty!");
        }

        Optional.of(privilegeRepository.findOneByName(name)).orElseThrow(() -> new PrivilegeConflictException(
            "Privilege with name \"" + name + "\"already " + "exists!"));
        return privilegeRepository.save(new Privilege(name));
    }

    @Override
    public void delete(long privilegeId) throws PrivilegeNotFoundException {
        privilegeRepository.delete(validate(privilegeId));
    }

    @Override
    public Set<Privilege> getAll() {
        return StreamSupport.stream(privilegeRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Privilege getById(long privilegeId) throws PrivilegeNotFoundException {
        return validate(privilegeId);
    }

    @Override
    public Privilege getByName(String name) throws PrivilegeNotFoundException, IllegalArgumentException {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be empty!");
        }
        return privilegeRepository.findOneByName(name);
    }

    @Override
    public void modifyName(long privilegeId, String newName) throws PrivilegeNotFoundException, PrivilegeConflictException, IllegalArgumentException {
        if (newName.trim().isEmpty()) {
            throw new IllegalArgumentException("new name must not be empty!");
        }
        Optional.of(privilegeRepository.findOneByName(newName)).orElseThrow(() -> new PrivilegeConflictException(
            "Privilege with name \"" + newName + "\"already " + "exists!"));
        Privilege privilege = validate(privilegeId);
        privilege.setName(newName);
        privilegeRepository.save(privilege);
    }

    @Override
    public Privilege save(Privilege privilege) throws NullPointerException {
        return requireNonNull(privilegeRepository.save(privilege), "privilege must not be null!");
    }

    @Override
    public Privilege validate(long privilegeId) throws PrivilegeNotFoundException {
        return Optional.ofNullable(privilegeRepository.findOne(privilegeId)).orElseThrow(() -> new PrivilegeNotFoundException(privilegeId));
    }
}
