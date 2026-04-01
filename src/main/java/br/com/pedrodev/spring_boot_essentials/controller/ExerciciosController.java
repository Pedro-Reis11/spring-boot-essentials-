package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import br.com.pedrodev.spring_boot_essentials.service.ExerciciosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercicios")
@RequiredArgsConstructor
@Validated
public class ExerciciosController {

    private final ExerciciosService exerciciosService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciciosDto> findAll() {
        return exerciciosService.findAll();
    }

    @GetMapping("/grupos/{grupoMuscular}")
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciciosDto> findByGrupoMuscular(@PathVariable String grupoMuscular) {
        return exerciciosService.findByGrupoMuscular(grupoMuscular);
    }

    @GetMapping("/grupos")
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciciosDto> findAllByOrderByGrupoMuscularAsc() {
        return exerciciosService.findAllByOrderByGrupoMuscularAsc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciciosDto save(@Valid @RequestBody ExerciciosDto dto) {
        return exerciciosService.save(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        exerciciosService.delete(id);
    }
}
