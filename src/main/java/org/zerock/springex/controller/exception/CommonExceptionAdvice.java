package org.zerock.springex.controller.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@ControllerAdvice
@Log4j2
public class CommonExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    public String exceptnUMBER(NumberFormatException numberFormatException) {

        log.error("---------------------------");
        log.error(numberFormatException.getMessage());

        return "NUMBER FORMAT EXCEPTION";
    }

        @ResponseBody
        @ExceptionHandler(Exception.class)
        public String exceptCommon(Exception exception) {

            log.error("---------------------------");
            log.error(exception.getMessage());

            StringBuffer buffer = new StringBuffer("<ul>");
            buffer.append("<li>" + exception.getStackTrace() + "</li>");

            Arrays.stream(exception.getStackTrace()).forEach(StackTraceElement -> {
                    buffer.append("<li>" + StackTraceElement + "</li>");
                });

            buffer.append("</ul>");

            return buffer.toString();
        }
}
