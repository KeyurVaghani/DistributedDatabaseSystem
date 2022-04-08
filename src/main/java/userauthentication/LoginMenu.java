package userauthentication;

import java.io.*;
import java.util.Scanner;

public class LoginMenu {
    private final Scanner scanner;
    public static String USER_NAME;
    File file;

    public LoginMenu() {
        scanner = new Scanner(System.in);
        System.out.println("============ WELCOME ==========");
        file = new File("UserProfiles.txt");
    }

    void userFirstMenu() throws IOException, InterruptedException {
        System.out.println("========== USER SELECTION =========");
        System.out.println("1. User Login\n2. New User? Register\n3. Exit");
        System.out.print("Enter your Choice: ");
        String selectUser = scanner.nextLine();
        userMainMenu(selectUser);
    }

    void userMainMenu(String selectUser) throws IOException, InterruptedException {
        switch (selectUser) {
            case "1":
                System.out.print("Enter username: ");
                USER_NAME = scanner.nextLine();
                System.out.print("Enter password: ");
                String userPassword = scanner.nextLine();
//                System.out.print("Enter your security question: ");
//                String securityQuestion = scanner.nextLine();
                System.out.print("What's your favorite movie? \nEnter your security answer: ");
                String securityAnswer = scanner.nextLine();
                userLoginChecker(userPassword, securityAnswer);
                break;
            case "2":
                System.out.print("Username: ");
                USER_NAME = scanner.nextLine();
                if (userExists()) {
                    System.out.println("User already exist.. Login again!!\n");
                    userFirstMenu();
                }
                while (userExists()) {
                    System.out.print("Username: ");
                    USER_NAME = scanner.nextLine();
                    if (userExists()) {
                        System.out.println("User already exist.. Login again!!\n");
                        userFirstMenu();
                    }
                }
                System.out.print("Password: ");
                userPassword = scanner.nextLine();

                // TODO: SHA Algorithm check

//                try {
//                    userPassword=AlgorithmHashSHA.getSHA256Hash(userPassword);
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
                System.out.print("What's your favorite movie? ");
                securityAnswer = scanner.nextLine();
                System.out.println("Registered Successfully");
                userRegistrationAddFile(userPassword, securityAnswer);
                userFirstMenu();
                break;
            case "3":
                System.out.println("Bye");
                System.exit(0);
                break;

            default:
                System.out.println("Invalid input please try again!!");
                System.exit(0);
        }
    }

    void userLoginChecker(String userPassword, String securityAnswer) throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String lineSingleUser;
        boolean loginChecker = false;

        while ((lineSingleUser = bufferedReader.readLine()) != null) {
            String[] lineSingleUserArray = lineSingleUser.split(" \\| ");
            for (int i = 0; i < lineSingleUserArray.length; i++) {
                try {
                    if (USER_NAME.equals(lineSingleUserArray[i].trim()))
                        if (userPassword.equals(lineSingleUserArray[i + 1].trim())) {
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
            userFirstMenu();
        }
    }

    void userRegistrationAddFile(String userPassword, String securityAnswer) throws IOException {
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
        printWriter.println(USER_NAME + " | " + userPassword + " | " + securityAnswer);
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
        //adding menu class after login
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        LoginMenu lm = new LoginMenu();
        lm.userFirstMenu();
    }
}

