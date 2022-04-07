package com.dpgten.distributeddb.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController(value = "/rest")
public class TestRestTemplateController {

    @Autowired
    RestTemplate restTemplate;

//    @RequestMapping(value = "/template/products", method = RequestMethod.POST)
//    public String createProducts(@RequestBody Product product) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<Product> entity = new HttpEntity<Product>(product, headers);
//
//        return restTemplate.exchange(
//                "http://localhost:8080/products", HttpMethod.POST, entity, String.class).getBody();
//    }

    @RequestMapping(value = "/template/get", method = RequestMethod.GET)
    public String getProducts() {
//        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl  = "http://localhost:8087/test/get";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl , String.class);
//    Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        String result = "";
        String responseStr ="";
        try {
            responseStr = response.getBody();
            System.out.println(responseStr);
//            root = mapper.readTree(response.getBody());
//            result = root.toString();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
//        JsonNode name = root.path("name");
        return responseStr;
    }


}
