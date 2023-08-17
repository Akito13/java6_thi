package com.java6.nhom1.rest.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.java6.nhom1.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

@Service
public class RestService {
    private RestTemplate client = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> ResponseEntity<List<T>> get(String url, Authentication auth, Class<T> clazz) throws Exception{
        return request(url, HttpMethod.GET, null, auth, clazz);
    }

    public <T> ResponseEntity<List<T>> post(String url, Object data, Authentication auth, Class<T> clazz) throws Exception{
        return request(url, HttpMethod.POST, data, auth, clazz);
    }

    public <T> ResponseEntity<List<T>> put(String url, Object data, Authentication auth, Class<T> clazz) throws Exception{
        return request(url, HttpMethod.PUT, data, auth, clazz);
    }

    public <T> ResponseEntity<List<T>> delete(String url, Authentication auth, Class<T> clazz) throws Exception{
        return request(url, HttpMethod.DELETE, null, auth, clazz);
    }

    private <T> ResponseEntity<List<T>> request(String url,
                                                HttpMethod method,
                                                Object data,
                                                Authentication authentication,
                                                Class<T> clazz)
        throws Exception
    {
        User user = (User)authentication.getPrincipal();
        String auth = user.getEmail() + ":" + user.getPassword();

        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Basic " + new String(encodedAuth));

        HttpEntity<Object> entity = new HttpEntity<>(data, header);

        ResponseEntity<List<?>> response = client.exchange(url, method, entity, new ParameterizedTypeReference<List<?>>(){});
        List<?> body = response.getBody();
        List<T> resultList = null;
        if(body != null) {
            TypeFactory typeFactory = TypeFactory.defaultInstance();
            resultList = objectMapper.convertValue(response.getBody(), typeFactory.constructCollectionType(ArrayList.class, clazz));
//            resultList = objectMapper.readValue(
//                    (JsonParser) response.getBody(),
//                    typeFactory.constructCollectionType(ArrayList.class, clazz)
//            );
        }
        return new ResponseEntity<List<T>>(resultList, response.getStatusCode());
    }
}
