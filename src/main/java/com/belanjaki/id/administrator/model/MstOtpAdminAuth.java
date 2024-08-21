package com.belanjaki.id.administrator.model;

import com.belanjaki.id.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "mstAdministrator")
@Entity
@Table(name = "mst_otp_admin_auth", schema = "admin")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MstOtpAdminAuth extends BaseEntity {

    @Id
    @Column(name = "otp_auth_id")
    private UUID otpAuthId;

    @OneToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "admin_id")
    private MstAdministrator mstAdministrator;

    @Column(name = "secret_otp")
    private String secretOtp;

}
