package it.cgmconsulting.mspost.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SectionTitle title;

    private String subTitle;

    @Column(nullable = false, length = 5000)
    private String content;

    private String sectionImage;


}
