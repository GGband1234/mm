package org.example.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class OutboundDto {
    private Integer outboundId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column outbound.material_name
     *
     * @mbggenerated Mon Dec 02 13:44:33 CST 2024
     */
    private String materialName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column outbound.warehouse_id
     *
     * @mbggenerated Mon Dec 02 13:44:33 CST 2024
     */
    private String warehouseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column outbound.project_id
     *
     * @mbggenerated Mon Dec 02 13:44:33 CST 2024
     */
    private String projectName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column outbound.quantity
     *
     * @mbggenerated Mon Dec 02 13:44:33 CST 2024
     */
    private BigInteger quantity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime outboundDate;


    private Integer createdBy;
}