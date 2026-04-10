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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AvaliacaoFisicaDto> findAll() {
        return avaliacaoFisicaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoFisicaDto createAvaliacaoFisica(@Valid @RequestBody AvaliacaoFisicaDto dto) throws BadRequestException {
        return avaliacaoFisicaService.criarAvaliacaoFisica(dto);
    }

    @PutMapping("/{idAluno}")
    @ResponseStatus(HttpStatus.OK)
    public AvaliacaoFisicaDto updateAvaliacaoFisica(@PathVariable Integer idAluno, @Valid @RequestBody AvaliacaoFisicaDto dto) throws BadRequestException {
        return avaliacaoFisicaService.updateAvaliacaoFisica(idAluno, dto);
    }

    @DeleteMapping("/{idAluno}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvaliacaoFisica(@PathVariable Integer idAluno) {
        avaliacaoFisicaService.deleteAvaliacaoFisica(idAluno);
    }
}
