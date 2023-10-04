package fptu.swp391.shoppingcart;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends AbstractApplicationController{
    @GetMapping("/")
    String test() {
        return "Hello world!";
    }
    @GetMapping("/admin")
    String admin() {
        return "Hello Admin!";
    }
    @GetMapping("/user")
    String user() {
        return "Hello User!";
    }
}
