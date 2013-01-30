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
package org.jboss.beach.cryptor.askpass;

import org.jboss.beach.askpass.Askpass;
import org.jboss.beach.cryptor.Cryptor;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class AskpassPasswordProviderTestCase {
    static String absolutePathToResource(String resource) {
        // something that works on my machine :-)
        return AskpassPasswordProviderTestCase.class.getResource(resource).getFile();
    }

    @Test
    public void test1() throws Exception {
        System.setProperty(Askpass.PROP_JBOSS_ASKPASS, absolutePathToResource("/askpass.sh"));
        final Cryptor cryptor = new Cryptor();
        cryptor.init();
        final String data = "hello world";
        final byte[] encrypted = cryptor.encrypt(data.getBytes("UTF8"));
        assertArrayEquals(new byte[]{-78, 76, 36, 6, -23, -75, 39, -80, 59, 13, 120, 17, 76, 60, -4, -60}, encrypted);
        final String result = new String(cryptor.decrypt(encrypted), "UTF8");
        assertEquals("hello world", result);
    }
}
