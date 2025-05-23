package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.backend1.pensionat.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long> {}
