package be.uantwerpen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


import javax.validation.Valid;

/**
 * Created by dries on 19/04/2017.
 */

@Controller
public class cardController extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/testPage").setViewName("testPage");
    }

    @GetMapping("/")
    public String showForm(orderInput order_input) {
        return "reception";
    }
    @PostMapping("/")
    public String importOrder(@Valid orderInput order_input, BindingResult bindingResult) {

        // Check inputdata. Aslong as it isn't correct, remain on reception page
        if (bindingResult.hasErrors()) {
            return "reception";
        }

        // valid input has been submitted, redirect to next page (currently temporary page, needs to be fixed later on
        //TODO: redirect to correct page on client side!
        return "redirect:/testPage";
    }

 /*
    //Old code. keeping for now, but will have to be cleaned up at a later stage
    @RequestMapping({"/"})
    public String showHomepage(final ModelMap model)
    {
        return "reception";
    }

    @RequestMapping({"/hello"})
    public String showHello(final ModelMap model)
    {
        return "testPage";
    }
    @RequestMapping({"/lol"})
    public String lol(final ModelMap model)
    {
        return "lol";
    }
*/
}
