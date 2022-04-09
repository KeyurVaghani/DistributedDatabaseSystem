package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.userauthentication.User;

import java.util.Locale;
import java.util.Scanner;
import static com.dpgten.distributeddb.utils.Utils.*;

public class QueryImpl {
    public void executeQuery(User user) {
        System.out.println("\n"+YELLOW+"-----------------------Welcome "+ user.getUsername()+"---------------------"+RESET);
        System.out.println(BLUE+"AUTHENTICATION SUCCESS"+RESET);
        String currentUser = "user_0";
        String currentDatabase = "";

        System.out.println("\nEnter Query Here==>");
        Scanner input = new Scanner(System.in);
        String inputQuery = "y";
        DatabaseQuery dbQuery = new DatabaseQuery(SERVER_1, SERVER_2, currentUser);
        while (inputQuery.toLowerCase(Locale.ROOT).equals("y")) {
//                inputQuery = input.nextLine();
                inputQuery = "Delete FROM tb1 where sno = abcd;";
            if (dbQuery.isUseQuery(inputQuery)) {
                currentDatabase = dbQuery.selectDatabase(inputQuery);
            } else if (!currentDatabase.equals("") && dbQuery.isCreateTableQuery(inputQuery)) {
                String database1 = dbQuery.getDatabase1Path();
                String database2 = dbQuery.getDatabase2Path();
                TableQuery tableQuery = new TableQuery(database1, database2);
                tableQuery.createTable(inputQuery);
            } else if (dbQuery.isCreateQuery(inputQuery)) {
                dbQuery.createDatabase(inputQuery);
            }else if(dbQuery.isSelectQuery(inputQuery)){
                dbQuery.selectRows(inputQuery);
            }else if(dbQuery.isDeleteQuery(inputQuery)){
                DeleteQueryParser deleteQueryParser = new DeleteQueryParser();
                deleteQueryParser.executeDeleteQueryWithConditionQuery(inputQuery, user);
            }
            else {
                System.out.println(RED+"PLEASE ENTER VALID QUERY"+RESET);
            }
            System.out.println("please type "+RED+"y"+RESET +" to continue");
            inputQuery = input.nextLine();
        }
    }
}
