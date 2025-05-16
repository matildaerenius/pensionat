package se.backend1.pensionat.webbController;
import org.springframework.ui.Model;
//import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.service.CustomerService;

@Controller
@RequestMapping
public class CustumerWebController {

    private CustomerService customerService;

    public CustumerWebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomer(Model model) {
        var all = customerService.getAllCustomers();
        model.addAttribute("customer", all);
        return "customer";
    }
    @GetMapping("/ny")
    public String showForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";

    }
    @PostMapping
    public String createCustomer(@ModelAttribute("customer") Customer customer) {}
}
