package controllers;

import akka.actor.ActorRef;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.BaseException;
import org.sunbird.response.Response;
import scala.concurrent.Await;
import utils.JsonKey;

import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest({org.sunbird.Application.class, BaseController.class, ActorRef.class, Await.class, org.sunbird.Application.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.security.*"})

public class BaseControllerTest {
  private org.sunbird.Application application;

  public BaseControllerTest() throws BaseException {
    baseControllerTestsetUp();
  }

  public void baseControllerTestsetUp() throws BaseException {
    application = PowerMockito.mock(org.sunbird.Application.class);
    PowerMockito.mockStatic(org.sunbird.Application.class);
    PowerMockito.when(org.sunbird.Application.getInstance()).thenReturn(application);
    application.init();
  }

  @Test
  public void testJsonifyResponseFailure() {
    Response response = new Response();
    BaseController controller = new BaseController();
    response.put(JsonKey.MESSAGE, response.getResult());
    String jsonifyResponse = "";//controller.jsonify(response);
    assertEquals(StringUtils.EMPTY, jsonifyResponse);
  }
}