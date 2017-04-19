package be.uantwerpen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dries on 19/04/2017.
 */

@Controller
public class cardController {
    @RequestMapping({"/"})
    public String showHomepage(final ModelMap model)
            {
                return "testPage";
            }
}
