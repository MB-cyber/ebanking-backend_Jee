package org.mouhssinebentabet.ebankingbackend.services;

import org.mouhssinebentabet.ebankingbackend.dtos.*;
import org.mouhssinebentabet.ebankingbackend.entities.BankAccount;
import org.mouhssinebentabet.ebankingbackend.entities.CurrentAccount;
import org.mouhssinebentabet.ebankingbackend.entities.Customer;
import org.mouhssinebentabet.ebankingbackend.entities.SavingAccount;
import org.mouhssinebentabet.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.mouhssinebentabet.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.mouhssinebentabet.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);
    CurrentBankAccountDTO savecuurentBankAccount(double initialBalance, double overdraft, Long customerid) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException;

    List<CustomerDTO> lisCustomers();
    BankAccountDTO getBankAccount(String accountid) throws BankAccountNotFoundException;
    void debit(String accountid, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountid, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
