package de.communicode.communikey.service.payload;

import de.communicode.communikey.domain.UserEncryptedPassword;

import org.hibernate.validator.constraints.NotBlank;

/**
 * A payload container object for a {@link UserEncryptedPassword}.
 *
 * @author sgreb@communicode.de
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
public class KeyPayloadEncryptedPasswords {
    @NotBlank
    private String login;

    @NotBlank
    private String encryptedPassword;

    public String getLogin() {
        return login;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
