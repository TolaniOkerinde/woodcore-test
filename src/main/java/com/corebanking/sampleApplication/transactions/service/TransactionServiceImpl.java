package com.corebanking.sampleApplication.transactions.service;

import com.corebanking.sampleApplication.account.config.Properties;
import com.corebanking.sampleApplication.account.domain.Account;
import com.corebanking.sampleApplication.account.domain.AccountRepository;
import com.corebanking.sampleApplication.account.exceptions.AccountNumberNotFoundException;
import com.corebanking.sampleApplication.transactions.domain.Transaction;
import com.corebanking.sampleApplication.transactions.domain.TransactionRepository;
import com.corebanking.sampleApplication.transactions.dto.DebitRequestDto;
import com.corebanking.sampleApplication.transactions.dto.DepositRequestDto;
import com.corebanking.sampleApplication.transactions.dto.WithdrawMoneyRequestDto;
import com.corebanking.sampleApplication.transactions.dto.DepositResponseDto;
import com.corebanking.sampleApplication.transactions.dto.TransferMoneyRequestDto;
import com.corebanking.sampleApplication.transactions.exception.AccountNotInActiveStateException;
import com.corebanking.sampleApplication.transactions.exception.FailedTransactionException;
import com.corebanking.sampleApplication.transactions.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final Properties properties;
    private final TransactionRepository transactionRepository;
    final String errorMessage = "Error processing request";
    final String sucessMessage = "Successful!";


    @Override
    @Transactional
    public DepositResponseDto depositMoney(DepositRequestDto depositMoneyRequestDto) {
        DepositResponseDto depositResponseDto = new DepositResponseDto();
        depositResponseDto.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        depositResponseDto.setDefaultMessage(errorMessage);
        try {
            Account accountDetails = accountRepository.findByAccountNumber(depositMoneyRequestDto.getReceiverAccountNumber());
            if (accountDetails == null) {
                throw new AccountNumberNotFoundException("Invalid account Number");
            }
            DebitRequestDto debitRequestDto = new DebitRequestDto();
            debitRequestDto.setAmount(depositMoneyRequestDto.getTransactionAmount());
            debitRequestDto.setAccountNumber(depositMoneyRequestDto.getReceiverAccountNumber());
            credit(debitRequestDto);

            Transaction transaction = new Transaction();
            transaction.setAmount(depositMoneyRequestDto.getTransactionAmount());
            transaction.setTransactionType(properties.getCredit());
            transaction.setToAccount(depositMoneyRequestDto.getReceiverAccountNumber());
            transaction.setSenderName(depositMoneyRequestDto.getSenderName());
            transaction.setReceiverName(accountDetails.getDisplayname());
            transaction.setTransactionReference(generateTransactionRef());
            transaction.setNote(depositMoneyRequestDto.getNotes());
            transaction.setCreatedDate(Instant.now());
            Transaction savedTransaction = transactionRepository.save(transaction);
            depositResponseDto.setStatus(String.valueOf(HttpStatus.OK.value()));
            depositResponseDto.setDefaultMessage(sucessMessage);
            depositResponseDto.setTransactionId(savedTransaction.getId());
            return depositResponseDto;
        } catch (Exception e) {
            log.error("System failure when depositing money :: {}", e.getMessage());
            return depositResponseDto;
        }

    }

    @Override
    public DepositResponseDto withdrawMoney(WithdrawMoneyRequestDto depositMoneyRequestDto) {
        DepositResponseDto depositResponseDto = new DepositResponseDto();
        depositResponseDto.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        depositResponseDto.setDefaultMessage(errorMessage);
        try {
            Account accountDetails = accountRepository.findByAccountNumber(depositMoneyRequestDto.getAccountNumber());
            if (accountDetails == null) {
                throw new AccountNumberNotFoundException("Invalid account Number");
            }
            DebitRequestDto debitRequestDto = new DebitRequestDto();
            debitRequestDto.setAmount(depositMoneyRequestDto.getTransactionAmount());
            debitRequestDto.setAccountNumber(depositMoneyRequestDto.getAccountNumber());
            debit(debitRequestDto);
            Transaction transaction = new Transaction();
            transaction.setAmount(depositMoneyRequestDto.getTransactionAmount());
            transaction.setTransactionType(properties.getDebit());
            transaction.setFromAccount(depositMoneyRequestDto.getAccountNumber());
            transaction.setTransactionReference(generateTransactionRef());
            transaction.setNote(depositMoneyRequestDto.getNotes());
            transaction.setCreatedDate(Instant.now());
            Transaction savedTransaction = transactionRepository.save(transaction);
            depositResponseDto.setStatus(String.valueOf(HttpStatus.OK.value()));
            depositResponseDto.setDefaultMessage(sucessMessage);
            depositResponseDto.setTransactionId(savedTransaction.getId());
            return depositResponseDto;
        } catch (Exception e) {
            log.error("System failure when closing withdrawing money :: {}", e.getMessage());
            return depositResponseDto;
        }
    }

    @Override
    public DepositResponseDto transferMoney(TransferMoneyRequestDto transferMoneyRequestDto) {
        DepositResponseDto depositResponseDto = new DepositResponseDto();
        depositResponseDto.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        depositResponseDto.setDefaultMessage(errorMessage);
        try {
            Account sendersAccount = accountRepository.findByAccountNumber(transferMoneyRequestDto.getSenderAccountNumber());
            if (sendersAccount == null) {
                throw new AccountNumberNotFoundException("Invalid account Number");
            }
            Account receiversAccount = accountRepository.findByAccountNumber(transferMoneyRequestDto.getReceiverAccountNumber());
            if (receiversAccount == null) {
                throw new AccountNumberNotFoundException("Invalid account Number");
            }
            DebitRequestDto debitRequestDto = new DebitRequestDto();
            debitRequestDto.setAmount(transferMoneyRequestDto.getTransactionAmount());
            debitRequestDto.setAccountNumber(transferMoneyRequestDto.getSenderAccountNumber());
            debit(debitRequestDto);
            debitRequestDto.setAccountNumber(transferMoneyRequestDto.getReceiverAccountNumber());
            credit(debitRequestDto);
            Transaction transaction = new Transaction();
            transaction.setAmount(transferMoneyRequestDto.getTransactionAmount());
            transaction.setTransactionType(properties.getDebit());
            transaction.setFromAccount(transferMoneyRequestDto.getSenderAccountNumber());
            transaction.setToAccount(transferMoneyRequestDto.getReceiverAccountNumber());
            transaction.setReceiverName(receiversAccount.getDisplayname());
            transaction.setSenderName(sendersAccount.getDisplayname());
            transaction.setTransactionReference(generateTransactionRef());
            transaction.setNote(transferMoneyRequestDto.getNotes());
            transaction.setCreatedDate(Instant.now());
            Transaction savedTransaction = transactionRepository.save(transaction);
            depositResponseDto.setStatus(String.valueOf(HttpStatus.OK.value()));
            depositResponseDto.setDefaultMessage(sucessMessage);
            depositResponseDto.setTransactionId(savedTransaction.getId());
            return depositResponseDto;
        } catch (Exception e) {
            log.error("System failure when closing withdrawing money :: {}", e.getMessage());
            return depositResponseDto;
        }
    }


    public String debit(DebitRequestDto debitRequestDto) {
        try {
            Account findByAccount = accountRepository.findByAccountNumber(debitRequestDto.getAccountNumber());
            BigDecimal currentBalance = findByAccount.getAccountBalance();
            BigDecimal debitAmount = debitRequestDto.getAmount();

            if (!Objects.equals(findByAccount.getStatus(), properties.getActiveStatus())) {
                throw new AccountNotInActiveStateException("Account Inactive");
            }

            if (findByAccount.getAccountBalance().compareTo(debitRequestDto.getAmount()) < 0) {
                throw new InsufficientBalanceException("InsufficientBalance");
            }
            BigDecimal newBalance = currentBalance.subtract(debitAmount);
            log.info("new account balance is >>>>>>{}", newBalance);
            findByAccount.setAccountBalance(newBalance);
            accountRepository.save(findByAccount);
            return sucessMessage;
        } catch (Exception ex) {
            throw new FailedTransactionException("failedTransaction");
        }
    }

    public String credit(DebitRequestDto debitRequestDto) {

        try {
            Account findByAccount = accountRepository.findByAccountNumber(debitRequestDto.getAccountNumber());
            BigDecimal currentBalance = findByAccount.getAccountBalance();
            BigDecimal debitAmount = debitRequestDto.getAmount();

            if (!Objects.equals(findByAccount.getStatus(), properties.getActiveStatus())) {
                throw new AccountNotInActiveStateException("Account Inactive");
            }
            BigDecimal newBalance = currentBalance.add(debitAmount);
            log.info("new account balance is >>>>>>{}", newBalance);
            findByAccount.setAccountBalance(newBalance);
            accountRepository.save(findByAccount);
            String infoMessage = "Successful!";
            return infoMessage;
        } catch (Exception ex) {
            throw new FailedTransactionException("failedTransaction");
        }

    }

    private String generateTransactionRef() {
        String ALLOWED_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVQXYZ";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            builder.append(
                    ALLOWED_CHARACTERS.charAt((int) (Math.random() * ALLOWED_CHARACTERS.length())));
        }
        return builder.toString();
    }

}
