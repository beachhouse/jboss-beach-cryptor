/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.beach.cryptor;

import org.jboss.beach.cryptor.spi.PasswordProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class Cryptor {
    private static final int ITERATION_COUNT = 1001;
    private static final byte[] SALT = { 1, 2, 3, 4, 5, 6, 7, 8 };

    private Key key;
    private AlgorithmParameterSpec params = new PBEParameterSpec(SALT, ITERATION_COUNT);
    private PasswordProvider passwordProvider = new DefaultPasswordProvider();
    private String transformation = "PBEwithMD5andDES";
    private char[] defaultPassword;

    private static class DefaultPasswordProvider implements PasswordProvider {
        @Override
        public char[] providePassword(final Cryptor cryptor) {
            if (cryptor.defaultPassword == null)
                throw new IllegalStateException("No password has been set");
            return cryptor.defaultPassword;
        }
    };

    protected byte[] decipher(final int opmode, final byte[] data) throws GeneralSecurityException {
        verifyInitialized();
        final Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, key, params);
        return cipher.doFinal(data);
    }

    public byte[] decrypt(final byte[] data) throws GeneralSecurityException {
        return decipher(Cipher.DECRYPT_MODE, data);
    }

    public byte[] encrypt(final byte[] data) throws GeneralSecurityException {
        return decipher(Cipher.ENCRYPT_MODE, data);
    }

    public void init() throws GeneralSecurityException {
        if (this.key != null)
            throw new IllegalStateException("Cryptor has already been initialized");
        final char[] password = passwordProvider.providePassword(this);
        final KeySpec keySpec = new PBEKeySpec(password);
        final SecretKeyFactory factory = SecretKeyFactory.getInstance(transformation);
        this.key = factory.generateSecret(keySpec);
    }

    /**
     * The password to use if no PasswordProvider is installed.
     *
     * @param password
     */
    public void setPassword(final char[] password) {
        this.defaultPassword = password.clone();
    }

    private final void verifyInitialized() {
        if (key == null)
            throw new IllegalStateException("Cryptor has not been initialized");
    }
}
