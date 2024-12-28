package org.example.client;

import io.swagger.v3.oas.annotations.Operation;
import org.example.config.DefaultFeignConfig;
import org.example.domain.po.Purchases;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "mm-purchases", configuration = DefaultFeignConfig.class)
public interface PurchasesClient {
    @PostMapping("/purchases/listCondition")
     List<Purchases> listPurchaseCondition(@RequestBody Purchases purchases);

    @PutMapping("/purchases/updateCondition")
     void updatePurchaseCondition(@RequestParam Integer deleted, @RequestParam Integer purchaseId);
}
