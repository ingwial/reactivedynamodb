package alvarez.wilfredo.reactivedynamodb.dist.rest.user;


import alvarez.wilfredo.reactivedynamodb.service.user.UserService;
import alvarez.wilfredo.reactivedynamodb.service.user.datasource.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Mono<List<User>>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Mono<User>> findUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PostMapping
    public ResponseEntity<Mono<User>> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Mono<User>> update(
            @PathVariable String id,
            @RequestBody User user
    ){
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Mono<String>> delete(@PathVariable String id) {
        return ResponseEntity.ok(userService.delete(id));
    }

}
