package org.epam.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainers")
@NamedEntityGraph(name = "trainer-only-graph")
@NamedEntityGraph(name = "trainer-with-users-training-type-and-trainees-graph",
                  attributeNodes = {@NamedAttributeNode("user"), @NamedAttributeNode("trainingType"),
                          @NamedAttributeNode("trainees")})
public class Trainer {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specialization")
    private TrainingType trainingType;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", updatable = false)
    private User user;

    @ManyToMany(mappedBy = "trainers",
                cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Trainee> trainees;
}
