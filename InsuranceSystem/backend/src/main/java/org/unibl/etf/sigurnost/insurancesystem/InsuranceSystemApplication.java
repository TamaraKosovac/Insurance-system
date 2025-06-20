package org.unibl.etf.sigurnost.insurancesystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaAuditing
@SpringBootApplication
public class InsuranceSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(InsuranceSystemApplication.class, args);
    }
    
}
