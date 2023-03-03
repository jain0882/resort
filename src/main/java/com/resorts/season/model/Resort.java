package com.resorts.season.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resort")
public class Resort {

    @Column(name = "resort_name")
    @Getter
    @Setter
    private String resortName;
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resortId;
}
