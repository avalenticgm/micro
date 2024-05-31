package it.cgmconsulting.mspost.configuration;

import it.cgmconsulting.mspost.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class BeanManagement {

    @Value("${application.security.internalToken}")
    String internalToken;

    @Bean("getWriters")
    public Map<String, String> getWriters(){

        RestTemplate restTemplate = new RestTemplate();
        String url= Consts.GATEWAY+"/"+Consts.MS_AUTH+"/v99/role/WRITER";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        try{
            ResponseEntity<?> r = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
            if(r.getStatusCode().equals(HttpStatus.OK))
                return (Map<String, String>) r.getBody();
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
        return null;
    }
}
