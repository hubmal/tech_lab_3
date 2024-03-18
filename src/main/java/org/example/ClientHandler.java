package org.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket client = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject("ready");
            objectOutputStream.flush();

            objectInputStream = new ObjectInputStream(client.getInputStream());
            int n = objectInputStream.readInt();
            System.out.println("Client on PORT " + client.getPort()
                    + ": Client sent number " + n + ".");

            objectOutputStream.writeObject("ready for messages");
            objectOutputStream.flush();

            System.out.println(n);
            for (int i = 0; i < n; i++) {
                Message message = (Message) objectInputStream.readObject();
                System.out.println("Server on PORT " + client.getPort() +
                        ": Message object received, number = " +
                        message.getNumber() + ", content = " + message.getContent() + ".");
            }

            objectOutputStream.writeObject("finally");
            objectOutputStream.flush();

        } catch (Exception e) {
            System.out.println("Connection with client on port " + client.getPort() + " failed.");
        } finally {
            try {
                if (client != null && !client.isClosed())
                    client.close();
                if (objectInputStream != null)
                    objectInputStream.close();
                if (objectOutputStream != null)
                    objectOutputStream.close();
            } catch (Exception e) {
                System.out.println("Error");
            }
        }

    }
}
