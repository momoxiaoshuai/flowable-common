package com.mxs.flowable.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mxs.domain.entity.WorkFlowFormField;

import java.util.List;


public interface WorkFlowFormFieldService  extends IService<WorkFlowFormField> {

    /**
     * 根据orderType获取字段名
     * @param orderType 流程类型
     * @return 流程表单字段集合
     */
    List<WorkFlowFormField> selectFieldCNameByOrderType(String orderType);
}
