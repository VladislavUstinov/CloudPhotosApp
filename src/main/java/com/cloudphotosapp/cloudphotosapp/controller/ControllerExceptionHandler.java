package com.cloudphotosapp.cloudphotosapp.controller;

import com.cloudphotosapp.cloudphotosapp.customexception.CustomExceptionAccessDenied;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleFileTooLarge(Exception exception){
        log.error("MaxUploadSizeExceededException");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("custom_errors/filetoolarge");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomExceptionAccessDenied.class)
    public ModelAndView handleAccessDenied(Exception exception){
        log.error("CustomExceptionAccessDenied");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("custom_errors/403error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }




    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception){

        log.error("Not Found Exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("custom_errors/404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormat(Exception exception){

        log.error("Handling Number Format Exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("custom_errors/400error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }
}