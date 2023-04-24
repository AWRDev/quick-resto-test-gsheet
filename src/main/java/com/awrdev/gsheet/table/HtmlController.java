package com.awrdev.gsheet.table;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {
    @GetMapping("/page")
    public String myPage() {

        return "index.html";
    }
}
