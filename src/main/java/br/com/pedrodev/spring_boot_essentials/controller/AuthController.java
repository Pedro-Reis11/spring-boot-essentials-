package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.LoginDto;
import br.com.pedrodev.spring_boot_essentials.dto.RegisterDto;
import br.com.pedrodev.spring_boot_essentials.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterDto registerDto) {
        authService.register(registerDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginDto loginDto) {
        authService.login(loginDto);
    }
}
