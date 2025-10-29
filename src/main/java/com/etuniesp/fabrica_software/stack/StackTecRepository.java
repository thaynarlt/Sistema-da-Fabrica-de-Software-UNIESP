package com.etuniesp.fabrica_software.stack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StackTecRepository extends JpaRepository<StackTecnologia, Long> {
}
