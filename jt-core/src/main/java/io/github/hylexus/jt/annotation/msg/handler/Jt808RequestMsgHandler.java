package io.github.hylexus.jt.annotation.msg.handler;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2020-02-01 2:52 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestMsgHandler {

    int msgType() default 0;

    String desc() default "";
}
