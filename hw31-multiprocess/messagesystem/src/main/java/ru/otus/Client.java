package ru.otus;

import com.google.gson.Gson;
import ru.otus.messagesystem.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    private final Gson gson = new Gson();
    private final MessageSystem messageSystem;
    private final Socket s;
    String line = null;
    BufferedReader is = null;
    PrintWriter os = null;

    public Client(Socket s, MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.s = s;
    }

    public void run() {
        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream(), true);

        } catch (IOException e) {
            System.out.println("IO error in server thread");
            try {
                messageSystem.dispose();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        try {
            while (!"stop".equals(line)) {
                line = is.readLine();
                if (line != null) {
                    final var message = gson.fromJson(line, Message.class);
                    if (message.getType().equals(MessageType.CONNECT.getValue()))
                        messageSystem.addClient(this, message.getFrom());
                    else
                        messageSystem.newMessage(message);
                }
            }
        } catch (IOException e) {
            line = this.getName();
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName();
            System.out.println("Client " + line + " Closed");
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (is != null) {
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (os != null) {
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (s != null) {
                    s.close();
                    System.out.println("Socket Closed");
                }

            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }//end finally
    }

    public void sendMessage(Message msg) {
        os.println(gson.toJson(msg));
    }
}
