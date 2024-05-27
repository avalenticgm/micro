package it.cgmconsulting.msauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "_user") // antepongo l'underscore per non avere problemi con termini che potrebbero essere interpretati come parole chiave dal db
@Getter @Setter @AllArgsConstructor @Builder @ToString
@NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper=false)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @EqualsAndHashCode.Include
    private int id;

    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private boolean enabled;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}


