package org.zerock.springex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    private String[] types;
    private String keyword;
    private boolean finished;
    private LocalDate from;
    private LocalDate to;

    // 클래스의 주목적
    // 페이징을 처리하기 위해서, 앞단(화면)에서 파라미터 정보를 보내는데
    // 이것 하나의 양식 폼에 담아주기 PageRequestDTO
    // 화면에서 전달한 파라미터를 담아두는 클래스 DTO
    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    @Positive
    private int size = 10;

    public int getSkip() {
        return (page - 1) * size;
    }

    private String link;

    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);
            if (finished) {
                builder.append("&finished=on");
            }

            if (types != null && types.length > 0) {
                for (int i = 0; i < types.length; i++) {
                    builder.append("&types=" + types[i]);
                }
            }
            if (keyword != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (from != null) {
                builder.append("&from=" + from.toString());
            }
            if (to != null) {
                builder.append("&to=" + to.toString());
            }
            return builder.toString();
        }
        // link = "10&size=10"

        return link;
    }


    public boolean checkType(String type) {
        if (types == null || types.length == 0) {
            return false;
        }
        return Arrays.stream(types).anyMatch(type::equals);
    }
}
