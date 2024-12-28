package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.InventorysDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.po.Inventorys;
import org.example.result.PageResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

public interface InventorysService extends IService<Inventorys> {

    void savePurchaseMaterial();


    void saveProvidedMaterial();


    PageResult pageQuery(PageQueryDto pageQueryDto);

    void updateInventoryById(InventorysDto inventorysDto);

    void reduceQuantity(Integer inventoryId, BigInteger inventoryQuantiry);
}
