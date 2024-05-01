package org.zerock.springex.controller;

import com.sun.tools.javac.comp.Todo;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.TodoDTO;

import java.time.LocalDate;

@Controller
@Log4j2
public class SampleController {

        @GetMapping("/hello")
            public void hello() {
            log.info("hello........");
        }

        @GetMapping("/ex1")
        public void ex1(String name, int age) {
            log.info("ex1........");
            log.info("name : " + name);
            log.info("age : " + age);
        }

        @GetMapping("/ex2")
        public void ex2(@RequestParam(name = "name", defaultValue = "AAA") String name, @RequestParam(name = "age", defaultValue = "20") int age) {
            log.info("ex2........");
            log.info("name : " + name);
            log.info("age : " + age);
        }

        @GetMapping("/ex3")
        public void ex3(LocalDate dueDate) {
            log.info("ex3........");
            log.info("dueDate : " + dueDate);
        }

        @GetMapping("/ex4")
        public void ex4(Model model) {
            log.info("-----------------------");
            model.addAttribute("message", "Hello World");
        }

        @GetMapping("/ex4_1")
        public void ex4Extra(Model model) {
            log.info("ex4Extra............");
            TodoDTO todoDTO = TodoDTO.builder()
                    .tno(20L)
                    .title("Test title")
                    .dueDate(LocalDate.parse("2020-02-10"))
                    .finished(true)
                    .writer("Test writer")
                    .build();
            model.addAttribute("todoDTO", todoDTO);
    }

    @GetMapping("/ex5")
    public String ex5(RedirectAttributes redirectAttributes) {
        log.info("ex5...........");
        redirectAttributes.addAttribute("name", "ABC");
        redirectAttributes.addFlashAttribute("result", "success");

        return "ex6";
    }
    @GetMapping("/ex6")
    public void ex6(String name, Model model) {
        log.info("ex6..........." + name);
        model.addAttribute("name", name);
    }

    @GetMapping("/ex7")
    public void ex7(int p1, int p2) {
        log.info("p1..........." + p1);
        log.info("p2..........." + p2);
    }

}
