package com.chatapp.client;

import com.chatapp.util.ReadThread;
import com.chatapp.util.WriteThread;

import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
    public FileWriter conversationDump;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {

        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public FileWriter getConversationDump() {
        return this.conversationDump;
    }

    public void setConversationDump(FileWriter fileWriter) {
        this.conversationDump = fileWriter;
    }


    public static void main(String[] args) {
        /*if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);*/

        ChatClient client = new ChatClient("localhost", 8989);
        client.execute();
    }
}