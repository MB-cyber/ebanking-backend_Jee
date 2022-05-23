package org.mouhssinebentabet.ebankingbackend.dtos;

import lombok.Data;
import org.mouhssinebentabet.ebankingbackend.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
}