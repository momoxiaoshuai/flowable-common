package com.mxs.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mxs.domain.dto.R;
import com.mxs.domain.entity.WorkFlowAssignee;
import com.mxs.flowable.service.WorkFlowAssigneeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/assignee")
public class WorkFlowAssigneeController {
    @Autowired
    private WorkFlowAssigneeService workFlowAssigneeService;

    @GetMapping("/list")
    public R<List<WorkFlowAssignee>> list() {
        List<WorkFlowAssignee> workFlowAssigneeList = workFlowAssigneeService.list();
        return R.success(workFlowAssigneeList);
    }

    @GetMapping("/orderType")
    public R<List<WorkFlowAssignee>> listByType(String orderType) {
        Assert.notNull(orderType, "代理类型不能为空");
        LambdaQueryWrapper<WorkFlowAssignee> lqw = Wrappers.<WorkFlowAssignee>lambdaQuery().eq(WorkFlowAssignee::getOrderId, orderType);
        List<WorkFlowAssignee> workFlowAssigneeList = workFlowAssigneeService.list(lqw);
        return R.success(workFlowAssigneeList);
    }

    @PostMapping("/insert")
    public R<String> insert(WorkFlowAssignee workFlowAssignee) {
        Assert.notNull(workFlowAssignee.getOrderId(), "流程ID不能为空");
        Assert.notNull(workFlowAssignee.getAssignee(), "代理人不能为空");
        workFlowAssignee.setAssignee("1");
        Date now = new Date();
        workFlowAssignee.setCreateTime(now);
        workFlowAssignee.setUpdateTime(now);
        workFlowAssigneeService.save(workFlowAssignee);
        return R.success();
    }

    @PostMapping("/update")
    public R<String> update(WorkFlowAssignee workFlowAssignee) {
        Integer id = workFlowAssignee.getId();
        Assert.notNull(id, "代理ID不能为空");
        workFlowAssigneeService.updateById(workFlowAssignee);
        return R.success();
    }

    @GetMapping("/delete")
    public R<String> delete(Integer id) {
        Assert.notNull(id, "ID不能为空");
        workFlowAssigneeService.removeById(id);
        return R.success();
    }

}
