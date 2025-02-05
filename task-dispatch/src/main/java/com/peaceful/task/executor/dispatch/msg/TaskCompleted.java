package com.peaceful.task.executor.dispatch.msg;


import com.peaceful.task.context.coding.TUR;

/**
 * confirm task unit completed msg
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class TaskCompleted {

    // task id
    public String id;
    // queue of task
    public String queue;
    // execute of task
    public String executor;
    // task 提交时间
    public long submitTime;
    // task 拉取时间
    public long createTime;
    // task 开始时间
    public long startTime;
    // task 完成时间
    public long completeTime;

    public TaskCompleted(TUR taskUnit) {
        this.id = taskUnit.getTask().id;
        this.queue = taskUnit.getTask().queueName;
        this.executor = taskUnit.getTask().executor;
        this.createTime = taskUnit.createTime;
        this.submitTime = taskUnit.getTask().getSubmitTime();
    }

}
