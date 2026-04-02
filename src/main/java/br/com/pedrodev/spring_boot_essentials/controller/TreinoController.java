package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.TreinoDto;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import br.com.pedrodev.spring_boot_essentials.exception.NotFoundException;
import br.com.pedrodev.spring_boot_essentials.service.TreinosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treinos")
public class TreinoController {

    private final TreinosService treinosService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TreinoDto> findAll(){
        return treinosService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreinoDto criarTreino(@Valid @RequestBody TreinoDto dto) throws NotFoundException, BadRequestException {
        return treinosService.criarTreino(dto);
    }
}
