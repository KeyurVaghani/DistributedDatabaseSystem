package com.dpgten.distributeddb.query;

import java.util.regex.Pattern;

public class QueryParser {
    public static String USE_DATABASE = "USE\\s(\\w+);";
    public static final Pattern USE_DATABASE_PATTERN = Pattern.compile(USE_DATABASE);

    public static String CREATE_DATABASE = "CREATE\\s(\\w+);";
    public static final Pattern CREATE_DATABASE_PATTERN = Pattern.compile(CREATE_DATABASE);

    public static String CREATE_TABLE = "CREATE\\s+TABLE\\s+(\\w+)\\s*\\(((?:\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\);";
    public static final Pattern CREATE_TABLE_PATTERN = Pattern.compile(CREATE_TABLE);

    public static String SELECT_TABLE = "SELECT\\s+((\\*)?((\\w+)?((,(\\w+))*)?))\\s+FROM\\s+(\\w+)" +
            "(\\s+WHERE\\s+(\\w+)\\s+=\\s+(\\w+))*;";
    public static final Pattern SELECT_TABLE_PATTERN = Pattern.compile(SELECT_TABLE);

    public static String INSERT_TABLE = "INSERT\\sINTO\\s(\\w+)\\s\\(([\\s\\S]+)\\)\\sVALUES\\s\\(([\\s\\S]+)\\);";
    public static final Pattern INSERT_TABLE_PATTERN = Pattern.compile(INSERT_TABLE);

    public static String SELECT_TABLE_WHERE = "SELECT\\s+((\\*)?((\\w+)?((,(\\w+))*)?))\\s+FROM\\s+(\\w+)";
    public static final Pattern SELECT_TABLE_WHERE_PATTERN = Pattern.compile(SELECT_TABLE_WHERE);

    public static String WHERE_CONDITION = "WHERE\\s+(\\w+)\\s+=\\s+(\\w+);";
    public static final Pattern WHERE_CONDITION_PATTERN = Pattern.compile(WHERE_CONDITION);
}

