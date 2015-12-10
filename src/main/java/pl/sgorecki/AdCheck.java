package pl.sgorecki;

import org.springframework.social.facebook.api.PagedList;
import pl.sgorecki.facebook.marketing.ads.*;

import java.util.Arrays;

/**
 * @author Sebastian Górecki
 */
class AdCheck {
	private AdCheck() {
		throw new AssertionError();
	}

	public static void run(FacebookAds template, String accountId, String campaignId, String adSetId) {
		System.out.println("********* adOperations().getAccountAds(accountId) *********");
		PagedList<Ad> ads = template.adOperations().getAccountAds(accountId);
		ads.stream().forEach(ad -> System.out.println("Name: " + ad.getName() + ", status: " + ad.getConfiguredStatus() + ", bid_amount: " + ad.getBidAmount()));

		System.out.println("********* adOperations().getCampaignAds(campaignId) *********");
		PagedList<Ad> campaignAds = template.adOperations().getCampaignAds(campaignId);
		campaignAds.stream().forEach(ad -> System.out.println("Name: " + ad.getName() + ", status: " + ad.getConfiguredStatus() + ", bid_amount: " + ad.getBidAmount()));

		System.out.println("********* adOperations().getAdSetAds(adSetId) *********");
		PagedList<Ad> adSetAds = template.adOperations().getAdSetAds(adSetId);
		adSetAds.stream().forEach(ad -> System.out.println("Name: " + ad.getName() + ", status: " + ad.getConfiguredStatus() + ", bid_amount: " + ad.getBidAmount()));

		System.out.println("********* adOperations().getAd(adId) *********");
		String adId = adSetAds.stream().findAny().orElseThrow(RuntimeException::new).getId();
		Ad ad = template.adOperations().getAd(adId);
		System.out.println("Name: " + ad.getName());
		System.out.println("Effective status: " + ad.getEffectiveStatus());
		System.out.println("Creative ID: " + ad.getCreativeId());
		System.out.println("Created time: " + ad.getCreatedTime());
		System.out.println("Bid Info: " + ad.getBidInfo());

		System.out.println("********* adOperations().getAdInsight(adId) *********");
		AdInsight insight = template.adOperations().getAdInsight(adId);
		System.out.println("Cost per inline click: " + insight.getCostPerInlineLinkClick());
		System.out.println("Cost per inline post engagement: " + insight.getCostPerInlinePostEngagement());
		System.out.println("Cost per total action: " + insight.getCostPerTotalAction());
		System.out.println("Impressions: " + insight.getImpressions());
		System.out.println("Inline link clicks: " + insight.getInlineLinkClicks());
		System.out.println("Inline post engagement: " + insight.getInlinePostEngagement());

		System.out.println("********* adOperations().createAd(accountId, ad) *********");
		// create new adSet
		AdSet toCreateAdSet = new AdSet();
		toCreateAdSet.setName(Utils.getRandomString());
		toCreateAdSet.setCampaignId(campaignId);
		toCreateAdSet.setAutobid(true);
		toCreateAdSet.setBillingEvent(AdSet.BillingEvent.POST_ENGAGEMENT);
		toCreateAdSet.setStatus(AdSet.AdSetStatus.PAUSED);
		toCreateAdSet.setDailyBudget("5000");
		toCreateAdSet.setOptimizationGoal(AdSet.OptimizationGoal.POST_ENGAGEMENT);
		TargetingLocation location = new TargetingLocation();
		location.setCountries(Arrays.asList("PL"));
		Targeting targeting = new Targeting();
		targeting.setGeoLocations(location);
		toCreateAdSet.setTargeting(targeting);
		String newAdSetId = template.adSetOperations().createAdSet(accountId, toCreateAdSet);
		// create ad
		Ad toCreateAd = new Ad();
		toCreateAd.setCreativeId("6024167896650");
		toCreateAd.setAdSetId(newAdSetId);
		toCreateAd.setName(Utils.getRandomString());
		toCreateAd.setStatus(Ad.AdStatus.PAUSED);
		String createdAdId = template.adOperations().createAd(accountId, toCreateAd);
		Ad createdAd = template.adOperations().getAd(createdAdId);
		if (!createdAd.getCreativeId().equals(toCreateAd.getCreativeId()))
			throw new RuntimeException("Failed to create proper craetive id for ad!");
		if (!createdAd.getAdSetId().equals(toCreateAd.getAdSetId()))
			throw new RuntimeException("Failed to create proper adset id for ad!");
		if (!createdAd.getName().equals(toCreateAd.getName()))
			throw new RuntimeException("Failed to create proper name for ad!");
		System.out.println("Ad created successfully!");

		System.out.println("********* adOperations().updateAd(accountId, ad) *********");
		Ad toUpdateAd = new Ad();
		toUpdateAd.setName(Utils.getRandomString());
		boolean success = template.adOperations().updateAd(createdAdId, toUpdateAd);
		Ad updatedAd = template.adOperations().getAd(createdAdId);
		if (!success) throw new RuntimeException("Failed to update an ad!");
		if (!updatedAd.getName().equals(toUpdateAd.getName()))
			throw new RuntimeException("Failed to update name of an ad!");
		System.out.println("Ad updated successfully!");

		System.out.println("********* adOperations().updateAd(accountId, ad) *********");
		template.adOperations().deleteAd(createdAdId);
		System.out.println("Ad deleted successfully!");
	}

}
