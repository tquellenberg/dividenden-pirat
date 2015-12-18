package de.tomsplayground.dividendenpirat.googlesheet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

@Component
public class WorksheetService {

	private static final String SHEET_KEY = "1XxGyi1EPsheM0ayIAqrnK-vLgz1hSkWD5fUdeSKN03M";

	// https://developers.google.com/identity/protocols/OAuth2ServiceAccount
	private static final String CLIENT_ID = "460965763302-k8e9ie35tts1sausgd2ct1ph47jjj4lh@developer.gserviceaccount.com";
	// Add requested scopes.
	private static final List<String> SCOPES = Arrays.asList("https://spreadsheets.google.com/feeds");
	// The name of the p12 file you created when obtaining the service account
	private static final String P12FILE = "/DiviSammler-81f37d649e9c.p12";


	public SpreadsheetService getService() throws GeneralSecurityException, IOException {
		SpreadsheetService service = new SpreadsheetService("google-spreadsheet");
		service.setOAuth2Credentials(getCredentials());
		return service;
	}

	public WorksheetEntry findWorksheetEntryByName(SpreadsheetService service, String name) throws MalformedURLException, IOException, ServiceException {
		WorksheetFeed worksheetFeed = getWorksheet(service);
		return worksheetFeed.getEntries().stream().filter(new Predicate<WorksheetEntry>() {
			@Override
			public boolean test(WorksheetEntry t) {
				return StringUtils.equals(t.getTitle().getPlainText(), name);
			}
		}).findFirst().get();
	}

	public WorksheetFeed getWorksheet(SpreadsheetService service) throws MalformedURLException, IOException, ServiceException {
		URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/worksheets/" + SHEET_KEY + "/private/full");
		WorksheetFeed worksheetFeed = service.getFeed(SPREADSHEET_FEED_URL, WorksheetFeed.class);
		return worksheetFeed;
	}

	private GoogleCredential getCredentials() throws GeneralSecurityException, IOException {
		JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		InputStream keyStream = WatchlistDataService.class.getResource(P12FILE).openStream();
		PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
				SecurityUtils.getPkcs12KeyStore(), keyStream,
				"notasecret", "privatekey", "notasecret");
		IOUtils.closeQuietly(keyStream);
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).setServiceAccountId(CLIENT_ID)
				.setServiceAccountPrivateKey(privateKey).setServiceAccountScopes(SCOPES)
				.build();
		credential.refreshToken();
		return credential;
	}

}
