package com.errandly.Errandly.user.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users",indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_student_id", columnList = "studentId"),
        @Index(name = "idx_user_enabled", columnList = "enabled"),
        @Index(name = "idx_user_created_at", columnList = "createdAt")
    })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String studentId;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;
    private String phone;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    private double reputationscore=0.0;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles",
                     joinColumns = @JoinColumn(name="user_id"))
    @Column(name="role")
    @Builder.Default
    private Set<Roles> roles=new HashSet<>();
    
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Runner runner;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void assignRunner(Runner runner){
        this.runner=runner;
        runner.setUser(this);
    }
}
