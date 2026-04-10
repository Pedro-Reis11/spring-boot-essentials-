package br.com.pedrodev.spring_boot_essentials.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
