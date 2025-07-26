package vn.id.thongdanghoang.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserResource.PATH)
@RequiredArgsConstructor
public class UserResource {

  public static final String PATH = "/users";

  private final UserService userService;

}
