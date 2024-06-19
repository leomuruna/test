package com.jencys.apibcijencys.presentation;

import com.jencys.apibcijencys.domain.service.UserService;
import com.jencys.apibcijencys.dto.UserRequestDTO;
import com.jencys.apibcijencys.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequestDTO user){

        return ResponseEntity.created(URI.create("/create")).body(userService.registerUser(user));
    }
}
