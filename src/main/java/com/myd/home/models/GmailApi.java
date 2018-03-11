package com.myd.home.models;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Thread;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GmailApi {

        /** Application name. */
        private static final String APPLICATION_NAME =
                "Home";

        /** Directory to store user credentials for this application. */
        private static final java.io.File DATA_STORE_DIR = new java.io.File(
                System.getProperty("user.home"), ".credentials/gmail");

        /** Global instance of the {@link FileDataStoreFactory}. */
        private static FileDataStoreFactory DATA_STORE_FACTORY;

        /** Global instance of the JSON factory. */
        private static final JsonFactory JSON_FACTORY =
                JacksonFactory.getDefaultInstance();

        /** Global instance of the HTTP transport. */
        private static HttpTransport HTTP_TRANSPORT;

        /** Global instance of the scopes required by this quickstart.
         *
         * If modifying these scopes, delete your previously saved credentials
         * at ~/.credentials/gmail-java-quickstart
         */
        private static final List<String> SCOPES =
                Arrays.asList(GmailScopes.GMAIL_READONLY);

        static {
            try {
                HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(1);
            }
        }

        /**
         * Creates an authorized Credential object.
         * @return an authorized Credential object.
         * @throws IOException
         */
        public static Credential authorize() throws IOException {
            // Load client secrets.
            InputStream in =
                    GmailApi.class.getResourceAsStream("/client_secret.json");
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline")
                            .build();
            Credential credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");
            System.out.println(
                    "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
            return credential;
        }

        /**
         * Build and return an authorized Gmail client service.
         * @return an authorized Gmail client service
         * @throws IOException
         */
        public static Gmail getGmailService() throws IOException {
            Credential credential = authorize();
            return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }

        public ArrayList<String> generateEmails() throws IOException{
            // Build a new authorized API client service.
            Gmail service = getGmailService();
            ArrayList<String> allMessageList = new ArrayList<>();
            String query = "jacob francois";
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            // Print the labels in the user's account.
            String user = "me";
            ListMessagesResponse response =
                    service.users().messages().list(user).setQ(query).execute();
            ArrayList<Message> messages = new ArrayList<>();

            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(user).setQ(query)
                            .setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }

            for (Message message : messages) {
                Message messageDetails = service.users().messages().get(user, message.getThreadId()).setFormat("raw").execute();
                byte[] emailBytes = Base64.decodeBase64(message.getRaw());

                try {
                    MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

                    messageDetails.put("subject", email.getSubject());
                    messageDetails.put("from", email.getSender() != null ? email.getSender().toString() : "None");
                    messageDetails.put("time", email.getSentDate() != null ? email.getSentDate().toString() : "None");
                    messageDetails.put("snippet", message.getSnippet());
                    messageDetails.put("threadId", message.getThreadId());
                    messageDetails.put("id", message.getId());
                    // messageDetails.put("body", getText(email));

                    allMessageList.add();

                } catch (IOException ex) {
                    throw ex;
                } catch (MessagingException ex) {
                throw ex;
                }

            return allMessageList;
        }



