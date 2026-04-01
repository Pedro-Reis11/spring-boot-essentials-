package br.com.pedrodev.spring_boot_essentials.exception;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private List<FieldErrorResponse> errors;
}
