package br.com.pedrodev.spring_boot_essentials.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
