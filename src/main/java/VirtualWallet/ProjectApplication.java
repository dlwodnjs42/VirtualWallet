package VirtualWallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories("VirtualWallet/Repositories")
@EntityScan("VirtualWallet/model")
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}


}


//TODO: SYCNHRONIZE SUCH THAT IT WORKS CONCURRENTLY
//TODO: DOCUMENT PUBLIC INTERFACES
//TODO: TESTING CHECK AGAIN IF HISTORY OF LAST N TRANSACTIONS

