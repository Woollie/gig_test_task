package com.gig.moneytransfer.service;

import com.gig.moneytransfer.model.Account;
import com.gig.moneytransfer.model.Transfer;
import com.gig.moneytransfer.repository.AccountRepository;
import com.gig.moneytransfer.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    /**
     * Performs a money transfer between two accounts.
     * Only successful transfers are saved in the database.
     */
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // Validation: amount must be > 0
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // Validation: cannot transfer to self
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Fetch accounts
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("From account not found"));
        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("To account not found"));

        // Check balance
        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Perform balance update
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        // Record successful transfer
        Transfer transfer = new Transfer();
        transfer.setFromAccount(from);
        transfer.setToAccount(to);
        transfer.setAmount(amount);

        transferRepository.save(transfer);
    }
}
