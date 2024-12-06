package com.mxs.flowable.controller;


import com.mxs.domain.dto.R;
import com.mxs.domain.entity.WorkFlowDef;
import com.mxs.flowable.service.WorkFlowDefService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 自定义流程控制器 基于通用工作流基础上完成自定义业务
 */
@Slf4j
@RestController
@RequestMapping("def")
public class WorkFlowDefController {

    @Autowired
    private WorkFlowDefService workFlowDefService;

    /**
     * 获取自定义流程列表
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<WorkFlowDef>> list() {
        List<WorkFlowDef> list = workFlowDefService.list();
        return R.success(list);
    }

    /**
     * 根据主键查询
     *
     * @param id 主键id
     * @return
     */
    @GetMapping("/getById")
    public R<WorkFlowDef> getById(int id) {
        WorkFlowDef workFlowDef = workFlowDefService.getById(id);
        return R.success(workFlowDef);
    }

    /**
     * 新增
     *
     * @param workFlowDef
     * @return
     */
    @PostMapping("/insert")
    public R<Void> insert(@RequestBody WorkFlowDef workFlowDef) {
        workFlowDefService.save(workFlowDef);
        return R.success();
    }

    /**
     * 修改
     *
     * @param workFlowDef
     * @return
     */
    @PostMapping("/update")
    public R<Void> update(@RequestBody WorkFlowDef workFlowDef) {
        workFlowDefService.updateById(workFlowDef);
        return R.success();
    }

    /**
     * 删除
     *
     * @param id 主键id
     * @return
     */
    @GetMapping("/delete")
    public R<Void> delete(int id) {
        workFlowDefService.removeById(id);
        return R.success();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @GetMapping("/deleteBatch")
    public R<Void> deleteBatch(List<Integer> ids) {
        workFlowDefService.removeBatchByIds(ids);
        return R.success();
    }
}
