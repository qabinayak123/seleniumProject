package com.qualitrix.common.utils.gsheet;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GSheet {

    private final String applicationName = "Google Sheets API Java Quickstart";
    private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private final String tokenDirPath = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private final String credentialPath = "/credentials.json";

    private Sheets service;
    private final String spreadsheetId;

    public GSheet(String sheetId) throws GeneralSecurityException, IOException {
        this.spreadsheetId = sheetId;
        this.setSheets();
    }

    private void setSheets() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.service = new Sheets.Builder(httpTransport, jsonFactory, getCredentials(httpTransport))
            .setApplicationName(applicationName)
            .build();
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param netHttpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport netHttpTransport) throws IOException {
        // Load client secrets.
        InputStream in = GSheet.class.getResourceAsStream(credentialPath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialPath);
        }
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            netHttpTransport, jsonFactory, clientSecrets, scopes)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenDirPath)))
            .setAccessType("offline")
            .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Method is to get cell value from the google sheet.
     *
     * @param sheetName - sheet name wants to work with
     * @param row       - row number
     * @param colName   - coloumn name
     * @return values
     * @throws IOException - Throws IOException
     */
    public String getCellValue(String sheetName, int row, String colName) throws IOException {
        //Sheet1!A4:A4
        //Sheet1!A2:D5
        String range = sheetName + "!" + colName + row + ":" + row;
        ValueRange response = service.spreadsheets().values()
            .get(this.spreadsheetId, range)
            .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
            return "";
        } else {
            return values.get(0).get(0).toString();
        }
    }

    /**
     * This Method is to set the value in cell.
     *
     * @param sheetName - sheetName of google sheet
     * @param row       - row number
     * @param colName   - coloumn name
     * @param value     - value which want to set
     * @throws IOException - Throws IOException
     */
    public void setCellValue(String sheetName, int row,
                             String colName, String value) throws IOException {

        List<List<Object>> values = Arrays.asList(Arrays.asList(value));
        String range = sheetName + "!" + colName + row + ":" + row;
        ValueRange body = new ValueRange()
            .setValues(values);
        UpdateValuesResponse result =
            service.spreadsheets().values().update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
        System.out.printf("%d cells updated.", result.getUpdatedCells());
    }

    /**
     * Unit Test.
     **/
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        GSheet googleSheet = new GSheet("1pPkZ3wZNBMfMTJVFse7Aer_9eaw2Lmhbn2sU3Akoj9E");
        String value = googleSheet.getCellValue("Sheet1", 3, "C");
        System.out.println("Value: " + value);

        googleSheet.setCellValue("Sheet1", 3, "C", "Hello Ahmad");
    }
}