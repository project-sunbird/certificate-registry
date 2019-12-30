package controllers;

import akka.actor.ActorRef;

import akka.dispatch.Futures;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.sunbird.message.IResponseMessage;
import org.sunbird.message.Localizer;
import org.sunbird.request.Request;
import org.sunbird.response.Response;
import play.Application;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;
import utils.JsonKey;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({org.sunbird.Application.class, BaseController.class, ActorRef.class,Patterns.class, FutureConverters.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.security.*"})

public class BaseControllerTest {
  Localizer localizer = Localizer.getInstance();
  BaseController controllerObject;
  TestHelper testHelper;
  public static Application app;
  public static Map<String, String[]> headerMap;
  private org.sunbird.Application application;
  private static ActorRef actorRef;
  private static BaseController baseController;
  //private OpenSaberApplication openSaberApplication;

  public BaseControllerTest() {
    baseControllerTestsetUp();
  }

  public void baseControllerTestsetUp() {

    application = PowerMockito.mock(org.sunbird.Application.class);
    PowerMockito.mockStatic(org.sunbird.Application.class);
    when(org.sunbird.Application.getInstance()).thenReturn(application);
    application.init();
    mockRequestHandler();
  }

  public void mockRequestHandler() {

    try {
      baseController = Mockito.mock(BaseController.class);
      actorRef = Mockito.mock(ActorRef.class);
      when(baseController.getActorRef(Mockito.anyString())).thenReturn(actorRef);
      PowerMockito.mockStatic(Patterns.class);
      Future<Object>f1= Futures.successful(getResponseObject());
      when(Patterns.ask(Mockito.any(ActorRef.class),Mockito.any(Request.class),Mockito.any(Timeout.class))).thenReturn(f1);
    }catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  private Response getResponseObject() {

    Response response = new Response();
    response.put("ResponseCode", "success");
    return response;
  }


  @Test
  public void testJsonifyResponseSuccess() {
    Response response = new Response();
    BaseController controller = new BaseController();
    response.put(JsonKey.MESSAGE, localizer.getMessage(IResponseMessage.INTERNAL_ERROR,null));
    String jsonifyResponse = controller.jsonify(response);
    assertEquals(
            "{\"id\":null,\"ver\":null,\"ts\":null,\"params\":null,\"responseCode\":\"OK\",\"result\":{\"message\":\"Process failed,please try again later.\"}}", jsonifyResponse);
  }

  @Test
  public void testJsonifyResponseFailure() {
    Response response = new Response();
    BaseController controller = new BaseController();
    response.put(JsonKey.MESSAGE, response.getResult());
    String jsonifyResponse = controller.jsonify(response);
    assertEquals(StringUtils.EMPTY, jsonifyResponse);
  }
}