package org.sunbird.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ESResponseMapperTest {

    @Test
    public void whenDeserializingUsingJsonCreator_thenCorrect() throws IOException {
        String json = "{\"total\":1}";
        ESResponseMapper bean = new ObjectMapper()
                .readerFor(ESResponseMapper.class)
                .readValue(json);
        assertEquals(1, bean.getCount());
    }
}