package org.sunbird.cassandra.helper;

import org.sunbird.cassandra.common.Constants;

import java.util.HashMap;
import java.util.Map;

/** Created by arvind on 10/10/17. */
public class CassandraConnectionMngrFactory {

  private static Map<String, CassandraConnectionManager> connectionFactoryMap = new HashMap<>();

  /**
   * Factory method to get the cassandra connection manager oject on basis of mode name pass in
   * argument . default mode is standalone mode .
   *
   * @param name
   * @return
   */
  public static CassandraConnectionManager getObject(String name) {

    if (!connectionFactoryMap.containsKey(name)) {
      // create object
      synchronized (CassandraConnectionMngrFactory.class) {
        if (name.equalsIgnoreCase(Constants.EMBEDDED_MODE)) {
          String mode = Constants.EMBEDDED_MODE;
          if (null == connectionFactoryMap.get(Constants.EMBEDDED_MODE)) {
            CassandraConnectionManager embeddedcassandraConnectionManager =
                new CassandraConnectionManagerImpl(mode);
            connectionFactoryMap.put(Constants.EMBEDDED_MODE, embeddedcassandraConnectionManager);
          }
        } else {
          if (null == connectionFactoryMap.get(Constants.STANDALONE_MODE)) {
            String mode = Constants.STANDALONE_MODE;
            CassandraConnectionManager standaloneCassandraConnectionManager =
                new CassandraConnectionManagerImpl(mode);
            connectionFactoryMap.put(Constants.STANDALONE_MODE, standaloneCassandraConnectionManager);
          }
        }
      }
    }
    return connectionFactoryMap.get(name);
  }
}
