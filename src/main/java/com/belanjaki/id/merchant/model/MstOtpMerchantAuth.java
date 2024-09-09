package com.belanjaki.id.merchant.model;

import com.belanjaki.id.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "mstAdministrator")
@Entity
@Table(name = "mst_otp_merchant_auth", schema = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MstOtpMerchantAuth extends BaseEntity {

    @Id
    @Column(name = "otp_auth_id")
    private UUID otpAuthId;

    @OneToOne
    @JoinColumn(name = "merchant_id", referencedColumnName = "merchant_id")
    private MstMerchant mstMerchant;

    @Column(name = "secret_otp")
    private String secretOtp;
}
