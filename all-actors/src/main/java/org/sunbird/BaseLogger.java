package org.sunbird;

import org.slf4j.MDC;

import java.util.Map;

public class BaseLogger {

    /**
     * sets requestId in Sl4j MDC
     * @param trace
     */
    public void setReqId(Map<String, Object> trace) {
        MDC.clear();
        MDC.put(JsonKeys.REQ_ID, (String) trace.get(JsonKeys.REQ_ID));
    }

    public String getReqId() {
        return MDC.get(JsonKeys.REQ_ID);
    }
}