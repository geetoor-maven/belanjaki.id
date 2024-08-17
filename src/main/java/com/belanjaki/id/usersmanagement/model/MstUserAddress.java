package com.belanjaki.id.usersmanagement.model;

import com.belanjaki.id.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mst_user_address", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MstUserAddress extends BaseEntity {

    @Id
    @Column(name = "address_id")
    private UUID addressId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private MstUser mstUser;

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "postal_code", length = 10)
    private String postalCode;
    
}
