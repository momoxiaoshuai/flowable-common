package com.mxs.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxs.domain.entity.WorkFlowDef;


/**
 * 维护用户定义的流程名称
 */
public interface WorkFlowDefService extends IService<WorkFlowDef> {

    /**
     * 根据工单前缀获取自定义流程
     * @param orderIdPre 工单前缀
     * @return  自定义流程
     */
    WorkFlowDef getByOrderIdPre(String orderIdPre);
}
