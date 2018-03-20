package com.myd.home.models;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class GmailApi {

    /**Application name.*/
    private static final String APPLICATION_NAME = "Home";

    /**Global instance of the HTTP transport.*/
    private static HttpTransport HTTP_TRANSPORT;

    /**Global instance of the JSON factory.*/
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**Global instance of Gmail client**/
    private static com.google.api.services.gmail.Gmail client;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    private String clientId = "597782319739-5e99ncf1nju70hdevko1qll9eo538u61.apps.googleusercontent.com";

    private String clientSecret = "HB-XtOeMgD_anrH7LTgFytF_";

    private String redirectUri = "http://localhost:8080/homepage";

    public String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                    Collections.singleton(GmailScopes.GMAIL_READONLY)).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

        return authorizationUrl.build();
    }

    public ArrayList<String> generateEmails( String code) throws IOException, MessagingException {
        // Build a new authorized API client service.
        TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
        credential = flow.createAndStoreCredential(tokenResponse, "userID");

        client = new com.google.api.services.gmail.Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).
                setApplicationName(APPLICATION_NAME).build();



        ArrayList<String> sender = new ArrayList<>();
        String query = "label:inbox is:unread newer_than:5d category:primary";
        Integer maxSearchResults = 10;
        Integer index = 0;

        String userID = "me";

        /**
        try {
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:8889/home";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "home", "home");

            String query_sql = "UPDATE user SET token='butter' WHERE email='tylerco.56@gmail.com'";

            PreparedStatement preparedStmt = conn.prepareStatement(query_sql);

            preparedStmt.executeUpdate();

            conn.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        **/

        // Print the labels in the user's account.
        ListMessagesResponse response =
                client.users().messages().list(userID).setQ(query).execute();

        List<Message> messageIds = response.getMessages();
        List<MessagePartHeader> header = new ArrayList<>();


        for (Message messageId : messageIds) {
            if (index < maxSearchResults){

                Message message = client.users().messages().get(userID, messageId.getId()).execute();
                int lastHeaderIndex = message.getPayload().getHeaders().size();
                String fromAddress = "";

                for (int arrayIndexOfFrom = 0; arrayIndexOfFrom < lastHeaderIndex; arrayIndexOfFrom++) {

                    String headerName = message.getPayload().getHeaders().get(arrayIndexOfFrom).getName();

                    if (headerName.equals("From")){
                        fromAddress = message.getPayload().getHeaders().get(arrayIndexOfFrom).getValue().split("<")[0];
                        break;
                    }

                }

                sender.add(fromAddress);
                index++;

            } else { break; }
        }

        return sender;

    }

    public static String getApplicationName() {
        return APPLICATION_NAME;
    }

    public static HttpTransport getHttpTransport() {
        return HTTP_TRANSPORT;
    }

    public static JsonFactory getJsonFactory() {
        return JSON_FACTORY;
    }

    public static Gmail getClient() {
        return client;
    }

    public GoogleClientSecrets getClientSecrets() {
        return clientSecrets;
    }

    public GoogleAuthorizationCodeFlow getFlow() {
        return flow;
    }

    public Credential getCredential() {
        return credential;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public static void setHttpTransport(HttpTransport httpTransport) {
        HTTP_TRANSPORT = httpTransport;
    }

    public static void setClient(Gmail client) {
        GmailApi.client = client;
    }

    public void setClientSecrets(GoogleClientSecrets clientSecrets) {
        this.clientSecrets = clientSecrets;
    }

    public void setFlow(GoogleAuthorizationCodeFlow flow) {
        this.flow = flow;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}


