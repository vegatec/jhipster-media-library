package net.vegatec.media_library.web.rest;

import static net.vegatec.media_library.test.util.OAuth2TestUtil.TEST_USER_LOGIN;
import static net.vegatec.media_library.test.util.OAuth2TestUtil.registerAuthenticationToken;
import static net.vegatec.media_library.test.util.OAuth2TestUtil.testAuthenticationToken;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.vegatec.media_library.IntegrationTest;
import net.vegatec.media_library.security.AuthoritiesConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@AutoConfigureMockMvc
@IntegrationTest
class AccountResourceIT {

    @Autowired
    private MockMvc restAccountMockMvc;

    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    ClientRegistration clientRegistration;

    @Test
    @Transactional
    void testGetExistingAccount() throws Exception {
        TestSecurityContextHolder.getContext()
            .setAuthentication(registerAuthenticationToken(authorizedClientService, clientRegistration, testAuthenticationToken()));

        restAccountMockMvc
            .perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(TEST_USER_LOGIN))
            .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
    }

    @Test
    void testGetUnknownAccount() throws Exception {
        restAccountMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testNonAuthenticatedUser() throws Exception {
        restAccountMockMvc
            .perform(get("/api/authenticate").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(TEST_USER_LOGIN)
    void testAuthenticatedUser() throws Exception {
        restAccountMockMvc
            .perform(get("/api/authenticate").with(request -> request).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(TEST_USER_LOGIN));
    }
}
