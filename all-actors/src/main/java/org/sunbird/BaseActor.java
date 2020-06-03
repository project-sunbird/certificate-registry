package org.sunbird;

import akka.actor.UntypedAbstractActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import org.slf4j.MDC;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.Localizer;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author Amit Kumar
 */
public abstract class BaseActor extends UntypedAbstractActor {
    public final DiagnosticLoggingAdapter logger = Logging.getLogger(this);
    public abstract void onReceive(Request request) throws Throwable;
    protected Localizer localizer = Localizer.getInstance();

    @Override
    public void onReceive(Object message) throws Throwable {
        Map<String, Object> mdc = new HashMap<>();
        mdc.put(JsonKeys.REQ_ID, UUID.randomUUID());
        logger.setMDC(mdc);
        //set mdc for non Actor
        new BaseLogger().setReqId(logger.getMDC());
        if (message instanceof Request) {
            Request request = (Request) message;
            String operation = request.getOperation();
            logger.info("onReceive called for operation:" + operation);
            try {
                logger.info("onReceive:method started at"+System.currentTimeMillis());
                onReceive(request);
                logger.info("onReceive:method ended at"+System.currentTimeMillis());
            } catch (Exception e) {
                logger.error("Exception : operation {} : message : {} {}", operation, e.getMessage(), e);
                onReceiveException(operation, e);
            } finally {
                logger.clearMDC();
            }
        } else {
            logger.info("onReceive called with invalid type of request.");
        }
    }

    /**
     * this method will handle the exception
     * @param callerName
     * @param exception
     * @throws Exception
     */
    protected void onReceiveException(String callerName, Exception exception) throws Exception {
        logger.error("Exception in message processing for: " + callerName + " :: message: " + exception.getMessage(), exception);
        sender().tell(exception, self());
    }


    /**
     * this message will handle the unsupported actor operation
     * @param callerName
     */
    protected void onReceiveUnsupportedMessage(String callerName) {
        logger.info(callerName + ": unsupported operation");
        /**
         * TODO Need to replace null reference from getLocalized method and replace with requested local.
         */
        BaseException exception =
                new ActorServiceException.InvalidOperationName(
                        IResponseMessage.INVALID_OPERATION_NAME,
                        getLocalizedMessage(IResponseMessage.INVALID_OPERATION_NAME,null),
                        ResponseCode.CLIENT_ERROR.getCode());
        sender().tell(exception, self());
    }


    /**
     * this is method is used get message in different different locales
     * @param key
     * @param locale
     * @return
     */

    protected String getLocalizedMessage(String key, Locale locale){
        return localizer.getMessage(key, locale);
    }

    /**
     * This method will return the current timestamp.
     *
     * @return long
     */
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

}
