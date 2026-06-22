package br.com.pedrodev.spring_boot_essentials.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    @NotBlank
    private String email;
    @NotBlank
    private String senha;
}
