package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.utils.FileResourceUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;

import static com.dpgten.distributeddb.utils.Utils.*;
import static com.dpgten.distributeddb.query.QueryParser.*;

public class DatabaseQuery {
    String currentUser;
    File server1;
    File server2;
    String database1Path;
    String database2Path;

    public DatabaseQuery(String server1,String server2,String currentUser){
        this.server1 = new File(server1);
        this.server2 = new File(server2);
        this.currentUser = currentUser;
    }

    public boolean isUseQuery(String inputQuery){
        Matcher queryMatcher = USE_DATABASE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isCreateTableQuery(String inputQuery){
        Matcher queryMatcher = CREATE_TABLE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isCreateQuery(String inputQuery){
        Matcher queryMatcher = CREATE_DATABASE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isSelectQuery(String inputQuery){
        Matcher queryMatcher = SELECT_TABLE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isInsertQuery(String inputQuery){
        Matcher queryMatcher = INSERT_TABLE_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public boolean isWhereCondition(String inputQuery){
        Matcher queryMatcher = WHERE_CONDITION_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public void createDatabase(String inputQuery){

        Matcher createQueryMatcher = CREATE_DATABASE_PATTERN.matcher(inputQuery);
        String databaseName = "";

        if(createQueryMatcher.find()){
            databaseName = createQueryMatcher.group(1);
            File database1 = new File(server1+"/"+databaseName);
            File database2 = new File(server2+"/"+databaseName);
            if(!database1.exists() && !database2.exists()){
                database1.mkdir();
                database2.mkdir();
            }else {
                System.out.println("database "+databaseName+" already exists");
            }
        }else {
            System.out.println("Please enter valid query");
        }
    }

    public void selectRows(String inputQuery){
        Matcher selectRowsMatcher = SELECT_TABLE_PATTERN.matcher(inputQuery);
        String tableName = "";
        String tablePath = "";
        List<String> selectRows = new ArrayList<>();

        if(selectRowsMatcher.find()){
            tableName = selectRowsMatcher.group(8);
            tablePath = SERVERS+"/"+findTableInDatabase(tableName);
        }

        File tableFile = new File(tablePath+"/"+tableName+".txt");

        if(!selectRowsMatcher.group(9).isEmpty()){
            String columnName = selectRowsMatcher.group(10);
            String columnValue = selectRowsMatcher.group(11);
            selectRows = executeWhere(tableFile,columnName,columnValue);
        }
    }

    public List<String> executeWhere(File tableFile, String columnName
            , String columnValue){
        List<String> selectRows = new ArrayList<>();
        try {
            Scanner tableScanner = new Scanner(tableFile);
            tableScanner.nextLine();
            int columnIndex = -1;
            String columnHeader = tableScanner.nextLine();
            String[] headers = columnHeader.split("\\|");
            List<String> headerArray = new ArrayList<>();
            for(String header: headers){
                headerArray.add(header.split(",")[0]);
            }
            columnIndex = headerArray.indexOf(columnName);

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

    public String findTableInDatabase(String tableName){
        File metaData = new File(GLOBAL_METADATA);
        String server = "";

        try {
            Scanner fileScanner = new Scanner(metaData);
            while(fileScanner.hasNext()){
                String row = fileScanner.next();
                   if(Arrays.asList(row.split("\\|")).contains(tableName)){
                       server = row.split("\\|")[0] + "/" + row.split("\\|")[1];
                   }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return server;
    }

    public void insertRow(String inputQuery){
        List<String[]> line=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(database1Path+"/student.txt"))){
            br.readLine();
            String columnNamesLine=br.readLine();
            List<String> columns = Arrays.asList(columnNamesLine.split("\\|"));
            List<String> columnNames = new ArrayList<>();

            for(String column:columns){
                columnNames.add(column.split(",")[0]);
            }

            int i=0;
            boolean increase=false;
                Matcher queryMatcher = INSERT_TABLE_PATTERN.matcher(inputQuery);
                while (queryMatcher.find()) {
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
                }
                if(increase) {
                    i++;
                }
            } catch (IOException ex) {
            ex.printStackTrace();
        }
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(database1Path+"/student.txt",true))){
            for(String[] list:line) {
                List<String> alist=Arrays.asList(list);
                System.out.println(alist.toString().substring(1,alist.toString().length()-1));
                bw.append("\n");
                bw.append(alist.toString().substring(1,alist.toString().length()-1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public boolean selectRows(String tableName){
//        File database1 = new File(database1Path);
//        File database2 = new File(database2Path);
//
//        String[] tables1 = database1.list();
//        String[] tables2 = database2.list();
//
//        if(tables1 != null) {
//            for (String table : tables1) {
//                if(table.equals(tableName)){
//                    return true;
//                }
//            }
//        }
//
//        if(tables2 != null) {
//            for (String table : tables2) {
//                if(table.equals(tableName)){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public boolean isTableExists(String tableName){

        return false;
    }

    public String selectDatabase(String selectDb){
        File server1 = new File(SERVER_1);
        File server2 = new File(SERVER_2);

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
        this.database2Path = searchDatabase(SERVER_1,databaseName);
        if(database2Path.equals("")){
            System.out.println("Database does not exists");
            return databaseName;
        }
//        }else {
//            System.out.println("User does not exists");
//        }

//        if(!currentUserPath2.equals("")){
        this.database1Path = searchDatabase(SERVER_2,databaseName);
        if(database1Path.equals("")){
            System.out.println("Database does not exits");
        }
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

    public String getDatabase1Path(){
        return this.database1Path;
    }

    public String getDatabase2Path(){
        return this.database2Path;
    }
}
