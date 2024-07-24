package com.belanjaki.id.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@MappedSuperclass
@Data
public class BaseEntity {

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Timestamp updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

}
