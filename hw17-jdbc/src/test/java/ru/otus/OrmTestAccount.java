package ru.otus;

import lombok.Cleanup;
import org.junit.jupiter.api.*;
import ru.otus.accountApi.model.Account;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.JdbcTemplateImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.service.DBService;
import ru.otus.service.DbServiceImpl;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrmTestAccount {
    private static DBService<Account> dbService;
    private static DataSource dataSource;
    private static Account account;

    @BeforeAll
    static void beforeAll() throws SQLException {
        account = Account.builder()
                .no(1)
                .rest(new BigDecimal(150.5))
                .type("Debit")
                .build();

        dataSource = new DataSourceH2();
        createTable(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);
        var dbExecutor = new DbExecutor<Account>();
        var jdbcTemplate = new JdbcTemplateImpl<Account>(sessionManager, dbExecutor);
        dbService = new DbServiceImpl<>(jdbcTemplate);
    }

    private static void createTable(DataSource dataSource) throws SQLException {
        @Cleanup Connection connection = dataSource.getConnection();
        @Cleanup PreparedStatement pst = connection.prepareStatement("create table account(no bigint(20) IDENTITY PRIMARY KEY auto_increment, type varchar(255), rest number)");
        pst.executeUpdate();

        System.out.println("table created");
    }

    private static void dropTable(DataSource dataSource) throws SQLException {
        @Cleanup Connection connection = dataSource.getConnection();
        @Cleanup PreparedStatement pst = connection.prepareStatement("drop table account");
        pst.executeUpdate();
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        dropTable(dataSource);
    }

    @Test
    @Order(1)
    public void createTest() {
        dbService.save(account);

        final var optionalAccount = findById();
        final var accountFromDB = optionalAccount.orElse(null);

        assertEquals(account, accountFromDB);
    }

    @Test
    @Order(2)
    public void updateTest() {
        account.setType("Credit");
        account.setRest(new BigDecimal(0.0));

        dbService.update(account);
        final var optionalAccount = findById();
        final var accountFromDB = optionalAccount.orElse(null);

        assertEquals(account, accountFromDB);
    }

    @Test
    @Order(3)
    public void loadTest() {
        final var accountOptional = dbService.load(1, Account.class);

        final var loadedAccount = accountOptional.orElse(null);
        assertEquals(account, loadedAccount);
    }

    public Optional<Account> findById() {
        try {
            return selectRecord(dataSource.getConnection(), "select no, type, rest from account where no  = 1", resultSet -> {
                try {
                    if (resultSet.next()) {
                        return new Account(resultSet.getLong("no"), resultSet.getString("type"), resultSet.getBigDecimal("rest"));
                    }
                } catch (SQLException e) {

                }
                return null;
            });
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Account> selectRecord(Connection connection, String sql, Function<ResultSet, Account> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }
}
