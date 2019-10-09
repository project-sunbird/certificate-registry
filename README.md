## Certification-Service

### Pre-requisites
0. Make a copy of setVars.sh.sample to list all the environment vars needed.
1. Change all-actors/application.conf `thisActorSystem` to your own actor system name
2. Use the same string in /all-actors/Application.java in the field `actorSystemName`
3. Change the play secret key value in /play-service/application.conf - `play.http.secret.key`

### Note
1.In this Application , throw only org.sunbird.BaseException

### Build

1. Execute clean install `mvn clean install`


### Run 
1. For debug mode, <br> 
   `cd play-service` <br>
   `mvn play2:dist`  <br>
   `mvn play2:run`

2. For run mode, 
   `cd play-service` <br>
   `mvn play2:dist`  <br>
   `mvn play2:start`

### Verify running status

Hit the following Health check curl command 

`curl -X GET \
   http://localhost:9000/health \
   -H 'Postman-Token: 6a5e0eb0-910a-42d1-9077-c46f6f85397d' \
   -H 'cache-control: no-cache'`

And, a successful response must be like this:

`{"id":"api.200ok","ver":"v1","ts":"2019-01-17 16:53:26:286+0530","params":{"resmsgid":null,"msgid":"8e27cbf5-e299-43b0-bca7-8347f7ejk5abcf","err":null,"status":"success","errmsg":null},"responseCode":"OK","result":{"response":{"response":"SUCCESS","errors":[]}}}`
## ElasticSearch V-6.4.0 need to be installed in the system, 
   - Create Indices and Mappings using below command

## ES-INDICES
  ``curl -X PUT \
      http://localhost:9200/cert \
      -H 'Accept: */*' \
      -H 'Accept-Encoding: gzip, deflate' \
      -H 'Cache-Control: no-cache' \
      -H 'Connection: keep-alive' \
      -H 'Content-Length: 6710' \
      -H 'Content-Type: application/json' \
      -H 'Host: localhost:9200' \
      -H 'Postman-Token: 16543e03-f923-44f9-9c8e-f3201af68f0f,e1b9aabd-20ef-4e7d-9175-22f0a9a482b0' \
      -H 'User-Agent: PostmanRuntime/7.15.2' \
      -H 'cache-control: no-cache' \
      -d '{
        "settings": {
            "index": {
                "number_of_shards": "5",
                "number_of_replicas": "1",
                "analysis": {
                    "filter": {
                        "mynGram": {
                            "token_chars": [
                                "letter",
                                "digit",
                                "whitespace",
                                "punctuation",
                                "symbol"
                            ],
                            "min_gram": "1",
                            "type": "ngram",
                            "max_gram": "20"
                        }
                    },
                    "analyzer": {
                        "cs_index_analyzer": {
                            "filter": [
                                "lowercase",
                                "mynGram"
                            ],
                            "type": "custom",
                            "tokenizer": "standard"
                        },
                        "keylower": {
                            "filter": "lowercase",
                            "type": "custom",
                            "tokenizer": "keyword"
                        },
                        "cs_search_analyzer": {
                            "filter": [
                                "lowercase",
                                "standard"
                            ],
                            "type": "custom",
                            "tokenizer": "standard"
                        }
                    }
                }
            }
        },
        "mappings": {
            "_doc": {
                "dynamic": false,
                "properties": {
                    "id": {
                        "type": "text",
                        "fields": {
                            "raw": {
                                "type": "text",
                                "analyzer": "keylower",
                                "fielddata": true
                            }
                        },
                        "copy_to": [
                            "all_fields"
                        ],
                        "analyzer": "cs_index_analyzer",
                        "search_analyzer": "cs_search_analyzer",
                        "fielddata": true
                    },
                    "accessCode": {
                        "type": "text",
                        "fields": {
                            "raw": {
                                "type": "text",
                                "analyzer": "keylower",
                                "fielddata": true
                            }
                        },
                        "copy_to": [
                            "all_fields"
                        ],
                        "analyzer": "cs_index_analyzer",
                        "search_analyzer": "cs_search_analyzer",
                        "fielddata": true
                    },
                    "course": {
                        "type": "object"
                    },
                    "certData": {
                        "type": "object"
                    },
                    "rawData": {
                        "type": "object"
                    },
                    "recipientId": {
                        "type": "text",
                        "fields": {
                            "raw": {
                                "type": "text",
                                "analyzer": "keylower",
                                "fielddata": true
                            }
                        },
                        "copy_to": [
                            "all_fields"
                        ],
                        "analyzer": "cs_index_analyzer",
                        "search_analyzer": "cs_search_analyzer",
                        "fielddata": true
                    },
                    "createdAt": {
                        "type": "date",
                        "fields": {
                            "raw": {
                                "type": "date"
                            }
                        }
                    },
                    "updatedAt": {
                        "type": "date",
                        "fields": {
                            "raw": {
                                "type": "date"
                            }
                        }
                    },
                    "pdfUrl": {
                        "type": "text",
                        "fields": {
                            "raw": {
                                "type": "text",
                                "analyzer": "keylower",
                                "fielddata": true
                            }
                        },
                        "copy_to": [
                            "all_fields"
                        ],
                        "analyzer": "cs_index_analyzer",
                        "search_analyzer": "cs_search_analyzer",
                        "fielddata": true
                    },
                    "jsonUrl": {
                        "type": "text",
                        "fields": {
                            "raw": {
                                "type": "text",
                                "analyzer": "keylower",
                                "fielddata": true
                            }
                        },
                        "copy_to": [
                            "all_fields"
                        ],
                        "analyzer": "cs_index_analyzer",
                        "search_analyzer": "cs_search_analyzer",
                        "fielddata": true
                    },
                    "createdBy": {
                        "type": "text",
                        "fields": {
                            "raw": {
                                "type": "text",
                                "analyzer": "keylower",
                                "fielddata": true
                            }
                        },
                        "copy_to": [
                            "all_fields"
                        ],
                        "analyzer": "cs_index_analyzer",
                        "search_analyzer": "cs_search_analyzer",
                        "fielddata": true
                    },
                    "updatedBy": {
                        "type": "text",
                        "fields": {
                            "raw": {
                                "type": "text",
                                "analyzer": "keylower",
                                "fielddata": true
                            }
                        },
                        "copy_to": [
                            "all_fields"
                        ],
                        "analyzer": "cs_index_analyzer",
                        "search_analyzer": "cs_search_analyzer",
                        "fielddata": true
                    },
                    "isRevoked": {
                        "type": "boolean",
                        "fields": {
                            "raw": {
                                "type": "boolean"
                            }
                        }
                    }
                }        }
        }
    }'``
    
