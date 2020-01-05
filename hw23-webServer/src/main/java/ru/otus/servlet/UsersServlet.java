package ru.otus.servlet;

import com.google.gson.Gson;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceUser;
import ru.otus.api.service.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UsersServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "users";

    private final DBServiceUser userService;
    private final TemplateProcessor templateProcessor;
    private final Gson gson;

    public UsersServlet(TemplateProcessor templateProcessor, DBServiceUser userService, Gson gson) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var paramsMap = new HashMap<String, Object>();
        final var users = userService.getAll();
        paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, users);

        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        final var user = gson.fromJson(userString, User.class);
        final var id = userService.saveUser(user);
        user.setId(id);

        resp.setContentType("text/html");
        resp.getOutputStream().print(gson.toJson(user));
    }
}
