package com.mastercard.api.funding;

import com.mastercard.api.funding.model.FundingRequestWrapper;
import com.mastercard.api.funding.service.TransferBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class FundingReferenceApplicationTests {
    @Autowired
    private MockMvc mvc;

    @org.junit.Test
    public void testCreatePaymentSuccess() throws Exception {
        FundingRequestWrapper fundingRequestWrapper = TransferBuilder.createPrefilledWrapper();
        mvc.perform(post("/submitForm").flashAttr("fundingRequestWrapper", fundingRequestWrapper)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.flash().attribute("success", "Success!"))
                .andExpect(redirectedUrl("/"));

    }
    @org.junit.Test
    public void testCreatePaymentFailure() throws Exception {
        FundingRequestWrapper fundingRequestWrapper = TransferBuilder.createPrefilledWrapper();
        fundingRequestWrapper.setSenderUriIdentifier("3333432233"); // some invalid value
        mvc.perform(post("/submitForm").flashAttr("fundingRequestWrapper", fundingRequestWrapper)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.flash().attribute("error", "Error creating payment"))
                .andExpect(redirectedUrl("/"));
    }
}
