package vn.id.thongdanghoang.service.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(UserResource.PATH)
@RequiredArgsConstructor
public class UserResource {

    public static final String PATH = "/users";

    private final UserService userService;

}
