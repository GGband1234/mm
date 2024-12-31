package org.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.bind.v2.TODO;
import io.seata.spring.annotation.GlobalTransactional;
import org.example.client.BudgetClient;
import org.example.client.PurchasesClient;
import org.example.client.UserClient;
import org.example.domain.dto.InventorysDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.po.*;
import org.example.domain.vo.InventorysVo;
import org.example.mapper.InventorysMapper;
import org.example.result.PageResult;
import org.example.service.*;
import org.example.utils.ThreadLocalUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class InventorysServiceImpl extends ServiceImpl<InventorysMapper, Inventorys> implements InventorysService {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    PurchasesClient purchasesClient;
    @Autowired
    ProvidedMaterialService providedMaterialService;
    @Autowired
    WarehousesService warehousesService;
    @Autowired
    BudgetClient budgetClient;
    @Autowired
    OutboundService outboundService;
    @Autowired
    UserClient userClient;
    @Override
    public void savePurchaseMaterial() {
        List<Inventorys> inventorysList = new ArrayList<>();
        Purchases purchases1 = new Purchases();
        purchases1.setStatus(1);
        purchases1.setDeleted(0);
        List<Integer> purchasesIds = new ArrayList<>();
        List<Purchases> purchasesList = purchasesClient.listPurchaseCondition(purchases1);
        for (Purchases purchases : purchasesList) {
            Inventorys inventorys = new Inventorys();
            BeanUtils.copyProperties(purchases,inventorys);
            purchasesIds.add(purchases.getPurchaseId());
//            purchasesClient.updatePurchaseCondition(1,purchases.getPurchaseId());
            inventorys.setCreateTime(LocalDateTime.now());
            inventorys.setUpdateTime(LocalDateTime.now());
            inventorysList.add(inventorys);
        }
        saveBatch(inventorysList);
        rabbitTemplate.convertAndSend("inventory.direct", "purchease.logic.del", purchasesIds, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("user-info",ThreadLocalUtil.get());
                return message;
            }
        });
    }

    @Override
    public void saveProvidedMaterial() {
        List<Inventorys> inventorysList = new ArrayList<>();
        List<ProvidedMaterial> providedMaterialList = providedMaterialService.lambdaQuery()
                .eq(ProvidedMaterial::getDeleted, 0)
                .list();
        for (ProvidedMaterial providedMaterial : providedMaterialList) {
            Inventorys inventorys = new Inventorys();
            BeanUtils.copyProperties(providedMaterial,inventorys);
            providedMaterialService.lambdaUpdate().set(ProvidedMaterial::getDeleted,1)
                    .eq(ProvidedMaterial::getProvidedMaterialId,providedMaterial.getProvidedMaterialId())
                    .update();
            inventorys.setCreateTime(LocalDateTime.now());
            inventorys.setUpdateTime(LocalDateTime.now());
            inventorysList.add(inventorys);
        }
        saveBatch(inventorysList);
    }

    @Override
    public PageResult pageQuery(PageQueryDto pageQueryDto) {
        Page<Inventorys> pages = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
        lambdaQuery().like(Inventorys::getMaterialName,pageQueryDto.getName()).page(pages);
        List<Inventorys> records = pages.getRecords();
        List<InventorysVo> inventorysVoList = new ArrayList<>();
        for (Inventorys record : records) {
            InventorysVo inventorysVo = new InventorysVo();
            BeanUtils.copyProperties(record,inventorysVo);
            if (record.getWarehouseId() != null )
                inventorysVo.setWarehouseName(warehousesService.getById(record.getWarehouseId()).getWarehouseName());
            inventorysVo.setProjectName(budgetClient.findProjectById(record.getProjectId()).getProjectName());
            inventorysVoList.add(inventorysVo);
        }
        return new PageResult(pages.getTotal(),inventorysVoList);
    }

    @Override
    public void updateInventoryById(InventorysDto inventorysDto) {
        if (inventorysDto.getMaxQuantity().compareTo(inventorysDto.getMinQuantity()) < 0)
            throw new RuntimeException("库存上限不能小于下限");
        Warehouses warehouse = warehousesService.lambdaQuery().eq(Warehouses::getWarehouseName, inventorysDto.getWarehouseName())
                .one();
        if (warehouse == null)
            throw new RuntimeException("仓库信息不存在");
        Inventorys nowInventory = getById(inventorysDto.getInventoryId());
        List<Inventorys> queryInventoryList = lambdaQuery().eq(Inventorys::getMaterialName, nowInventory.getMaterialName())
                    .eq(Inventorys::getProjectId, nowInventory.getProjectId())
                    .eq(Inventorys::getUnit, nowInventory.getUnit())
                    .eq(Inventorys::getWarehouseId, warehouse.getWarehouseId())
                    .list();
        if (queryInventoryList != null && !queryInventoryList.isEmpty() &&
                !Objects.equals(queryInventoryList.get(0).getInventoryId(), nowInventory.getInventoryId())){
            List<Integer> ids = new ArrayList<>();
            BigInteger sum = new BigInteger("0");
            sum = sum.add(nowInventory.getQuantity());
            for (Inventorys inventorys : queryInventoryList) {
                ids.add(inventorys.getInventoryId());
                sum = sum.add(inventorys.getQuantity());
            }
            lambdaUpdate().set(Inventorys::getQuantity,sum)
                    .set(Inventorys::getWarehouseId,warehouse.getWarehouseId())
                    .set(Inventorys::getMaxQuantity,inventorysDto.getMaxQuantity())
                    .set(Inventorys::getMinQuantity,inventorysDto.getMinQuantity())
                    .set(Inventorys::getUpdateTime,LocalDateTime.now())
                    .eq(Inventorys::getInventoryId,nowInventory.getInventoryId())
                    .update();
            removeByIds(ids);
        }else {
            Inventorys inventorys = new Inventorys();
            BeanUtils.copyProperties(inventorysDto,inventorys);
            inventorys.setUpdateTime(LocalDateTime.now());
            inventorys.setWarehouseId(warehouse.getWarehouseId());
            updateById(inventorys);
        }
        rabbitTemplate.convertAndSend("inventory.direct","inventory.monitor.change",inventorysDto.getInventoryId());
    }
    @Transactional
    @Override
    public void reduceQuantity(Integer inventoryId, BigInteger inventoryQuantiry) {
        Inventorys byId = getById(inventoryId);
        if (byId.getQuantity().compareTo(inventoryQuantiry) <0)
            throw new RuntimeException("库存不足");
        BigInteger newQuantiry = new BigInteger("0");
        newQuantiry = newQuantiry.add(byId.getQuantity());
        lambdaUpdate().set(Inventorys::getQuantity,newQuantiry.subtract(inventoryQuantiry))
                .eq(Inventorys::getInventoryId,inventoryId)
                .update();
        Outbound outbound = new Outbound();
        String username = ThreadLocalUtil.get();
        User byUserName = userClient.findByUserName(username);
        BeanUtils.copyProperties(byId,outbound);
        outbound.setQuantity(inventoryQuantiry);
        outbound.setOutboundDate(LocalDateTime.now());
        outbound.setCreatedBy(byUserName.getUserId());
        outboundService.save(outbound);
        rabbitTemplate.convertAndSend("inventory.direct","inventory.monitor.change",inventoryId);
    }


}
