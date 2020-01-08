package ru.otus.servlet;

import com.google.gson.Gson;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class CreateUserServlet extends HttpServlet {
    private final DBServiceUser userService;
    private final Gson gson;

    public CreateUserServlet( DBServiceUser userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
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
