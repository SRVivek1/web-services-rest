package com.rpsoft.ws.rest.client.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClientAppForXml {

	private static String HTTPS = "https";

	public static void main(String[] args) {

		String responseType = "application/json";
		String serviceUrl = "http://localhost:3020/calculator/squareRoot?input=100";
		
		responseType = "application/xml";
		serviceUrl = "http://localhost:3020/calculator/square?input=100";
		
		// HTTP Library
		try {
			
			System.out.println("\nHTTP Library");
			System.out.println("=======================");
			httpRestClient(serviceUrl, responseType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Jetty Client Lib
		System.out.println("\nJetty Library");
		System.out.println("=======================");
		jettyRestClient(serviceUrl, responseType);
	}

	private static void httpRestClient(final String serviceUrl, final String responseType) throws Exception {

		URL endpointURL = null;
		endpointURL = new URL(serviceUrl);

		// prepare connection object
		if (StringUtils.startsWith(serviceUrl, HTTPS)) {

		} else {

			HttpURLConnection httpURLConnection = null;
			httpURLConnection = (HttpURLConnection) endpointURL.openConnection();

			// Set request properties
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Accept", responseType);

			// Open connection with server

			httpURLConnection.connect();

			if (httpURLConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpURLConnection.getResponseCode());
			}

			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream()));
			
			System.out.println("Output from Server.... ");
			String output;
			while ((output = bufferedReader.readLine()) != null) {
				System.out.println("Loop" + output);
			}
			
			httpURLConnection.disconnect();

		}

	}

	private static void jettyRestClient(final String serviceUrl, final String responseType) {

		final Client client = Client.create();
		
		final WebResource webResource = client.resource(serviceUrl);
		
		final ClientResponse response = webResource.type(responseType)
				   .get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			     + response.getStatus());
		}

		System.out.println("Output from Server.... ");
		String output = response.getEntity(String.class);
		System.out.println(output);
	}
}
