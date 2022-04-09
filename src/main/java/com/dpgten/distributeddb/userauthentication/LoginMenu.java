package com.dpgten.distributeddb.userauthentication;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Authentication Login Menus
 * @author DMWA Group 10
 */
public class LoginMenu {
    private final Scanner scanner;
    public static String USER_NAME;
    File file;

    /**
     * Default Constructor
     */
    public LoginMenu() {
        scanner = new Scanner(System.in);
        System.out.println("============ WELCOME ============");
        file = new File("src/main/resources/UserProfile.txt");
    }

    /**
     * Displays first selection menu.
     * @throws IOException handles the file not found etc.
     * @throws InterruptedException handles the interruption errors.
     * @param userName
     */
    void userFirstMenu(String userName) throws IOException, InterruptedException {
        System.out.println("========== USER SELECTION =========");
        System.out.println("1. User Login\n2. New User? Register\n3. Exit");
        System.out.print("Enter your Choice: ");
        String selectUser = scanner.nextLine();
        userMainMenu(selectUser, userName);
    }

    /**
     * Displays second menu according choice
     * @param selectUser provides user choice
     * @param userName
     * @throws IOException handles the file not found etc.
     * @throws InterruptedException handles the interruption errors.
     */
    void userMainMenu(String selectUser, String userName) throws IOException, InterruptedException {
        switch (selectUser) {
            case "1":
                System.out.print("Enter username: ");
                userName = scanner.nextLine();
                System.out.print("Enter password: ");
                String userPassword = scanner.nextLine();
//                System.out.print("Enter your security question: ");
//                String securityQuestion = scanner.nextLine();
                System.out.print("What's your favorite movie? \nEnter your security answer: ");
                String securityAnswer = scanner.nextLine();
                userLoginChecker(userName, userPassword, securityAnswer);
                break;
            case "2":
                System.out.print("Username: ");
                USER_NAME = scanner.nextLine();
                try {
                    USER_NAME= AlgorithmHashSHA.convertSHA256Hash(USER_NAME);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (userExists()) {
                    System.out.println("User already exist.. Login again!!\n");
                    userFirstMenu(USER_NAME);
                }
                while (userExists()) {
                    System.out.print("Username: ");
                    USER_NAME = scanner.nextLine();
                    if (userExists()) {
                        System.out.println("User already exist.. Login again!!\n");
                        userFirstMenu(USER_NAME);
                    }
                }
                System.out.print("Password: ");
                userPassword = scanner.nextLine();

                //converting in hash SHA
                try {
                    userPassword=AlgorithmHashSHA.convertSHA256Hash(userPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                System.out.print("What's your favorite movie? ");
                securityAnswer = scanner.nextLine();
                System.out.println("Registered Successfully");
                userRegistrationAddFile(userPassword, securityAnswer);
                userFirstMenu(USER_NAME);
                break;
            case "3":
                System.out.println("========== BYE ===========");
                System.exit(0);
                break;

            default:
                System.out.println("Invalid input please try again!!");
                System.exit(0);
        }
    }

    /**
     * Validates user login page
     *
     * @param userName
     * @param userPassword passed from the userMainMenu
     * @param securityAnswer passed from previous menu
     * @throws IOException handles the file not found etc.
     * @throws InterruptedException handles the interruption errors.
     */
    void userLoginChecker(String userName, String userPassword, String securityAnswer) throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String lineSingleUser;
        boolean loginChecker = false;

        while ((lineSingleUser = bufferedReader.readLine()) != null) {
            String[] lineSingleUserArray = lineSingleUser.split(" \\| ");
            for (int i = 0; i < lineSingleUserArray.length; i++) {
                try {
                    if (userName.equals(lineSingleUserArray[i].trim()) && AlgorithmHashSHA.compareSHA256Hash(lineSingleUserArray[i].trim(), userName))
                        if (userPassword.equals(lineSingleUserArray[i + 1].trim()) && AlgorithmHashSHA.compareSHA256Hash(lineSingleUserArray[i + 1].trim(),userPassword)) {
                            if (securityAnswer.equals(lineSingleUserArray[i + 2].trim())) {
                                loginChecker = true;
                            }
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (loginChecker) {
            System.out.println("Login Successfully");
            afterLoginMenu();
        } else {
            System.out.println("\n Invalid credentials or not registered \n");
            userFirstMenu(USER_NAME);
        }
    }

    /**
     * Adds the user info from registration input method
     * @param userPassword passed from user main menu
     * @param securityAnswer passed from previous method
     * @throws IOException handles the file not found etc.
     */
    void userRegistrationAddFile(String userPassword, String securityAnswer) throws IOException {
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
        printWriter.println(USER_NAME + " | " + userPassword + " | " + securityAnswer + " | " + "NOT_SIGNED");
        printWriter.close();
        afterLoginMenu();
    }

    Boolean userExists() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lineSingleUser = line.split(" \\| ");
            for (String userInfo : lineSingleUser) {
                try {
                    if (USER_NAME.equals(userInfo.trim())) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    void afterLoginMenu() {
        AccessMenu.openAccessMenu();
    }

    /*
     * Check with main
     */

    public static void main(String[] args) throws IOException, InterruptedException {
        LoginMenu lm = new LoginMenu();
        lm.userFirstMenu(USER_NAME);
    }
}

