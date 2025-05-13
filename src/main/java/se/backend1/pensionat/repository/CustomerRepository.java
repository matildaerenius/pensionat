package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.backend1.pensionat.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> { // Tillgång till Crud-metoder mot SQL.DB

                //Mer specifika metoder utanför JpaRepository
    List<Customer> findByLastName(String lastName);
    List<Customer> findByFirstName(String firstName);
    List<Customer> findByLastNameAndFirstName(String lastName, String firstName);


}
