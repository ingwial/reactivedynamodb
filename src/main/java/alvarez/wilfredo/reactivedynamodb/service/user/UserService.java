package alvarez.wilfredo.reactivedynamodb.service.user;


import alvarez.wilfredo.reactivedynamodb.service.user.datasource.UserRepository;
import alvarez.wilfredo.reactivedynamodb.service.user.datasource.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> findUser(String id) {
        return userRepository.findById(id);
    }

    public Mono<String> delete(String userId) {
        return userRepository.delete(userId);
    }

    public Mono<User> update(String userId, User user) {
        return userRepository.update(userId, user);
    }

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<List<User>> findAllUsers() {
        return userRepository.findAll()
                .collect(Collectors.toList());
    }
}
