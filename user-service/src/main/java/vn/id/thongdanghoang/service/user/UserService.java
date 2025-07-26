package vn.id.thongdanghoang.service.user;

import vn.id.thongdanghoang.service.user.entities.User;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User insert(User user) {
    return userRepository.save(user);
  }

  public Optional<User> findByProviderLinksId(String providerLinkId) {
    return userRepository.findByProviderLinksProviderId(providerLinkId);
  }

}
