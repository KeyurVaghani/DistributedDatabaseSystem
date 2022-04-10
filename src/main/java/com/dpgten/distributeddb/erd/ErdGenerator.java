package com.dpgten.distributeddb.erd;

import ch.qos.logback.classic.db.names.ColumnName;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class ErdGenerator {
    private final String schema = "schema";
    private final String primarykey = "Sno";
    private final String delimeter1 = "\\|";
    private final String DELIMETER_COMMA = ",";
    private final String delimeter2 = "~";
    private final String NEW_LINE = "\n";
    private final String SEMI_COLON = ";";
    public Scanner scanner;


    public void firstErd() throws IOException {
        File obj = getFileResource(schema);
        scanner = new Scanner(System.in);
        System.out.println("Please select database: ");
//        String requiredDatabase = scanner.next();
        String requiredDatabase="db1";
        String[] databaseList = obj.list();
        StringBuilder builder = new StringBuilder();
//        ArrayList<String> databaseCheckerList = new ArrayList<>();
        for (int i = 0; i < databaseList.length; i++) {
//            if (!databaseCheckerList.contains(databaseList[i])) {
            String selectedDatabase = null;
            if (requiredDatabase == databaseList[i]) {
                selectedDatabase = requiredDatabase;

//                builder.append("CREATE DATABASE " + databaseList[i] + SEMI_COLON + NEW_LINE);
//                databaseCheckerList.add(databaseList[i]);
//            }
                System.out.println("Database: " + selectedDatabase + "selected.");
            } else {
                System.out.println("Database not found please re enter the database name.");
            }
//            String column = schema + "//" + databaseList[i];
            String column = schema + "//" + selectedDatabase;
            File tableList = getFileResource(column);
            String[] tableIterate = tableList.list();
            for (int j = 0; j < tableIterate.length; j++) {
                InputStream table = getFileFromResourceAsStream(column + "//" + tableIterate[j]);
                String tableName = tableIterate[j];
                tableName = tableName.substring(0, tableName.length() - 4);
                InputStreamReader inputStreamReader = new InputStreamReader(table, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String primaryKey = bufferedReader.readLine().split(delimeter1)[1];
                //add logic to add foreign key inside create table
                String foreignKey = bufferedReader.readLine();
                String[] columnList = bufferedReader.readLine().split(delimeter1);
                ArrayList<String> columnNames = new ArrayList<String>();
                ArrayList<String> columnDataTypes = new ArrayList<String>();
                for (int index = 1; index < columnList.length; index++) {
                    String[] temp = columnList[index].split(DELIMETER_COMMA);
                    columnNames.add(temp[0].trim());
                    columnDataTypes.add(temp[1].trim());
                }
                String prefix = "";
//                builder.append("CREATE TABLE " + tableName + "( ");
                System.out.println("============ Table: "+tableName+" ===============");
                for (int index = 0; index < columnNames.size(); index++) {
//                    builder.append(prefix);
//                    builder.append(columnNames.get(index)).append(" ").append(columnDataTypes.get(index));
                    System.out.println(columnNames + " " + columnDataTypes);
                    prefix = ", ";
                }
                builder.append(");\n");
                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    String[] rowData = line.split(delimeter1);
//                    String insertQuery = "INSERT INTO " + tableName + " VALUES(";
//                    prefix = "";
//                    for (int index = 1; index < rowData.length; index++) {
//                        insertQuery += prefix;
//                        insertQuery += rowData[index];
//                        prefix = ",";
//                    }
//                    insertQuery += ");\n";
//                    builder.append(insertQuery);
//                }

            }
        }
        //System.out.println(builder);

    }

    public File getFileResource(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!" + fileName);
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public InputStream getFileFromResourceAsStream(String fileName) {
        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public static void main(String[] args) throws IOException {
        ErdGenerator erd= new ErdGenerator();
        erd.firstErd();
    }

}
