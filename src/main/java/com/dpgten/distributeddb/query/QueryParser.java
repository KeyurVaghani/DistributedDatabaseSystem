package com.dpgten.distributeddb.query;

import java.util.regex.Pattern;

public class QueryParser {
    public static String USE_DATABASE = "USE\\s(\\w+);";
    public static final Pattern USE_DATABASE_PATTERN = Pattern.compile(USE_DATABASE);

    public static String CREATE_DATABASE = "CREATE\\s(\\w+);";
    public static final Pattern CREATE_DATABASE_PATTERN = Pattern.compile(CREATE_DATABASE);

    public static String CREATE_TABLE = "CREATE\\s+TABLE\\s+(\\w+)\\s*\\(((?:\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\);";
    public static final Pattern CREATE_TABLE_PATTERN = Pattern.compile(CREATE_TABLE);

    public static String SELECT_TABLE = "SELECT\\s((\\*)?((\\w+)?((,(\\w+))*)?))" +
            "\\sFROM\\s(\\w+)\\s*(where\\s+(\\w+)\\s+=\\s+(\\w+))*;";
    public static final Pattern SELECT_TABLE_PATTERN = Pattern.compile(SELECT_TABLE);
}

