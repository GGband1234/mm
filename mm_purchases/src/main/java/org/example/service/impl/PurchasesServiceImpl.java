package org.example.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.client.BudgetClient;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.PurchasesDto;
import org.example.domain.po.Projects;
import org.example.domain.po.Purchases;
import org.example.domain.po.PurchasesReturn;
import org.example.domain.po.Suppliers;
import org.example.mapper.PurchasesMapper;
import org.example.result.PageResult;

import org.example.service.PurchasesReturnService;
import org.example.service.PurchasesService;
import org.example.service.SuppliersService;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;
@Service
public class PurchasesServiceImpl extends ServiceImpl<PurchasesMapper, Purchases> implements PurchasesService {
    @Autowired
    BudgetClient budgetClient;
    @Autowired
    SuppliersService suppliersService;
    @Autowired
    PurchasesReturnService purchasesReturnService;
    @Override
    public void saveOrUpdatePurchase(PurchasesDto purchasesDto) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(purchasesDto.getPurchaseDate().toInstant(), ZoneId.systemDefault());
        if (dateTime.isAfter(LocalDateTime.now()))
            throw new RuntimeException("采购时间不能大于当前时间");
        Projects byProjectName = budgetClient.findProjectByName(purchasesDto.getProjectName());
        if (byProjectName == null)
            throw new RuntimeException("项目名不存在");
        Suppliers supplierByName = suppliersService.getSupplierByNameAndMaterials(purchasesDto.getSupplierName()
                ,purchasesDto.getMaterialName());
        /*如果有该供应商则导入供应商id 没有则创建这个供应商*/
        Purchases purchases = new Purchases();
        Suppliers suppliers = new Suppliers();
        if (supplierByName == null){
            BeanUtils.copyProperties(purchasesDto,suppliers);
            suppliers.setCreateTime(LocalDateTime.now());
            suppliers.setUpdateTime(LocalDateTime.now());
            suppliersService.save(suppliers);
            purchases.setSupplierId(suppliers.getSupplierId());
        }else {
            purchases.setSupplierId(supplierByName.getSupplierId());
        }
        BeanUtils.copyProperties(purchasesDto,purchases);
        purchases.setProjectId(byProjectName.getProjectId());
        purchases.setCreateTime(LocalDateTime.now());
        purchases.setUpdateTime(LocalDateTime.now());
        purchases.setType("采购");
        saveOrUpdate(purchases);
    }

    @Override
    public PageResult pageQuery(PageQueryDto pageQueryDto) {
        PageResult pageResult = new PageResult();

        Page<Purchases> page = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
        if ("审核中".equals(pageQueryDto.getName())){
            Page<Purchases> pages = lambdaQuery().eq(Purchases::getStatus, 0).page(page);
            List<Purchases> records = pages.getRecords();
            List<PurchasesDto> purchasesDtos = getPurchasesDtos(records);
            pageResult.setTotal(pages.getTotal());
            pageResult.setRecords(purchasesDtos);
        } else if ("已通过".equals(pageQueryDto.getName())) {
            Page<Purchases> pages = lambdaQuery().eq(Purchases::getStatus, 1).page(page);
            List<Purchases> records = pages.getRecords();
            List<PurchasesDto> purchasesDtos = getPurchasesDtos(records);
            pageResult.setTotal(pages.getTotal());
            pageResult.setRecords(purchasesDtos);
        }else if ("未通过".equals(pageQueryDto.getName())) {
            Page<Purchases> pages = lambdaQuery().eq(Purchases::getStatus, 2).page(page);
            List<Purchases> records = pages.getRecords();
            List<PurchasesDto> purchasesDtos = getPurchasesDtos(records);
            pageResult.setTotal(pages.getTotal());
            pageResult.setRecords(purchasesDtos);
        }else if (!StringUtils.hasLength(pageQueryDto.getName())){
            Page<Purchases> pages = page(page);
            List<Purchases> records = pages.getRecords();
            List<PurchasesDto> purchasesDtos = getPurchasesDtos(records);
            pageResult.setTotal(pages.getTotal());
            pageResult.setRecords(purchasesDtos);
        }else {
            throw new RuntimeException("请输入（已通过）或者（审核中）或者（未通过）");
        }
        return pageResult;
    }
    private List<PurchasesDto> getPurchasesDtos(List<Purchases> records){
        List<PurchasesDto> purchasesDtoList = new ArrayList<>();
        List<Integer> supplierIds= new ArrayList<>();
        List<Integer> projectIds = new ArrayList<>();
        for (Purchases record : records) {
            PurchasesDto purchasesDto = new PurchasesDto();
            BeanUtils.copyProperties(record,purchasesDto);
            if (record.getStatus() == 0)
                purchasesDto.setStatus("审核中");
            else if (record.getStatus() == 1)
                purchasesDto.setStatus("已通过");
            else if (record.getStatus() == 2)
                purchasesDto.setStatus("未通过");
            purchasesDtoList.add(purchasesDto);
            supplierIds.add(record.getSupplierId());
            projectIds.add(record.getProjectId());
        }
        copyProjectAndSupplierName(purchasesDtoList, supplierIds, projectIds);
        return purchasesDtoList;
    }
    private void copyProjectAndSupplierName(List<PurchasesDto> purchasesDtoList
    ,List<Integer> supplierIds,List<Integer> projectIds){
        List<Projects> projects = new ArrayList<>();
        List<Suppliers> suppliers = new ArrayList<>();
        int i = 0;
        for (Integer projectId : projectIds) {
            Projects byId = budgetClient.findProjectById(projectId);
            projects.add(byId);
            Suppliers byId1 = suppliersService.getById(supplierIds.get(i++));
            suppliers.add(byId1);
        }
        i =0;
        for (PurchasesDto purchasesDto : purchasesDtoList) {
            purchasesDto.setProjectName(projects.get(i).getProjectName());
            purchasesDto.setSupplierName(suppliers.get(i).getSupplierName());
            i++;
        }
    }

    @Override
    public void removePurchasesById(Integer purchasesId) {
        Purchases byId = getById(purchasesId);

//        已通过的材料copy到材料退货表/未通过的材料直接删除
        if (byId.getStatus() == 1) {
            PurchasesReturn purchasesReturn = new PurchasesReturn();
            BeanUtils.copyProperties(byId,purchasesReturn);
            Projects byId1 = budgetClient.findProjectById(byId.getProjectId());
            Suppliers byId2 = suppliersService.getById(byId.getSupplierId());
            purchasesReturn.setProjectName(byId1.getProjectName());
            purchasesReturn.setSupplierName(byId2.getSupplierName());
            purchasesReturn.setCreateTime(LocalDateTime.now());
            purchasesReturnService.save(purchasesReturn);
        }
        removeById(purchasesId);

    }

    @Override
    public PageResult pageQueryReturn(PageQueryDto pageQueryDto) {
        Page<PurchasesReturn> page = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
        purchasesReturnService.lambdaQuery().like(PurchasesReturn::getMaterialName,pageQueryDto.getName())
                .page(page);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    public void updatePurchaseStatu(Integer purchaseId, String statuName) {
        String username = ThreadLocalUtil.get();
        if ("员工".equals(username))
            throw new RuntimeException("您没有审核权限");
        if ("已通过".equals(statuName))
            lambdaUpdate().set(Purchases::getStatus,1).eq(Purchases::getPurchaseId,purchaseId).update();
        else if ("未通过".equals(statuName))
            lambdaUpdate().set(Purchases::getStatus,2).eq(Purchases::getPurchaseId,purchaseId).update();
        else
            throw new RuntimeException("操作失败");
    }

    @Override
    public List<PurchasesDto> logicList() {
        List<Purchases> list = lambdaQuery()
                .eq(Purchases::getDeleted, 0)
                .eq(Purchases::getStatus, 1)
                .list();
        return getPurchasesDtos(list);
    }

    @Override
    public void logicRemoveByIds(List<Integer> purchasesIds1) {
        lambdaUpdate().set(Purchases::getDeleted,1)
                .eq(Purchases::getStatus, 1)
                .in(purchasesIds1 != null,Purchases::getPurchaseId,purchasesIds1)
                .update();
    }

}
