package be.uantwerpen.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.internal.Throwables;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Map;

@Controller
public class MyErrorController implements ErrorController {
    private static final Logger logger = LogManager.getLogger(MyErrorController.class);

    private ErrorAttributes errorAttributes;
    private final static String ERROR_PATH = "/error";


    /**
     * Controller for the error controller
     * @param errorAttributes
     */
    public MyErrorController( ErrorAttributes errorAttributes ) {
        this.errorAttributes = errorAttributes;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    @RequestMapping( value= ERROR_PATH, produces = "txt/html" )
    public ModelAndView errorHtml( HttpServletRequest request) {
        logger.error(getErrorAttributes( request, false ));
        ModelAndView mv = new ModelAndView("redirect:/");
        mv.addObject("errorStatus",getErrorAttributes( request, false ).get("status"));
        mv.addObject("errorMsg", getErrorAttributes( request, false ).get("error"));
        return mv;


    }




    /**
     * Returns the path of the error page
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }



    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
