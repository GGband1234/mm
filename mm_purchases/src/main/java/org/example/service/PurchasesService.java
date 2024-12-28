package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.PurchasesDto;
import org.example.domain.po.Purchases;
import org.example.result.PageResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PurchasesService extends IService<Purchases> {

    @Transactional
    void saveOrUpdatePurchase(PurchasesDto purchasesDto);


    PageResult pageQuery(PageQueryDto pageQueryDto);

    @Transactional
    void removePurchasesById(Integer purchasesId);

    PageResult pageQueryReturn(PageQueryDto pageQueryDto);
    @Transactional
    void updatePurchaseStatu(Integer purchaseId, String statuName);

    List<PurchasesDto> logicList();
    @Transactional
    void logicRemoveByIds(List<Integer> purchasesIds1);
}
