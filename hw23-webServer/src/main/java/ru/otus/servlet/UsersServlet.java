package ru.otus.servlet;

import ru.otus.api.service.DBServiceUser;
import ru.otus.api.service.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class UsersServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "users";

    private final DBServiceUser userService;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DBServiceUser userService) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var paramsMap = new HashMap<String, Object>();
        final var users = userService.getAll();
        paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, users);

        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}
