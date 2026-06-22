package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.config.TokenProvider;
import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.RolesEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IRolesRepository;
import br.com.pedrodev.spring_boot_essentials.dto.LoginDto;
import br.com.pedrodev.spring_boot_essentials.dto.RegisterDto;
import br.com.pedrodev.spring_boot_essentials.dto.TokenDto;
import br.com.pedrodev.spring_boot_essentials.enums.RoleEnum;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IAlunosRepository repository;
    private final IRolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private Long expiration;


    @Transactional
    public void register(RegisterDto registerDto) throws BadRequestException {
        AlunosEntity aluno = repository.findByEmail(registerDto.getEmail())
                .orElse(null);
        if (aluno != null) {
            throw new BadRequestException("Já existe um aluno cadastrado com esse email");
        }

        var role = rolesRepository.findByNome(RoleEnum.ALUNO.name())
                .orElseGet(() -> rolesRepository.save(RolesEntity.builder()
                        .nome(RoleEnum.ALUNO.name())
                        .build()));

        repository.save(AlunosEntity.builder()
                .nome(registerDto.getNome())
                .email(registerDto.getEmail())
                .roles(Set.of(role))
                .senha(passwordEncoder.encode(registerDto.getSenha()))
                .build());
    }

    public TokenDto login(LoginDto loginDto) {
        try {
            var authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha()));
            return new TokenDto(tokenProvider.generateToken(authentication), expiration);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("invalid credentials");

        }
    }
}
