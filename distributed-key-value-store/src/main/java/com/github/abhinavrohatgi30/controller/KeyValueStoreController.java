package com.github.abhinavrohatgi30.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abhinavrohatgi30.model.Response;
import com.github.abhinavrohatgi30.service.KeyValueStoreService;
import com.github.abhinavrohatgi30.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@EnableAutoConfiguration
public class KeyValueStoreController {


    @Autowired
    private KeyValueStoreService keyValueStoreService;

    @Autowired
    private ObjectMapper mapper;

    @RequestMapping(value = "/get/{key}", method = RequestMethod.GET, produces = "application/json")
    public String getValue(@PathVariable String key) throws JsonProcessingException{
        String response =  keyValueStoreService.getValue(key);
        if(ResponseMessage.KEY_NOT_AVAILABLE == response){
            return mapper.writeValueAsString(new Response(HttpStatus.BAD_REQUEST.value(),response));
        }else if(response.contains(ResponseMessage.NO_NODES_TO_HANDLE_SHARDS.substring(0,10))){
            return mapper.writeValueAsString(new Response(HttpStatus.SERVICE_UNAVAILABLE.value(),response));
        }
        return mapper.writeValueAsString(new Response(HttpStatus.OK.value(),response));
    }


    @RequestMapping(value = "/set/{key}", method = RequestMethod.POST, produces = "application/json")
    public String indexKeyValue(@PathVariable String key, @RequestBody String value, @RequestParam(required = false, defaultValue = "false", value = "isRouted") Boolean isRouted) throws JsonProcessingException{
        String response = keyValueStoreService.indexKeyValue(key,value,isRouted);
        if(ResponseMessage.WRITE_OPERATION_FAILED.equals(response))
            return mapper.writeValueAsString(new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),response));
        return  mapper.writeValueAsString(new Response(HttpStatus.OK.value(),response));
    }
}
