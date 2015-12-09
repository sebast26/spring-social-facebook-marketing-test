package pl.sgorecki;

import pl.sgorecki.facebook.marketing.ads.FacebookAds;
import pl.sgorecki.facebook.marketing.ads.impl.FacebookAdsTemplate;

/**
 * @author Sebastian Górecki
 */
public class App {
	public static void main(String[] args) {
		String accessToken = args[0];
		String userId = args[1];
		String accountId = args[2];
		String campaignId = args[3];
		String adSetId = args[4];

		FacebookAds template = new FacebookAdsTemplate(accessToken);
//		AdAccountCheck.run(template, userId);
//		AdCampaignCheck.run(template, accountId);
//		AdSetCheck.run(template, accountId, campaignId, adSetId);

		AdCreativeCheck.run(template, accountId, adSetId);

	}


}