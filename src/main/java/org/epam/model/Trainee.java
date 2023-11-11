package org.epam.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainees")
@NamedEntityGraph(name = "trainee-with-trainers-graph", attributeNodes = {@NamedAttributeNode("trainers")})
@NamedEntityGraph(name = "trainee-with-users-and-trainers-graph",
                  attributeNodes = {@NamedAttributeNode("user"), @NamedAttributeNode("trainers")})
public class Trainee {

    @Id
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", updatable = false)
    private User user;

    @ManyToMany
    @JoinTable(name = "trainee_trainer", joinColumns = @JoinColumn(name = "trainee_id"),
               inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private List<Trainer> trainers;
}
