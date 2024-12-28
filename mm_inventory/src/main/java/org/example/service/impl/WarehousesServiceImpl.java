package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.po.Warehouses;
import org.example.mapper.WarehousesMapper;
import org.example.service.WarehousesService;
import org.springframework.stereotype.Service;

@Service
public class WarehousesServiceImpl extends ServiceImpl<WarehousesMapper, Warehouses> implements WarehousesService {

}
