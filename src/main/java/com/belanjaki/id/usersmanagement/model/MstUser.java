package com.belanjaki.id.usersmanagement.model;

import com.belanjaki.id.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mst_user", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MstUser extends BaseEntity {

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

    @Column(name = "img_profile", length = 50)
    private String imgProfile;

    @Column(name = "otp_validation", length = 10)
    private String otpValidation;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private MstRole mstRole;

}
