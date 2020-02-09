package ru.otus;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class MessageSystemImpl implements MessageSystem {
    private static final int MESSAGE_QUEUE_SIZE = 1_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;
    private static final int PORT = 8080;
    private final Gson gson = new Gson();
    private final AtomicBoolean runFlag = new AtomicBoolean(true);
    private final Map<String, Client> clientMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT, new ThreadFactory() {
        private final AtomicInteger threadNameSeq = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
            return thread;
        }
    });

    public MessageSystemImpl() {
        msgProcessor.submit(this::msgProcessor);
        ServerSocket serverSocket = null; // can also use static final PORT_NUM , when defined
        try {

            serverSocket = new ServerSocket(PORT);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        Socket s;
        while (true) {
            try {
                s = serverSocket.accept();
                System.out.println("connection Established");
                Client st = new Client(s);
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }

    private void msgProcessor() {
        while (runFlag.get()) {
            try {
                Message msg = messageQueue.take();
                if (msg == Message.VOID_MESSAGE) {

                } else {
                    Client clientTo = clientMap.get(msg.getTo());
                    if (clientTo == null) {

                    } else {
                        msgHandler.submit(() -> handleMessage(clientTo, msg));
                    }
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {

            }
        }
        msgHandler.submit(this::messageHandlerShutdown);
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
    }


    private void handleMessage(Client msClient, Message msg) {
        msClient.sendMessage(msg);
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(Message.VOID_MESSAGE);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(Message.VOID_MESSAGE);
        }
    }

    @Override
    public void addClient(Client client, String clientName) {
        if (clientMap.containsKey(clientName)) {
            throw new IllegalArgumentException("Error. client: " + clientName + " already exists");
        }
        clientMap.put(clientName, client);
    }

    @Override
    public void removeClient(String clientId) {
        Client removedClient = clientMap.remove(clientId);
        if (removedClient == null) {
        } else {
        }
    }

    @Override
    public boolean newMessage(Message msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            return false;
        }
    }

    @Override
    public void dispose() throws InterruptedException {
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    class Client extends Thread {
        private final Socket s;
        String line = null;
        BufferedReader is = null;
        PrintWriter os = null;

        public Client(Socket s) {
            this.s = s;
        }

        public void run() {
            try {
                is = new BufferedReader(new InputStreamReader(s.getInputStream()));
                os = new PrintWriter(s.getOutputStream(), true);

            } catch (IOException e) {
                System.out.println("IO error in server thread");
            }
            try {
                while (!"stop".equals(line)) {
                    line = is.readLine();
                    if (line != null) {
                        final var message = gson.fromJson(line, Message.class);
                        if (message.getType().equals(MessageType.CONNECT.getValue()))
                            addClient(this, message.getFrom());
                        else
                            newMessage(message);
                    }
                }
            } catch (IOException e) {
                line = this.getName(); //reused String line for getting thread name
                System.out.println("IO Error/ Client " + line + " terminated abruptly");
            } catch (NullPointerException e) {
                line = this.getName(); //reused String line for getting thread name
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
}
