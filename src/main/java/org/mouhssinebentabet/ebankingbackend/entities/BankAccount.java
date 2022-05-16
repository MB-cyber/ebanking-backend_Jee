package org.mouhssinebentabet.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mouhssinebentabet.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy  = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4,discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor @AllArgsConstructor

public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus Status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy ="bankAccount" )
    private List<AccountOperation> accountOperations;

}