## ES-MAPPINGS
``curl -X PUT \
    http://localhost:9200/cert/_doc/_mapping \
    -H 'Accept: */*' \
    -H 'Accept-Encoding: gzip, deflate' \
    -H 'Cache-Control: no-cache' \
    -H 'Connection: keep-alive' \
    -H 'Content-Length: 3003' \
    -H 'Content-Type: application/json' \
    -H 'Host: localhost:9200' \
    -H 'Postman-Token: a6d1149f-e377-4ec5-ab72-46e268a80cca,7739b203-4304-4fc1-a379-438870a8de89' \
    -H 'User-Agent: PostmanRuntime/7.15.2' \
    -H 'cache-control: no-cache' \
    -d '{
    "dynamic": false,
    "properties": {
      "id": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "text",
            "analyzer": "keylower",
            "fielddata": true
          }
        },
        "copy_to": [
          "all_fields"
        ],
        "analyzer": "cs_index_analyzer",
        "search_analyzer": "cs_search_analyzer",
        "fielddata": true
      },
      "accessCode": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "text",
            "analyzer": "keylower",
            "fielddata": true
          }
        },
        "copy_to": [
          "all_fields"
        ],
        "analyzer": "cs_index_analyzer",
        "search_analyzer": "cs_search_analyzer",
        "fielddata": true
      },
      "course": {
        "type": "object"
      },
      "certData": {
        "type": "object"
      },
      "rawData": {
        "type": "object"
      },
      "recipientId": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "text",
            "analyzer": "keylower",
            "fielddata": true
          }
        },
        "copy_to": [
          "all_fields"
        ],
        "analyzer": "cs_index_analyzer",
        "search_analyzer": "cs_search_analyzer",
        "fielddata": true
      },
      "createdAt": {
        "type": "date",
        "fields": {
          "raw": {
            "type": "date"
          }
        }
      },
      "updatedAt": {
        "type": "date",
        "fields": {
          "raw": {
            "type": "date"
          }
        }
      },
      "pdfUrl": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "text",
            "analyzer": "keylower",
            "fielddata": true
          }
        },
        "copy_to": [
          "all_fields"
        ],
        "analyzer": "cs_index_analyzer",
        "search_analyzer": "cs_search_analyzer",
        "fielddata": true
      },
      "jsonUrl": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "text",
            "analyzer": "keylower",
            "fielddata": true
          }
        },
        "copy_to": [
          "all_fields"
        ],
        "analyzer": "cs_index_analyzer",
        "search_analyzer": "cs_search_analyzer",
        "fielddata": true
      },
      "createdBy": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "text",
            "analyzer": "keylower",
            "fielddata": true
          }
        },
        "copy_to": [
          "all_fields"
        ],
        "analyzer": "cs_index_analyzer",
        "search_analyzer": "cs_search_analyzer",
        "fielddata": true
      },
      "updatedBy": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "text",
            "analyzer": "keylower",
            "fielddata": true
          }
        },
        "copy_to": [
          "all_fields"
        ],
        "analyzer": "cs_index_analyzer",
        "search_analyzer": "cs_search_analyzer",
        "fielddata": true
      },
      "isRevoked": {
        "type": "boolean",
        "fields": {
          "raw": {
            "type": "boolean"
          }
        }
      }
    }
  }'``
  

