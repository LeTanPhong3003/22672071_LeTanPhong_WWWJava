package phong.lab5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller để hiển thị giao diện web
 */
@Controller
public class WebController {

    /**
     * Trang chủ - Giao diện quản lý sinh viên
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}

