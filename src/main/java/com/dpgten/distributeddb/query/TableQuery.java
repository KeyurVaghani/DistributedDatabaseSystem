package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.utils.MetadataUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;

import static com.dpgten.distributeddb.query.QueryParser.INSERT_TABLE_PATTERN;
import static com.dpgten.distributeddb.query.QueryParser.SELECT_TABLE_PATTERN;
import static com.dpgten.distributeddb.utils.Utils.*;

public class TableQuery {

    public void selectRows(String inputQuery){
        Matcher selectRowsMatcher = SELECT_TABLE_PATTERN.matcher(inputQuery);
        String tableName = "";
        String tablePath = "";

        if(selectRowsMatcher.find()){
            MetadataUtils mdUtils = new MetadataUtils();
            tableName = selectRowsMatcher.group(8);
            tablePath = mdUtils.getTablePath(tableName);
        }
        File tableFile = new File(tablePath);

        if(!selectRowsMatcher.group(9).isEmpty()){
            String columnName = selectRowsMatcher.group(10);
            String columnValue = selectRowsMatcher.group(11);
            List<String> selectRows = executeWhere(tableFile,columnName,columnValue);
            selectRows.forEach(System.out::println);
        }
    }

    public List<String> executeWhere(File tableFile, String columnName
            , String columnValue){
        List<String> selectRows = new ArrayList<>();
        try {
            Scanner tableScanner = new Scanner(tableFile);
            tableScanner.nextLine();
            String columnHeader = tableScanner.nextLine();
            String[] headers = columnHeader.split("\\|");
            List<String> headerArray = new ArrayList<>();
            for(String header: headers){
                headerArray.add(header.split(",")[0]);
            }
            int columnIndex = headerArray.indexOf(columnName);

            while(tableScanner.hasNext()){
                String row = tableScanner.nextLine();
                if(row.split(",")[columnIndex].equals(columnValue)){
                    selectRows.add(row);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return selectRows;
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

    public void createTable(String inputQuery,String databaseName){
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

        MetadataUtils mdUtils = new MetadataUtils();
        //todo try to put tables into the either server based on some requirements

        File table = new File(SCHEMA+"/"+databaseName+"/"+tableName+".txt");

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

    public void insertRow(String inputQuery){
        List<String[]> line=new ArrayList<>();
        Matcher queryMatcher = INSERT_TABLE_PATTERN.matcher(inputQuery);
        MetadataUtils mdUtils = new MetadataUtils();
        String tablePath = "";

        if(queryMatcher.find()){
            String tableName = queryMatcher.group(1);
            tablePath = mdUtils.getTablePath(tableName);
        }
        try(BufferedReader br=new BufferedReader(new FileReader(tablePath))){
            br.readLine();
            String columnNamesLine=br.readLine();
            List<String> columns = Arrays.asList(columnNamesLine.split(PRIMARY_DELIMITER_REGEX));
            List<String> columnNames = new ArrayList<>();

            for(String column:columns){
                columnNames.add(column.split(",")[0]);
            }

            int i=0;
            boolean increase=false;
                increase=true;
                List<String> currentCol=Arrays.asList(queryMatcher.group(2).split(","));
                String[] currentVal=queryMatcher.group(3).split(",");
                line.add(new String[currentVal.length]);
                int vIndex=0;
                for(String col:currentCol) {
                    int index=columnNames.indexOf(col.trim());
                    if(index!=-1) {
                        line.get(i)[index]= currentVal[vIndex++];
                    }else {
                        System.out.println("Column Name Mismatch.");
                        line.remove(line.size()-1);
                        increase = false;
                        break;
                    }
                }
            if(increase) {
                i++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(tablePath,true))){
            for(String[] list:line) {
                List<String> alist=Arrays.asList(list);
               bw.append("\n");
                bw.append(alist.toString().substring(1,alist.toString().length()-1).replace(",",PRIMARY_DELIMITER));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
