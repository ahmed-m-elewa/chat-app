package com.chatapp.util;

import com.chatapp.client.ChatClient;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
    private static Scanner scanner = new Scanner(System.in);
    private Map<String , Integer> conversationStatsMap = new HashMap<>();

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void start() {


        String userName = new AuthorizationUtil().authorizeUser();
        try {
            client.setConversationDump(new FileWriter( new File("conversation-" + userName + "-" +
                    new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.setUserName(userName);
        writer.println(userName);

        String text = null;

        do {
            System.out.print("[" + userName + "]: ");
            text = scanner.nextLine();
            writer.println(text);
            updateConversationStats(text);
            try {
                client.getConversationDump().write("\n[" + userName + "]: " + text);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (!text.equals("Bye Bye"));

        try {
            writeConversationStatisticsFile(userName);
            socket.close();
            client.getConversationDump().close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
    private void updateConversationStats(String message) {
        String[] words = message.split(" ");
        for (String word : words) {
            Integer wordCount = conversationStatsMap.get(word);
            if (wordCount == null) {
                conversationStatsMap.put(word , 1);
            } else {
                conversationStatsMap.put(word , wordCount + 1);
            }
        }
    }
    private void writeConversationStatisticsFile(String userName) {
        FileWriter conversationStatisticsFileWriter = null;
        try {
            conversationStatisticsFileWriter = new FileWriter( new File("conversation-stats-" + userName + "-" +
                    new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String word : conversationStatsMap.keySet()) {
            try {
                conversationStatisticsFileWriter.write(word + " : " + conversationStatsMap.get(word) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            conversationStatisticsFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}