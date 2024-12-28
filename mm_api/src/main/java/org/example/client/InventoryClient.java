package org.example.client;

import io.swagger.v3.oas.annotations.Operation;
import org.example.config.DefaultFeignConfig;
import org.example.domain.po.Inventorys;
import org.example.domain.po.Warehouses;
import org.example.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.Collection;

@FeignClient(value = "mm-inventory",configuration = DefaultFeignConfig.class)
public interface InventoryClient {
    @GetMapping("/warehouses/findById")
    @Operation(summary = "根据id查询仓库信息")
    Warehouses findWarehouseById(@RequestParam("warehouseId") Integer warehouseId);

    @GetMapping("/warehouses/findByName")
    @Operation(summary = "根据仓库名查询仓库信息")
     Warehouses findWarehouseByName(@RequestParam("warehouseId") String warehouseName);

    @PutMapping("/inventorys/reduceInventory")
    @Operation(summary = "减少库存数量")
     Result reduceInventory(@RequestParam("inventoryId") Integer inventoryId, @RequestParam("inventoryQuantiry") BigInteger inventoryQuantiry);
    @GetMapping("/inventorys/findInventoryByCondition")
    @Operation(summary = "查询库存数量")
    Inventorys findInventoryByCondition(@RequestParam("materialName") String materialName, @RequestParam("projectId") Integer projectId, @RequestParam("warehouseId") Integer warehouseId);
}
