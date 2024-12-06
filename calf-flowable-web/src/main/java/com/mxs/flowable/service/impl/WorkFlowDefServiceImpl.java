package com.mxs.flowable.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxs.domain.entity.WorkFlowDef;
import com.mxs.flowable.mapper.WorkFlowDefMapper;
import com.mxs.flowable.service.WorkFlowDefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WorkFlowDefServiceImpl extends ServiceImpl<WorkFlowDefMapper, WorkFlowDef> implements WorkFlowDefService {

    @Autowired
    private WorkFlowDefMapper workFlowMapper;


    @Override
    public WorkFlowDef getByOrderIdPre(String orderIdPre) {
        LambdaQueryWrapper<WorkFlowDef> lqw = Wrappers.<WorkFlowDef>lambdaQuery().eq(WorkFlowDef::getOrderIdPrex, orderIdPre);
        return workFlowMapper.selectOne(lqw);
    }
}
