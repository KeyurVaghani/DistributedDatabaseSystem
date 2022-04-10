package com.dpgten.distributeddb.userauthentication;

import com.dpgten.distributeddb.query.QueryImpl;
import com.dpgten.distributeddb.sqldump.SqlDump;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Scanner;

import static com.dpgten.distributeddb.utils.Utils.*;
import static com.dpgten.distributeddb.utils.Utils.RESET;

@Data
@Component
public class MainMenu {

    public boolean drive(User user) {
        System.out.println("\n" + YELLOW + "-----------------------Welcome " + user.getUsername() + "---------------------" + RESET);
        System.out.println(BLUE + "AUTHENTICATION SUCCESS" + RESET);
        boolean loopCheck = true;
        Scanner scanner = new Scanner(System.in);
        while (loopCheck) {
            System.out.println("\nMain Menu");
            System.out.println("Press 1 ==> Process Query\nPress 2 ==> Generate SQL Dump\nPress 3 ==> Generate ERD \nPress 4 ==> Logout");
            System.out.print("Enter your Choice::: ");
            String selectedOption = scanner.nextLine();
            switch (selectedOption) {
                case "1":
                    QueryImpl impl = new QueryImpl();
                    boolean result = impl.executeQuery(user);
                    break;
                case "2":
                    SqlDump dump = new SqlDump();
                    System.out.print(YELLOW+"Enter database Name:"+RESET);
                    String databaseName = scanner.nextLine();
                    dump.generateDump(databaseName);
                    break;
                case "3":
                    //ERD generator
                    System.out.println("generating ERD");
                    break;
                case "4":
                    loopCheck = false;
                    break;
                default:
                    System.out.println("Invalid input try again");
            }
        }
        return true;
    }
}
