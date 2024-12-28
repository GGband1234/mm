package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.ProvidedMaterialDto;
import org.example.domain.po.ProvidedMaterial;
import org.example.domain.vo.ProvidedMaterialVo;
import org.example.result.PageResult;
import org.example.result.Result;
import org.example.service.ProvidedMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/providedMaterial")
@Tag(name = "甲供材管理")
public class ProvidedMaterialController {

    @Autowired
    private ProvidedMaterialService providedMaterialService;

    @PostMapping
    @Operation(summary = "新增甲供材信息")
    public Result addprovidedMaterial(@RequestBody ProvidedMaterialDto providedMaterialDto){
        providedMaterialService.saveOrUpdateProvidedMaterial(providedMaterialDto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改甲供材信息")
    public Result updateprovidedMaterial(@RequestBody ProvidedMaterialDto providedMaterialDto){
        ProvidedMaterial providedMaterial = new ProvidedMaterial();
        BeanUtils.copyProperties(providedMaterialDto,providedMaterial);
        providedMaterial.setUpdateTime(LocalDateTime.now());
        providedMaterialService.saveOrUpdate(providedMaterial);
        return Result.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询甲供材信息")
    public Result<PageResult> pageQuery(@RequestBody PageQueryDto pageQueryDto){
        PageResult pageResult= providedMaterialService.pageQuery(pageQueryDto);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{providedMaterialId}")
    @Operation(summary = "删除甲供材信息")
    public Result delprovidedMaterial(@PathVariable("providedMaterialId") Integer providedMaterialId){
        providedMaterialService.removeById(providedMaterialId);
        return Result.success();
    }

    @GetMapping("/logic")
    @Operation(summary = "查询逻辑未删除信息")
    public Result<List<ProvidedMaterialVo>> queryLogicProvidedMaterial(){
        List<ProvidedMaterialVo> providedMaterialVoList = providedMaterialService.logicList();
        return Result.success(providedMaterialVoList);
    }

    @DeleteMapping("/logic")
    @Operation(summary = "逻辑删除甲供材信息")
    public Result delLogicProvidedMaterial(@RequestBody Map<String,List<Integer>> providedMaterialIds){
        List<Integer> providedMaterialIds1 = providedMaterialIds.get("providedMaterialIds");
        providedMaterialService.logicRemoveByIds(providedMaterialIds1);
        return Result.success();
    }


}
