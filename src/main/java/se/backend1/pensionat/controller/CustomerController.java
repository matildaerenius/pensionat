package se.backend1.pensionat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.exception.CustomerNotFoundException;
import se.backend1.pensionat.service.CustomerService;

@Controller
@RequiredArgsConstructor //detta gör att vi kan ta bort Autowired o slipper göra konstruktorer
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;


    /**
     * Visar en lista med alla kunder på sidan customers/list.html, OBS: ändra till rätt om vi inte kör just den html
     */
    @GetMapping
    public String getAllCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers/list";
    }

    /**
     * Tar emot formuläret från "Skapa kund"-sidan.
     * Om allt är giltigt sparas kunden, annars visas formuläret med felmeddelanden.
     * OBS: ändra html till rätt sedan när thymeleaf är gjord
     */
    @PostMapping("/create")
    public String createCustomer(@ModelAttribute("customerDto") @Valid CustomerDto customerDto, BindingResult result) {
        if (result.hasErrors()) return "customers/create";
        customerService.createCustomer(customerDto);
        return "redirect:/customers";
    }

    /**
     * Tar emot det ifyllda formuläret för att uppdatera en befintlig kund.
     * Om det finns valideringsfel visas formuläret igen.
     */
    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute("customerDto") @Valid CustomerDto customerDto, BindingResult result) {
        if (result.hasErrors()) return "customers/edit";
        customerService.updateCustomer(id, customerDto);
        return "redirect:/customers";
    }

    /**
     * Tar bort en kund om det inte finns några bokningar kopplade till den.
     * Om kunden har bokningar eller inte hittas visas ett felmeddelande.
     */
    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
        } catch (CustomerHasBookingsException e) {
            redirectAttributes.addFlashAttribute("error", "Kunden har aktiva bokningar och kan inte tas bort");
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Kunden kunde inte hittas");
        }
        return "redirect:/customers";
    }

    /**
     * Visar redigeringsformulär för en specifik kund baserat på ID
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        model.addAttribute("customer", customerDto);
        return "customers/edit";
    }

    /**
     * Visar formuläret för att skapa en ny kund (create.html), OBS: ändra html till rätt sedan när thymeleaf är gjord
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
        return "customers/create";
    }
}
