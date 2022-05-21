package org.mouhssinebentabet.ebankingbackend.services;

import org.mouhssinebentabet.ebankingbackend.entities.BankAccount;
import org.mouhssinebentabet.ebankingbackend.entities.CurrentAccount;
import org.mouhssinebentabet.ebankingbackend.entities.Customer;
import org.mouhssinebentabet.ebankingbackend.entities.SavingAccount;
import org.mouhssinebentabet.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.mouhssinebentabet.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount savecuurentBankAccount(double initialBalance, double overdraft, Long customerid) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException;

    List<Customer> lisCustomers();
    BankAccount getBankAccount(String accountid) throws BankAccountNotFoundException;
    void debit(String accountid, double amount, String description);
    void credit(String accountid, double amount, String description);
    void transfer(String accountIdSource, String accountIdDestination, double amount);
}
