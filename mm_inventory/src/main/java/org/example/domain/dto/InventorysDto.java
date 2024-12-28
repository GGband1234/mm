package org.example.domain.dto;

import lombok.Data;

import java.math.BigInteger;


@Data
public class InventorysDto {

    private Integer inventoryId;


    private String warehouseName;


    private BigInteger minQuantity;


    private BigInteger maxQuantity;

}
