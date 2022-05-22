package org.mouhssinebentabet.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mouhssinebentabet.ebankingbackend.dtos.CustomerDTO;
import org.mouhssinebentabet.ebankingbackend.entities.Customer;
import org.mouhssinebentabet.ebankingbackend.exceptions.CustomerNotFoundException;
import org.mouhssinebentabet.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return  bankAccountService.lisCustomers();
    }
    @GetMapping("/customer/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
    return  bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
    return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO){

        customerDTO.setId(id);

        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
    bankAccountService.deleteCustomer(id);
    }



}
