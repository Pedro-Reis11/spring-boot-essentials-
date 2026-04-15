package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.EnderecoDto;
import br.com.pedrodev.spring_boot_essentials.service.CepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
public class CepController {

    private final CepService cepService;

    @GetMapping("/{cep}")
    @ResponseStatus(HttpStatus.OK)
    public EnderecoDto buscar(@PathVariable String cep) {
        return cepService.buscarCep(cep);
    }
}
