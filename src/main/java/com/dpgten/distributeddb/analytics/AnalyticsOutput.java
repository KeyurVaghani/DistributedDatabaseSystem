package com.dpgten.distributeddb.analytics;

public class AnalyticsOutput {
    public AnalyticsOutput(){
        System.out.println("user abc submitted "+  DatabaseAnalytics.TOTAL_QUERY_COUNT+ "queries for DB1 running on Virtual Machine 1");
        System.out.println("user abc updated "+  DatabaseAnalytics.UPDATE_QUERY_COUNT+ "queries for DB1 running on Virtual Machine 1");
        System.out.println("user abc create "+  DatabaseAnalytics.CREATE_QUERY_COUNT+ "queries for DB1 running on Virtual Machine 1");
        System.out.println("user abc used select "+  DatabaseAnalytics.SELECT_QUERY_COUNT+ "queries for DB1 running on Virtual Machine 1");
        System.out.println("user abc used insert "+  DatabaseAnalytics.INSERT_QUERY_COUNT+ "queries for DB1 running on Virtual Machine 1");
        System.out.println("user abc used database "+  DatabaseAnalytics.DATABASE_QUERY_COUNT+ "queries for DB1 running on Virtual Machine 1");

    }
}
