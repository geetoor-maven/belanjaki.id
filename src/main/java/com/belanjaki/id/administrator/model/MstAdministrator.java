package com.belanjaki.id.administrator.model;

import com.belanjaki.id.common.BaseEntity;
import com.belanjaki.id.usersmanagement.model.MstRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "mst_admin", schema = "admin")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MstAdministrator extends BaseEntity implements Serializable {

    @Id
    @Column(name = "admin_id")
    private UUID adminId;

    @Column(name = "admin_name", length = 100)
    private String adminName;

    @Column(name = "admin_number", length = 50)
    private String adminNumber;

    @Column(name = "email", length = 50)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "number_phone", length = 50)
    private String numberPhone;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private MstRole mstRole;
}
