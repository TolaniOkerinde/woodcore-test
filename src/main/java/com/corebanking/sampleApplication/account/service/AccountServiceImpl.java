package com.corebanking.sampleApplication.account.service;

import com.corebanking.sampleApplication.account.config.Properties;
import com.corebanking.sampleApplication.account.domain.Account;
import com.corebanking.sampleApplication.account.domain.AccountRepository;
import com.corebanking.sampleApplication.account.domain.Customer;
import com.corebanking.sampleApplication.account.domain.CustomerRepository;
import com.corebanking.sampleApplication.account.dto.AccountDetails;
import com.corebanking.sampleApplication.account.dto.AccountDetailsResponseDto;
import com.corebanking.sampleApplication.account.dto.BaseResponse;
import com.corebanking.sampleApplication.account.dto.CreateAccountRequestDto;
import com.corebanking.sampleApplication.account.dto.CreateCustomerResponseDto;
import com.corebanking.sampleApplication.account.dto.CustomerDetails;
import com.corebanking.sampleApplication.account.dto.UpdateAccountRequestDto;
import com.corebanking.sampleApplication.account.exceptions.AccountNumberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CustomerRepository customerAccountRepository;
    private final Properties properties;
    private final AccountRepository accountRepository;
    final String errorMessage = "Error processing request";
    final String sucessMessage = "Successful!";

    @Override
    @Transactional
    public CreateCustomerResponseDto createCustomerAccount(CreateAccountRequestDto createAccountRequestDto) {
        CreateCustomerResponseDto customerResponseDto = new CreateCustomerResponseDto();
        customerResponseDto.setStatus(HttpStatus.BAD_REQUEST.toString());
        customerResponseDto.setDefaultMessage(errorMessage);
        try {
            customerResponseDto.setDefaultMessage(errorMessage);
            customerResponseDto.setStatus(HttpStatus.BAD_REQUEST.toString());
            Customer customerAccount = new Customer();
            customerAccount.setFirstname(createAccountRequestDto.getFirstname());
            customerAccount.setLastname(createAccountRequestDto.getLastname());
            customerAccount.setCreatedDate(Instant.now());
            customerAccount.setPhoneNumber(createAccountRequestDto.getPhoneNumber());
            Customer customerDetails = customerAccountRepository.save(customerAccount);
            Account account = new Account();
            account.setCustomer(customerDetails);
            account.setAccountNumber(generateAccountNumber());
            String displayName = createAccountRequestDto.getFirstname() + " " + createAccountRequestDto.getLastname();
            account.setDisplayname(displayName);
            account.setStatus(properties.getActiveStatus());
            account.setCreatedDate(Instant.now());
            account.setAccountType(createAccountRequestDto.getAccountType());
            accountRepository.save(account);
            customerResponseDto.setCustomerId(customerDetails.getId());
            customerResponseDto.setAccountNumber(account.getAccountNumber());
            customerResponseDto.setStatus(String.valueOf(HttpStatus.OK.value()));
            customerResponseDto.setDefaultMessage(sucessMessage);
            return customerResponseDto;
        } catch (Exception e) {
            log.error("System failure when creating customer account :: {}", e.getMessage());
            return customerResponseDto;
        }

    }

    @Override
    public AccountDetailsResponseDto retrieveAccountDetails(String accountNumber) {
        AccountDetailsResponseDto accountDetailsResponseDto = new AccountDetailsResponseDto();
        accountDetailsResponseDto.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        accountDetailsResponseDto.setDefaultMessage(errorMessage);
        try {
            Account findAccount = accountRepository.findByAccountNumber(accountNumber);
            if (findAccount == null) {
                throw new AccountNumberNotFoundException("AccountNumber Invalid");
            }

            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setAccountBalance(findAccount.getAccountBalance());
            accountDetails.setAccountNumber(accountNumber);
            accountDetails.setAccountType(findAccount.getAccountType());
            if (findAccount.getClosedOnDate().isPresent()) {
                accountDetails.setClosedOnDate(findAccount.getClosedOnDate().get());

            }
            accountDetails.setStatus(findAccount.getStatus());
            accountDetails.setDisplayName(findAccount.getDisplayname());
            CustomerDetails customerDetails = new CustomerDetails();
            customerDetails.setCreatedDate(findAccount.getCreatedDate().get());
            customerDetails.setFirstName(findAccount.getCustomer().getFirstname());
            customerDetails.setLastName(findAccount.getCustomer().getLastname());
            customerDetails.setPhoneNumber(findAccount.getCustomer().getPhoneNumber());
            accountDetailsResponseDto.setStatus(String.valueOf(HttpStatus.OK.value()));
            accountDetailsResponseDto.setDefaultMessage(sucessMessage);
            accountDetailsResponseDto.setAccount(accountDetails);
            accountDetailsResponseDto.setCustomer(customerDetails);
            return accountDetailsResponseDto;
        } catch (Exception e) {
            log.error("System failure when retrieving customer account :: {}", e.getMessage());
            return accountDetailsResponseDto;
        }
    }

    @Override
    public BaseResponse updateAccountDetails(String accountNumber, UpdateAccountRequestDto updateAccountRequestDto) {
        BaseResponse response = new BaseResponse();
        response.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.setDefaultMessage(errorMessage);
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber);
            if (account == null) {
                throw new AccountNumberNotFoundException("AccountNumber Invalid");
            }
            if (updateAccountRequestDto.getStatus() != null) {
                account.setStatus(updateAccountRequestDto.getStatus());
            }
            if (updateAccountRequestDto.getAccountType() != null) {
                account.setAccountType(updateAccountRequestDto.getAccountType());
            }
            if (updateAccountRequestDto.getFirstName() != null) {
                account.getCustomer().setFirstname(updateAccountRequestDto.getFirstName());
            }
            if (updateAccountRequestDto.getLastName() != null) {
                account.getCustomer().setLastname(updateAccountRequestDto.getLastName());
            }
            if (updateAccountRequestDto.getPhoneNumber() != null) {
                account.getCustomer().setPhoneNumber(updateAccountRequestDto.getPhoneNumber());
            }
            accountRepository.save(account);
            response.setStatus(String.valueOf(HttpStatus.OK.value()));
            response.setDefaultMessage(sucessMessage);
            return response;
        } catch (Exception e) {
            log.error("System failure when updating customer account :: {}", e.getMessage());
            return response;
        }

    }

    @Override
    public BaseResponse closeAccount(String accountNumber, String command) {
        BaseResponse response = new BaseResponse();
        response.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.setDefaultMessage(errorMessage);
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber);
            if (account == null) {
                throw new AccountNumberNotFoundException("AccountNumber Invalid");
            }
            account.setStatus(properties.getClosedStatus());
            accountRepository.save(account);

            response.setStatus(String.valueOf(HttpStatus.OK.value()));
            response.setDefaultMessage(sucessMessage);
            return response;
        } catch (Exception e) {
            log.error("System failure when closing customer account :: {}", e.getMessage());
            return response;
        }

    }


    public String generateAccountNumber() {
        StringBuilder accountNumberBuilder = new StringBuilder("00");
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            accountNumberBuilder.append(digit);
        }
        return accountNumberBuilder.toString();
    }

}
