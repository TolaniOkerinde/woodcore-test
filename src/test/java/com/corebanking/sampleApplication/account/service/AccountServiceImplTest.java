package com.corebanking.sampleApplication.account.service;

import com.corebanking.sampleApplication.account.config.Properties;
import com.corebanking.sampleApplication.account.domain.Account;
import com.corebanking.sampleApplication.account.domain.AccountRepository;
import com.corebanking.sampleApplication.account.domain.Customer;
import com.corebanking.sampleApplication.account.domain.CustomerRepository;
import com.corebanking.sampleApplication.account.dto.AccountDetailsResponseDto;
import com.corebanking.sampleApplication.account.dto.BaseResponse;
import com.corebanking.sampleApplication.account.dto.CreateAccountRequestDto;
import com.corebanking.sampleApplication.account.dto.CreateCustomerResponseDto;
import com.corebanking.sampleApplication.account.dto.CustomerDetails;
import com.corebanking.sampleApplication.account.dto.UpdateAccountRequestDto;
import com.corebanking.sampleApplication.account.exceptions.AccountNumberNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Properties properties;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Customer customer;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void testCreateCustomerAccount() {
        // Mock input data
        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setFirstname("John");
        createAccountRequestDto.setLastname("Doe");
        createAccountRequestDto.setPhoneNumber("1234567890");
        createAccountRequestDto.setAccountType("savings");

        // Mock customer data
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstname("John");
        mockCustomer.setLastname("Doe");
        mockCustomer.setPhoneNumber("08084445555");
        mockCustomer.setCreatedDate(Instant.now());

        // Mock account data
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setAccountNumber("12345678");
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("active");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountType("savings");

        when(customerRepository.save(any())).thenReturn(mockCustomer);

        CreateCustomerResponseDto result = accountService.createCustomerAccount(createAccountRequestDto);

        assertEquals("Successful!", result.getDefaultMessage());
        assertEquals("200", result.getStatus());
        assertEquals(1L, result.getCustomerId());
        assertNotNull(result.getAccountNumber());
    }


    @Test
    void testRetrieveAccountDetails_Success() {
        String accountNumber = "12345678";

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstname("John");
        mockCustomer.setLastname("Doe");
        mockCustomer.setPhoneNumber("1234567890");
        mockCustomer.setCreatedDate(Instant.now());

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("active");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountType("savings");

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);

        AccountDetailsResponseDto result = accountService.retrieveAccountDetails(accountNumber);

        assertNotNull(result);
        assertEquals("Successful!", result.getDefaultMessage());
        assertEquals("200", result.getStatus());

        CustomerDetails customerDetails = result.getCustomer();
        assertNotNull(customerDetails);
        assertEquals("John", customerDetails.getFirstName());
        assertEquals("Doe", customerDetails.getLastName());
        assertEquals("1234567890", customerDetails.getPhoneNumber());

        assertNotNull(result.getAccount());
        assertEquals(accountNumber, result.getAccount().getAccountNumber());
        assertEquals("active", result.getAccount().getStatus());
        verify(accountRepository, times(1)).findByAccountNumber(any(String.class));
    }


    @Test
    void testRetrieveAccountDetails_AccountNotFound() throws AccountNumberNotFoundException {
        String accountNumber = "nonexistent";

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        AccountDetailsResponseDto result = accountService.retrieveAccountDetails(accountNumber);

        // Assertions
        assertNotNull(result);
        assertEquals("Error processing request", result.getDefaultMessage());
        assertEquals("400", result.getStatus());
    }


    @Test
    void testUpdateAccountDetails_Success() {
        // Mock input data
        String accountNumber = "12345678";
        UpdateAccountRequestDto updateRequestDto = new UpdateAccountRequestDto();
        updateRequestDto.setAccountType("Savings");
        updateRequestDto.setFirstName("Tolani");
        updateRequestDto.setLastName("Okerinde");
        updateRequestDto.setPhoneNumber("08085449999");

        // Mock customer data
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstname("John");
        mockCustomer.setLastname("Doe");
        mockCustomer.setPhoneNumber("1234567890");
        mockCustomer.setCreatedDate(Instant.now());

        // Mock account data
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("active");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountType("savings");

        // Mock repository behavior
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

        // Perform the test
        BaseResponse result = accountService.updateAccountDetails(accountNumber, updateRequestDto);

        // Assertions
        assertNotNull(result);
        assertEquals("Successful!", result.getDefaultMessage());
        assertEquals("200", result.getStatus());

        // Check if the account is updated with the new values
        assertEquals("Savings", mockAccount.getAccountType());
        assertEquals("Tolani", mockAccount.getCustomer().getFirstname());
        assertEquals("Okerinde", mockAccount.getCustomer().getLastname());
        assertEquals("08085449999", mockAccount.getCustomer().getPhoneNumber());
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void testUpdateAccountDetails_AccountNotFound() throws AccountNumberNotFoundException {
        String accountNumber = "nonexistent";
        UpdateAccountRequestDto updateRequestDto = new UpdateAccountRequestDto();
        updateRequestDto.setStatus("newStatus");

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        BaseResponse result = accountService.updateAccountDetails(accountNumber, updateRequestDto);

        assertNotNull(result);
        assertEquals("Error processing request", result.getDefaultMessage());
        assertEquals("404", result.getStatus());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testCloseAccount_Success() {
        String accountNumber = "12345678";

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstname("John");
        mockCustomer.setLastname("Doe");
        mockCustomer.setPhoneNumber("1234567890");
        mockCustomer.setCreatedDate(Instant.now());

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setDisplayname("John Doe");
        mockAccount.setStatus("active");
        mockAccount.setCreatedDate(Instant.now());
        mockAccount.setAccountType("savings");

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

        BaseResponse result = accountService.closeAccount(accountNumber, "command");

        assertNotNull(result);
        assertEquals("Successful!", result.getDefaultMessage());
        assertEquals("200", result.getStatus());

        // Check if the account is closed
        assertEquals(properties.getClosedStatus(), mockAccount.getStatus());
        assertNotNull(mockAccount.getClosedOnDate());
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void testCloseAccount_AccountNotFound() throws AccountNumberNotFoundException {
        String accountNumber = "nonexistent";

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        BaseResponse result = accountService.closeAccount(accountNumber, "command");

        assertNotNull(result);
        assertEquals("Error processing request", result.getDefaultMessage());
        assertEquals("404", result.getStatus());
        verify(accountRepository, never()).save(any());
    }
}