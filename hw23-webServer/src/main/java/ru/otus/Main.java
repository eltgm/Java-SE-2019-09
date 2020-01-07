package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.hibernate.SessionFactory;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DbServiceUserImpl;
import ru.otus.api.service.TemplateProcessor;
import ru.otus.api.service.TemplateProcessorImpl;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerImpl;

public class Main {
    private static final int WEB_SERVER_PORT = 8989;
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    private static final String TEMPLATES_DIR = "/templates/";
    private static UsersWebServer webServer;

    public static void main(String[] args) throws Exception {
        final var hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        var sessionManager = new SessionManagerHibernate(sessionFactory);
        var dbTemplate = new UserDaoHibernate(sessionManager);
        var dbServiceUser = new DbServiceUserImpl(dbTemplate);

        dbServiceUser.getInitializer().init();

        webServer = new UsersWebServerImpl(WEB_SERVER_PORT,
                new HashLoginService(REALM_NAME, hashLoginServiceConfigPath),
                dbServiceUser,
                gson,
                templateProcessor);
        webServer.start();
        webServer.join();
    }
}
