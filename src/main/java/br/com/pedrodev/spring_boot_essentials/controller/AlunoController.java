package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import br.com.pedrodev.spring_boot_essentials.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<AlunoDto> findAll(){
        return alunoService.findAll();
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
}
