package com.mxs.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxs.domain.entity.WorkFlowDef;
import com.mxs.domain.entity.WorkFlowOrder;
import com.mxs.enums.OrderStatusEnum;
import com.mxs.enums.TaskResultEnum;
import com.mxs.flowable.mapper.WorkFlowDefMapper;
import com.mxs.flowable.mapper.WorkFlowOrderMapper;
import com.mxs.flowable.service.CommonFlowService;
import com.mxs.flowable.service.WorkFlowOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class WorkFlowOrderServiceImpl extends ServiceImpl<WorkFlowOrderMapper, WorkFlowOrder> implements WorkFlowOrderService {

    @Autowired
    private WorkFlowDefMapper workFlowDefMapper;
    @Resource
    private WorkFlowOrderMapper workFlowOrderMapper;
    @Autowired
    private CommonFlowService commonFlowService;

    @Override
    public List<WorkFlowOrder> selectOrdersByHandler(String handler, String orderType) {
        LambdaQueryWrapper<WorkFlowOrder> lqw = Wrappers.<WorkFlowOrder>lambdaQuery()
                .eq(WorkFlowOrder::getHandler, handler)
                .eq(WorkFlowOrder::getOrderStatus, 1)
                .eq(WorkFlowOrder::getOrderType, orderType);
        return this.workFlowOrderMapper.selectList(lqw);
    }

    @Override
    public List<WorkFlowOrder> selectOrdersByApplicant(String applicant) {
        LambdaQueryWrapper<WorkFlowOrder> lqw = Wrappers.<WorkFlowOrder>lambdaQuery().eq(WorkFlowOrder::getApplicant, applicant);
        return this.workFlowOrderMapper.selectList(lqw);
    }

    @Override
    public List<WorkFlowOrder> selectMyHandled(String handler) {
        return workFlowOrderMapper.selectMyHandled(handler);
    }

    @Override
    public WorkFlowOrder addWorkOrder(String applicant, String orderType, String formJsonStr) {

        /*String subjectType = MapUtils.getStringValue(params, "subjectType");
        Integer subjectId = MapUtils.getIntegerValue(params, "subjectId");*/

        WorkFlowDef def = getByOrderType(orderType);
        if (def == null) {
            throw new RuntimeException("工单类型orderType参数非法");
        }
        String orderId = def.getOrderIdPrex() + "_" + System.currentTimeMillis();

        WorkFlowOrder workOrder = new WorkFlowOrder();
        workOrder.setOrderId(orderId);//orderId,工单ID
        workOrder.setOrderType(orderType);//工单类型
        //workOrder.setSubjectId(subjectId);//工单所属主体的ID.关联其他表时，存其他表的记录的ID
        //workOrder.setSubjectType(subjectType);//工单所属主体的类型，关联其他表时，存其他表的类型
        workOrder.setApplicant(applicant);//申请人登陆账号
        workOrder.setOrderSummary("{\"title\":\"" + def.getOrderName() + "\",\"createTime\":\"" + new Date() + "\",\"userId\":\"" + applicant + "\"}");
        workOrder.setOrderStatus(OrderStatusEnum.WAIT_FOR_VERIFY.getValue());//工单状态\
        workOrder.setReason("");//提单原因
        workOrder.setFormData(formJsonStr);//工单可变字段数据,json文本存储
        workOrder.setRemark("");//remark
        workOrder.setHandler("");//处理人
        workOrder.setCreateUser(applicant);
        workOrder.setUpdateUser(applicant);
        //保存工单表单
        workFlowOrderMapper.insert(workOrder);
        //1.启动流程
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("businessKey", orderId);
        variables.put("applicant", workOrder.getApplicant());
        commonFlowService.startCommonFlow(variables);
        return workOrder;

    }

    @Override
    public void upsertByOrderId(WorkFlowOrder workFlowOrder) {
        LambdaQueryWrapper<WorkFlowOrder> lqw = Wrappers.<WorkFlowOrder>lambdaQuery().eq(WorkFlowOrder::getOrderId, workFlowOrder.getOrderId());
        WorkFlowOrder order = workFlowOrderMapper.selectOne(lqw);
        workFlowOrder.setId(order.getId());
        workFlowOrderMapper.updateById(workFlowOrder);
    }

    @Override
    public void modificationWorkOrder(WorkFlowOrder workOrder) {
        //修改数据
        workOrder.setUpdateTime(new Date());
        workFlowOrderMapper.updateById(workOrder);
        //调用service进行审批操作
        String businessKey = workOrder.getOrderId();
        commonFlowService.modification(businessKey, TaskResultEnum.MODIFICATION_PASS_TO_FIRST, "重新提交申请");
    }

    @Override
    public WorkFlowOrder selectOrderByOrderId(String orderId) {
        LambdaQueryWrapper<WorkFlowOrder> lqw = Wrappers.<WorkFlowOrder>lambdaQuery().eq(WorkFlowOrder::getOrderId, orderId);
        return workFlowOrderMapper.selectOne(lqw);
    }

    @Override
    public List<WorkFlowOrder> selectByOrderType(String orderType) {
        LambdaQueryWrapper<WorkFlowOrder> lqw = Wrappers.<WorkFlowOrder>lambdaQuery().eq(WorkFlowOrder::getOrderType, orderType);
        return workFlowOrderMapper.selectList(lqw);
    }

    /**
     * 根据类型获取
     *
     * @param orderType 工单类型
     * @return 自定流程
     */
    private WorkFlowDef getByOrderType(String orderType) {
        LambdaQueryWrapper<WorkFlowDef> lqw = Wrappers.<WorkFlowDef>lambdaQuery().eq(WorkFlowDef::getOrderType, orderType);
        return workFlowDefMapper.selectOne(lqw);
    }


}
