/*
  * Copyright (C) 2016 communicode AG
 */
package de.communicode.communikey.domain;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PasswordTest {

  @Test
  public void shouldReturnCorrectPasswordValue() {
    Password p = new Password("yogurt");
    assertEquals("yogurt", p.getValue());
  }
}
