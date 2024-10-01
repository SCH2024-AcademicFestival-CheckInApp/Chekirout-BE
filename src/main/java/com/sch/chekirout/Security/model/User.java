package com.sch.chekirout.Security.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_participation_counts", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "program_id")
    @Column(name = "participation_count")
    private Map<UUID, Integer> participationCounts = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_program_type_counts", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "program_type")
    @Column(name = "program_type_count")
    private Map<String, Integer> programTypeCounts = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_program_history", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "program_name")
    private List<String> programHistory = new ArrayList<>();

    private Boolean isEligibleForPrize = false;

    private Boolean isWinner = false;

    private Boolean isNotificationEnabled = true;

}

