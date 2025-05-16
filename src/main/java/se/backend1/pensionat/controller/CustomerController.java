package se.backend1.pensionat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.backend1.pensionat.service.CustomerService;


/**
 * Gjorde om denna, den va skriven som om vi har REST api, men vi kör ju på Thymeleaf
 * vi behöver inte ResponseEntity eller RequestBody, det behövs när vi skickar Json vilket vi inte gör
 * vi ska returnera html sidor, hur man gör vet jag ännu inte :(
 */


@Controller
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String getAllCustomers() {
    return null;
    }


    @PostMapping("/create")
    public String createCustomer() {
    return null;
    }

    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
     return null;
    }

    // Här måste tror jag getCustomerById() vara
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return null;
    }
}
