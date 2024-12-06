package com.mxs.flowable.listener;

import com.mxs.domain.entity.WorkFlowOrder;
import com.mxs.enums.OrderStatusEnum;
import com.mxs.flowable.service.WorkFlowOrderService;
import com.mxs.flowable.utils.AppCtxHolder;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.api.Task;

import java.util.Map;

@Slf4j
public class FlowExecListener implements ExecutionListener {

    private static final long serialVersionUID = -1230947120947012345L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        boolean infoEnabled = log.isInfoEnabled();
        String processInstanceId = delegateExecution.getProcessInstanceId();
        String orderId = delegateExecution.getProcessInstanceBusinessKey();
        String eventName = delegateExecution.getEventName();
        boolean isEndingProcess = EVENTNAME_END.equals(eventName);
        Map<String, Object> variables = delegateExecution.getVariables();

        //因为当前类不处于IOC的管理中，所以通过AppCtxHolder获取代理执行监听器列表，再根据校验结果执行它们
        Map<String, DelegateExecListener> delMap = AppCtxHolder.getBeanOfType(DelegateExecListener.class);

        for (Map.Entry<String, DelegateExecListener> entry : delMap.entrySet()) {
            String delegateExecListenerBeanName = entry.getKey();
            DelegateExecListener delegateExecListener = entry.getValue();
            if (delegateExecListener.validate(orderId, eventName, delegateExecution)) {
                if (infoEnabled) {
                    log.info("({})通过校验,将执行其业务方法", delegateExecListenerBeanName);
                }
                delegateExecListener.doExecNotify(delegateExecution);
            }
        }

        if (isEndingProcess) {
            //与任务数据（任务表）相关的Service
            TaskService taskService = AppCtxHolder.getBean(TaskService.class);
            //流程执行到此时，因为流程未完结，所以act_ru_task表中还有记录，这条记录就是最后一个任务节点
            Task lastTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

            String lastTaskId = lastTask.getId();

            //更新工单表的工单状态为已归档
            String orderStatus = (String) variables.get("orderStatus");
            updateWorkOrderStatus(orderId, orderStatus);
            if (OrderStatusEnum.FINISHED.getValue().equals(orderStatus)) {
                taskService.addComment(lastTaskId, processInstanceId, "审批结束（归档）");
            } else if (OrderStatusEnum.CANCELED.getValue().equals(orderStatus)) {
                taskService.addComment(lastTaskId, processInstanceId, "审批取消（撤单）");
            }

        }
    }


    /**
     * 处理工单状态
     *
     * @param orderId     工单ID
     * @param orderStatus 当前工单状态
     */
    private void updateWorkOrderStatus(String orderId, String orderStatus) {

        String userId = "system";//这里为了测试写死成system,正式环境改成获取当前登录好的用户//AuthUtils.getUserId();
        WorkFlowOrder workFlowOrder = new WorkFlowOrder();
        workFlowOrder.setOrderId(orderId);
        workFlowOrder.setOrderStatus(orderStatus);
        workFlowOrder.setUpdateUser(userId);
        AppCtxHolder.getBean(WorkFlowOrderService.class).upsertByOrderId(workFlowOrder);

    }
}
