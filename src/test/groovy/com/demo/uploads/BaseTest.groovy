package com.demo.uploads


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(
        classes = UploadsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BaseTest extends Specification {

    @Autowired
    protected TestApiClient testApiClient

    protected TestObjectMapper testObjectMapper = new TestObjectMapper()

}
