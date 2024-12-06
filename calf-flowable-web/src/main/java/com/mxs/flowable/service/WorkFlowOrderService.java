package com.mxs.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxs.domain.entity.WorkFlowOrder;

import java.util.List;

public interface WorkFlowOrderService extends IService<WorkFlowOrder> {

    /**
     * 根据当前处理人获取工单单
     *
     * @param handler 当前处理人
     * @return 工单
     */
    List<WorkFlowOrder> selectOrdersByHandler(String handler, String orderType);

    /**
     * 根据申请人获取工单
     *
     * @param applicant 申请人
     * @return 工单
     */
    List<WorkFlowOrder> selectOrdersByApplicant(String applicant);


    List<WorkFlowOrder> selectMyHandled(String handler);

    /**
     * 新增工单
     *
     * @param applicant   申请人
     * @param orderType   流程类型
     * @param formJsonStr 申请表单数据
     * @return 工单
     */
    WorkFlowOrder addWorkOrder(String applicant, String orderType, String formJsonStr);

    /**
     * 根据orderId修改
     *
     * @param workFlowOrder 流程工单
     */
    void upsertByOrderId(WorkFlowOrder workFlowOrder);

    /**
     * 修改并重新提交
     *
     * @param workOrder 流程工单
     */
    void modificationWorkOrder(WorkFlowOrder workOrder);

    /**
     * 根据工单ID查询工单
     *
     * @param orderId 工单Id
     * @return 流程工单
     */
    WorkFlowOrder selectOrderByOrderId(String orderId);

    List<WorkFlowOrder> selectByOrderType(String orderType);
}
