/*package be.uantwerpen.controller;


import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice(basePackages = "be.uantwerpen.controller")
public class GlobablControllerAdvice {

    @InitBinder
    public void dataBinding(WebDataBinder binder ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        dateFormat.setLenient( false );
        binder.registerCustomEditor( Date.class, "dob", new CustomDateEditor( dateFormat, true ));
    }


    @ModelAttribute
    public void globalAttributes(Model model) {
        model.addAttribute("msg", "Welcome to My World!");
    }


    /*@ExceptionHandler( FileNotFoundException.class )
    public ModelAndView myError( Exception exception ) {
        System.out.println("Error rip");
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception );
        mav.setViewName("error");
        return mav;
    }*/


   /* @ExceptionHandler( value= Exception.class )
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e ) throws Exception {
        System.out.println("Errorkeeeuh :'(");

        if( AnnotationUtils.findAnnotation( e.getClass(), ResponseStatus.class ) != null ) throw e;

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e );
        mav.addObject("utl", req.getRequestURL() );
        mav.setViewName("error");
        return mav;

    }

}*/
