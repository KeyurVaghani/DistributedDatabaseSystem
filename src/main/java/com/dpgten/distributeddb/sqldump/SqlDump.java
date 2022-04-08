package com.dpgten.distributeddb.sqldump;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SqlDump {

    private String schema = "schema";

    //RENAME VARIABLES
    public void readFile() throws IOException {
        File obj = getFileResource(schema);


        String[] databaseList = obj.list();
        for (int i = 0; i < databaseList.length; i++) {

            System.out.println("CREATE DATABASE "+databaseList[i]);
            //System.out.println(databaseList[i]);
            String column = schema + "//" + databaseList[i];
            File tableList = getFileResource(column);
            String[] tableIterate = tableList.list();
            for (int j = 0; j < tableIterate.length; j++) {
                InputStream table = getFileFromResourceAsStream(column + "//"+tableIterate[j]);
                    String tableName=tableIterate[j];
                    //System.out.println(tableName);
                    System.out.println("CREATE TABLE "+tableName);
                    InputStreamReader inputStreamReader= new InputStreamReader(table,StandardCharsets.UTF_8);
                    BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
                    bufferedReader.lines().forEach(line-> {
                        //System.out.println(line);
                        String[] lineSplit= line.split("~");
                        for(int z=0;z< lineSplit.length;z++) {

                           // if(line)
                            System.out.println(lineSplit[z]);
                        }
                    });
                }
        }
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

//    public void printFile(File file) {
//        List<String> lines;
//        try {
//            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//            lines.forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

