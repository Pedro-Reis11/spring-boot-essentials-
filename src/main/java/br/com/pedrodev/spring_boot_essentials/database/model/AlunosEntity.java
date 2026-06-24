package br.com.pedrodev.spring_boot_essentials.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunosEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    private String senha;

    // A avaliação física é mapeada como um relacionamento OneToOne, onde cada aluno tem uma única avaliação física associada a ele. O cascade = CascadeType.ALL garante que as operações de persistência sejam propagadas para a avaliação física quando o aluno for salvo ou removido. O fetch = FetchType.EAGER indica que a avaliação física deve ser carregada imediatamente junto com o aluno, e orphanRemoval = true garante que a avaliação física seja removida automaticamente quando o aluno for deletado.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "avaliacao_fisica_id")
    private AvaliacoesFisicasEntity avaliacaoFisica;

    @OneToMany(mappedBy = "aluno")
    private Set<TreinosEntity> treinos = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "alunos_roles", joinColumns = @JoinColumn(name = "aluno_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RolesEntity> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
