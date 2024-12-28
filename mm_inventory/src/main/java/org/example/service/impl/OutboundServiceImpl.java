package org.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.client.BudgetClient;
import org.example.domain.dto.OutboundDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.po.Outbound;
import org.example.mapper.OutboundMapper;
import org.example.result.PageResult;
import org.example.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutboundServiceImpl extends ServiceImpl<OutboundMapper, Outbound> implements OutboundService {

    @Autowired
    WarehousesService warehousesService;
    @Autowired
    BudgetClient budgetClient;

    @Override
    public PageResult pageQuery(PageQueryDto pageQueryDto) {
        Page<Outbound> page = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
        lambdaQuery().like(Outbound::getMaterialName,pageQueryDto.getName()).page(page);
        List<Outbound> records = page.getRecords();
        List<OutboundDto> outboundDtos = new ArrayList<>();
        for (Outbound record : records) {
            OutboundDto outboundDto = new OutboundDto();
            BeanUtils.copyProperties(record,outboundDto);
            outboundDto.setWarehouseName(warehousesService.getById(record.getWarehouseId()).getWarehouseName());
            outboundDto.setProjectName(budgetClient.findProjectById(record.getWarehouseId()).getProjectName());
            outboundDtos.add(outboundDto);
        }
        return new PageResult(page.getTotal(),outboundDtos);
    }
}
