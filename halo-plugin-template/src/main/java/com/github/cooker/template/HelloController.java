package com.github.cooker.template;

import com.github.cooker.api.AbstractController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * grant
 * 26/10/2021 5:28 下午
 * 描述：
 */
@RequestMapping("/hello")
@RestController
public class HelloController extends AbstractController {

    @GetMapping
    public String hello() {
        return "hello";
    }
}
