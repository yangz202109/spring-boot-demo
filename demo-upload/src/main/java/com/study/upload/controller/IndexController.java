package com.study.upload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 首页Controller
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-10-20 21:22
 */
@Controller
public class IndexController {
    @GetMapping("")
    public String index() {
        return "index";
    }
}
