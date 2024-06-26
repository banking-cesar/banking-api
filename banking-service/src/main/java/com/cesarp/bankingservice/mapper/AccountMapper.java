package com.cesarp.bankingservice.mapper;

import com.cesarp.bankingservice.model.dto.AccountRequestDTO;
import com.cesarp.bankingservice.model.dto.AccountResponseDTO;
import com.cesarp.bankingservice.model.entity.Account;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AccountMapper {

    private final ModelMapper modelMapper;

    public Account convertToEntity(AccountRequestDTO accountRequestDTO) {
        return modelMapper.map(accountRequestDTO, Account.class);
    }

    public AccountResponseDTO convertToDTO(Account account) {
        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public List<AccountResponseDTO> convertToListDTO(List<Account> accounts) {
        return accounts.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
