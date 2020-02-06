package ru.otus;

import java.io.IOException;
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
    private ServerSocket serverSocket = null;

    public MessageSystemImpl() {
        msgProcessor.submit(this::msgProcessor);

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
                Client st = new Client(s, this);
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
            System.out.println("Client doesnt exists");
        } else {
            System.out.println("Client remove successfully");
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
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
