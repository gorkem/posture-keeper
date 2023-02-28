package com.redhat.postureKeeper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import com.redhat.postureKeeper.service.ErrorUtilities;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ErrorUtilitiesTest {
    
    @Test
    public void testGetErrorJsonNullValue() {
        assertEquals("{}", ErrorUtilities.getErrorJson(null));

    }

    @Test
    public void testGetErrorJson() { 
        String msg = "Error message";
        assertEquals("{ \"message\": \""+ msg +"\" }", ErrorUtilities.getErrorJson(msg));
    }
}
