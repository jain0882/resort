package com.resorts.season.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "season")
public class Season {
    @Getter
    @Setter
    @Column(name = "season", unique = true)
    private String season;

    @Id
    @Getter
    @Setter
    private Long id;
    @Column(name = "resort_id")
    @Getter
    @Setter
    private Long resortId;
}
