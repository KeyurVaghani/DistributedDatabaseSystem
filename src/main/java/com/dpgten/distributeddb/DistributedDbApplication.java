package com.dpgten.distributeddb;

import com.dpgten.distributeddb.query.QueryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedDbApplication.class, args);

		QueryImpl implement = new QueryImpl();
		implement.executeQuery();
	}

}
