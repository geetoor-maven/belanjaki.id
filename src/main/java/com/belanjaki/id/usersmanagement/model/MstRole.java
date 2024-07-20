package com.belanjaki.id.usersmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "mst_role", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MstRole {

    @Id
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "role_name", length = 100)
    private String roleName;

    @OneToMany(mappedBy = "mstRole", cascade = CascadeType.ALL)
    private Set<MstUser> mstUsers = new HashSet<>();
}
