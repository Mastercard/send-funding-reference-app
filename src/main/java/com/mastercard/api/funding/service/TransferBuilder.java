package com.mastercard.api.funding.service;

import com.mastercard.api.funding.model.FundingRequestWrapper;
import org.openapitools.client.model.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Class in charge of building requests
 */
public class TransferBuilder {
    /**
     * Takes a fundingRequestWrapper, and unpacks it into the appropriate models that our
     * API client instances want
     * @param fundingRequestWrapper fundingRequestWrapper instance, that contains all data needed to construct request
     * @return Instance of FundingTransfer that is ready to be used with an instance of FundingAPI to make API calls
     * Note: This application only uses the required parameters for the Funding API
     */
    static FundingTransfer createFundingRequest(FundingRequestWrapper fundingRequestWrapper) {

        FundingTransfer fundingTransfer = new FundingTransfer();

        FundingSender fundingSender = new FundingSender();

        FundingSenderAddress fundingSenderAddress = new FundingSenderAddress();
        fundingSenderAddress.setLine1(fundingRequestWrapper.getSenderStreet());
        fundingSenderAddress.setCity(fundingRequestWrapper.getSenderCity());
        fundingSenderAddress.setCountry(fundingRequestWrapper.getSenderCountry());
        fundingSenderAddress.setCountrySubdivision(fundingRequestWrapper.getSenderCountrySubdivision());
        fundingSenderAddress.setPostalCode(fundingRequestWrapper.getSenderPostalCode());

        fundingSender.setFirstName(fundingRequestWrapper.getSenderFirstName());
        fundingSender.setLastName(fundingRequestWrapper.getSenderLastName());
        fundingSender.setAddress(fundingSenderAddress);

        FundingRecipient fundingRecipient = new FundingRecipient();

        FundingRecipientAddress fundingRecipientAddress = new FundingRecipientAddress();
        fundingRecipientAddress.setLine1(fundingRequestWrapper.getRecipientStreet());
        fundingRecipientAddress.setCity(fundingRequestWrapper.getRecipientCity());
        fundingRecipientAddress.setCountry(fundingRequestWrapper.getRecipientCountry());
        fundingRecipientAddress.setCountrySubdivision(fundingRequestWrapper.getRecipientCountrySubdivision());
        fundingRecipientAddress.setPostalCode(fundingRequestWrapper.getRecipientPostalCode());

        fundingRecipient.setFirstName(fundingRequestWrapper.getRecipientFirstName());
        fundingRecipient.setLastName(fundingRequestWrapper.getRecipientLastName());
        fundingRecipient.setAddress(fundingRecipientAddress);

        // Generating random transfer reference number
        String transferReference = generateTransferReference();

        // Grabbing the current time in ISO 8601 format
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String currentMoment = df.format(new Date());

        fundingTransfer.setSender(fundingSender);
        fundingTransfer.setRecipient(fundingRecipient);
        fundingTransfer.setSenderAccountUri("pan:" + fundingRequestWrapper.getSenderUriIdentifier() + ";exp=" + fundingRequestWrapper.getSenderUriExpYear()
                + "-" + fundingRequestWrapper.getSenderUriExpMonth() + ";cvc=" + fundingRequestWrapper.getSenderUriCvc());
        fundingTransfer.setRecipientAccountUri("pan:" + fundingRequestWrapper.getRecipientUriIdentifier() + ";exp=" + fundingRequestWrapper.getRecipientUriExpYear()
                + "-" + fundingRequestWrapper.getRecipientUriExpMonth() + ";cvc=" + fundingRequestWrapper.getRecipientUriCvc());
        fundingTransfer.setPaymentType(fundingRequestWrapper.getPaymentType());
        fundingTransfer.setAmount(fundingRequestWrapper.getAmount());
        fundingTransfer.setCurrency(fundingRequestWrapper.getCurrency());

        fundingTransfer.setParticipationId("1234567890");
        //fundingTransfer.setAdditionalMessage("Adding message for this payment");
        fundingTransfer.setTransferReference(transferReference);
        return fundingTransfer;
    }

    public static String generateTransferReference () {
        Random random = new Random();
        long n = (long) (100000000000000L + random.nextFloat() * 900000000000000L);
        return "FUNDING_" + n;
    }

    /**
     * Creates an instance of fundingRequestWrapper with pre-populated fields to
     * allow for fast form submissions.
     * @return Instance of fundingRequestWrapper class
     */
    public static FundingRequestWrapper createPrefilledWrapper() {
        FundingRequestWrapper fundingRequestWrapper = new FundingRequestWrapper();
        fundingRequestWrapper.setSenderFirstName("John");
        fundingRequestWrapper.setSenderLastName("Jones");
        fundingRequestWrapper.setSenderStreet("1 Main St");
        fundingRequestWrapper.setSenderCity("Chicago");
        fundingRequestWrapper.setSenderPostalCode("63368");
        fundingRequestWrapper.setSenderCountrySubdivision("MO");
        fundingRequestWrapper.setSenderCountry("USA");
        fundingRequestWrapper.setSenderUriScheme("PAN");
        fundingRequestWrapper.setSenderUriIdentifier("4007589999999953");
        fundingRequestWrapper.setSenderUriExpYear("2077");
        fundingRequestWrapper.setSenderUriExpMonth("08");
        fundingRequestWrapper.setSenderUriCvc("123");
        fundingRequestWrapper.setRecipientStreet("1 Main St");
        fundingRequestWrapper.setRecipientCity("St. Louis");
        fundingRequestWrapper.setRecipientPostalCode("63368");
        fundingRequestWrapper.setRecipientCountrySubdivision("MO");
        fundingRequestWrapper.setRecipientCountry("USA");
        fundingRequestWrapper.setRecipientFirstName("John");
        fundingRequestWrapper.setRecipientLastName("Jones");
        fundingRequestWrapper.setRecipientUriScheme("PAN");
        fundingRequestWrapper.setRecipientUriIdentifier("5102589999999988");
        fundingRequestWrapper.setRecipientUriExpYear("2077");
        fundingRequestWrapper.setRecipientUriExpMonth("08");
        fundingRequestWrapper.setRecipientUriCvc("123");
        fundingRequestWrapper.setPaymentType("P2P");
        fundingRequestWrapper.setAmount("1000");
        fundingRequestWrapper.setCurrency("USD");
        return fundingRequestWrapper;
    }
}
