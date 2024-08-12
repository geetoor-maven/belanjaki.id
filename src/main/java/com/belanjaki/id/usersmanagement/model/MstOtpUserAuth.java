package com.belanjaki.id.usersmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@ToString(exclude = "mstUser")
@Entity
@Table(name = "mst_otp_user_auth", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MstOtpUserAuth {

    @Id
    @Column(name = "otp_auth_id")
    private UUID otpAuthId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private MstUser mstUser;

    @Column(name = "secret_otp")
    private String secretOtp;
}
