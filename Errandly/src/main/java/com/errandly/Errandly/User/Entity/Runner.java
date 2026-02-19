package com.errandly.Errandly.User.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "runners")
@Getter
@Setter
@Builder
public class Runner {
    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name="id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    private String zone;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
