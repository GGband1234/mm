package org.example.listener;


import org.example.domain.po.Purchases;
import org.example.service.impl.PurchasesServiceImpl;
import org.example.utils.ThreadLocalUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Component
public class PurcheaseMessageListener {
    @Autowired
    PurchasesServiceImpl purchasesService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "purchease.queue",
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "inventory.direct"),
            key = "purchease.logic.del"
    ))
    public void reduceQuantityMessage(List<Integer> ids, Message message){
        String username = message.getMessageProperties().getHeader("user-info");
        ThreadLocalUtil.set(username);
        purchasesService.lambdaUpdate()
                .set(Purchases::getDeleted,1)
                .in(Purchases::getPurchaseId,ids)
                .update();
    }

}
