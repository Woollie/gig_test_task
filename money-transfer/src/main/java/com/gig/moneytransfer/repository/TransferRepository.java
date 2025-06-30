package com.gig.moneytransfer.repository;

import com.gig.moneytransfer.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransferRepository extends JpaRepository<Transfer, Long> {}
