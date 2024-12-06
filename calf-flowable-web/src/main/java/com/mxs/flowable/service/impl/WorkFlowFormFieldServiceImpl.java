package com.mxs.flowable.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxs.domain.entity.WorkFlowFormField;
import com.mxs.flowable.mapper.WorkFlowFormFieldMapper;
import com.mxs.flowable.service.WorkFlowFormFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WorkFlowFormFieldServiceImpl extends ServiceImpl<WorkFlowFormFieldMapper, WorkFlowFormField> implements WorkFlowFormFieldService {

    @Autowired
    private WorkFlowFormFieldMapper workFlowFormFieldMapper;


    @Override
    public List<WorkFlowFormField> selectFieldCNameByOrderType(String orderType) {
        LambdaQueryWrapper<WorkFlowFormField> lqw = Wrappers.<WorkFlowFormField>lambdaQuery().eq(WorkFlowFormField::getOrderType, orderType);
        return workFlowFormFieldMapper.selectList(lqw);
    }
}



