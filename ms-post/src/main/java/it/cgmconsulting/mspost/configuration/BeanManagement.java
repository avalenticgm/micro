package it.cgmconsulting.mspost.configuration;

import it.cgmconsulting.mspost.repository.PostRepository;
import it.cgmconsulting.mspost.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BeanManagement {

    @Value("${application.security.internalToken}")
    String internalToken;

    private final PostRepository postRepository;

    @Bean("getWriters")
    @Scope("prototype")
    public Map<String, String> getWriters(){

        Set<Integer> authorIds = postRepository.getAuthorIds();

        RestTemplate restTemplate = new RestTemplate();

        String url= Consts.GATEWAY+"/"+Consts.MS_AUTH+"/v99/role";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<Set<Integer>> httpEntity = new HttpEntity<>(authorIds, headers);
        try{
            ResponseEntity<?> r = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            if(r.getStatusCode().equals(HttpStatus.OK))
                return (Map<String, String>) r.getBody();
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
        return null;
    }
}


