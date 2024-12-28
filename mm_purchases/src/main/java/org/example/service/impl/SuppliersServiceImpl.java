package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.dto.SuppliersDto;
import org.example.domain.po.Suppliers;
import org.example.mapper.SuppliersMapper;
import org.example.service.SuppliersService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SuppliersServiceImpl extends ServiceImpl<SuppliersMapper, Suppliers> implements SuppliersService {

    @Override
    public void saveSupplier(SuppliersDto supplierDto) {
        Suppliers supplierBynaeme = getSupplierByNameAndMaterials(supplierDto.getSupplierName(),supplierDto.getMaterialName());
        if (supplierBynaeme !=null){
            throw new RuntimeException("该信息已存在");
        }
        Suppliers supplier = new Suppliers();
        BeanUtils.copyProperties(supplierDto,supplier);
        supplier.setCreateTime(LocalDateTime.now());
        supplier.setUpdateTime(LocalDateTime.now());
        save(supplier);
    }

    @Override
    public List<Suppliers> listByName(String supplierName) {
        List<Suppliers> list = lambdaQuery().like(Suppliers::getSupplierName, supplierName).list();
        if(list == null || list.isEmpty())
            throw new RuntimeException("没有相关信息");
        return list;
    }

    @Override
    public Suppliers getSupplierByNameAndMaterials(String supplierName,String materialsName) {
        Suppliers suppliers = lambdaQuery().eq(Suppliers::getSupplierName, supplierName)
                .eq(Suppliers::getMaterialName,materialsName).one();
        return suppliers;
    }


}
