package org.mouhssinebentabet.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue("SA")

@Data @NoArgsConstructor @AllArgsConstructor
public class SavingAccount extends  BankAccount{
    private double interestRate;
}
