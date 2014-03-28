package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends GsonConvertibleObject {
	private String firstName, lastName;
	private String headline;
	private UserUrl siteStandardProfileRequest;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getHeadline() {
		return headline;
	}

	public UserUrl getSiteStandardProfileRequest() {
		return siteStandardProfileRequest;
	}

	public String getMemberId() {
		String memberId = "";
		String url = siteStandardProfileRequest.getUrl();

		Pattern p = Pattern.compile(".*id=(\\d*).*");
		Matcher pMatcher = p.matcher(url);

		if (pMatcher.matches())
			memberId = pMatcher.group(1);

		return memberId;
	}
}
