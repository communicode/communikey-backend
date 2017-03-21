package de.communicode.communikey;

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

import java.util.HashSet;
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
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.communikeyProperties = communikeyProperties;
        this.keyCategoryRepository = keyCategoryRepository;
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
        roleUser.setName("ROLE_USER");
        roleAdmin.setName("ROLE_ADMIN");

        authorityRepository.save(roleUser);
        authorityRepository.save(roleAdmin);
    }

    private void initializeUser() {
        Set<Authority> authorities = new HashSet<>();
        Authority authorityAdmin = authorityRepository.findOne(AuthoritiesConstants.ADMIN);
        Authority authorityUser = authorityRepository.findOne(AuthoritiesConstants.USER);

        rootUser.setEmail(communikeyProperties.getSecurity().getRoot().getEmail());
        rootUser.setLogin(communikeyProperties.getSecurity().getRoot().getLogin());
        rootUser.setFirstName(communikeyProperties.getSecurity().getRoot().getFirstName());
        rootUser.setPassword(passwordEncoder.encode(communikeyProperties.getSecurity().getRoot().getPassword()));
        rootUser.setActivationKey(SecurityUtils.generateRandomActivationKey());
        rootUser.setActivated(true);
        authorities.add(authorityAdmin);
        authorities.add(authorityUser);
        rootUser.setAuthorities(authorities);

        userRepository.save(rootUser);
    }
}
