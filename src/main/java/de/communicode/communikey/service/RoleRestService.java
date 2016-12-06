/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.service;

import static java.util.Objects.requireNonNull;

import de.communicode.communikey.domain.Role;
import de.communicode.communikey.exception.RoleConflictException;
import de.communicode.communikey.exception.RoleNotFoundException;
import de.communicode.communikey.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The REST API service to process {@link Role} entities via a {@link RoleRepository}.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Service
public class RoleRestService implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleRestService(RoleRepository roleRepository) {
        this.roleRepository = requireNonNull(roleRepository, "roleRepository must not be null!");
    }

    @Override
    public Role create(String name) throws RoleConflictException, IllegalArgumentException {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be empty!");
        }

        Optional.of(roleRepository.findOneByName(name)).orElseThrow(() -> new RoleConflictException("Role with name \"" + name + "\"already " + "exists!"));
        return roleRepository.save(new Role(name));
    }

    @Override
    public void delete(long roleId) throws RoleNotFoundException {
        roleRepository.delete(validate(roleId));
    }

    @Override
    public Set<Role> getAll() {
        return StreamSupport.stream(roleRepository.findAll().spliterator(), false)
            .collect(Collectors.toSet());
    }

    @Override
    public Role getById(long roleId) throws RoleNotFoundException {
        return validate(roleId);
    }

    @Override
    public Role getByName(String name) throws RoleNotFoundException, IllegalArgumentException {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be empty!");
        }
        return roleRepository.findOneByName(name);
    }

    @Override
    public void modifyName(long roleId, String newName) throws RoleNotFoundException, RoleConflictException, IllegalArgumentException {
        if (newName.trim().isEmpty()) {
            throw new IllegalArgumentException("new name must not be empty!");
        }
        Optional.of(roleRepository.findOneByName(newName)).orElseThrow(() -> new RoleConflictException(
            "Role with name \"" + newName + "\"already " + "exists!"));
        Role role = validate(roleId);
        role.setName(newName);
        roleRepository.save(role);
    }

    @Override
    public Role save(Role role) throws NullPointerException {
        return requireNonNull(roleRepository.save(role), "role must not be null!");
    }

    @Override
    public Role validate(long roleId) throws RoleNotFoundException {
        return Optional.ofNullable(roleRepository.findOne(roleId)).orElseThrow(() -> new RoleNotFoundException(roleId));
    }
}
