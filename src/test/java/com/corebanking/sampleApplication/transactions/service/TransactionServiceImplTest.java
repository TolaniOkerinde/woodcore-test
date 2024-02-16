package com.corebanking.sampleApplication.transactions.service;

import com.corebanking.sampleApplication.account.config.Properties;
import com.corebanking.sampleApplication.account.domain.Account;
import com.corebanking.sampleApplication.account.domain.AccountRepository;
import com.corebanking.sampleApplication.account.domain.Customer;
import com.corebanking.sampleApplication.transactions.domain.Transaction;
import com.corebanking.sampleApplication.transactions.domain.TransactionRepository;
import com.corebanking.sampleApplication.transactions.dto.DebitRequestDto;
import com.corebanking.sampleApplication.transactions.dto.DepositRequestDto;
import com.corebanking.sampleApplication.transactions.dto.DepositResponseDto;
import com.corebanking.sampleApplication.transactions.dto.TransferMoneyRequestDto;
import com.corebanking.sampleApplication.transactions.dto.WithdrawMoneyRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Properties properties;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testDepositMoney_Success() {
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setTransactionAmount(BigDecimal.TEN);
        depositRequestDto.setNotes("Test deposit");
        depositRequestDto.setReceiverAccountNumber("12345678");
        depositRequestDto.setSenderName("John Doe");

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstname("John");
        mockCustomer.setLastname("Doe");
        mockCustomer.setPhoneNumber("1234567890");
        mockCustomer.setCreatedDate(Instant.now());

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setAccountNumber(depositRequestDto.getReceiverAccountNumber());
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("Active");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountType("savings");
        mockAccount.setAccountBalance(BigDecimal.ZERO);


        when(accountRepository.findByAccountNumber(depositRequestDto.getReceiverAccountNumber())).thenReturn(mockAccount);
        when(properties.getActiveStatus()).thenReturn("Active");
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        DepositResponseDto result = transactionService.depositMoney(depositRequestDto);

        assertNotNull(result);
        assertEquals("Successful!", result.getDefaultMessage());
        assertEquals("200", result.getStatus());
        assertNotNull(result.getTransactionId());
    }

    @Test
    void testDepositMoney_AccountNotFound() {
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setTransactionAmount(BigDecimal.TEN);
        depositRequestDto.setNotes("Test deposit");
        depositRequestDto.setReceiverAccountNumber("nonexistent");
        depositRequestDto.setSenderName("John Doe");

        when(accountRepository.findByAccountNumber(depositRequestDto.getReceiverAccountNumber())).thenReturn(null);

        DepositResponseDto result = transactionService.depositMoney(depositRequestDto);

        assertNotNull(result);
        assertEquals("Error processing request", result.getDefaultMessage());
        assertEquals("400", result.getStatus());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testDepositMoney_AccountInactive() {
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setTransactionAmount(BigDecimal.TEN);
        depositRequestDto.setNotes("Test deposit");
        depositRequestDto.setReceiverAccountNumber("12345678");
        depositRequestDto.setSenderName("John Doe");

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstname("John");
        mockCustomer.setLastname("Doe");
        mockCustomer.setPhoneNumber("1234567890");
        mockCustomer.setCreatedDate(Instant.now());

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setAccountNumber(depositRequestDto.getReceiverAccountNumber());
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("inactive");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountType("savings");
        mockAccount.setAccountBalance(BigDecimal.ZERO);

        when(accountRepository.findByAccountNumber(depositRequestDto.getReceiverAccountNumber())).thenReturn(mockAccount);

        DepositResponseDto result = transactionService.depositMoney(depositRequestDto);

        assertNotNull(result);
        assertEquals("Error processing request", result.getDefaultMessage());
        assertEquals("400", result.getStatus());
    }

    @Test
    void testDepositMoney_FailedTransaction() {
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setTransactionAmount(BigDecimal.TEN);
        depositRequestDto.setNotes("Test deposit");
        depositRequestDto.setReceiverAccountNumber("12345678");
        depositRequestDto.setSenderName("John Doe");


        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstname("John");
        mockCustomer.setLastname("Doe");
        mockCustomer.setPhoneNumber("1234567890");
        mockCustomer.setCreatedDate(Instant.now());

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setAccountNumber(depositRequestDto.getReceiverAccountNumber());
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("active");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountType("savings");
        mockAccount.setAccountBalance(BigDecimal.ZERO);

        when(accountRepository.findByAccountNumber(depositRequestDto.getReceiverAccountNumber())).thenReturn(mockAccount);

        DepositResponseDto result = transactionService.depositMoney(depositRequestDto);

        assertNotNull(result);
        assertEquals("Error processing request", result.getDefaultMessage());
        assertEquals("400", result.getStatus());
    }

    @Test
    public void testWithdrawMoney() {
        when(properties.getActiveStatus()).thenReturn("ACTIVE");
        when(properties.getDebit()).thenReturn("DEBIT");

        WithdrawMoneyRequestDto requestDto = new WithdrawMoneyRequestDto();
        requestDto.setAccountNumber("12345678");
        requestDto.setTransactionAmount(BigDecimal.TEN);
        requestDto.setNotes("Test withdrawal");

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber(requestDto.getAccountNumber());
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("ACTIVE");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountBalance(BigDecimal.valueOf(100.0));

        when(accountRepository.findByAccountNumber(requestDto.getAccountNumber())).thenReturn(mockAccount);

        DepositResponseDto responseDto = transactionService.withdrawMoney(requestDto);

        assertEquals("Successful!", responseDto.getDefaultMessage());
        assertEquals(String.valueOf(HttpStatus.OK.value()), responseDto.getStatus());

        verify(accountRepository, times(1)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransferMoney() {
        when(properties.getActiveStatus()).thenReturn("ACTIVE");
        when(properties.getDebit()).thenReturn("DEBIT");

        TransferMoneyRequestDto requestDto = new TransferMoneyRequestDto();
        requestDto.setSenderAccountNumber("12345678");
        requestDto.setReceiverAccountNumber("87654321");
        requestDto.setTransactionAmount(BigDecimal.TEN);
        requestDto.setNotes("Test transfer");

        Account mockSenderAccount = new Account();
        mockSenderAccount.setId(1L);
        mockSenderAccount.setAccountNumber(requestDto.getSenderAccountNumber());
        mockSenderAccount.setDisplayname("John Doe");
        mockSenderAccount.setStatus("ACTIVE");
        mockSenderAccount.setCreatedDate(Instant.now());
        mockSenderAccount.setAccountBalance(BigDecimal.valueOf(100.0));

        Account mockReceiverAccount = new Account();
        mockReceiverAccount.setId(2L);
        mockReceiverAccount.setAccountNumber(requestDto.getReceiverAccountNumber());
        mockReceiverAccount.setDisplayname("Jane Doe");
        mockReceiverAccount.setStatus("ACTIVE");
        mockReceiverAccount.setCreatedDate(Instant.now());
        mockReceiverAccount.setAccountBalance(BigDecimal.valueOf(50.0));

        when(accountRepository.findByAccountNumber(requestDto.getSenderAccountNumber())).thenReturn(mockSenderAccount);
        when(accountRepository.findByAccountNumber(requestDto.getReceiverAccountNumber())).thenReturn(mockReceiverAccount);

        DepositResponseDto responseDto = transactionService.transferMoney(requestDto);

        assertEquals("Successful!", responseDto.getDefaultMessage());
        assertEquals(String.valueOf(HttpStatus.OK.value()), responseDto.getStatus());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testDebit() {
        when(properties.getActiveStatus()).thenReturn("ACTIVE");

        DebitRequestDto requestDto = new DebitRequestDto();
        requestDto.setAccountNumber("12345678");
        requestDto.setAmount(BigDecimal.TEN);

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber(requestDto.getAccountNumber());
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("ACTIVE");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountBalance(BigDecimal.valueOf(100.0));

        when(accountRepository.findByAccountNumber(requestDto.getAccountNumber())).thenReturn(mockAccount);

        String result = transactionService.debit(requestDto);

        assertEquals("Successful!", result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }


    @Test
    public void testCredit() {
        when(properties.getActiveStatus()).thenReturn("ACTIVE");

        DebitRequestDto requestDto = new DebitRequestDto();
        requestDto.setAccountNumber("12345678");
        requestDto.setAmount(BigDecimal.TEN);

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber(requestDto.getAccountNumber());
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("ACTIVE");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountBalance(BigDecimal.valueOf(100.0));

        when(accountRepository.findByAccountNumber(requestDto.getAccountNumber())).thenReturn(mockAccount);

        String result = transactionService.credit(requestDto);

        assertEquals("Successful!", result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}