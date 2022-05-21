package org.mouhssinebentabet.ebankingbackend;

import org.mouhssinebentabet.ebankingbackend.entities.*;
import org.mouhssinebentabet.ebankingbackend.enums.AccountStatus;
import org.mouhssinebentabet.ebankingbackend.enums.OperationType;
import org.mouhssinebentabet.ebankingbackend.repositories.AccountOperationRepository;
import org.mouhssinebentabet.ebankingbackend.repositories.BankAccountRepository;
import org.mouhssinebentabet.ebankingbackend.repositories.CustomerRepository;
import org.mouhssinebentabet.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

  //  @Bean
   CommandLineRunner commandLineRunner(BankService bankService){
        return args -> {
            bankService.consulter();


        };

   }
    @Bean
    CommandLineRunner start(CustomerRepository customerRepository ,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("hassan","Yassine","bentabet").forEach(name->{
                Customer customer =new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentaccount =new CurrentAccount();
                currentaccount.setId(UUID.randomUUID().toString());
                currentaccount.setBalance(Math.random()*9);
                currentaccount.setCreatedAt(new Date());
                currentaccount.setStatus(AccountStatus.CREATED);
                currentaccount.setCustomer(cust);
                currentaccount.setOverDraft(9000);
                bankAccountRepository.save(currentaccount);

                SavingAccount savingAccount =new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());

                savingAccount.setBalance(Math.random()*9);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }






            });



            };

    }
}
