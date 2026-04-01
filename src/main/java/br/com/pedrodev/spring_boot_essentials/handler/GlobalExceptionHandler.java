package br.com.pedrodev.spring_boot_essentials.handler;

import br.com.pedrodev.spring_boot_essentials.exception.ErrorResponse;
import br.com.pedrodev.spring_boot_essentials.exception.FieldErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> FieldErrorResponse.builder()
                        .field(error.getField())
                        .message(getCustomMessage(error.getField()))
                        .build())
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private String getCustomMessage(String field) {
        return switch (field) {
            case "nome" -> "O nome do exercício é obrigatório";
            case "grupoMuscular" -> "O grupo muscular é obrigatório";
            default -> "Campo inválido";
        };
    }
}
