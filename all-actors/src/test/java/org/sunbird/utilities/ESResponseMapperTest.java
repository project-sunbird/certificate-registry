package org.sunbird.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ESResponseMapperTest {

    @Test
    public void mappingTotalWithCountSuccess() throws IOException {
        String json = "{\"total\":1}";
        ESResponseMapper bean = new ObjectMapper()
                .readerFor(ESResponseMapper.class)
                .readValue(json);
        assertEquals(1, bean.getCount());
    }
    @Test
    public void mappingHitsWithContentSuccess() throws IOException {
        String json = "{\"content\": [{ \"_type\": \"_doc\"}]}";
        ESResponseMapper bean = new ObjectMapper()
                .readerFor(ESResponseMapper.class)
                .readValue(json);
        Assert.assertNotNull(bean.getContent());
    }
}