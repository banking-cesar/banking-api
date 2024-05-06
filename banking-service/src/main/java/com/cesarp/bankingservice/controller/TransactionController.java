package com.cesarp.bankingservice.controller;

import com.cesarp.bankingservice.model.dto.TransactionReportDTO;
import com.cesarp.bankingservice.model.dto.TransactionRequestDTO;
import com.cesarp.bankingservice.model.dto.TransactionResponseDTO;
import com.cesarp.bankingservice.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Validated @RequestBody TransactionRequestDTO transactionDTO) {
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @PostMapping("/report")
    public ResponseEntity<List<TransactionReportDTO>> generateTransactionReport(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam("accountNumber") String accountNumber) {
        List<TransactionReportDTO> reportDTOs = transactionService.generateTransactionReport(startDateStr, endDateStr, accountNumber);
        return new ResponseEntity<>(reportDTOs, HttpStatus.OK);
    }
}
