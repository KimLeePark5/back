package com.kimleepark.thesilver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class temp {

    @GetMapping("/test")
    public String test () {
        System.out.println("백엔드에서 sout 호출 성공.");
        return "프론트 and 백 연결 성공";
    }

}
