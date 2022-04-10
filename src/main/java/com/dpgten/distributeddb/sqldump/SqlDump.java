package com.dpgten.distributeddb.sqldump;

import io.swagger.v3.oas.models.security.SecurityScheme;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class SqlDump {

    //    private final String schema = "schema";
    private final String schema = "src\\main\\resources\\schema\\";

    private final String primarykey = "Sno";
    private final String delimeter1 = "\\|";
    private final String DELIMETER_COMMA = ",";
    private final String delimeter2 = "~";
    private final String NEW_LINE = "\n";
    private final String SEMI_COLON = ";";

    //RENAME VARIABLES
    public void generateDump(String selectedDatabase) {
        File dataBase = new File(schema + selectedDatabase);
        File[] tables = dataBase.listFiles();
        StringBuilder builder = new StringBuilder();
        List<String> dumpQueries = new ArrayList<>();
        String createDataBaseCommand = "CREATE DATABASE " + selectedDatabase +";";
        dumpQueries.add(createDataBaseCommand);
        Map<Integer, String> currentDataMap = new HashMap<>();
        List<String> columnList = null;
        String tableName = null;
        for (File table: tables) {
            try {
                currentDataMap = getDataForCurrentTable(table, currentDataMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            tableName = table.getName().replaceAll(".txt", "");
            StringBuffer createTableQuery = new StringBuffer();
            createTableQuery.append("CREATE TABLE ").append(tableName).append("(");
            String primaryKey = "";
            for (String line: currentDataMap.values()) {
                if (line.startsWith("Column")) {
                    columnList = new LinkedList<>(Arrays.asList(line.split(Pattern.quote("|"))));
                    columnList.remove(0);
                }
                if (line.startsWith("primary_key")) {
                    primaryKey = line.split(Pattern.quote("|"))[1];
                }
            }

            for (String column: columnList) {
                createTableQuery.append(column.replace(",", " ")).append(",");
            }
            createTableQuery.deleteCharAt(createTableQuery.toString().length()-1);
            if (!primaryKey.isEmpty() || !primaryKey.isBlank()) {
                createTableQuery.append(",").append("PRIMARY KEY (").append(primaryKey).append(")");
            }
            createTableQuery.append(");");
            dumpQueries.add(createTableQuery.toString());
            String insertQuery = null;
            for (String line: currentDataMap.values()) {
                if (line.startsWith("Row")) {
                    StringBuffer insertQueryBuffer = new StringBuffer();
                    insertQueryBuffer.append("INSERT INTO ").append(tableName).append(" ").append("(");
                    for (String column: columnList) {
                        insertQueryBuffer.append(column.split(",")[0]).append(",");
                    }
                    insertQueryBuffer.deleteCharAt(insertQueryBuffer.toString().length()-1);
                    insertQueryBuffer.append(")").append(" VALUES (");
                    String[] rowData = line.split(Pattern.quote("|"));
                    List<String> rowDataList = new LinkedList<>(Arrays.asList(rowData));
                    rowDataList.remove(0);
                    for (String data: rowDataList) {
                        insertQueryBuffer.append(data).append(",");
                    }
                    insertQueryBuffer.deleteCharAt(insertQueryBuffer.toString().length()-1);
                    insertQueryBuffer.append(");");
                    dumpQueries.add(insertQueryBuffer.toString());
                }
            }
        }
        try {
            writeDumpDataToFile(dumpQueries, selectedDatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(builder);
    }

    private void writeDumpDataToFile(List<String> dumpQueries, String selectedDatabase) throws IOException {
        String dumpFileDirectory = "src\\main\\resources\\sqlDump";
        File file = new File(dumpFileDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        File dumpPath = new File(dumpFileDirectory + "\\" + selectedDatabase + ".sql");
        dumpPath.createNewFile();
        String newline = System.getProperty("line.separator");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dumpPath, true));
        for (String query: dumpQueries) {
            bufferedWriter.append(query + newline);
        }
        bufferedWriter.flush();
    }

    private Map<Integer, String> getDataForCurrentTable(File table, Map<Integer, String> currentDataMap) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(table));
        String line;
        Integer lineCount = 1;
        while ((line = reader.readLine()) != null) {
            currentDataMap.put(lineCount, line);
            lineCount++;
        }
        return currentDataMap;
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

}

