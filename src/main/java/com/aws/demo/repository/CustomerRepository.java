package com.aws.demo.repository;


import com.aws.demo.domain.Customer;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    List<Customer> findAll();

    @Query(value = "delete from customer where id=?0")
    void deleteById(String id);

}
