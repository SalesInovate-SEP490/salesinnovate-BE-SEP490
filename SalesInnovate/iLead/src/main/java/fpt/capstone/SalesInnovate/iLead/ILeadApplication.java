package fpt.capstone.SalesInnovate.iLead;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ILeadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ILeadApplication.class, args);
	}

}
