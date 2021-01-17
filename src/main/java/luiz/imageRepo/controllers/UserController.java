package luiz.imageRepo.controllers;

import lombok.AllArgsConstructor;
import luiz.imageRepo.entities.image.Image;
import luiz.imageRepo.entities.user.User;
import luiz.imageRepo.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RequestMapping("/v1/user")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {

        if(userService.findUserByEmail(user.getEmail()).isPresent())
            return new ResponseEntity<>(user, HttpStatus.CONFLICT);

        User userSaved = userService.create(user);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable long userId) {
        Optional<User> userOp = userService.findUserById(userId);

        if(userOp.isPresent())
            return new ResponseEntity<>(userOp.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}/images")
    public ResponseEntity<Set<Image>> getUserImages(@PathVariable long userId) {
        Optional<User> userOp = userService.findUserById(userId);

        if(userOp.isPresent())
            return new ResponseEntity<>(userOp.get().getImages(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
