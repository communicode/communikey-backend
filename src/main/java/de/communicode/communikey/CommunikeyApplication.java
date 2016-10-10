/*
  * Copyright (C) 2016 communicode AG
 */
package de.communicode.communikey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A simple centralized, cross-platform credential manager.
 *
 * <p>
 *   This is the application boot class.
 * </p>
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
@SpringBootApplication
public class CommunikeyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunikeyApplication.class, args);
    }
}
