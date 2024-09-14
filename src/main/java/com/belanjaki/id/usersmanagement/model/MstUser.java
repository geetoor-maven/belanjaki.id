package com.belanjaki.id.usersmanagement.model;

import com.belanjaki.id.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mst_user", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MstUser extends BaseEntity implements Serializable {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 50)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "number_phone", length = 50)
    private String numberPhone;

    private String address;

    @Column(name = "img_profile")
    private String imgProfile;

    @Column(name = "url_profile")
    private String urlProfile;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private MstRole mstRole;

}
