package org.mouhssinebentabet.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mouhssinebentabet.ebankingbackend.entities.BankAccount;
import org.mouhssinebentabet.ebankingbackend.entities.CurrentAccount;
import org.mouhssinebentabet.ebankingbackend.entities.Customer;
import org.mouhssinebentabet.ebankingbackend.entities.SavingAccount;
import org.mouhssinebentabet.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.mouhssinebentabet.ebankingbackend.exceptions.CustomerNotFoundException;
import org.mouhssinebentabet.ebankingbackend.repositories.AccountOperationRepository;
import org.mouhssinebentabet.ebankingbackend.repositories.BankAccountRepository;
import org.mouhssinebentabet.ebankingbackend.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j //Logger log= LoggerFactory.getLogger(this.getClass().getName());

public class BankAccountServiceImp implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

   /* public BankAccountServiceImp(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }*/
    //Logger log= LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving new !Customer");
        Customer savedCustomer= customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount savecuurentBankAccount(double initialBalance, double overdraft, Long customerid) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerid).orElse(null);

        if (customer==null)
            throw new CustomerNotFoundException("customer not found");

        CurrentAccount currentAccount =new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);

        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerid).orElse(null);

        if (customer==null)
            throw new CustomerNotFoundException("customer not found");

        SavingAccount savingAccount =new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);

        return savedBankAccount;
    }


    @Override
    public List<Customer> lisCustomers() {

        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountid) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountid)
                .orElseThrow(()->new BankAccountNotFoundException("Bank"));

        return null;
    }

    @Override
    public void debit(String accountid, double amount, String description) {

    }

    @Override
    public void credit(String accountid, double amount, String description) {

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) {

    }
}
