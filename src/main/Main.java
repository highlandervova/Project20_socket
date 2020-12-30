package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Starting client socket logic in separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                do {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Enter message:");
                    message = new Scanner(System.in).nextLine();
                    //Client socket create and start
                    try (Socket client = new Socket("localhost", 8888)) {
                        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                        oos.writeObject(message);
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (!"exit".equalsIgnoreCase(message));
            }
        }).start();
        //Creating server...
        ServerSocket server = new ServerSocket(8888);
        //Starting to wait for incoming connections...
        while (true) {
            Socket socket = server.accept();
            System.out.println("Server started;");
            //Starting stream to read incoming data
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //reading data
            String incommingMessage = (String) ois.readObject();
            //close everything which was opened by us
            ois.close();
            socket.close();
            System.out.println("INC: "+incommingMessage);
            if ("exit".equalsIgnoreCase(incommingMessage)) {
                break;
            }
        }
        server.close();
    }
}
