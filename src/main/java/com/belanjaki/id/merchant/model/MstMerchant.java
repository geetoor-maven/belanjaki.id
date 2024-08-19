package com.belanjaki.id.merchant.model;

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
@Table(name = "mst_merchant", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MstMerchant extends BaseEntity implements Serializable {

    @Id
    @Column(name = "merchant_id")
    private UUID merchantId;

    @Column(name = "merchant_name", length = 100)
    private String merchantName;

    @Column(name = "email", length = 50)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "number_phone", length = 50)
    private String numberPhone;

    @Column(name = "status", length = 15)
    private String status;

    @Column(name = "img_profile", length = 50)
    private String imgProfile;

    @Column(name = "description")
    private String desc;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private MstRole mstRole;

}
