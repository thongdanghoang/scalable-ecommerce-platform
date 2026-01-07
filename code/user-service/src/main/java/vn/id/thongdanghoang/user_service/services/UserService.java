package vn.id.thongdanghoang.user_service.services;

import vn.id.thongdanghoang.user_service.entities.User;
import vn.id.thongdanghoang.user_service.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User insert(User user) {
        return userRepository.save(user);
    }

}
