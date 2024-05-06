package com.cesarp.bankingservice.service;

import com.cesarp.bankingservice.exception.ResourceNotFoundException;
import com.cesarp.bankingservice.mapper.AccountMapper;
import com.cesarp.bankingservice.model.dto.AccountRequestDTO;
import com.cesarp.bankingservice.model.dto.AccountResponseDTO;
import com.cesarp.bankingservice.model.entity.Account;
import com.cesarp.bankingservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accountMapper.convertToListDTO(accounts);
    }

    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con el numero: " + id));
        return accountMapper.convertToDTO(account);
    }

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        Account account = accountMapper.convertToEntity(accountRequestDTO);
        account.setCreateAt(LocalDate.now());
        Account savedAccount = accountRepository.save(account);
        return accountMapper.convertToDTO(savedAccount);
    }

    @Transactional
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO accountRequestDTO) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con el numero: " + id));
        account.setAccountNumber(accountRequestDTO.getAccountNumber());
        account.setBalance(accountRequestDTO.getBalance());
        account.setOwnerName(accountRequestDTO.getOwnerName());
        account.setOwnerEmail(accountRequestDTO.getOwnerEmail());
        account.setCreateAt(LocalDate.now());

        account = accountRepository.save(account);

        return accountMapper.convertToDTO(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
