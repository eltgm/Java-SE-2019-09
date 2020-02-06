package ru.otus.messagesystem;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import ru.otus.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class MsClientImpl implements MsClient {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    private final Gson gson = new Gson();
    private final Serializer serializer;
    private final String name;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();
    private Socket messageSystem;
    private PrintWriter messageSystemOut;
    private BufferedReader messageSystemIn;


    public MsClientImpl(Serializer serializer, String name) {
        this.serializer = serializer;
        this.name = name;

        try {
            this.messageSystem = new Socket(HOST, PORT);
            messageSystemOut = new PrintWriter(messageSystem.getOutputStream(), true);
            messageSystemIn = new BufferedReader(new InputStreamReader(messageSystem.getInputStream()));

            messageSystemOut.println(gson.toJson(new Message(name, "server", null, MessageType.CONNECT.getValue(), "".getBytes())));
        } catch (Exception exc) {
            exc.fillInStackTrace();
        }
    }

    public void startListening() {
        String serverResponse = null;
        try {
            while (!"stop".equals(serverResponse)) {
                serverResponse = messageSystemIn.readLine();
                if (serverResponse != null) {
                    try {
                        Message message = gson.fromJson(serverResponse, Message.class);
                        handle(message);
                    } catch (Exception ex) {
                        ex.fillInStackTrace();
                        continue;
                    }
                }
            }
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public String getName() {
        return name;
    }

    @SneakyThrows
    @Override
    public boolean sendMessage(Message msg) {
        messageSystemOut.println(gson.toJson(msg));
        messageSystemOut.flush();

        return true;
    }

    @Override
    public void handle(Message msg) {
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType.getValue(), serializer.serialize(data));
    }

    @Override
    public void closeConnection() {
        try {
            messageSystemOut.close();
            messageSystemIn.close();
            messageSystem.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
