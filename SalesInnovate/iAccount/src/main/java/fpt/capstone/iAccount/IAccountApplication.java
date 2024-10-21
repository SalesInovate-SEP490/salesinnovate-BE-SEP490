package fpt.capstone.iAccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(IAccountApplication.class, args);
	}

}
