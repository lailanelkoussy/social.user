package com.social.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{username}")
    public UserDTO getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PostMapping
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(userService.addUser(userDTO) ? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userDTO) ? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping(value = "/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

    @PatchMapping(value = "/{username}/{activate}")
    public void ActivateOrDeactivateUser(@PathVariable String username, @PathVariable boolean activate ) {
        userService.ActivateOrDeactivateUser(username, activate);
    }
}
