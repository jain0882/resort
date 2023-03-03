package com.resorts.season.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "liftrid")
public class LiftRide {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long liftId;
    @Getter
    @Setter
    @Column(name = "time")
    private int time;
    @Column(name = "season")
    @Getter
    @Setter
    private String season;
    @Column(name = "resort_id")
    @Getter
    @Setter
    private Long resortId;
}
