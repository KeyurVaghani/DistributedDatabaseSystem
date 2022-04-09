package com.dpgten.distributeddb.userauthentication;

import com.dpgten.distributeddb.utils.FileResourceUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Authentication Login Menus
 *
 * @author DMWA Group 10
 */
@Data
@Component
public class LoginMenu {

    public static String USER_NAME;

    FileResourceUtils fileResourceUtils;
    private Scanner scanner;
    private String delimeter = "$\\|$";
    @Autowired
    private UserSession userSession;
    private User user;

    private File file;

    private String userProfile = "src//main//resources//UserProfiles.txt";

    /**
     * Default Constructor
     */
    public LoginMenu() {
        scanner = new Scanner(System.in);
        fileResourceUtils = new FileResourceUtils();
        System.out.println("-------------------------------------------------------");
        System.out.println("------Welcome to dpg-10 Distributed Database-----------");
        System.out.println("-------------------------------------------------------");
        file = new File(userProfile);
        user = new User();
    }

    /**
     * Displays first selection menu.
     */
    public void userFirstMenu() {
        boolean loopCheck = true;
        while (loopCheck) {
//            System.out.println("1. User Login\n2. New User? Register\n3. Exit");
            System.out.println("\nMain Menu");
            System.out.println("Press 1 ==> User Login\nPress 2 ==> Register new user\nPress 3 ==> Exit");
            System.out.print("Enter your Choice::: ");
            String selectedOption = scanner.nextLine();
            switch (selectedOption) {
                case "1":
                    boolean result = attemptLogin();
                    if (!result) {
                        System.out.println("Invalid credentials/User does not exist");
                    } else {
                        System.out.println("Calling query executor");
//                        QueryImpl queryImpl = new QueryImpl();
//                        queryImpl.executeQuery();
                        loopCheck = false;
                    }
                    break;
                case "2":
                    registerNewUser();
                    break;
                case "3":
                    loopCheck = logout();
                    break;
                default:
                    System.out.println("Invalid input please try again!!");
            }
        }
        //below line needs to be removed.
        System.exit(1);
    }


    /**
     * Validates user login page
     *
     * @throws IOException          handles the file not found etc.
     * @throws InterruptedException handles the interruption errors.
     */
    private boolean attemptLogin() {
        System.out.print("Enter username::");
        user.setUsername(scanner.nextLine());
        System.out.print("Enter password::");
        user.setPassword(scanner.nextLine());
        BufferedReader bufferedReader = null;
        String lineSingleUser;
        boolean loginChecker = false;
        InputStream inputStream = fileResourceUtils.getFileFromResourceAsStream(userProfile);
//            bufferedReader = new BufferedReader(new FileReader(file));

        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                String[] lineSingleUserArray = line.split("\\|");
                if (lineSingleUserArray.length > 4) {
                    if (user.getUsername().equals(lineSingleUserArray[0].trim()) && user.getPassword().equals(lineSingleUserArray[1].trim())) {
                        loginChecker = true;
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

//            while ((lineSingleUser = bufferedReader.readLine()) != null) {
//                String[] lineSingleUserArray = lineSingleUser.split(" \\| ");
//                for (int i = 0; i < lineSingleUserArray.length; i++) {
//
//                    if (user.getUsername().equals(lineSingleUserArray[i].trim()))
//                        if (user.getPassword().equals(lineSingleUserArray[i + 1].trim())) {
////                            if (securityAnswer.equals(lineSingleUserArray[i + 2].trim())) {
//                            loginChecker = true;
////                            }
//                        }
//
//                }
//            }

        return loginChecker;
    }


    private void registerNewUser() {
        System.out.print("\nPreparing to register a new user\n");
        System.out.print("Enter Username: ");
        user.setUsername(scanner.nextLine());
        if (userExists()) {
            System.out.println("User already exists.. Try registering again!!\n");
            return;
        }
        System.out.print("Enter Password: ");
        user.setPassword(scanner.nextLine());

        /*
         * TODO SHA Algorithm check
         */
//                try {
//                    userPassword=AlgorithmHashSHA.getSHA256Hash(userPassword);
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
        System.out.print("Enter Security question ");
        user.setSecurityQuestion(scanner.nextLine());
        System.out.print("Enter Answer");
        user.setSecurityAnswer(scanner.nextLine());
        System.out.println("User " + user.getUsername() + " was registered Successfully.");
        userRegistrationAddFile();
    }

    /**
     * Adds the user info from registration input method
     *
     * @throws IOException handles the file not found etc.
     */
    public boolean userRegistrationAddFile() {
        String line = user.getUsername() + "|" + user.getPassword() + "|" + user.getSecurityQuestion() + "|" + user.getSecurityAnswer() + "|" + "N";
        return FileResourceUtils.appendToFile(userProfile, line);
    }

    public Boolean userExists() {
        boolean result = false;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        while (!result) {
            try {
                if ((line = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] lineSingleUser = line.split(delimeter);
            if (lineSingleUser.length >= 4) {
                if (user.getUsername().equals(lineSingleUser[0])) {
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean logout() {
        user.setLoggedIn("N");
        // write code to update file.
        System.out.println("Logging out, Have a great day.");
        return false;
    }

    void afterLoginMenu() {
        AccessMenu.openAccessMenu();
    }
}

