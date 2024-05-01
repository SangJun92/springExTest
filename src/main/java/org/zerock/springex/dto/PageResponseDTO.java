package org.zerock.springex.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size;
    private int total;

    //시작 페이지 번호
    private int start;
    //끝 페이지 번호
    private int end;

    //이전 페이지 존재여부
    private boolean prev;
    //다음 페이지 존재여부
    private boolean next;

    // dtoList 로 받기.
    private List<E> dtoList;

    // 생성자 만들기. 파라미터 3개짜리.
    // 페이지 계산 방법,
    // 예)
    // 기본, 1,2,3,4,5,6,7,8,9,10
    // 페이지 2: 2,3,4,5,6,7,8,9,10,11
    // 페이지 3: 3,4,5,6,7,8,9,10,11,12
    // 페이지 10:, 10,11,12,13,14,15,16,17,18,19

    // end 페이지 계산후, start 페이지 계산 해보기.



    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO,
                           List<E> dtoList, int total){
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        // end 페이지 먼저 계산하기.
        // 예) 1/10 = 0.1 -> 1 => 10
        // 2 페이지
        // 예) 2/10 = 0.2 -> 1 => 10
        // 11 페이지
        // 예) 11/10 = 1.1 -> 2 => 20
        // 21 페이지
        // 예) 21/10 = 2.1 -> 3 => 30
        // 딱 안떨어지는 경우,
        // 예) 게시글 총 : 75개. 가정.
        // 75 / 10 = 7.5 -> 8페이지
        // 예2) 현재 페이지에서, 끝 페이지가 고정으로 : 10으로 나옴.
        this.end = (int)(Math.ceil(this.page / 10.0))*10;

        // start , 시작 페이지 구하기.
        // 예) end = 30 , start = 30 - 9 = 21
        // 예) end = 20 , start = 20 - 9 = 11
        this.start = this.end - 9;

        // last 값이 없어서 , 생성후, end 멤버에 재사용하기.
        // 예) 게시글 총 : 75개. 가정.
        // 75 / 10 = 7.5 -> 8페이지
        int last = (int) (Math.ceil(total/(double)size));

        // 결론,
        // 예)
        // end > last -> last
        // 10 > 8 -> 8
        this.end = end > last ? last : end;

        // prev , 이전 페이지 존재 유무
        this.prev = this.start > 1;

        // next , 다음 페이지 존재 유무
        // 예) 총 게시글 : 75 개 가정.
        //  end = 8 , size = 10(고정)
        // 75 > 80 , 거짓 -> false

        // 예) 총 게시글 : 103개
        //  last = total / 10 : 올림, 10.3 -> 11
        // end = 10,
        // 10 > 11 :false , -> end
        // end > last ? last : end
        // end -> 11 변경.
        // 103 > 110 : false ,
        // 1,2,3,4, ... , 10,11
        // 1,2,3, ... ,9,10 : next 가 존재,
        // 예) total : 103 , end : 10, -> 103 > 10 * 10 : true
        // 2,3,4,...10,11 : next 가 존재 안함.
        // 예) total : 103 , end : 11, -> 103 > 11 * 10 : false
        this.next = total > this.end * this.size;


    }
}
