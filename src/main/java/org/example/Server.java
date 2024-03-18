package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int SERVER_PORT = 9090;
    private static final LinkedList<ClientHandler> clients = new LinkedList<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        while (true) {
            System.out.println("Server: Server is waiting for request...");
            Socket client = serverSocket.accept();
            System.out.println("Server: Server accepted client on port: " + client.getPort() + ".");
            ClientHandler clientHandler = new ClientHandler(client);
            clients.add(clientHandler);
            executorService.execute(clientHandler);
        }
    }
}
