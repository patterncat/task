package com.peaceful.task.executor.dispatch.actor;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import com.peaceful.task.context.TaskExecutor;
import com.peaceful.task.context.coding.TUR;
import com.peaceful.task.executor.dispatch.msg.DecorateTask;
import scala.concurrent.duration.Duration;

/**
 * execute 监管者,如果executor执行Runnable失败,策略是重启该Actor,目的是重启下面的executor
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class ExecutorActor extends UntypedActor {

    // // TODO: 16/1/12 测试executor失败的情况 
    TaskExecutor taskExecutor;
    DiagnosticLoggingAdapter log = Logging.getLogger(this);

    public ExecutorActor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof TUR) {
            TUR taskUnit = (TUR) o;
            DecorateTask decorate = new DecorateTask(taskUnit,self());
            taskExecutor.execute(decorate);
        } else {
            unhandled(o);
        }
    }

    /**
     * supervisor is restart
     *
     * @return
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        SupervisorStrategy strategy = new OneForOneStrategy(
                10, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) {
                log.error("{} supervisor will restart child actor exception {}", self().path().name(), t);
                return SupervisorStrategy.restart();
            }
        });
        return strategy;
    }

}
