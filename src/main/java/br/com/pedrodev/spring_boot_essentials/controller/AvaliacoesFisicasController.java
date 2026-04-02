package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.service.AvaliacaoFisicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacoesFisicasController {

    private final AvaliacaoFisicaService avaliacaoFisicaService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoFisicaDto createAvaliacaoFisica(@Valid @RequestBody AvaliacaoFisicaDto dto) throws BadRequestException {
        return avaliacaoFisicaService.criarAvaliacaoFisica(dto);
    }
}
