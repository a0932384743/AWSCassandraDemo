package com.aws.demo.controller;


import com.aws.demo.domain.Customer;
import com.aws.demo.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public ResponseEntity findAllCustomer() {
        return ResponseEntity.ok(customerService.findAllCustomer());
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAllCustomer() {
        try {
            customerService.deleteAllCustomer();
            return ResponseEntity.ok("Delete Success");

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
            return ResponseEntity.ok("Delete Error");

        }
    }

    @PostMapping("/insert")
    public ResponseEntity insertCustomer(@RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(customerService.insertCustomer(customer));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
            return ResponseEntity.ok("Delete Error");
        }
    }
}
