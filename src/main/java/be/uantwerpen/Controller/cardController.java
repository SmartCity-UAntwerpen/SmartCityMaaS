package be.uantwerpen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dries on 19/04/2017.
 */

@Controller
public class cardController {
    @RequestMapping({"/test"})
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

}
