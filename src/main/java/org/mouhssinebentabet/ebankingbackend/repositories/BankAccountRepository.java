package org.mouhssinebentabet.ebankingbackend.repositories;

import org.mouhssinebentabet.ebankingbackend.entities.AccountOperation;
import org.mouhssinebentabet.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    List<BankAccount> findBankAccountsByCustomer_Id(Long customer_id);
}
