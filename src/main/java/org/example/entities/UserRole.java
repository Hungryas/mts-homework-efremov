package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @Enumerated(EnumType.STRING)
    private EUserRole name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;
}