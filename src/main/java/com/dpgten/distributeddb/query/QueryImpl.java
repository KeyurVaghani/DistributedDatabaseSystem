package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.userauthentication.User;

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
        DatabaseQuery dbQuery = new DatabaseQuery();
        while (inputQuery.toLowerCase(Locale.ROOT).equals("y")) {
            QueryValidator validator = new QueryValidator();
                inputQuery = input.nextLine();
            if (validator.isUseQuery(inputQuery)) {
                currentDatabase = dbQuery.selectDatabase(inputQuery);
            } else if (!currentDatabase.equals("") && validator.isCreateTableQuery(inputQuery)) {
                TableQuery tableQuery = new TableQuery();
                tableQuery.createTable(inputQuery,currentDatabase);
            } else if (!currentDatabase.equals("") && validator.isCreateQuery(inputQuery)) {
                dbQuery.createDatabase(inputQuery);
            }else if(!currentDatabase.equals("") && validator.isSelectQuery(inputQuery)){
                TableQuery tblQuery = new TableQuery();
                tblQuery.selectRows(inputQuery);
            }else if(!currentDatabase.equals("") && validator.isInsertQuery(inputQuery)){
                TableQuery tableQuery = new TableQuery();
                tableQuery.insertRow(inputQuery);
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
