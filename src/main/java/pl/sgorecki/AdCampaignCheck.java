package pl.sgorecki;

import org.springframework.social.facebook.api.PagedList;
import pl.sgorecki.facebook.marketing.ads.*;

/**
 * @author Sebastian Górecki
 */
public class AdCampaignCheck {
	private AdCampaignCheck() {
		throw new AssertionError();
	}

	static void run(FacebookAds template, String accountId) {
		PagedList<AdCampaign> adCampaigns = template.campaignOperations().getAdCampaigns(accountId);
		System.out.println("********** campaignOperations().getAdCampaigns(accountId) ***********");
		adCampaigns.stream().forEach(adCampaign -> System.out.println("Campaign ID: " + adCampaign.getId() + ", name: " + adCampaign.getName()));

		String campaignId = adCampaigns.stream().findAny().orElseThrow(RuntimeException::new).getId();
		System.out.println("********** campaignOperations().getAdCampaign(campaignId) ***********");
		AdCampaign adCampaign = template.campaignOperations().getAdCampaign(campaignId);
		System.out.println("Campaign name: " + adCampaign.getName());
		System.out.println("Campaign spend cap: " + adCampaign.getSpendCap());
		System.out.println("Campaign objective: " + adCampaign.getObjective().name());
		System.out.println("Campaign configured status: " + adCampaign.getConfiguredStatus());
		System.out.println("Campaign effective status: " + adCampaign.getEffectiveStatus());
		System.out.println("Campaign buying type: " + adCampaign.getBuyingType());

		PagedList<AdSet> adSets = template.campaignOperations().getAdCampaignSets(campaignId);
		System.out.println("********** campaignOperations().getAdCampaignSets(campaignId) ***********");
		adSets.stream().forEach(adSet -> System.out.println("Adset ID: " + adSet.getId() + ", name " + adSet.getName() + ", budget remaining " + adSet.getBudgetRemaining()));

		AdInsight insight = template.campaignOperations().getAdCampaignInsight(campaignId);
		System.out.println("******** campaignOperations().getAdCampaignInsight(campaignId)  *********");
		System.out.println("Cost per inline click: " + insight.getCostPerInlineLinkClick());
		System.out.println("Cost per inline post engagement: " + insight.getCostPerInlinePostEngagement());
		System.out.println("Cost per total action: " + insight.getCostPerTotalAction());
		System.out.println("Impressions: " + insight.getImpressions());
		System.out.println("Inline link clicks: " + insight.getInlineLinkClicks());
		System.out.println("Inline post engagement: " + insight.getInlinePostEngagement());

		AdCampaign campaign = new AdCampaign();
		campaign.setName("Campaign created with spring-social-facebook-marketing");
		campaign.setObjective(AdCampaign.CampaignObjective.POST_ENGAGEMENT);
		campaign.setStatus(AdCampaign.CampaignStatus.PAUSED);
		System.out.println("******** campaignOperations().createAdCampaign(accountId, campaign)  *********");
		String newCampId = template.campaignOperations().createAdCampaign(accountId, campaign);
		AdCampaign createdCampaign = template.campaignOperations().getAdCampaign(newCampId);
		if (!createdCampaign.getName().equals(campaign.getName()))
			throw new RuntimeException("Created campaign does not have given name!");
		if (createdCampaign.getObjective() != AdCampaign.CampaignObjective.POST_ENGAGEMENT)
			throw new RuntimeException("Created campaign does not have given objective!");
		if (createdCampaign.getConfiguredStatus() != ConfiguredStatus.PAUSED)
			throw new RuntimeException("Created campaign does not have given status!");
		System.out.println("Campaign created successfully!");

		AdCampaign toUpdateCampaign = new AdCampaign();
		toUpdateCampaign.setName("Updated campaign name");
		toUpdateCampaign.setObjective(AdCampaign.CampaignObjective.PAGE_LIKES);
		System.out.println("******** campaignOperations().updateAdCampaign(accountId, campaign)  *********");
		boolean status = template.campaignOperations().updateAdCampaign(createdCampaign.getId(), toUpdateCampaign);
		if (!status) throw new RuntimeException("Failed to update campaign!");
		AdCampaign updatedCampaign = template.campaignOperations().getAdCampaign(newCampId);
		if (!updatedCampaign.getName().equals(toUpdateCampaign.getName()))
			throw new RuntimeException("Failed to update campaign name!");
		if (updatedCampaign.getObjective() != AdCampaign.CampaignObjective.PAGE_LIKES)
			throw new RuntimeException("Failed to update campaign objective!");
		if (updatedCampaign.getConfiguredStatus() != ConfiguredStatus.PAUSED)
			throw new RuntimeException("Unnessesary update of campaign status!");
		System.out.println("Campaign updated successfully!");

		System.out.println("******** campaignOperations().deletedCampaign(campaignId)  *********");
		template.campaignOperations().deleteAdCampaign(newCampId);
		System.out.println("Campaign deleted successfully!");
	}
}
