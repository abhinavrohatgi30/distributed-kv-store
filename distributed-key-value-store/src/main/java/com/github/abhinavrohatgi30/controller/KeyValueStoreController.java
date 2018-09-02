package com.github.abhinavrohatgi30.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abhinavrohatgi30.model.Response;
import com.github.abhinavrohatgi30.service.KeyValueStoreService;
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
        String value =  keyValueStoreService.getValue(key);
        return mapper.writeValueAsString(new Response(HttpStatus.OK.value(),value));
    }


    @RequestMapping(value = "/set/{key}", method = RequestMethod.POST, produces = "application/json")
    public String indexKeyValue(@PathVariable String key, @RequestBody String value, @RequestParam(required = false, defaultValue = "false") Boolean isRouted) throws JsonProcessingException{
        int response = keyValueStoreService.indexKeyValue(key,value,isRouted);
        return  mapper.writeValueAsString(new Response(HttpStatus.OK.value(),String.valueOf(response)));
    }
}
