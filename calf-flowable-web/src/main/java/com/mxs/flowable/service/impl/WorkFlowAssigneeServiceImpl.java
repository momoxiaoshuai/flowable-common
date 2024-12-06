package com.mxs.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxs.domain.entity.WorkFlowAssignee;
import com.mxs.flowable.mapper.WorkFlowAssigneeMapper;
import com.mxs.flowable.service.WorkFlowAssigneeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class WorkFlowAssigneeServiceImpl extends ServiceImpl<WorkFlowAssigneeMapper, WorkFlowAssignee> implements WorkFlowAssigneeService {

    @Resource
    private WorkFlowAssigneeMapper workFlowAssigneeMapper;

    @Override
    public WorkFlowAssignee getFirstAssigneeByOrderId(String orderType) {
        LambdaQueryWrapper<WorkFlowAssignee> lqw = Wrappers.<WorkFlowAssignee>lambdaQuery()
                .eq(WorkFlowAssignee::getOrderId, orderType) // 条件过滤
                .orderByAsc(WorkFlowAssignee::getAssigneeOrder) // 按 AssigneeOrder 正序排列
                .last("LIMIT 1"); // 只取第一条记录
        return workFlowAssigneeMapper.selectOne(lqw);
    }
}
