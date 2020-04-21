package com.demo.uploads

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.context.WebApplicationContext

import javax.servlet.Filter

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic

@Component
class TestApiClient {

    @Autowired
    private TestSecurityUtils testSecurityUtils

    private MockMvc clientWithSecurity

    private TestObjectMapper testMapper = new TestObjectMapper()

    @Autowired
    TestApiClient(WebApplicationContext context, Filter springSecurityFilterChain) {
        clientWithSecurity = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build()
    }

    MockHttpServletResponse postForResponse(Map map = [:]) {
        def requestBuilder = MockMvcRequestBuilders.post(map.url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.asJsonString(map.content))
        setupAuth(map.auth ?: false, requestBuilder, map.email ?: TestSecurityUtils.EMAIL, map.password ?: TestSecurityUtils.PASSWORD)
        perform(requestBuilder).andReturn().response
    }

    MockHttpServletResponse getForResponse(Map map = [:]) {
        def requestBuilder = MockMvcRequestBuilders.get(map.url)
                .contentType(MediaType.APPLICATION_JSON)
        setupAuth(map.auth ?: false, requestBuilder, map.email ?: TestSecurityUtils.EMAIL, map.password ?: TestSecurityUtils.PASSWORD)
        perform(requestBuilder).andReturn().response
    }

    MockHttpServletResponse uploadForResponse(Map map = [:]) {
        def requestBuilder = MockMvcRequestBuilders.multipart("/api/file")
                .file(map.content)
                .params(new LinkedMultiValueMap<String, String>(map.params ?: [:]))
        setupAuth(map.auth ?: false, requestBuilder, map.email ?: TestSecurityUtils.EMAIL, map.password ?: TestSecurityUtils.PASSWORD)
        perform(requestBuilder).andReturn().response
    }

    private void setupAuth(Boolean auth, MockHttpServletRequestBuilder requestBuilder, String email, String password) {
        if (auth) {
            testSecurityUtils.ensureUserExists(email, password)
            requestBuilder.with(httpBasic(email, password))
        }
    }

    private def perform(MockHttpServletRequestBuilder request) {
        clientWithSecurity.perform(request)
    }
}
