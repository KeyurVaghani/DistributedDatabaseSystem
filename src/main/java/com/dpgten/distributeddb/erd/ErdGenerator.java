package com.dpgten.distributeddb.erd;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import static com.dpgten.distributeddb.utils.Utils.RESET;
import static com.dpgten.distributeddb.utils.Utils.YELLOW;

public class ErdGenerator {
    private final String schema = "src\\main\\resources\\schema\\";
    private final String primarykey = "Sno";
    private final String delimeter1 = "\\|";
    private final String DELIMETER_COMMA = ",";
    private final String delimeter2 = "~";
    private final String NEW_LINE = "\n";
    private final String SEMI_COLON = ";";
    public Scanner scanner;


    public void generateRequiredERD() {
        Scanner sc = new Scanner(System.in);
        System.out.print(YELLOW + "Enter a existing database name:: " + RESET);
        String selectedDatabase = sc.nextLine();
        File file = new File(schema + "\\" + selectedDatabase);
        StringBuffer finalStringBuffer = new StringBuffer();
        if (file.exists()) {
            File[] tableList = file.listFiles();
            if (tableList.length != 0) {
                for (File table : tableList) {
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(table));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String tableName = table.getName().replace(".txt", "");
                    stringBuffer.append("TABLE NAME : ").append(tableName).append(NEW_LINE);
                    String line = "";
                    List<String> columnList = null;
                    String[] columnArray = null;
                    while (true) {
                        try {
                            if (!((line = reader.readLine()) != null)) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (line.startsWith("Column")) {
                            columnArray = line.split(Pattern.quote("|"));
                        }
                    }
                    String[] dataType = new String[columnArray.length];
                    for (int i = 0; i < dataType.length; i++) {
                        dataType[i] = (String.format("%s", columnArray[i]));
                    }
                    String columns = String.join(", ", Arrays.copyOfRange(dataType, 1, dataType.length));
                    stringBuffer.append("COLUMNS in table : ").append(columns).append(NEW_LINE);
                    finalStringBuffer.append(stringBuffer.toString()).append(NEW_LINE);
                }
                finalStringBuffer.append("RelationShip between tables : ").append(NEW_LINE);
                for (File table : tableList) {
                    StringBuffer realtionBuffer = new StringBuffer();
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(table));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String line = null;
                    String[] foreignKey = null;
                    List<String> foreignKeyList = new ArrayList<>();
                    while (true) {
                        try {
                            if (!((line = reader.readLine()) != null)) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (line.startsWith("foreign_key")) {
                            foreignKey = line.split(Pattern.quote("|"));
                            List<String> relation = new LinkedList<>(Arrays.asList(foreignKey));
                            relation.remove(0);
                            List<String> finalRealtion = null;
                            for (String s : relation) {
                                String[] arr = s.split(",");
                                finalRealtion = new LinkedList<>(Arrays.asList(arr));
                            }
                            for (String relTale : finalRealtion) {
                                String fkTable = relTale.split(Pattern.quote("->"))[0].split(Pattern.quote("$$"))[1];
                                foreignKeyList.add(fkTable);
                            }
                        }
                    }

                    if (!foreignKeyList.isEmpty()) {
                        realtionBuffer.append(table.getName().replace(".txt", ""));
                        realtionBuffer.append(foreignKeyList.get(0));
                        realtionBuffer.append(" one to many relationship -  ").append(",");
                        for (int i = 0; i < foreignKeyList.size(); i++) {
                            realtionBuffer.append(foreignKeyList.get(i)).append(",");

                        }
                        finalStringBuffer.append(realtionBuffer).append(NEW_LINE);
                    }
                }
                try {
                    writeToFile(finalStringBuffer, selectedDatabase);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Please Create The Database First As The Database Does Not Exist");
        }
    }

    private void writeToFile(StringBuffer finalStringBuffer, String selectedDatabase) throws IOException {
        String generatedErdLocation = "src\\main\\resources\\ERD";
        File file = new File(generatedErdLocation);
        if (!file.exists()) {
            file.mkdir();
        }
        File dumpPath = new File(generatedErdLocation + "\\" + selectedDatabase + ".txt");
        dumpPath.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dumpPath, true));
        bufferedWriter.append(finalStringBuffer.toString());
        bufferedWriter.flush();

    }
}
