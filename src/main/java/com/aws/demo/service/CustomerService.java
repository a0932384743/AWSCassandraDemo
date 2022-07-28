package com.aws.demo.service;

import com.aws.demo.domain.Customer;
import com.aws.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CassandraTemplate cassandraTemplate;

    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    public Object insertCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteAllCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        customerList.forEach(c -> {
            cassandraTemplate.delete(c);
        });
    }
}
