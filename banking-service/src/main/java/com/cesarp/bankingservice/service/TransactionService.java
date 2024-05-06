package com.cesarp.bankingservice.service;

import com.cesarp.bankingservice.exception.BadRequestException;
import com.cesarp.bankingservice.exception.ResourceNotFoundException;
import com.cesarp.bankingservice.mapper.TransactionMapper;
import com.cesarp.bankingservice.model.dto.TransactionReportDTO;
import com.cesarp.bankingservice.model.dto.TransactionRequestDTO;
import com.cesarp.bankingservice.model.dto.TransactionResponseDTO;
import com.cesarp.bankingservice.model.entity.Account;
import com.cesarp.bankingservice.model.entity.Transaction;
import com.cesarp.bankingservice.repository.AccountRepository;
import com.cesarp.bankingservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findBySourceOrTargetAccountNumber(accountNumber);
        return transactionMapper.convertToListDTO(transactions);
    }

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO) {
        Account sourceAccount = accountRepository.findByAccountNumber(transactionDTO.getSourceAccountNumber())
                .orElseThrow(()-> new ResourceNotFoundException("La cuenta de origen no existe"));
        Account targetAccount = accountRepository.findByAccountNumber(transactionDTO.getTargetAccountNumber())
                .orElseThrow(()-> new ResourceNotFoundException("La cuenta de origen no existe"));

        BigDecimal amount = transactionDTO.getAmount();
        BigDecimal sourceAccountBalance = sourceAccount.getBalance();
        if (sourceAccountBalance.compareTo(amount) < 0){
            throw new BadRequestException("Saldo insuficiente en la cuenta origen");
        }

        Transaction transaction = transactionMapper.convertToEntity(transactionDTO);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction = transactionRepository.save(transaction);

        BigDecimal newSourceAccountBalance = sourceAccountBalance.subtract(amount);
        BigDecimal targetAccountBalance = targetAccount.getBalance().add(amount);

        sourceAccount.setBalance(newSourceAccountBalance);
        targetAccount.setBalance(targetAccountBalance);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return transactionMapper.convertToDTO(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionReportDTO> generateTransactionReport(String startDateStr,
                                                                String endDateStr,
                                                                String accountNumber) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        List<Object[]> transactionCounts = transactionRepository.getTransactionCountByDateRangeAndAccountNumber(startDate, endDate, accountNumber);
        return transactionCounts.stream()
                .map(transactionMapper::convertTransactionReportDTO)
                .toList();
    }
}
