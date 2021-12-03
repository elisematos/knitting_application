package com.application.knitting.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer numberOfRows;
    private Integer numberOfStitches;
    private String yarn;
    private String description;
    private String instructions;
    private Instant created;
    @ManyToMany
    @JoinTable(name = "pattern_material",
    joinColumns = @JoinColumn(name = "pattern_id"),
    inverseJoinColumns = @JoinColumn(name = "material_id"))
    private List<Material> materialList = new ArrayList<>();
}
