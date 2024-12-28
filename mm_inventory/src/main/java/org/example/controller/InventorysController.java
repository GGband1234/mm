package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.dto.InventorysDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.po.Inventorys;
import org.example.result.PageResult;
import org.example.result.Result;
import org.example.service.InventorysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@Tag(name = "库存管理")
@RequestMapping("/inventorys")
public class InventorysController {

    @Autowired
    InventorysService inventorysService;



    @PostMapping("/purchaseMaterial")
    @Operation(summary = "导入采购材料")
    public Result addPurchaseMaterial(){
        inventorysService.savePurchaseMaterial();
        return Result.success();
    }

    @PostMapping("/providedMaterial")
    @Operation(summary = "导入甲供材料")
    public Result addProvidedMaterial(){
        inventorysService.saveProvidedMaterial();
        return Result.success();
    }

    @PostMapping
    @Operation(summary = "分页查询库存信息")
    public Result<PageResult> pageQuery(@RequestBody PageQueryDto pageQueryDto){
        PageResult pageResult = inventorysService.pageQuery(pageQueryDto);
        return Result.success(pageResult);
    }

    @PutMapping
    @Operation(summary = "更新库存信息")
    public Result updateInventory(@RequestBody InventorysDto inventorysDto){
        inventorysService.updateInventoryById(inventorysDto);
        return Result.success();
    }

    @PutMapping("/reduceInventory")
    @Operation(summary = "减少库存数量")
    public Result reduceInventory(@RequestParam("inventoryId") Integer inventoryId, @RequestParam("inventoryQuantiry")BigInteger inventoryQuantiry){
        inventorysService.reduceQuantity(inventoryId,inventoryQuantiry);
        return Result.success();
    }
    @GetMapping("/findInventoryByCondition")
    @Operation(summary = "查询库存")
    public Inventorys findInventoryByCondition(@RequestParam("materialName") String materialName, @RequestParam("projectId") Integer projectId, @RequestParam("warehouseId") Integer warehouseId){
        return inventorysService.lambdaQuery().eq(Inventorys::getMaterialName, materialName)
                .eq(Inventorys::getProjectId, projectId)
                .eq(Inventorys::getWarehouseId, warehouseId)
                .one();
    }


}
