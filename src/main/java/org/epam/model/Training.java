package org.epam.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainings")
@NamedEntityGraph(name = "training-only-graph")
@NamedEntityGraph(name = "training-with-trainee-trainer-and-types-graph",
                  attributeNodes = {@NamedAttributeNode(value = "trainee", subgraph = "trainee-graph"),
                          @NamedAttributeNode(value = "trainer", subgraph = "trainer-graph"),
                          @NamedAttributeNode("trainingType"), @NamedAttributeNode(value = "trainingType")},
                  subgraphs = {@NamedSubgraph(name = "trainee-graph", attributeNodes = {@NamedAttributeNode("user")}),
                          @NamedSubgraph(name = "trainer-graph", attributeNodes = {@NamedAttributeNode("user")})})

public class Training {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private TrainingType trainingType;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "duration")
    private Long duration;
}
