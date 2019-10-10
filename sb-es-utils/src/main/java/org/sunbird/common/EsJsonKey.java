package org.sunbird.common;

/**
 * This class will contains all the key related to request and response.
 *
 * @author Manzarul
 */
public final class EsJsonKey {
  public static final String CONTENT = "content";
  public static final String COUNT = "count";
  public static final String DATE_HISTOGRAM = "DATE_HISTOGRAM";
  public static final String EXISTS = "exists";
  public static final String FACETS = "facets";
  public static final String FIELDS = "fields";
  public static final String FILTERS = "filters";
  public static final String GROUP_QUERY = "groupQuery";
  public static final String ID = "id";
  public static final String LIMIT = "limit";
  public static final String NAME = "name";
  public static final String NOT_EXISTS = "not_exists";
  public static final String OFFSET = "offset";
  public static final String ORDER = "order";
  public static final String QUERY = "query";
  public static final String QUERY_FIELDS = "queryFields";
  public static final String SORT_BY = "sort_by";
  public static final String SUNBIRD_ES_CLUSTER = "sunbird_es_cluster";
  public static final String SUNBIRD_ES_IP = "sunbird_es_host";
  public static final String SUNBIRD_ES_PORT = "sunbird_es_port";
  public static final String SOFT_CONSTRAINTS = "softConstraints";
  public static final String TERM = "term";
  public static final String ES_OR_OPERATION = "$or";
  public static final String NESTED_KEY_FILTER = "nestedFilters";
  private EsJsonKey() {}
}
