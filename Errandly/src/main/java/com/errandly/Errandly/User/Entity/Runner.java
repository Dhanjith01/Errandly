package com.errandly.Errandly.User.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "runner")
@Getter
@Setter
@Builder
public class Runner {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @MapsId
    @OneToOne
    @JoinColumn(name="id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    private String zone;
}
