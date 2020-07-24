package com.mastercard.api.funding.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.api.funding.model.FundingRequestWrapper;
import com.mastercard.api.funding.service.FundingService;
import com.mastercard.api.funding.service.TransferBuilder;
import org.openapitools.client.model.Transfer;
import org.openapitools.client.model.TransferWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.LinkedList;
import java.util.List;

/**
 * Controller for application
 */
@Controller
public class FundingController {
    /**
     * Instance of FundingService that we autowire for convenience
     */
    @Autowired
    private FundingService service;

    /**
     * Index page that displays a form to create funding requests
     * It is prepopulated with a valid request
     * Visit localhost:8080 to view
     * @param model Spring model for adding attributes
     * @return Index page
     */
    @GetMapping("/")
    public String index(Model model) {
        FundingRequestWrapper fundingRequestWrapper = TransferBuilder.createPrefilledWrapper();
        List<String> recipientUriSchemes = new LinkedList<>();
        List<String> senderUriSchemes = new LinkedList<>();
        recipientUriSchemes.add("PAN"); // For this reference application, we'll only be working with PAN.
        senderUriSchemes.add("PAN");
        model.addAttribute("recipientUriSchemes", recipientUriSchemes);
        model.addAttribute("senderUriSchemes", senderUriSchemes);
        model.addAttribute("fundingRequestWrapper", fundingRequestWrapper);
        return "index";
    }

    /**
     * Handles form submission. Calls makeCall using the FundingRequestWrapper, which initiates the chain of calls needed
     * to make the call. Will cause an error/success message to be displayed, along with the accompanying request + response.
     * @param fundingRequestWrapper Instance of FundingRequestWrapper that is bound to the form being submitted
     * @param redirectAttributes For holding onto information
     * @return
     * @throws Exception
     */
    @PostMapping("/submitForm")
    public String submitForm(@ModelAttribute("fundingRequestWrapper") FundingRequestWrapper fundingRequestWrapper, RedirectAttributes redirectAttributes) throws Exception{
        TransferWrapper transferWrapper = service.makeCall(fundingRequestWrapper);

        ObjectMapper mapper = new ObjectMapper();
        redirectAttributes.addFlashAttribute("request", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(service.getFundingTransfer()));

        if (transferWrapper != null) {
            Transfer transfer = transferWrapper.getTransfer();
            redirectAttributes.addFlashAttribute("success", "Success!");
            redirectAttributes.addFlashAttribute("response", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(transfer));
        }
        else {
            redirectAttributes.addFlashAttribute("error", service.getErrorMessage());
            redirectAttributes.addFlashAttribute("response", service.getError());
            service.setErrorMessage("");
        }
        return "redirect:/";
    }
}

