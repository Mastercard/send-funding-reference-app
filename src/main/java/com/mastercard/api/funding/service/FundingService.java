package com.mastercard.api.funding.service;

import com.mastercard.api.funding.model.FundingRequestWrapper;
import org.openapitools.client.model.FundingTransferWrapper;
import org.json.JSONObject;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.FundingApi;
import org.openapitools.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Class that handles the service side of the application, i.e. making calls to APIs
 */
@Service
public class FundingService {
    // Partner ID which is pulled from application.properties
    @Value("${partnerId}")
    private String partnerId;

    // Used to interact with the transfer portion of the Funding API
    @Autowired
    private FundingApi fundingApi;

    // Most recent funding transfer made
    private FundingTransfer fundingTransfer;

    // Most recent error response
    private String error;

    // Message to accompany most recent error response
    private String errorMessage = "";

    @Autowired
    public FundingService() { }

    /**
     * Takes a FundingTransferWrapper, and uses it to make an API call which will create a funding.
     * Uses createFundingRequest to get the data in the form needed.
     * @param fundingRequestWrapper FundingRequestWrapper instance, that contains all data needed to construct a request
     * @return Instance of TransferWrapper if the call was made successfully, or null otherwise
     */
    public TransferWrapper makeCall(FundingRequestWrapper fundingRequestWrapper) {
        fundingTransfer = TransferBuilder.createFundingRequest(fundingRequestWrapper);
        FundingTransferWrapper fundingTransferWrapper = new FundingTransferWrapper();
        fundingTransferWrapper.setFundingTransfer(fundingTransfer);

        try {
            return fundingApi.createFunding(partnerId, fundingTransferWrapper);
        } catch (ApiException e) {
            JSONObject json = new JSONObject(e.getResponseBody()).getJSONObject("Errors").getJSONArray("Error").getJSONObject(0);
            errorMessage = "Error creating payment";
            error = json.toString(4);
        }
        return null;
    }

    /**
     * Returns most recent FundingTransfer
     * @return fundingTransfer
     */
    public FundingTransfer getFundingTransfer() {
        return fundingTransfer;
    }

    /**
     * Returns most recent error response body
     * @return error response
     */
    public String getError() {
        return error;
    }

    /**
     * Returns most recent error message
     * @return error message
     */
    public String getErrorMessage(){
        return errorMessage;
    }

    /**
     * Sets the errorMessage. Mostly used to reset the error message after it is used in the controller
     * @param newErrorMessage string containing new error message
     */
    public void setErrorMessage(String newErrorMessage) {
        errorMessage = newErrorMessage;
    }
}
