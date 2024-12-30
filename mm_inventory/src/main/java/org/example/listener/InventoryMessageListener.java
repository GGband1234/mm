package org.example.listener;

import org.example.service.InventorysService;
import org.example.utils.ThreadLocalUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Map;

@Component
public class InventoryMessageListener {
    @Autowired
    InventorysService inventorysService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "inventory.queue",
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "inventory.direct"),
            key = "inventory.reduce.quantity"
    ))
    public void reduceQuantityMessage(Map<String,Object> map, Message message){
        String username = message.getMessageProperties().getHeader("user-info");
        ThreadLocalUtil.set(username);
        Integer inventoryId = (Integer) map.get("inventoryId");
        BigInteger inventoryQuantiry = new BigInteger(map.get("inventoryQuantiry").toString()) ;
        inventorysService.reduceQuantity(inventoryId,inventoryQuantiry);
    }
}
