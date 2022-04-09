package com.dpgten.distributeddb.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.dpgten.distributeddb.utils.Utils.*;

public class MetadataUtils {

    public String[] listDatabases(String serverName){
        File globalMetaData = new File(GLOBAL_METADATA);
        List<String> databaseList = new ArrayList<>();

        try {
            Scanner metaDataScanner = new Scanner(globalMetaData);
            while (metaDataScanner.hasNext()){
                String row = metaDataScanner.nextLine();
                String server = row.split(PRIMARY_DELIMITER_REGEX)[0];
                String databaseName = row.split(PRIMARY_DELIMITER_REGEX)[1];
                if(server.equals(serverName)){
                    databaseList.add(databaseName);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return databaseList.toArray(new String[databaseList.size()]);
    }

    public boolean isDatabaseExist(String databaseName){
        MetadataUtils mdUtils = new MetadataUtils();
        String[] databaseList = mdUtils.listDatabases(databaseName);
        if(Arrays.asList(databaseList).contains(databaseName)){
            return true;
        }
        return false;
    }

    public String getTablePath(String currentTableName){
        File metadataFile = new File(GLOBAL_METADATA);
        try {
            Scanner metadataScanner = new Scanner(metadataFile);
            while(metadataScanner.hasNext()){
                String row = metadataScanner.nextLine();
                String databaseName = row.split(PRIMARY_DELIMITER_REGEX)[1];
                String tableName = row.split(PRIMARY_DELIMITER_REGEX)[2];
                if(currentTableName.equals(tableName)){
                    return SCHEMA+"/"+databaseName+"/"+tableName+".txt";
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

//    public String findTableInServer(String tableName){
//        File metaData = new File(GLOBAL_METADATA);
//        try {
//            Scanner fileScanner = new Scanner(metaData);
//            while(fileScanner.hasNext()){
//                String row = fileScanner.next();
//                if(Arrays.asList(row.split(PRIMARY_DELIMITER_REGEX)).contains(tableName)){
//                    return SCHEMA + "/" +row.split(PRIMARY_DELIMITER_REGEX)[1]
//                            + "/" + row.split(PRIMARY_DELIMITER_REGEX)[2]+".txt";
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public String getDatabasePath(String tableName){
        File metaData = new File(GLOBAL_METADATA);
        try {
            Scanner mdScanner = new Scanner(metaData);
            while (mdScanner.hasNext()){
                String row = mdScanner.nextLine();
                if(Arrays.asList(PRIMARY_DELIMITER_REGEX).contains(tableName)){
                    return SCHEMA + "/" + row.split(PRIMARY_DELIMITER_REGEX)[1];
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
