package com.dpgten.distributeddb.query;

import java.util.Locale;
import java.util.Scanner;
import static com.dpgten.distributeddb.utils.Utils.*;

public class QueryImpl {
    public void executeQuery() {
        System.out.println(YELLOW+"-----------------------INSTANCE STARTED---------------------"+RESET);
        System.out.println(BLUE+"AUTHENTICATION HAS BEEN SUCCESSFULLY"+RESET);
        String currentUser = "user_0";
        String currentDatabase = "";

        Scanner input = new Scanner(System.in);
        String inputQuery = "y";
        DatabaseQuery dbQuery = new DatabaseQuery(SERVER_1, SERVER_2, currentUser);
        while (inputQuery.toLowerCase(Locale.ROOT).equals("y")) {
                inputQuery = input.nextLine();
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
            }else {
                System.out.println(RED+"PLEASE ENTER VALID QUERY"+RESET);
            }
            System.out.println("please type "+RED+"y"+RESET +" to continue");
            inputQuery = input.nextLine();
        }
    }
}
