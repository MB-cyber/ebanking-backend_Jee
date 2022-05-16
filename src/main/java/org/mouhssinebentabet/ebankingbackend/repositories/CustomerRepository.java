package org.mouhssinebentabet.ebankingbackend.repositories;

import org.mouhssinebentabet.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
