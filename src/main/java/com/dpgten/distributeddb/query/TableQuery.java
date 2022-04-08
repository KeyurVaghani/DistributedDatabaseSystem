package com.dpgten.distributeddb.query;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;

import static com.dpgten.distributeddb.utils.Utils.*;

public class TableQuery {
    String database1;
    String database2;

    public TableQuery(String database1Path,String database2Path){
        this.database1 = database1Path;
        this.database2 = database2Path;
    }

    public boolean searchTable(String tableName,String database){
        String[] tables = new File(database).list();

        if(tables != null) {
            for (String table : tables) {
                if (table.equals(tableName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isExistTable(String tableName){
        return searchTable(tableName, database1) || searchTable(tableName, database2);
    }

    public void createTable(String inputQuery){
        Matcher createTableMatcher = QueryParser.CREATE_TABLE_PATTERN.matcher(inputQuery);
        String tableName = null;
        String tableColumn = null;
        HashMap<String,String> tableColumnList= new HashMap<String,String>();
        if(createTableMatcher.find()){
            tableName = createTableMatcher.group(1);
            tableColumn = createTableMatcher.group(2);
            if(Arrays.asList(KEYWORDS).contains(tableName.toLowerCase(Locale.ROOT))
                    || Arrays.asList(KEYWORDS).contains(tableColumn.toLowerCase(Locale.ROOT))){
                System.out.println(RED+"CANNOT USE PREDEFINED KEYWORDS"+RESET);
            }
        }

        boolean isPrimaryKey = false;
        String primaryKeyTable  = "NULL";
        if (tableColumn != null) {
            for(String column:tableColumn.split(",")){
                String columnName = column.split("\\s+")[0];
                String columnType = column.split("\\s+")[1];

                if(!isPrimaryKey && column.contains("PRIMARY KEY") ){
                    isPrimaryKey = true;
                    primaryKeyTable = columnName;
                }else if(isPrimaryKey && column.contains("PRIMARY KEY")){
                    System.out.println(RED+"MULTIPLE PRIMARY KEY");
                }

                if(!tableColumnList.containsKey(columnName)){
                    tableColumnList.put(columnName, columnType);
                }else {
                    System.out.println(RED+"DUPLICATE COLUMN NAME"+RESET);
                }
            }
        }

        //todo try to put tables into the either server based on some requirements
        File table = new File(this.database1+'/'+tableName+".txt");

        if(!table.exists()){
            try {
                table.createNewFile();
            } catch (IOException e) {
                System.out.println("ERROR IN CREATION OF THE TABLE");
                System.out.println(e.getMessage());
            }
        }else {
            System.out.println("Table "+tableName+" already exists");
        }

        try {
            FileWriter writeHeader = new FileWriter(table);
            StringBuilder columnWriterString = new StringBuilder("");

            columnWriterString.append("PK").append(PRIMARY_DELIMITER).append(primaryKeyTable).append("\n");
            for (String columnName: tableColumnList.keySet()) {
                columnWriterString.append(columnName).append(SECONDARY_DELIMITER)
                        .append(tableColumnList.get(columnName)).append(PRIMARY_DELIMITER);
            }

            writeHeader.write(String.valueOf(columnWriterString.deleteCharAt(columnWriterString.length()-1)));
            writeHeader.close();
        } catch (IOException e) {
            System.out.println("ERROR IN INSERTING THE COLUMNS");
            System.out.println(e.getMessage());
        }
    }
}
