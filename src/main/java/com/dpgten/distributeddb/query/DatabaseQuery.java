package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.utils.MetadataUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;

import static com.dpgten.distributeddb.utils.Utils.*;
import static com.dpgten.distributeddb.query.QueryParser.*;

public class DatabaseQuery {
    String databasePath;
    String databaseName;

    public void createDatabase(String inputQuery){
        Matcher createQueryMatcher = CREATE_DATABASE_PATTERN.matcher(inputQuery);

        if(createQueryMatcher.find()){
            this.databaseName = createQueryMatcher.group(1);

            //todo decide in which server need to create the database
            File database = new File(SCHEMA+"/"+databaseName);

            MetadataUtils mdUtils = new MetadataUtils();
            if(!mdUtils.isDatabaseExist(databaseName)){
                database.mkdir();
            }else {
                System.out.println("database "+databaseName+" already exists");
            }
        }
    }

    public boolean isTableExists(String tableName){

        return false;
    }

    public String selectDatabase(String selectDb){
        File server2 = new File(SCHEMA);

        Matcher useQueryMatcher = USE_DATABASE_PATTERN.matcher(selectDb);
        String databaseName = "";

        if(useQueryMatcher.find()){
            databaseName = useQueryMatcher.group(1);
            if(Arrays.stream(KEYWORDS).allMatch(databaseName.toLowerCase(Locale.ROOT)::equals)){
                System.out.println(RED+"CANNOT USE PREDEFINED KEYWORDS"+RESET);
            }
        }else {
            System.out.println(RED+"DATABASE NOT FOUND"+RESET);
        }
//
//        String currentUserPath1 = searchUser(server1,currentUser);
//        String currentUserPath2 = searchUser(server2,currentUser);

//        if(!currentUserPath1.equals("")){
        this.databasePath = searchDatabase(SCHEMA,databaseName);
        if(databasePath.equals("")){
            System.out.println("Database does not exists");
            return databaseName;
        }
//        }else {
//            System.out.println("User does not exists");
//        }

//        if(!currentUserPath2.equals("")){
//        this.database1Path = searchDatabase(SERVER_2,databaseName);
//        if(database1Path.equals("")){
//            System.out.println("Database does not exits");
//        }
//        }else {
//            System.out.println("User does not exists");
//        }
        return databaseName;
    }

    public String searchDatabase(String currentUser,String currentDatabase){
        String[] databases = new File(currentUser).list();
        if(databases != null){
            for(String database:databases){
                if(database.equals(currentDatabase)){
                    return currentUser+"/"+currentDatabase;
                }
            }
        }
        return "";
    }

    public String searchUser(File server,String currentUser){
        String[] users = server.list();
        if(users != null){
            for(String user:users){
                if(user.equals(currentUser)){
                    return server+"/"+user;
                }
            }
        }
        return "";
    }
}
