package com.mxs.flowable.service;

import com.mxs.enums.TaskResultEnum;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

public interface CommonFlowService {

    /**
     * 开启通用流程
     * @param variables
     * @return 流程实例
     */
    ProcessInstance startCommonFlow(Map<String, Object> variables);

    /**
     * 审批申请
     */
    void examAndApprove(String businessKey, String agree, String comment);

    /**
     * 查看实例进度
     *
     * @param orderId 工单Id
     * @return 进度
     */
    List<Comment> listTaskComments(String orderId);

    /**
     * 修改重新提交申请
     * @param businessKey
     * @param taskResultEnum
     * @param msg
     */
    void modification(String businessKey, TaskResultEnum taskResultEnum, String msg);

    /**
     * 校验 是否为第一次申请
     * @param businessKey
     * @param currentTask
     * @return
     */
    boolean isTheFirstAssigneeNow(String businessKey, Task currentTask);

    /**
     * 逻辑删除流程实例
     *
     * @param businessKey  唯一业务key
     * @param deleteReason 删除原因
     */
    void deleteCurrenInstance(String businessKey, String deleteReason);

    /**
     * 物理删除流程实例
     *
     * @param businessKey 唯一业务key
     */
    void deleteFlowPhysically(String businessKey);

    /**
     *
     * @param params
     */
    void saveFlow(Map<String,Object> params);
}
