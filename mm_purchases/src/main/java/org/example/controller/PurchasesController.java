package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.PurchasesDto;
import org.example.domain.po.Purchases;
import org.example.result.PageResult;
import org.example.result.Result;
import org.example.service.PurchasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchases")
@Tag(name = "采购管理" )
public class PurchasesController {

    @Autowired
    private PurchasesService purchasesService;

    @PostMapping
    @Operation(summary = "新增采购信息")
    public Result addPurchase(@RequestBody PurchasesDto purchasesDto){
        purchasesService.saveOrUpdatePurchase(purchasesDto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改采购信息")
    public Result updatePurchase(@RequestBody PurchasesDto purchasesDto){
        purchasesService.saveOrUpdatePurchase(purchasesDto);
        return Result.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询采购信息")
    public Result<PageResult> pageQuery(@RequestBody PageQueryDto pageQueryDto){
        PageResult pageResult= purchasesService.pageQuery(pageQueryDto);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{purchasesId}")
    @Operation(summary = "删除采购信息")
    public Result delPurchases(@PathVariable("purchasesId") Integer purchasesId){
            purchasesService.removePurchasesById(purchasesId);
        return Result.success();
    }

    @PostMapping("/pageReturn")
    @Operation(summary = "分页查询退货信息")
    public Result<PageResult> pageQueryReturn(@RequestBody PageQueryDto pageQueryDto){
        PageResult pageResult= purchasesService.pageQueryReturn(pageQueryDto);
        return Result.success(pageResult);
    }

    @PutMapping("/{purchaseId}/{statuName}")
    @Operation(summary = "审核采购信息")
    public Result updatePurchaseStatu(@PathVariable Integer purchaseId,@PathVariable String statuName){
        purchasesService.updatePurchaseStatu(purchaseId,statuName);
        return Result.success();
    }

    @GetMapping("/logic")
    @Operation(summary = "查询逻辑未删除信息")
    public Result<List<PurchasesDto>> queryLogicPurchases(){
        List<PurchasesDto> purchasesDtoList = purchasesService.logicList();
        return Result.success(purchasesDtoList);
    }

    @DeleteMapping("/logic")
    @Operation(summary = "逻辑删除甲供材信息")
    public Result delLogicPurchases(@RequestBody Map<String,List<Integer>> purchasesIds){
        List<Integer> purchasesIds1 = purchasesIds.get("purchasesIds");
        purchasesService.logicRemoveByIds(purchasesIds1);
        return Result.success();
    }

    @PostMapping("/listCondition")
    @Operation(summary = "api查询采购信息集合")
    public List<Purchases> listPurchaseCondition(@RequestBody Purchases purchases){
        return purchasesService.lambdaQuery()
                .eq(purchases.getDeleted() !=null,Purchases::getDeleted,purchases.getDeleted())
                .eq(purchases.getStatus() !=null,Purchases::getStatus,purchases.getStatus())
                .eq(purchases.getProjectId() !=null,Purchases::getProjectId,purchases.getProjectId())
                .eq(purchases.getSupplierId() !=null,Purchases::getSupplierId,purchases.getSupplierId())
                .eq(purchases.getMaterialName() !=null,Purchases::getMaterialName,purchases.getMaterialName())
                .list();
    }

    @PutMapping("/updateCondition")
    @Operation(summary = "api查询采购信息集合")
    public void updatePurchaseCondition(@RequestParam Integer deleted,@RequestParam Integer purchaseId){
         purchasesService.lambdaUpdate()
                .set(Purchases::getDeleted,deleted)
                 .eq(Purchases::getPurchaseId,purchaseId)
                .update();
    }
}
