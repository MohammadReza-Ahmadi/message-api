package com.example.api;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet("/userapi")
public class MyUsersServlet extends HttpServlet {

    private static void checkAndRedirect(HttpServletRequest req, HttpServletResponse resp, String httpMethod) throws IOException, ServletException {
        UserService userService = UserServiceFactory.getUserService();

        String thisUrl = req.getRequestURI();

        resp.setContentType("text/html");
        if (req.getUserPrincipal() != null) {
            resp.getWriter()
                    .println(
                            "<p>Hello, "
                                    + req.getUserPrincipal().getName()
                                    + "!  You can <a href=\""
                                    + userService.createLogoutURL(thisUrl)
                                    + "\">sign out</a>.</p>");
        } else {
            resp.getWriter()
                    .println(
                            "<p>Please <a href=\"" + userService.createLoginURL(thisUrl) + "\">sign in</a>.</p>");
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkAndRedirect(req, resp, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkAndRedirect(req, resp, "POST");
    }
}
