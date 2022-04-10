package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.access.RestCallController;
import com.dpgten.distributeddb.userauthentication.User;

import java.util.Locale;
import java.util.Scanner;

import static com.dpgten.distributeddb.utils.Utils.*;

public class QueryImpl {
    public boolean executeQuery(User user) {
        System.out.println("\n" + YELLOW + "-----------------------Welcome " + user.getUsername() + "---------------------" + RESET);
        System.out.println(BLUE + "AUTHENTICATION SUCCESS" + RESET);
//        String currentUser = user.getUsername();
        String currentDatabase = "";


        String inputQuery = "1";

        DatabaseQuery dbQuery = new DatabaseQuery();
        while (inputQuery.toLowerCase(Locale.ROOT).equals("1")) {
            System.out.print("\nEnter Query Here ==>");
            Scanner input = new Scanner(System.in);
            QueryValidator validator = new QueryValidator();
            inputQuery = input.nextLine();
            if (validator.isUseQuery(inputQuery)) {
                currentDatabase = dbQuery.selectDatabase(inputQuery);
                if(currentDatabase.isEmpty()){
                    System.out.println(RED + "DATABASE NOT FOUND" + RESET);
                }else {
                    System.out.println(YELLOW + "Database selected. Current Database is "
                            + BLUE + currentDatabase + YELLOW + "." + RESET);
                }
            } else if (currentDatabase.isEmpty()) {
                if(validator.isCreateQuery(inputQuery)){
                    dbQuery.createDatabase(inputQuery);
                }
                else {
                    System.out.println(RED + "No Database selected, please select database!" + RESET);
                }
            } else if (validator.isCreateTableQuery(inputQuery)) {
                TableQuery tableQuery = new TableQuery();
                tableQuery.createTable(inputQuery, currentDatabase);
            } else if (validator.isCreateQuery(inputQuery)) {
                dbQuery.createDatabase(inputQuery);
            } else if (validator.isSelectQuery(inputQuery)) {
                TableQuery tblQuery = new TableQuery();
                tblQuery.selectRows(inputQuery);
            } else if (validator.isInsertQuery(inputQuery)) {
                TableQuery tableQuery = new TableQuery();
                tableQuery.insertRow(inputQuery);
            }else if(validator.isUpdateQuery(inputQuery)){
                TableQuery tableQuery = new TableQuery();
                tableQuery.updateRow(inputQuery);
            }
            else if (dbQuery.isDeleteQuery(inputQuery)) {
                DeleteQueryParser deleteQueryParser = new DeleteQueryParser();
                deleteQueryParser.executeDeleteQueryWithConditionQuery(inputQuery, user);
            } else {
                System.out.println(RED + "PLEASE ENTER VALID QUERY" + RESET + "\n");
            }
            System.out.println("please type " + RED + "1" + RESET + " to Continue");
            System.out.println("please type " + RED + "2" + RESET + " to Exit");
            System.out.print("Enter option here ==>");
            inputQuery = input.nextLine();
        }
        return true;
    }
}
