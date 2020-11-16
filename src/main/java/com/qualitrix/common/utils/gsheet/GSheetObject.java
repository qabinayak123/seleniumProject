package com.qualitrix.common.utils.gsheet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class GSheetObject {

    Map<String, GSheet> googleSheetMap;

    public GSheetObject() {
        this.googleSheetMap = new HashMap<>();
    }

    /**
     * Method is to get Google Sheet Instance.
     *
     * @param sheetId - sheet id for google sheet
     * @return googleSheet - Google sheet instance
     * @throws GeneralSecurityException - Throws GeneralSecurityException
     * @throws IOException              - Throws IOException
     */
    public GSheet getGSheet(String sheetId) throws GeneralSecurityException, IOException {
        if (googleSheetMap.containsKey(sheetId)) {
            return googleSheetMap.get(sheetId);
        } else {
            GSheet googleSheet = new GSheet(sheetId);
            googleSheetMap.put(sheetId, googleSheet);
            return googleSheet;
        }
    }

    /**
     * Unit Test.
     **/
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        GSheetObject googleSheetUtils = new GSheetObject();
        String value1 = googleSheetUtils.getGSheet("1pPkZ3wZNBMfMTJVFse7Aer_9eaw2Lmhbn2sU3Akoj9E")
            .getCellValue("Sheet1", 3, "C");
        System.out.println("Value 1: " + value1);
        String value2 = googleSheetUtils.getGSheet("1pPkZ3wZNBMfMTJVFse7Aer_9eaw2Lmhbn2sU3Akoj9E")
            .getCellValue("Sheet1", 3, "C");
        System.out.println("Value 2: " + value2);
    }
}