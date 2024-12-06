package com.mxs.flowable.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mxs.domain.entity.WorkFlowAssignee;
import com.mxs.domain.entity.WorkFlowDef;
import com.mxs.domain.entity.WorkFlowFormField;
import com.mxs.enums.OrderStatusEnum;
import com.mxs.enums.TaskDefinitionKeyEnum;
import com.mxs.enums.TaskResultEnum;

import com.mxs.flowable.except.CustomException;
import com.mxs.flowable.mapper.FlowableMapper;
import com.mxs.flowable.service.CommonFlowService;
import com.mxs.flowable.service.WorkFlowAssigneeService;
import com.mxs.flowable.service.WorkFlowDefService;
import com.mxs.flowable.service.WorkFlowFormFieldService;
import com.mxs.flowable.utils.MapUtils;
import com.mxs.flowable.utils.ParameterUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
public class CommonFlowServiceImpl implements CommonFlowService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;
    @Autowired
    private FlowableMapper flowableMapper;
    @Autowired
    private WorkFlowDefService workFlowDefService;
    @Autowired
    private WorkFlowFormFieldService workFlowFormFieldService;
    @Autowired
    private WorkFlowAssigneeService workFlowAssigneeService;

    public static final String PROCESS_DEFINITION_KEY = "common";   //processDefinitionKey


    /**
     * 开启一个通用的审批流程
     *
     * @param variables 流程实例初始流程变量，会在`act_hi_varinst`表中保存
     *                  businessKey      唯一业务key，打开流程后可以通过此key查询流程实例，要求必须全局唯一
     *                  applicant        申请人
     *                  assigneesOrderId 获取任务节点列表(代理人列表)的工单号 说明：wo_assignee表以orderId为主键，
     *                  待开启的流程的任务节点列表(代理人列表)
     *                  将按主键分别为orderId、assigneesOrderId、orderType三种顺序查找 先查select *
     *                  from wo_assignee where orderId = #{orderId} and ...的情况
     *                  如无再查select * from wo_assignee where orderId =
     *                  #{assigneesOrderId} and ...的情况 再无最后查select * from wo_assignee
     *                  where orderId = #{orderType} and ...的情况
     * @return 流程实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startCommonFlow(Map<String, Object> variables) {

        String businessKey = MapUtils.getStringValue(variables, "businessKey");
        String applicant = MapUtils.getStringValue(variables, "applicant");
        String assigneesOrderId = MapUtils.getStringValue(variables, "assigneesOrderId");

        if (!StringUtils.hasText(businessKey)) {
            throw new CustomException("开启审批流程时，唯一业务key不能为空");
        }
        if (!StringUtils.hasText(applicant)) {
            throw new CustomException("开启审批流程时，申请人不能为空");
        }

        if (variables == null || variables.isEmpty()) {
            variables = new HashMap<>();
        }
        // 调用此方法设置流程操作用户线程变量，主要是供taskService.addComment方法内部逻辑使用，这个用户ID将存入到`act_hi_comment`中
        Authentication.setAuthenticatedUserId(applicant);

        // 获取首席代理人
        WorkFlowAssignee assignee = workFlowAssigneeService.getFirstAssigneeByOrderId(businessKey);
        if (assignee == null) {
            if (!ParameterUtils.isEmpty(assigneesOrderId)) {
                assignee = workFlowAssigneeService.getFirstAssigneeByOrderId(assigneesOrderId);
            }
            if (assignee == null) {
                String orderType = getOrderType(businessKey);
                assignee = workFlowAssigneeService.getFirstAssigneeByOrderId(orderType);
            }
        }
        //封装参数
        putAssigneeInfoToVariables(assignee, variables);
        //开启流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(PROCESS_DEFINITION_KEY, businessKey, variables);
        String processInstanceId = processInstance.getProcessInstanceId();
        //开启任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        String taskId = task.getId();

        taskService.addComment(taskId, processInstanceId, "提交申请");
        return processInstance;

    }


    /**
     * 通过orderId获取工单号前缀，然后查询出此工单是什么工单类型,比如QJ_20220501001,返回QJ
     *
     * @param orderId 工单号
     */
    private String getOrderType(String orderId) {

        if (StringUtils.hasText(orderId)) {
            String[] tempArray = orderId.split("_");
            if (tempArray.length != 2) {
                throw new CustomException("请传入正确的工单号");
            }
            String orderIdPre = tempArray[0];
            //根据orderIdPre前缀去数据库中查询
            WorkFlowDef def = workFlowDefService.getByOrderIdPre(orderIdPre);
            if (def == null) {
                new CustomException("请传入正确的工单号");
            } else {
                return def.getOrderType();
            }
        }
        throw new CustomException("请传入正确的工单号");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examAndApprove(String businessKey, String agree, String comment) {
        TaskResultEnum eaaResultEnum = TaskResultEnum.getTaskResultEnum(Integer.parseInt(agree));
        HashMap<String, Object> variables = new HashMap<>();

        if (ParameterUtils.isEmpty(variables)) {
            variables = new HashMap<>();
        } else {
            variables = new HashMap<>(variables);
        }
        variables.put("businessKey", businessKey);

        if (TaskDefinitionKeyEnum.EXAM_AND_APPROVE != eaaResultEnum.getTaskDefinitionKeyEnum()) {
            throw new CustomException("请使用(" + TaskDefinitionKeyEnum.EXAM_AND_APPROVE.name() + ")对应的任务结果枚举");
        }
        // result > 0 ：通过，交给下一个人再审
        // result == 0 ：通过，流程结束，直接归档
        // result == -999 ：不通过，直接取消审批(撤单)
        // result < 0 && result != -999 ：不通过，下一步修改申请
        variables.put("result", eaaResultEnum.getResult());
        completeTask(businessKey, variables, comment, TaskDefinitionKeyEnum.EXAM_AND_APPROVE);
    }

    @Override
    public List<Comment> listTaskComments(String orderId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(orderId).singleResult();
        String processInstanceId = historicProcessInstance.getId();

        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
        if (ParameterUtils.isEmpty(processInstanceComments)) {
            return new ArrayList<>(0);
        }
        return processInstanceComments;
    }


    private void putAssigneeInfoToVariables(WorkFlowAssignee assignee, Map<String, Object> variables) {
        if (assignee == null) {
            throw new CustomException("找不到流程任务节点代理人列表的配置，请联系开发人员排查");
        }
        // 流程定义时使用assignee为代理人/审批人/处理人变量
        variables.put("assignee", assignee.getAssignee());
        // 指定当前工单的审批流程使用的是哪个版本的审批人员列表
        variables.put("assigneeVersion", assignee.getAssigneeVersion());
        // 指定当前工单的审批流程使用的是第几个代理人/审批人/处理人
        variables.put("assigneeOrder", assignee.getAssigneeOrder());
        // 设置工单状态，监听器会用于更新工单表的状态
        variables.put("orderStatus", assignee.getOrderStatus());
    }

    /**
     * 完成任务
     *
     * @param businessKey                   唯一业务key
     * @param variables                     完成任务时或给下一个节点使用的参数
     * @param comment                       完成任务时的备注/注释
     * @param expectedTaskDefinitionKeyEnum 期望完成的任务定义key的枚举，用于判断被完成的任务是否期望的任务
     */
    private void completeTask(String businessKey, Map<String, Object> variables, String comment,
                              TaskDefinitionKeyEnum expectedTaskDefinitionKeyEnum) {


        Assert.notNull(businessKey, "唯一业务key不能为空");
        Assert.notNull(expectedTaskDefinitionKeyEnum, "期望完成的任务定义key不能为空");
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        String taskName = task.getName();

        String expectedTaskDefinitionKey = expectedTaskDefinitionKeyEnum.getTaskDefinitionKey();
        String expectedTaskDefinition = expectedTaskDefinitionKeyEnum.getTaskDefinition();
        if (!expectedTaskDefinitionKey.equals(taskDefinitionKey)) {
            throw new CustomException(String.format("当前流程执行到“%s”，非“%s”这一步", taskName, expectedTaskDefinition));
        }
        String taskId = task.getId();
        String assignee = task.getAssignee();
        String processInstanceId = task.getProcessInstanceId();
        Map<String, Object> processVariables = taskService.getVariables(taskId);
        String assigneesOrderId = (String) processVariables.get("assigneesOrderId");
        Integer assigneeVersion = (Integer) processVariables.get("assigneeVersion");
        Integer assigneeOrder = (Integer) processVariables.get("assigneeOrder");
        Integer result = (Integer) variables.get("result");
        Authentication.setAuthenticatedUserId(assignee);

        // 判断当前任务节点是否是“审批”
        if (TaskDefinitionKeyEnum.EXAM_AND_APPROVE.getTaskDefinitionKey().equals(taskDefinitionKey)) {
            if (result >= 0) {
                // 把下一个任务节点的代理人信息放入到variables中
                ++assigneeOrder;
                putNextAssigneeInfoToVariables(assigneesOrderId, businessKey, assigneeVersion, assigneeOrder,
                        variables);
                comment = ParameterUtils.isEmpty(comment) ? "通过" : comment;
            } else if (result == TaskResultEnum.EXAM_AND_APPROVE_REJECT_TO_CANCEL.getResult()) {
                variables.put("orderStatus", OrderStatusEnum.CANCELED.getValue());
            } else {
                variables.put("orderStatus", OrderStatusEnum.WAIT_FOR_UPDATE.getValue());
                comment = ParameterUtils.isEmpty(comment) ? "不通过" : comment;
            }
        }
        // 如果不是“审批”节点，那就是“修改”节点
        else {
            // -999代表取消审批，如果不是-999，则要从初审开始重新审批
            if (result == TaskResultEnum.MODIFICATION_REJECT_TO_CANCEL.getResult()) {
                variables.put("orderStatus", OrderStatusEnum.CANCELED.getValue());
            } else {
                // 把第一个任务节点的代理人信息放回到variables中
                assigneeOrder = 1;
                putNextAssigneeInfoToVariables(assigneesOrderId, businessKey, assigneeVersion, assigneeOrder,
                        variables);
                comment = ParameterUtils.isEmpty(comment) ? "修改完成，重新审批" : comment;
            }
        }

        taskService.addComment(taskId, processInstanceId, ParameterUtils.isEmpty(comment) ? "（无批注）" : comment);
        taskService.complete(taskId, variables);
    }

    private void putNextAssigneeInfoToVariables(String assigneesOrderId, String businessKey, Integer assigneeVersion,
                                                Integer assigneeOrder, Map<String, Object> variables) {

        // 先查询是否有给此工单特殊指定了审批人员列表，如果没有则查询对应工单类型通用的审批人员列表
        WorkFlowAssignee woAssignee = getByOrderIdVersionOrder(businessKey, assigneeVersion, assigneeOrder);
        if (woAssignee == null) {
            if (!ParameterUtils.isEmpty(assigneesOrderId)) {
                woAssignee = getByOrderIdVersionOrder(assigneesOrderId, assigneeVersion,
                        assigneeOrder);
            }
            if (woAssignee == null) {
                String orderType = getOrderType(businessKey);
                woAssignee = getByOrderIdVersionOrder(orderType, assigneeVersion,
                        assigneeOrder);
            }
        }
        // 如果查询不到下一个审批人了，那就说明可以归档结束流程了
        if (woAssignee == null) {
            variables.put("result", TaskResultEnum.EXAM_AND_APPROVE_PASS_TO_END.getResult());
            variables.put("orderStatus", OrderStatusEnum.FINISHED.getValue());
        } else {
            putAssigneeInfoToVariables(woAssignee, variables);
        }
    }

    private WorkFlowAssignee getByOrderIdVersionOrder(String orderId, Integer assigneeVersion, Integer assigneeOrder) {
        LambdaQueryWrapper<WorkFlowAssignee> lqw = Wrappers.<WorkFlowAssignee>lambdaQuery()
                .eq(WorkFlowAssignee::getOrderId, orderId)
                .eq(WorkFlowAssignee::getAssigneeVersion, assigneeVersion)
                .eq(WorkFlowAssignee::getAssigneeOrder, assigneeOrder);
        return workFlowAssigneeService.getOne(lqw);
    }


    public void modification(String businessKey, TaskResultEnum modifyResultEnum, String msg) {
        HashMap<String, Object> variables = new HashMap<>();
        if (TaskDefinitionKeyEnum.MODIFICATION != modifyResultEnum.getTaskDefinitionKeyEnum()) {
            throw new CustomException("请使用({})对应的任务结果枚举:" + TaskDefinitionKeyEnum.MODIFICATION.name());
        }
        //获取当前的任务的定义key，用于判断当前任务是待初审时的修改、初审不通过后的修改、初审通过待终审时的修改、终审不通过后的修改
        Task currentTask = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        Assert.notNull(currentTask, "找不到工单({}), 无法进提交修改操作" + businessKey);
        String currentTaskDefinitionKey = currentTask.getTaskDefinitionKey();
        if (TaskDefinitionKeyEnum.EXAM_AND_APPROVE.getTaskDefinitionKey().equals(currentTaskDefinitionKey)) {
            if (isTheFirstAssigneeNow(businessKey, currentTask)) {
                //初审且未进行审批时，允许修改，但不用执行完成修改节点的操作，所以return
                return;
            } else {
                throw new CustomException("流程已经初审通过，不允许在待审批状态下修改工单");
            }
        }
        //result < 0 && result != -999 ：修改完成，下一步初审
        //result == -999 ：不通过，直接取消审批(撤单)
        variables.put("result", modifyResultEnum.getResult());
        completeTask(businessKey, variables, msg, TaskDefinitionKeyEnum.MODIFICATION);
    }

    /**
     * 校验是否第一次申请
     *
     * @param businessKey
     * @param currentTask
     * @return
     */
    @Override
    public boolean isTheFirstAssigneeNow(String businessKey, Task currentTask) {
        if (ParameterUtils.isEmpty(businessKey)) {
            return false;
        }
        if (currentTask == null) {
            currentTask = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
            if (currentTask == null) {
                return false;
            }
        }
        Map<String, Object> processVariables = taskService.getVariables(currentTask.getId());
        return (Integer) processVariables.get("assigneeOrder") == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCurrenInstance(String businessKey, String deleteReason) {
        //根据业务id查询出流程实例
        List<HistoricProcessInstance> historyProcess = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey).notDeleted().list();

        if (ParameterUtils.isEmpty(deleteReason)) {
            deleteReason = "未指明删除原因";
        }

        for (int i = 0; i < historyProcess.size(); i++) {
            HistoricProcessInstance historicProcessInstance = historyProcess.get(i);
            String processInstanceId = historicProcessInstance.getId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            //流程结束后，流程实例就查不到了，所以要避免删除不存在的实例而报错
            if (processInstance != null) {
                runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
            }
            //先删除runtime数据再删除历史数据(顺序不能换)
            historyService.deleteHistoricProcessInstance(processInstanceId);
        }
    }

    @Override
    public void deleteFlowPhysically(String businessKey) {
        flowableMapper.deleteProcessInstancePhysically(businessKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFlow(Map<String, Object> params) {
        String orderType = MapUtils.getStringValue(params, "orderType");
        WorkFlowDef workFlowDef = JSON.parseObject(JSON.toJSONString(params), WorkFlowDef.class);
        workFlowDef.setOrderIdPrex(orderType.toUpperCase());
        List<Map<String, Object>> fieldsMap = (List<Map<String, Object>>) params.get("fields");
        List<WorkFlowFormField> workFlowFormFieldList = new ArrayList<>();
        for (Map<String, Object> map : fieldsMap) {
            String jsonString = JSON.toJSONString(map);
            WorkFlowFormField field = JSON.parseObject(jsonString, WorkFlowFormField.class);
            field.setOrderType(workFlowDef.getOrderType());
            workFlowFormFieldList.add(field);
        }

        List<Map<String, Object>> assigneesMap = (List<Map<String, Object>>) params.get("assignees");
        List<WorkFlowAssignee> workFlowAssigneeList = new ArrayList<>();
        for (Map<String, Object> map : assigneesMap) {
            String jsonString = JSON.toJSONString(map);
            WorkFlowAssignee assignee = JSON.parseObject(jsonString, WorkFlowAssignee.class);
            assignee.setOrderId(workFlowDef.getOrderType());
            assignee.setTaskName("审批");
            workFlowAssigneeList.add(assignee);
        }

        //新增流程
        workFlowDefService.save(workFlowDef);
        //新增表单字段
        workFlowFormFieldService.saveBatch(workFlowFormFieldList);
        //新增代理人
        workFlowAssigneeService.saveBatch(workFlowAssigneeList);
    }
}
