package com.dpgten.distributeddb.query;

import com.dpgten.distributeddb.utils.MetadataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;

import static com.dpgten.distributeddb.query.QueryParser.*;
import static com.dpgten.distributeddb.utils.Utils.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseQuery {
    String databasePath;
    String databaseName;

    public void createDatabase(String inputQuery) {
        Matcher createQueryMatcher = CREATE_DATABASE_PATTERN.matcher(inputQuery);

        if (createQueryMatcher.find()) {
            this.databaseName = createQueryMatcher.group(1);

            //todo decide in which server need to create the database
            File database = new File(SCHEMA + "/" + databaseName);

            MetadataUtils mdUtils = new MetadataUtils();
            if (!mdUtils.isDatabaseExist(databaseName)) {
                database.mkdir();
            } else {
                System.out.println("database " + databaseName + " already exists");
            }
        }
    }

    public boolean isDeleteQuery(String inputQuery) {
        Matcher queryMatcher = DELETE_QUERY_PATTERN.matcher(inputQuery);
        return queryMatcher.find();
    }

    public String selectDatabase(String selectDb) {
        Matcher useQueryMatcher = USE_DATABASE_PATTERN.matcher(selectDb);
        String databaseName;

        if (useQueryMatcher.find()) {
            databaseName = useQueryMatcher.group(1);
            if (Arrays.stream(KEYWORDS).allMatch(databaseName.toLowerCase(Locale.ROOT)::equals)) {
                System.out.println(RED + "CANNOT USE PREDEFINED KEYWORDS" + RESET);
                return "";
            }
        } else {
            return "";
        }
        this.databasePath = searchDatabase(SCHEMA, databaseName);
        if (databasePath.equals("")) {
            return "";
        }
        return databaseName;
    }

    public String searchDatabase(String currentUser, String currentDatabase) {
        String[] databases = new File(currentUser).list();
        if (databases != null) {
            for (String database : databases) {
                if (database.equals(currentDatabase)) {
                    return currentUser + "/" + currentDatabase;
                }
            }
        }
        return "";
    }

    public String searchUser(File server, String currentUser) {
        String[] users = server.list();
        if (users != null) {
            for (String user : users) {
                if (user.equals(currentUser)) {
                    return server + "/" + user;
                }
            }
        }
        return "";
    }
}
