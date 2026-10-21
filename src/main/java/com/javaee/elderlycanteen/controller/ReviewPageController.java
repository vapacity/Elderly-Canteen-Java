package com.javaee.elderlycanteen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 评价详情页。
 *
 * SEC-LAB-A4：评价文本进入 Model，模板使用 th:utext 原样输出。
 */
@Controller
public class ReviewPageController {

    @GetMapping("/reviews/view")
    public String view(@RequestParam(name = "text", required = false, defaultValue = "") String text,
                       Model model) {
        model.addAttribute("text", text);
        return "review";
    }
}
