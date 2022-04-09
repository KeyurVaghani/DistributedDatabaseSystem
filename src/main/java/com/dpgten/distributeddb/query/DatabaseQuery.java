package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.utils.FileResourceUtils;
import com.dpgten.distributeddb.utils.MetadataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;

import static com.dpgten.distributeddb.query.QueryParser.*;
import static com.dpgten.distributeddb.utils.Utils.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseQuery {
    String databasePath;
    String databaseName;
    String currentUser;
    File server1;
    File server2;
    String database1Path;
    String database2Path;

    public DatabaseQuery(String server1, String server2, String currentUser) {
        this.server1 = new File(server1);
        this.server2 = new File(server2);
        this.currentUser = currentUser;
    }

    public void createDatabase(String inputQuery) {
        Matcher createQueryMatcher = CREATE_DATABASE_PATTERN.matcher(inputQuery);

        if (createQueryMatcher.find()) {
            this.databaseName = createQueryMatcher.group(1);

            //todo decide in which server need to create the database
            File database = new File(SCHEMA + "/" + databaseName);

            MetadataUtils mdUtils = new MetadataUtils();
            if (!mdUtils.isDatabaseExist(databaseName)) {
                database.mkdir();
            } else {
                System.out.println("database " + databaseName + " already exists");
            }
        }
    }


    public boolean isUseQuery(String inputQuery) {
        Matcher queryMatcher = USE_DATABASE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isCreateTableQuery(String inputQuery) {
        Matcher queryMatcher = CREATE_TABLE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isCreateQuery(String inputQuery) {
        Matcher queryMatcher = CREATE_DATABASE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isSelectQuery(String inputQuery) {
        Matcher queryMatcher = SELECT_TABLE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isDeleteQuery(String inputQuery) {
        Matcher queryMatcher = DELETE_QUERY_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public void selectRows(String inputQuery) {
        Matcher selectRowsMatcher = SELECT_TABLE_PATTERN.matcher(inputQuery);
        String tableName = "";
        String tablePath = "";
        String databaseName = "";
        if (selectRowsMatcher.find()) {
            tableName = selectRowsMatcher.group(8);
            tablePath = SERVERS + "/" + findTableInDatabase(tableName);
        }

        File table = new File(tablePath + "/" + tableName + ".txt");
        FileResourceUtils fileUtils = new FileResourceUtils();
        fileUtils.printFile(table);
    }

    public String findTableInDatabase(String tableName) {
        File metaData = new File(GLOBAL_METADATA);
        String server = "";

        try {
            Scanner fileScanner = new Scanner(metaData);
            while (fileScanner.hasNext()) {
                String row = fileScanner.next();
                if (Arrays.asList(row.split("\\|")).contains(tableName)) {
                    server = row.split("\\|")[0] + "/" + row.split("\\|")[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return server;
    }

    public boolean isTableExists(String tableName) {


        return false;
    }

    public String selectDatabase(String selectDb) {
        File server1 = new File(SERVER_1);
        File server2 = new File(SERVER_2);

        Matcher useQueryMatcher = USE_DATABASE_PATTERN.matcher(selectDb);
        String databaseName = "";

        if (useQueryMatcher.find()) {
            databaseName = useQueryMatcher.group(1);
            if (Arrays.stream(KEYWORDS).allMatch(databaseName.toLowerCase(Locale.ROOT)::equals)) {
                System.out.println(RED + "CANNOT USE PREDEFINED KEYWORDS" + RESET);
            }
        } else {
            System.out.println(RED + "DATABASE NOT FOUND" + RESET);
        }
//
//        String currentUserPath1 = searchUser(server1,currentUser);
//        String currentUserPath2 = searchUser(server2,currentUser);

//        if(!currentUserPath1.equals("")){
        this.databasePath = searchDatabase(SCHEMA, databaseName);
        if (databasePath.equals("")) {
            System.out.println("Database does not exists");
            return databaseName;
        }
        return databaseName;
    }

    public String searchDatabase(String currentUser, String currentDatabase) {
        String[] databases = new File(currentUser).list();
        if (databases != null) {
            for (String database : databases) {
                if (database.equals(currentDatabase)) {
                    return currentUser + "/" + currentDatabase;
                }
            }
        }
        return "";
    }

    public String searchUser(File server, String currentUser) {
        String[] users = server.list();
        if (users != null) {
            for (String user : users) {
                if (user.equals(currentUser)) {
                    return server + "/" + user;
                }
            }
        }
        return "";
    }
}
