package fr.univcotedazur.simpletcfs.components;

import fr.univcotedazur.simpletcfs.entities.Customer;
import fr.univcotedazur.simpletcfs.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.simpletcfs.interfaces.CustomerFinder;
import fr.univcotedazur.simpletcfs.interfaces.CustomerRegistration;
import fr.univcotedazur.simpletcfs.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerRegistry implements CustomerRegistration, CustomerFinder {

    private CustomerRepository customerRepository;

    @Autowired // annotation is optional since Spring 4.3 if component has only one constructor
    public CustomerRegistry(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer register(String name, String creditCard)
            throws AlreadyExistingCustomerException {
        if (findByName(name).isPresent())
            throw new AlreadyExistingCustomerException(name);
        Customer newcustomer = new Customer(name, creditCard);
        return customerRepository.save(newcustomer);
    }

    @Override
    public Optional<Customer> findByName(String name) {
        return customerRepository.findCustomerByName(name);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

}

