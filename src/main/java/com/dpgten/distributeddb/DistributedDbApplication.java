package com.dpgten.distributeddb;

import com.dpgten.distributeddb.sqldump.SqlDump;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DistributedDbApplication {

	public static void main(String[] args) throws IOException {
		SqlDump sq= new SqlDump();
		sq.readFile();

		SpringApplication.run(DistributedDbApplication.class, args);
	}

}
