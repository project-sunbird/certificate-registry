package org.sunbird;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class CustomJsonLayout extends JsonLayout {
    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        map.put("application", "log-odyssey");
        try {
            map.put("host", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
        }
    }
}
