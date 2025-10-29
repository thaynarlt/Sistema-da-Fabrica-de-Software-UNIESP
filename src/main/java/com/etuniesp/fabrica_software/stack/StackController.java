package com.etuniesp.fabrica_software.stack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stacks")
@RequiredArgsConstructor
public class StackController {

    private final StackTecRepository repository;

    @GetMapping
    public List<StackTecnologia> listar() {
        return repository.findAll();
    }
}
