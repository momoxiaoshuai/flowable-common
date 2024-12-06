package com.mxs.flowable.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mxs.domain.dto.R;
import com.mxs.domain.entity.WorkFlowFormField;
import com.mxs.flowable.service.WorkFlowFormFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自定义流程表单控制器
 */
@Slf4j
@RestController
@RequestMapping("/filed")
public class WorkFlowFormFieldController {

    @Autowired
    private WorkFlowFormFieldService workFlowFormFieldService;


    @GetMapping("/list")
    public R<List<WorkFlowFormField>> list() {
        List<WorkFlowFormField> workFlowFormFieldList = workFlowFormFieldService.list();
        return R.success(workFlowFormFieldList);
    }

    @GetMapping("orderType")
    public R<List<WorkFlowFormField>> selectByOrderType(String orderType) {
        List<WorkFlowFormField> workFlowFormFieldList = workFlowFormFieldService.list(Wrappers.<WorkFlowFormField>lambdaQuery().eq(WorkFlowFormField::getOrderType, orderType));
        return R.success(workFlowFormFieldList);
    }


    @PostMapping("/insert")
    public R<Void> insert(@RequestBody WorkFlowFormField workFlowFormField) {
        workFlowFormFieldService.save(workFlowFormField);
        return R.success();
    }

    @PostMapping("/update")
    public R<Void> update(@RequestBody WorkFlowFormField workFlowFormField) {
        workFlowFormFieldService.updateById(workFlowFormField);
        return R.success();
    }

    @GetMapping("delete")
    public R<Void> delete(Integer id) {
        workFlowFormFieldService.removeById(id);
        return R.success();
    }

    @GetMapping("deleteBatch")
    public R<Void> deleteBatch(List<Integer> ids) {
        workFlowFormFieldService.removeBatchByIds(ids);
        return R.success();
    }
}
