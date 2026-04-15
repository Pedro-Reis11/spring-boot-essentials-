package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import br.com.pedrodev.spring_boot_essentials.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AlunoDto> findAll(Pageable pageable) {
        return alunoService.findAll(pageable);
    }

    @GetMapping("/{idAluno}/avaliacao")
    @ResponseStatus(HttpStatus.OK)
    public AvaliacaoFisicaDto getAlunoAvaliacao(@PathVariable Integer idAluno) throws BadRequestException {
        return alunoService.getAlunoAvaliacao(idAluno);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoDto criarAluno(@Valid @RequestBody AlunoDto alunoDto) throws BadRequestException {
        return alunoService.criarAluno(alunoDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlunoDto updateAluno(@PathVariable Integer id, @Valid @RequestBody AlunoDto dto) {
        return alunoService.updateAluno(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAluno(@PathVariable Integer id) {
        alunoService.deletarAluno(id);
    }
}
