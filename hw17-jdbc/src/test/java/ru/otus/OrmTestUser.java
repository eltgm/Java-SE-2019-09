package ru.otus;

import lombok.Cleanup;
import org.junit.jupiter.api.*;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.JdbcTemplateImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.service.DBService;
import ru.otus.service.DbServiceImpl;
import ru.otus.userapi.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrmTestUser {
    private static DBService<User> dbService;
    private static DataSource dataSource;
    private static User user;

    @BeforeAll
    static void beforeAll() throws SQLException {
        user = new User(1, "Igor", 21);
        dataSource = new DataSourceH2();
        createTable(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);
        var dbExecutor = new DbExecutor<User>();
        var jdbcTemplate = new JdbcTemplateImpl<User>(sessionManager, dbExecutor);
        dbService = new DbServiceImpl<>(jdbcTemplate);
    }

    private static void createTable(DataSource dataSource) throws SQLException {
        @Cleanup Connection connection = dataSource.getConnection();
        @Cleanup PreparedStatement pst = connection.prepareStatement("create table user(id bigint(20) IDENTITY PRIMARY KEY auto_increment, name varchar(255), age int(3))");
        pst.executeUpdate();

        System.out.println("table created");
    }

    private static void dropTable(DataSource dataSource) throws SQLException {
        @Cleanup Connection connection = dataSource.getConnection();
        @Cleanup PreparedStatement pst = connection.prepareStatement("drop table user");
        pst.executeUpdate();
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        dropTable(dataSource);
    }

    @Test
    @Order(1)
    public void createTest() {
        dbService.save(user);

        final var optionalUser = findById();
        final var userFromDB = optionalUser.orElse(null);

        assertEquals(user, userFromDB);
    }

    @Test
    @Order(2)
    public void updateTest() {
        user.setName("Vlad");
        user.setAge(22);

        dbService.update(user);
        final var optionalUser = findById();
        final var userFromDB = optionalUser.orElse(null);

        assertEquals(user, userFromDB);
    }

    @Test
    @Order(3)
    public void loadTest() {
        final var userOptional = dbService.load(1, User.class);

        final var loadedUser = userOptional.orElse(null);
        assertEquals(user, loadedUser);
    }

    public Optional<User> findById() {
        try {
            return selectRecord(dataSource.getConnection(), "select id, name, age from user where id  = 1", resultSet -> {
                try {
                    if (resultSet.next()) {
                        return new User(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("age"));
                    }
                } catch (SQLException e) {

                }
                return null;
            });
        } catch (Exception e) {

        }
        return Optional.empty();
    }

    public Optional<User> selectRecord(Connection connection, String sql, Function<ResultSet, User> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }
}
