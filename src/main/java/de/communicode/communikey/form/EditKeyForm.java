/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.form;

import de.communicode.communikey.domain.Key;

/**
 * The form to edit {@link Key} entities.
 *
 * @author sgreb@communicode.de
 * @since 0.1.0
 */
public class EditKeyForm {
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
