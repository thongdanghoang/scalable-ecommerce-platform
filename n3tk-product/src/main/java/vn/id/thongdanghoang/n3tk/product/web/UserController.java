package vn.id.thongdanghoang.n3tk.product.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/test")
    public String demo() {
        return "Worked!";
    }

    @GetMapping("/demo")
    public Authentication demo(Authentication a) {
        return a;
    }
}
