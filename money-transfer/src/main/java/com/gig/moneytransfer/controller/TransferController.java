package com.gig.moneytransfer.controller;

import com.gig.moneytransfer.model.Account;
import com.gig.moneytransfer.model.Transfer;
import com.gig.moneytransfer.model.TransferRequest;
import com.gig.moneytransfer.repository.AccountRepository;
import com.gig.moneytransfer.repository.TransferRepository;
import com.gig.moneytransfer.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransferController {

    private final TransferService transferService;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferController(
            TransferService transferService,
            AccountRepository accountRepository,
            TransferRepository transferRepository
    ) {
        this.transferService = transferService;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    //Endpoint to perform a money transfer. Returns success or error message.
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        try {
            transferService.transfer(request.fromAccountId, request.toAccountId, request.amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error: " + e.getMessage());
        }
    }

    // Returns the list of all accounts with their balances.
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    //Returns the list of all successful transfers.
    @GetMapping("/transfers")
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}

