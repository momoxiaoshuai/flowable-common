package com.mxs.flowable.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowableMapper {

    /**
     * 物理删除流程实例，包含变量数据和历史数据
     *
     * @param businessKey 唯一业务key
     * @return int
     */
    @Delete("DELETE HP, EL, HT, HV, HI, HA, RV, RT, RI, RE, HC " +
            "FROM `ACT_HI_PROCINST` HP " +
            "LEFT JOIN `ACT_EVT_LOG` EL ON EL.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_TASKINST` HT ON HT.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_VARINST` HV ON HV.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_IDENTITYLINK` HI ON HI.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_ACTINST` HA ON HA.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_COMMENT` HC ON HC.TASK_ID_ = HT.ID_ " +
            "LEFT JOIN `ACT_RU_VARIABLE` RV ON RV.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_RU_TASK` RT ON RT.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_RU_IDENTITYLINK` RI ON RI.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_RU_EXECUTION` RE ON RE.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "WHERE HP.BUSINESS_KEY_ = #{businessKey};")
    int deleteProcessInstancePhysically(String businessKey);


    @Delete("DELETE HP, EL, HT, HV, HI, HA, RV, RT, RI, RE, HC " +
            "FROM `ACT_HI_PROCINST` HP " +
            "LEFT JOIN `ACT_EVT_LOG` EL ON EL.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_TASKINST` HT ON HT.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_VARINST` HV ON HV.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_IDENTITYLINK` HI ON HI.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_ACTINST` HA ON HA.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_HI_COMMENT` HC ON HC.TASK_ID_ = HT.ID_ " +
            "LEFT JOIN `ACT_RU_VARIABLE` RV ON RV.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_RU_TASK` RT ON RT.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_RU_IDENTITYLINK` RI ON RI.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "LEFT JOIN `ACT_RU_EXECUTION` RE ON RE.PROC_INST_ID_ = HP.PROC_INST_ID_ " +
            "WHERE HP.BUSINESS_KEY_ NOT IN (SELECT `ORDER_ID` FROM `WORK_FLOW_ORDER`);")
    int clearProcessInstancesPhysically();

}
