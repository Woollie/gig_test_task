package com.gig.moneytransfer;

import com.gig.moneytransfer.model.Account;
import com.gig.moneytransfer.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class MoneyTransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferApplication.class, args);
    }

    // Executes when app starts (for this test task purposes)
    @Bean
    CommandLineRunner seedDatabase(AccountRepository accountRepository) {
        return args -> {
            if (accountRepository.count() == 0) {
                System.out.println("Seeding initial accounts...");
                accountRepository.save(new Account("Milly", new BigDecimal("100")));
                accountRepository.save(new Account("Billy", new BigDecimal("200")));
                accountRepository.save(new Account("Charlie", new BigDecimal("50")));
            }
        };
    }
}
