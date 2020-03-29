package com.blue.optima.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.blue.optima.assignment.App;
import com.blue.optima.assignment.controller.intercept.ControllerFilter;
import com.blue.optima.assignment.controller.model.RecordDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class HttpITCase {
	
    
    @Autowired
    private GenericWebApplicationContext webApplicationContext;
    
    @Autowired
    private ControllerFilter controllerFilter;	

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Before
    public void getContext() {
        mockMvc = webAppContextSetup(webApplicationContext).addFilter(controllerFilter, "/*").build();
        assertNotNull(mockMvc);
    }

	
    @Test
    public void postRequestAndGetResponse() throws Exception {
    	// POST
    	RecordDTO recordDTO = new RecordDTO();
    	ResultActions resultActions;
    	MockHttpServletResponse mockResponse;
    	String fetchedId; 
    	
    	recordDTO.setCity("Pune");
    	recordDTO.setAge(12);
    	recordDTO.setUserName("TestUser");
    	
    	String jsonObject = objectMapper.writeValueAsString(recordDTO);
    	
    	resultActions = mockMvc.perform(post("/records")
    			.header("user-ID", "client-id")
                .content(jsonObject)
                .contentType(MediaType.APPLICATION_JSON_UTF8));
        mockResponse = resultActions.andReturn()
                .getResponse();
                
        fetchedId = mockResponse.getContentAsString();
        assertEquals(HttpStatus.CREATED.value(), mockResponse.getStatus());
        
        //GET
        for(int i = 0 ; i < 30 ; i++) {
        	resultActions = mockMvc.perform(get("/records?id="+fetchedId)
        			.header("user-ID", "client-id"));
        	mockResponse = resultActions.andReturn()
        			.getResponse();
        	
        	System.out.println(mockResponse.getContentAsString());
        	
        	if(i < 20) {
        		assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
        	}else {
        		assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), mockResponse.getStatus());
        	}
        }
    }
    
}
