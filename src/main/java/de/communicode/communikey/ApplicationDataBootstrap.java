/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Sets;
import de.communicode.communikey.config.CommunikeyProperties;
import de.communicode.communikey.domain.Authority;
import de.communicode.communikey.repository.AuthorityRepository;
import de.communicode.communikey.repository.KeyCategoryRepository;
import de.communicode.communikey.repository.UserRepository;
import de.communicode.communikey.security.AuthoritiesConstants;
import de.communicode.communikey.security.SecurityUtils;
import de.communicode.communikey.support.KeyCategoryChildrenMap;
import de.communicode.communikey.support.KeyCategoryParentMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import de.communicode.communikey.domain.User;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class ApplicationDataBootstrap {

    private final UserRepository userRepository;
    private final KeyCategoryRepository keyCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final CommunikeyProperties communikeyProperties;

    private Authority roleUser = new Authority();
    private Authority roleAdmin = new Authority();
    private User rootUser = new User();

    @Autowired
    public ApplicationDataBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository,
                                    CommunikeyProperties communikeyProperties, KeyCategoryRepository keyCategoryRepository) {
        this.userRepository = requireNonNull(userRepository, "userRepository must not be null!");
        this.passwordEncoder = requireNonNull(passwordEncoder, "passwordEncoder must not be null!");
        this.authorityRepository = requireNonNull(authorityRepository, "authorityRepository must not be null!");
        this.communikeyProperties = requireNonNull(communikeyProperties, "communikeyProperties must not be null!");
        this.keyCategoryRepository = requireNonNull(keyCategoryRepository, "keyCategoryRepository must not be null!");
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initialize() {
        initializeAuthorities();
        initializeUser();

        KeyCategoryChildrenMap keyCategoryChildrenMap = KeyCategoryChildrenMap.getInstance();
        keyCategoryChildrenMap.initialize(keyCategoryRepository.findAll());
        KeyCategoryParentMap keyCategoryParentMap = KeyCategoryParentMap.getInstance();
        keyCategoryParentMap.initialize(keyCategoryRepository.findAll());
    }

    private void initializeAuthorities() {
        if (!authorityRepository.exists(AuthoritiesConstants.USER)) {
            roleUser.setName(AuthoritiesConstants.USER);
            authorityRepository.save(roleUser);
        }
        if (!authorityRepository.exists(AuthoritiesConstants.ADMIN)) {
            roleAdmin.setName(AuthoritiesConstants.ADMIN);
            authorityRepository.save(roleAdmin);
        }
    }

    private void initializeUser() {
        if (Objects.isNull(userRepository.findOneByLogin(communikeyProperties.getSecurity().getRoot().getLogin()))) {
            rootUser.setEmail(communikeyProperties.getSecurity().getRoot().getEmail());
            rootUser.setLogin(communikeyProperties.getSecurity().getRoot().getLogin());
            rootUser.setFirstName(communikeyProperties.getSecurity().getRoot().getFirstName());
            rootUser.setPassword(passwordEncoder.encode(communikeyProperties.getSecurity().getRoot().getPassword()));
            rootUser.setActivationKey(SecurityUtils.generateRandomActivationKey());
            rootUser.setActivated(true);
            Set<Authority> authorities = Sets.newConcurrentHashSet();
            Authority authorityAdmin = authorityRepository.findOne(AuthoritiesConstants.ADMIN);
            Authority authorityUser = authorityRepository.findOne(AuthoritiesConstants.USER);
            authorities.add(authorityAdmin);
            authorities.add(authorityUser);
            rootUser.addAuthorities(authorities);

            userRepository.save(rootUser);
        }
    }
}
