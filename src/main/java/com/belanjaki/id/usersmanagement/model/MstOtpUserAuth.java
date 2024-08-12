package com.belanjaki.id.usersmanagement.model;

import com.belanjaki.id.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "mstUser")
@Entity
@Table(name = "mst_otp_user_auth", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MstOtpUserAuth extends BaseEntity {

    @Id
    @Column(name = "otp_auth_id")
    private UUID otpAuthId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private MstUser mstUser;

    @Column(name = "secret_otp")
    private String secretOtp;
}
