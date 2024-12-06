package com.mxs.flowable.listener;

import com.mxs.domain.entity.WorkFlowOrder;
import com.mxs.flowable.service.WorkFlowOrderService;
import com.mxs.flowable.utils.AppCtxHolder;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component
public class FlowTaskListener implements TaskListener {

    private static final long serialVersionUID = -9018236501850831510L;


    @Override
    public void notify(DelegateTask delegateTask) {

        boolean infoEnabled = log.isInfoEnabled();

        //从开启实例中获取数据
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String taskAssignee = delegateTask.getAssignee();
        String taskEventName = delegateTask.getEventName();
        Map<String, Object> variables = delegateTask.getVariables();
        String orderId = variables.get("businessKey").toString();

        //根据所执行到的流程任务更新工单表的工单状态
        if (EVENTNAME_CREATE.equals(taskEventName)) {
            String orderStatus = (String) variables.get("orderStatus");
            updateWorkOrderStatus(orderId, orderStatus, taskAssignee);
        }

        //因为当前类不处于IOC的管理中，所以通过AppCtxHolder获取代理执行监听器列表，再根据校验结果执行它们
        Map<String, DelegateTaskListener> dtlMap = AppCtxHolder.getBeanOfType(DelegateTaskListener.class);

        for (Map.Entry<String, DelegateTaskListener> entry : dtlMap.entrySet()) {
            String delegateTaskListenerBeanName = entry.getKey();
            DelegateTaskListener delegateTaskListener = entry.getValue();

            if (delegateTaskListener.validate(orderId, taskDefinitionKey, taskEventName, delegateTask)) {
                if (infoEnabled) {
                    log.info("({})通过校验,将执行其业务方法", delegateTaskListenerBeanName);
                }
                delegateTaskListener.doTaskNotify(delegateTask);
            }
        }
    }

    /**
     * 处理工单状态
     *
     * @param orderId     工单ID
     * @param orderStatus 当前工单状态
     * @param handler     当前处理人的用户ID
     */
    private void updateWorkOrderStatus(String orderId, String orderStatus, String handler) {

        String userId = "system";//这里为了测试写死成system,正式环境改成获取当前登录好的用户//AuthUtils.getUserId();
        WorkFlowOrder workFlowOrder = new WorkFlowOrder();
        workFlowOrder.setOrderId(orderId);
        workFlowOrder.setOrderStatus(orderStatus);
        workFlowOrder.setHandler(handler);
        workFlowOrder.setUpdateUser(userId);
        AppCtxHolder.getBean(WorkFlowOrderService.class).upsertByOrderId(workFlowOrder);

    }

}
