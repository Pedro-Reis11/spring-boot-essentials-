package br.com.pedrodev.spring_boot_essentials.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;

    // A avaliação física é mapeada como um relacionamento OneToOne, onde cada aluno tem uma única avaliação física associada a ele. O cascade = CascadeType.ALL garante que as operações de persistência sejam propagadas para a avaliação física quando o aluno for salvo ou removido. O fetch = FetchType.EAGER indica que a avaliação física deve ser carregada imediatamente junto com o aluno, e orphanRemoval = true garante que a avaliação física seja removida automaticamente quando o aluno for deletado.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "avaliacao_fisica_id")
    private AvaliacoesFisicasEntity avaliacaoFisica;

    @OneToMany(mappedBy = "aluno")
    private Set<TreinosEntity> treinos = new HashSet<>();
}
