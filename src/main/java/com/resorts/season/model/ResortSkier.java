package com.resorts.season.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resort_skier")
public class ResortSkier {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resortSkierId;
    @Column(name = "resort_id")
    @Getter
    @Setter
    private Long resortId;
    @Column(name = "time")
    @Getter
    @Setter
    private String time;

    @Column(name = "num_of_skiers")
    @Setter
    @Getter
    private int numOfSkiers;
}
