package com.chatapp.util;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class AuthorizationUtil {
    private static Scanner scanner = new Scanner(System.in);

    public String authorizeUser() {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Authorizing user...");
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.getUsersFileFromResourses()));
            String line;
            boolean usernameExists = false;
            while((line = bufferedReader.readLine()) != null) {
                if (line.equals(username + "-$$-" + PasswordHashUtil.encodeAndHash(password))) {
                    usernameExists = true;
                    break;
                }
            }
            if (usernameExists) {
                System.out.println("Logged in successfully.");
            } else {
                throw new Exception("Not Authorized");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }

    private File getUsersFileFromResourses() {
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource("users.txt");
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
}
