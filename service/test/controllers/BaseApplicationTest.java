package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.BaseException;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;
import org.sunbird.response.Response;
import org.sunbird.response.ResponseParams;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import utils.module.OnRequestHandler;
import utils.module.StartModule;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({org.sunbird.Application.class, BaseController.class, ActorRef.class, Await.class, org.sunbird.Application.class, Patterns.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.security.*"})
public abstract class BaseApplicationTest {
  protected Application application;
  private ActorSystem system;
  private Props props;
  private org.sunbird.Application app;
  private static ActorRef actorRef;
  private static BaseController baseController;

  public <T> void setup(Class<T> actorClass) {
    try {
      application =
          new GuiceApplicationBuilder()
              .in(new File("path/to/app"))
              .in(Mode.TEST)
              .disable(StartModule.class)
              .build();
      Helpers.start(application);
      system = ActorSystem.create("system");
      props = Props.create(actorClass);
      actorRef = system.actorOf(props);
      baseController = Mockito.mock(BaseController.class);
      applicationSetUp();
      Mockito.when(baseController.getActorRef(Mockito.anyString())).thenReturn(actorRef);
      PowerMockito.mockStatic(OnRequestHandler.class);
      PowerMockito.mockStatic(Patterns.class);
      Future<Object>f1= Futures.successful(getResponseObject());
      when(Patterns.ask(Mockito.any(ActorRef.class),Mockito.any(Request.class),Mockito.any(Timeout.class))).thenReturn(f1);
    } catch (Exception e) {
    }
  }

  public void applicationSetUp() throws BaseException {
    app = PowerMockito.mock(org.sunbird.Application.class);
    PowerMockito.mockStatic(org.sunbird.Application.class);
    PowerMockito.when(org.sunbird.Application.getInstance()).thenReturn(app);
    app.init();
  }
  private Response getResponseObject() {

    Response response = new Response();
    response.put("ResponseCode", "success");
    return response;
  }

  public Result performTest(String url, String method) {
    Http.RequestBuilder req = new Http.RequestBuilder().uri(url).method(method);
    Result result = Helpers.route(application, req);
    return result;
  }

  public String getResponseCode(Result result) {
    String responseStr = Helpers.contentAsString(result);
    ObjectMapper mapper = new ObjectMapper();
    try {
      Response response = mapper.readValue(responseStr, Response.class);
      if (response != null) {
        ResponseParams params = response.getParams();
        return params.getStatus();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public int getResponseStatus(Result result) {
    return result.status();
  }

  public Result performTest(String url, String method, Map map) {
    String data = mapToJson(map);
    Http.RequestBuilder req;
    if (StringUtils.isNotBlank(data)) {
      JsonNode json = Json.parse(data);
      req = new Http.RequestBuilder().bodyJson(json).uri(url).method(method);
    } else {
      req = new Http.RequestBuilder().uri(url).method(method);
    }
    Result result = Helpers.route(application, req);
    return result;
  }

  public String mapToJson(Map map) {
    ObjectMapper mapperObj = new ObjectMapper();
    String jsonResp = "";

    if (map != null) {
      try {
        jsonResp = mapperObj.writeValueAsString(map);
      } catch (IOException e) {
      }
    }
    return jsonResp;
  }


}
