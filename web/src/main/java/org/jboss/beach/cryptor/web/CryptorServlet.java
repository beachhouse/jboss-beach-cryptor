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
package org.jboss.beach.cryptor.web;

import org.jboss.beach.cryptor.Cryptor;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.misc.CharacterDecoder;
import sun.misc.CharacterEncoder;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class CryptorServlet extends HttpServlet {
    private static final CharacterDecoder DECODER = new BASE64Decoder();
    private static final CharacterEncoder ENCODER = new BASE64Encoder();

    @EJB
    private Cryptor cryptor;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String servletPath = req.getServletPath();
        final String value = req.getParameter("value");
        final PrintWriter out = resp.getWriter();
        out.println("<html><title>Cryptor</title><body><form method=\"post\">");
        input(out, "test", "value", value);
        out.println("<br/><input type=\"submit\" value=\"Submit\">");
        if (value != null) {
            try {
                out.print("<br/>");
                // use url-pattern from web.xml
                if (servletPath.equals("/encrypt"))
                    out.println(ENCODER.encode(cryptor.encrypt(value.getBytes("UTF8"))));
                else
                    out.println(new String(cryptor.decrypt(DECODER.decodeBuffer(value)), "UTF8"));
            } catch (GeneralSecurityException e) {
                out.println("<br/><font color=\"red\"><i>" + e.getMessage() + "</i></font>");
            }
        }
        out.println("</body></html>");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private static void input(final PrintWriter out, final String type, final String name, String value) {
        if (value == null)
            input(out, type, name);
        else
            out.println("<input type=\"" + type + "\" name=\"" + name + "\" value=\"" + value + "\"/>");
    }

    private static void input(final PrintWriter out, final String type, final String name) {
        out.println("<input type=\"" + type + "\" name=\"" + name + "\"/>");
    }

    // <input type="text" name="value" value="" + value + ""/>
}
