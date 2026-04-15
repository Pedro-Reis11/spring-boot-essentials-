package br.com.pedrodev.spring_boot_essentials.dto;

import lombok.Data;

@Data
public class EnderecoDto {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
}
