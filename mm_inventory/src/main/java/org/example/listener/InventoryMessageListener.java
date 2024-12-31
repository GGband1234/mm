package org.example.listener;

import org.checkerframework.checker.units.qual.C;
import org.example.domain.po.Inventorys;
import org.example.service.InventorysService;
import org.example.utils.ThreadLocalUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

@Component
public class InventoryMessageListener {
    @Autowired
    InventorysService inventorysService;
    @Autowired
    private JavaMailSender mailSender;
    private  final SimpleMailMessage message = new SimpleMailMessage();
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
        ThreadLocalUtil.remove();
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "inventory.monitor.queue",
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "inventory.direct"),
            key = "inventory.monitor.change"
    ))
    public void MonitorInventoryChanges(Integer id){
        Inventorys inventory = inventorysService.getById(id);
        if (inventory.getQuantity().compareTo(inventory.getMinQuantity()) < 0){
            String text = "材料id："+inventory.getInventoryId()
                    +"\n材料名称："+inventory.getMaterialName()
                    +"\n库存下限为："+inventory.getMinQuantity()
                    +"\n当前库存为："+inventory.getQuantity()
                    +"\n请及时补充库存";
            sendMessage("3576684445@qq.com","库存不足告警",text);
        } else if (inventory.getQuantity().compareTo(inventory.getMaxQuantity()) > 0) {
            String text = "材料id："+inventory.getInventoryId()
                    +"\n材料名称："+inventory.getMaterialName()
                    +"\n库存上限为："+inventory.getMaxQuantity()
                    +"\n当前库存为："+inventory.getQuantity()
                    +"\n请及时清理库存";
            sendMessage("3576684445@qq.com","库存超限告警",text);
        }
    }
    public void sendMessage(String account,String subject,String text){
        message.setFrom("2404566466@qq.com");//发送的qq邮箱
        message.setTo(account);//接收邮件的qq邮箱
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);//发送邮件
    }
}
