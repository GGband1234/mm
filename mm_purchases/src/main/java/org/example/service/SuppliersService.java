package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.SuppliersDto;
import org.example.domain.po.Suppliers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SuppliersService extends IService<Suppliers> {
    @Transactional
    void saveSupplier(SuppliersDto supplierDto);

    List<Suppliers> listByName(String supplierName);
    Suppliers getSupplierByNameAndMaterials(String supplierName,String materialsName);

}
