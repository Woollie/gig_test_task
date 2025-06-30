package com.gig.moneytransfer.repository;

import com.gig.moneytransfer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {}

