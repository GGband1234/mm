package org.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.example.client.BudgetClient;
import org.example.client.InventoryClient;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.ProcessingDto;
import org.example.domain.po.Inventorys;
import org.example.domain.po.Processing;
import org.example.domain.po.Projects;
import org.example.domain.po.Warehouses;
import org.example.domain.vo.ProcessingVo;
import org.example.mapper.ProcessingMapper;
import org.example.result.PageResult;
import org.example.result.Result;
import org.example.service.ProcessingService;
import org.example.utils.ThreadLocalUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ProcessingServiceImpl extends ServiceImpl<ProcessingMapper, Processing> implements ProcessingService {

    @Autowired
    InventoryClient inventoryClient;
    @Autowired
    BudgetClient budgetClient;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    @GlobalTransactional
    public void saveProcessing(ProcessingDto processingDto) {
//        校验材料名和材料比例是否符合要求
        if (processingDto.getNeedMaterialNames() == null || processingDto.getNeedMaterialNames().isEmpty()
        ||processingDto.getMaterialRatio() == null || processingDto.getMaterialRatio().isEmpty())
            throw  new RuntimeException("表单内容不能为空");
        String regEx = "[`_~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\[\\]\\\\-]";
        String[] needMaterialNamesSplit = processingDto.getNeedMaterialNames().split("，");
        String[] materialRatioSplit = processingDto.getMaterialRatio().split("：");
        if (needMaterialNamesSplit[0].equals(processingDto.getNeedMaterialNames())
            && Pattern.compile(regEx).matcher(processingDto.getNeedMaterialNames()).find()
                || materialRatioSplit[0].equals(processingDto.getMaterialRatio())
                    && Pattern.compile(regEx).matcher(processingDto.getMaterialRatio()).find()){
            throw new RuntimeException("请输入正确的操作符所需材料之间用（，），材料比例之间用（：）");
        }
        if (needMaterialNamesSplit.length != materialRatioSplit.length)
            throw new RuntimeException("所需材料与材料比例数量不一致");

        Projects projects = budgetClient.findProjectByName(processingDto.getProjectName());
        Warehouses warehouses = inventoryClient.findWarehouseByName(processingDto.getWarehouseName());
        if (projects == null || warehouses == null)
            throw new RuntimeException("项目或仓库不存在");
        List<Inventorys> inventorys = new ArrayList<>();
        for (String s : needMaterialNamesSplit) {
            Inventorys inventorysOne = inventoryClient.findInventoryByCondition(s,projects.getProjectId(),warehouses.getWarehouseId());
            if (inventorysOne == null)
                throw new RuntimeException("项目所在仓库中没有该材料");
            inventorys.add(inventorysOne);
        }
        Processing processing = new Processing();
        BeanUtils.copyProperties(processingDto,processing);
        processing.setProjectId(projects.getProjectId());
        processing.setWarehouseId(warehouses.getWarehouseId());
        processing.setCreateTime(LocalDateTime.now());
        processing.setUpdateTime(LocalDateTime.now());
        save(processing);

        for (int i1 = 0; i1 < inventorys.size(); i1++) {
            BigInteger bigInteger = new BigInteger(materialRatioSplit[i1]);
            BigInteger reduceNum  = bigInteger.multiply(processingDto.getQuantity());
            if (inventorys.get(i1).getQuantity().compareTo(reduceNum) < 0)
                throw new RuntimeException(inventorys.get(i1).getMaterialName()+"库存不足");
            Map<String,Object> map = new HashMap<>();
            map.put("inventoryId",inventorys.get(i1).getInventoryId());
            map.put("inventoryQuantiry",reduceNum);
//            TODO 异步调用
            rabbitTemplate.convertAndSend("inventory.direct", "inventory.reduce.quantity", map, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    String username = ThreadLocalUtil.get();
                    message.getMessageProperties().setHeader("user-info",username);
                    return message;
                }
            });
//            inventoryClient.reduceInventory(inventorys.get(i1).getInventoryId(), reduceNum);
        }



    }


    @Override
    public PageResult pageQuery(PageQueryDto pageQueryDto) {
        Page<Processing> page = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
        lambdaQuery().like(Processing::getMaterialName,pageQueryDto.getName()).page(page);
        List<Processing> records = page.getRecords();
        List<ProcessingVo> processingVoList = new ArrayList<>();
        for (Processing record : records) {
            ProcessingVo processingVo = new ProcessingVo();
            BeanUtils.copyProperties(record,processingVo);
            Projects projectById = budgetClient.findProjectById(record.getProjectId());
            Warehouses warehouseById = inventoryClient.findWarehouseById(record.getWarehouseId());
            processingVo.setProjectName(projectById.getProjectName());
            processingVo.setWarehouseName(warehouseById.getWarehouseName());
            processingVoList.add(processingVo);
        }
        return new PageResult(page.getTotal(),processingVoList);
    }
}
