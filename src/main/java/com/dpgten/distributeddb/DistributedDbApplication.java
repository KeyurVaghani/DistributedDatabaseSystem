package com.dpgten.distributeddb;

import com.dpgten.distributeddb.query.QueryImpl;
import com.dpgten.distributeddb.userauthentication.LoginMenu;
import com.dpgten.distributeddb.userauthentication.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedDbApplication.class, args);

//		QueryImpl implement = new QueryImpl();
//		User user = new User("karthik","kanna","","","","db1");
//		implement.executeQuery(user);

		LoginMenu lm = new LoginMenu();
        lm.userFirstMenu();
	}

}
