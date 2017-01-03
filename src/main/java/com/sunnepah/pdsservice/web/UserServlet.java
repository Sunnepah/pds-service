package com.sunnepah.pdsservice.web;

import net.minidev.json.JSONObject;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sunnepah on 28/10/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class UserServlet extends HttpServlet {

    @Inject
    public UserServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String accessToken = getBearerToken(req.getHeader("Authorization"));
        if(accessToken == null) {
            JSONObject res = new JSONObject();
            res.put("error", "Missing or malformed authorization header");
            resp.setContentType("application/json");
            resp.getWriter().write(res.toJSONString());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().close();
            return;
        }

        if (isValid(accessToken)) {
            JSONObject ret = new JSONObject();
            ret.put("sub", "sunnepah");
            ret.put("name", "Sunday Ayandokun");
            ret.put("email", "me@gmail.com");
            ret.put("picture", "");

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(ret.toJSONString());
            resp.getWriter().close();
        }

        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean isValid(String token) {
        return true;
    }

    private String getBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer")) {
            return null;
        }

        String [] bearer = authorization.split("Bearer ");
        if (bearer.length < 2 || bearer.length > 2) {
            return null;
        }

        return bearer[1];
    }
}
