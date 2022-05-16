package org.mouhssinebentabet.ebankingbackend.repositories;

import org.mouhssinebentabet.ebankingbackend.entities.AccountOperation;
import org.mouhssinebentabet.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
