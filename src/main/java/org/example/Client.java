package org.example;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 9090;
    public static void main(String[] args) throws IOException {

        Socket socket = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            socket = new Socket("localhost", SERVER_PORT);

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            String readyMSG = (String) objectInputStream.readObject();
            if (!readyMSG.equals("ready"))
                throw new Exception("Connection failed: Client didn't receive message \"ready\".");
            System.out.println("Server: " + readyMSG);

            System.out.println("Client: Enter a number");
            Scanner scanner = new Scanner(System.in);
            int n = -1;
            while (true){
                try {
                    n = Integer.parseInt(scanner.nextLine());
                    if (n > 0)
                        break;
                    else
                        throw new NumberFormatException();
                }
                catch (NumberFormatException e) {
                    System.out.println("Client: Incorrect type!");
                }
            }

            objectOutputStream.writeInt(n);
            objectOutputStream.flush();

            String readyForMessages = (String) objectInputStream.readObject();
            if (!readyForMessages.equals("ready for messages"))
                throw new Exception("Connection failed: Client didn't receive message \"ready for messages\".");
            System.out.println("Server: " + readyForMessages);

            int i = 0;
            int number;
            System.out.println(n);
            while (i < n) {
                try {
                    System.out.println("Client: Enter a number");
                    number = Integer.parseInt(scanner.nextLine());
                    System.out.println("Client on PORT: Enter a content");
                    String content = scanner.nextLine();
                    Message message = new Message(number, content);
                    objectOutputStream.writeObject(message);
                    i++;
                }
                catch (NumberFormatException e) {
                    System.out.println("Client: Incorrect type!");
                }
            }

            String finallyMSG = (String) objectInputStream.readObject();
            if (!finallyMSG.equals("finally"))
                throw new Exception("Connection failed: Client didn't receive message \"finally\".");
            System.out.println("Server: " + finallyMSG);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed())
                socket.close();
            if (objectInputStream != null)
                objectInputStream.close();
            if (objectOutputStream != null)
                objectOutputStream.close();
        }
    }

}