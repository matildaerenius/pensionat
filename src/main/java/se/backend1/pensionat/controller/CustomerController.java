package se.backend1.pensionat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public String getAllCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers/list";
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute("customerDto") @Valid CustomerDto customerDto, BindingResult result) {
        if (result.hasErrors()) return "customers/form";
        customerService.createCustomer(customerDto);
        return "redirect:/customers";
    }

    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute("customerDto") @Valid CustomerDto customerDto, BindingResult result) {
        if (result.hasErrors()) return "customers/edit";
        customerService.updateCustomer(id, customerDto);
        return "redirect:/customers";
    }

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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("customerDto", customerService.getCustomerById(id));
        model.addAttribute("edit", true);
        return "customers/form";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
        model.addAttribute("edit", false);
        return "customers/form";
    }
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("customerDto") CustomerDto customerDto) {
        customerService.save(customerDto);
        return "redirect:/customers";
    }
}
