package org.mouhssinebentabet.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mouhssinebentabet.ebankingbackend.dtos.*;
import org.mouhssinebentabet.ebankingbackend.entities.*;
import org.mouhssinebentabet.ebankingbackend.enums.OperationType;
import org.mouhssinebentabet.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.mouhssinebentabet.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.mouhssinebentabet.ebankingbackend.exceptions.CustomerNotFoundException;
import org.mouhssinebentabet.ebankingbackend.mappers.BankAccountMapperImpl;
import org.mouhssinebentabet.ebankingbackend.repositories.AccountOperationRepository;
import org.mouhssinebentabet.ebankingbackend.repositories.BankAccountRepository;
import org.mouhssinebentabet.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j //Logger log= LoggerFactory.getLogger(this.getClass().getName());

public class BankAccountServiceImp implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtomapper;

   /* public BankAccountServiceImp(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }*/
    //Logger log= LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new !Customer");
        Customer customer=dtomapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtomapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO savecuurentBankAccount(double initialBalance, double overdraft, Long customerid) throws CustomerNotFoundException {
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

        return dtomapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException {
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

        return dtomapper.fromSavingBankAccount(savedBankAccount);
    }


    @Override
    public List<CustomerDTO> lisCustomers() {

        List<Customer>  customers=customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(cust -> dtomapper.fromCustomer(cust))
                .collect(Collectors.toList());

        /*List<CustomerDTO> customerDTOS= new ArrayList<>();
        for (Customer customer: customers){

            CustomerDTO customerDTO=dtomapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
*/
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountid) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountid)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    if (bankAccount instanceof SavingAccount  ){
        SavingAccount savingAccount= (SavingAccount) bankAccount;
        return dtomapper.fromSavingBankAccount(savingAccount);
    }else{
        CurrentAccount currentAccount= (CurrentAccount) bankAccount;
        return dtomapper.fromCurrentBankAccount(currentAccount);

    }

    }

    @Override
    public void debit(String accountid, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount=bankAccountRepository.findById(accountid)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));

        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("balance not suffiicient");

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountid, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountid)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
            debit(accountIdSource,amount,"transfer");
            credit(accountIdDestination,amount,"transfer from"+accountIdSource);
    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
      List<BankAccount> bankAccounts= bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtomapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtomapper.fromCurrentBankAccount(currentAccount);

            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("customer not found"));
        return dtomapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new !Customer");
        Customer customer=dtomapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtomapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->dtomapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtomapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }



}
