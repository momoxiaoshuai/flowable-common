package com.mxs.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxs.domain.entity.WorkFlowAssignee;

public interface WorkFlowAssigneeService extends IService<WorkFlowAssignee> {

    /**
     * 获取第一代理人
     * @param orderType 流程类型
     * @return 代理人
     */
    WorkFlowAssignee getFirstAssigneeByOrderId(String orderType);
}
