package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.dto.EnderecoDto;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CepService {

    private final WebClient webClient;

    public EnderecoDto buscarCep(String cep) {
        return webClient.get()
                .uri("https://viacep.com.br/ws/{cep}/json/", cep)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new BadRequestException("CEP inválido"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Erro no servidor externo"))
                )
                .bodyToMono(EnderecoDto.class)
                .block();
    }

}
