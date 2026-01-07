package vn.id.thongdanghoang.user_service.it;

import vn.id.thongdanghoang.user_service.entities.User;
import vn.id.thongdanghoang.user_service.repositories.UserRepository;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserEntityTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void equalsAndHashCodeTest() {
        var saved = userRepository.save(new User());

        var savedId = saved.getId();
        var user = userRepository.findById(savedId).orElseThrow();
        var proxy = userRepository.getReferenceById(savedId);

        var users = new HashSet<User>();

        users.add(user);
        users.add(proxy);

        Assertions.assertEquals(1, users.size());
    }

}
