package org.zerock.springex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.PageRequestDTO;
import org.zerock.springex.dto.TodoDTO;
import org.zerock.springex.service.TodoService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

// 깃 커밋 테스트 , 주석입니다.

@Controller
@RequestMapping("/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoController {
  private final TodoService todoService;

  // 기존, 단순 전체 갯수 출력 -> 페이징 처리해서, 화면에 전달하는 코드 수정하기.

  // 기존 코드
//  @RequestMapping("/list")
//  public void list(Model model) {
//    log.info("todo list......");
//    model.addAttribute("dtoList",todoService.getAll());
//  }

  // 페이징 처리된 목록 출력 코드
  @RequestMapping("/list")
  // 화면에서, 전달 된 파라미터를 PageRequestDTO 가 자동 변환 해준다.
  // page, size 가, 1 또는 최소, 최대, 양수등 이 아니면, 오류가 발생하고,
  // 기본 페이지로 이동, page=1&size=10 , 이동 할 예정.
  public void list(@Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult, Model model) {
    log.info(pageRequestDTO);
    // 에러가 존재한다면, 출력후, 리다이렉트
    if(bindingResult.hasErrors()) {
      pageRequestDTO = PageRequestDTO.builder().build();
    }
    // responseDTO : 키 안에는, 페이징에 관련된 재료 준비물들이 다 들어 있다.
    // private int page;
    //  private int size;
    //  private int total;
    //  private int start;
    //  private int end;
    //  private boolean prev;
    //  private boolean next;
    //  private List<E> dtoList;
    // 화면에서는 -> responseDTO.page , 페이지 번호 사용가능.
    // 화면 : list.jsp , 만들어서 사용하기.
    model.addAttribute("responseDTO",todoService.getList(pageRequestDTO));
  }

  @GetMapping({"/read","/modify"})
  public void read(Long tno, Model model, PageRequestDTO pageRequestDTO) {
    //  화면에서, 페이지의 정보를 전달하면,
    // 서버에서는, PageRequestDTO 타입으로 받아 두겠다.
    // 화면에서, PageRequestDTO 를 사용하기.
    model.addAttribute("dto",todoService.getOne(tno));
  }

//  기존에 URL 파라미터를 사용하는 메서드 방식은 get 방식이었고,
//  post는 폼에 히든으로 숨겨서 전달합니다.
  @PostMapping("/remove")
  public String remove(Long tno, RedirectAttributes redirectAttributes, PageRequestDTO pageRequestDTO) {
    log.info("-----------------remove-------------------");
    log.info("tno:"+tno);
    todoService.remove(tno);

    // 페이지, 사이즈 정보를 화면에 전달하기.
    redirectAttributes.addAttribute("page",pageRequestDTO.getPage());
    redirectAttributes.addAttribute("size",pageRequestDTO.getSize());
    return "redirect:/todo/list?" + pageRequestDTO.getPage();
  }

    @PostMapping("/modify")
    public String modify(@Valid TodoDTO todoDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         PageRequestDTO pageRequestDTO) {
      if(bindingResult.hasErrors()){
        log.info("has errors.......");
        redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        redirectAttributes.addAttribute("tno",todoDTO.getTno());
        return "redirect:/todo/modify";
      }
      log.info(todoDTO);
      todoService.modify(todoDTO);
      redirectAttributes.addAttribute("tno", todoDTO.getTno());
      // 페이지, 사이즈 정보를 화면에 전달하기.
//      redirectAttributes.addAttribute("page",pageRequestDTO.getPage());
//      redirectAttributes.addAttribute("size",pageRequestDTO.getSize());
      return "redirect:/todo/read";
    }

//  @RequestMapping(value = "/register", method= RequestMethod.GET)
  @GetMapping("/register")
  public String register( @Valid TodoDTO todoDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes)  {
    log.info("POST todo register.......");

    if(bindingResult.hasErrors()) {
      log.info("has errors......");
      redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
      return "/todo/register";
    }
    log.info(todoDTO);
    todoService.register(todoDTO);
    return "redirect:/todo/list";
  }

  //@RequestMapping(value = "/register", method= RequestMethod.POST)
  @PostMapping("/register")
  public String registerPost(MultipartFile file,@Valid TodoDTO todoDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws IOException {
    log.info("POST todo register.......");

    log.info(file.getOriginalFilename()); // 실제 파일 이름 출력
    log.info(file.getSize()); // 파일의 크기
    log.info(file.getContentType()); // 파일의 확장자
    file.transferTo(new File("c://files//" + file.getOriginalFilename())); // 파일을 저장하는 메서드 : new File("파일을 저장 할 경로 //파일이름.확장자")

    if(bindingResult.hasErrors()){
      log.info("has errors.......");
      redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
      return "/todo/register";
    }
    log.info(todoDTO);
    todoService.register(todoDTO);
    return "redirect:/todo/list";
  }
}
