package pl.sgorecki;

import org.springframework.social.facebook.api.PagedList;
import pl.sgorecki.facebook.marketing.ads.*;

import java.util.Arrays;

/**
 * Created by janusz on 09.12.2015.
 */
public class AdSetCheck {
	private AdSetCheck() {
		throw new AssertionError();
	}

	public static void run(FacebookAds template, String accountId, String campaignId, String adSetId) {
		PagedList<AdSet> adSets = template.adSetOperations().getAccountAdSets(accountId);
		System.out.println("************ adSetOperations().getAccountAdSets(accountId) ************");
		adSets.stream().forEach(adSet -> System.out.println("ID: " + adSet.getId() + ", name " + adSet.getName() + ", status " + adSet.getEffectiveStatus().name()));

		System.out.println("************ adSetOperations().getCampaignAdSets(campaignId) ************");
		PagedList<AdSet> campaignAdSets = template.adSetOperations().getCampaignAdSets(campaignId);
		campaignAdSets.forEach(adSet -> System.out.println("ID: " + adSet.getId() + ", name " + adSet.getName() + ", status: " + adSet.getEffectiveStatus()));

		System.out.println("************ adSetOperations().getAdSet(adSetId) ************");
		AdSet adSet = template.adSetOperations().getAdSet(adSetId);
		System.out.println("Name: " + adSet.getName());
		System.out.println("Effective status: " + adSet.getEffectiveStatus());
		System.out.println("Budget remaining: " + adSet.getBudgetRemaining());
		System.out.println("Optimization goal: " + adSet.getOptimizationGoal());
		System.out.println("Lifetime budget: " + adSet.getLifetimeBudget());

		System.out.println("************ adSetOperations().getAdSetInsight(adSetId) ************");
		AdInsight insight = template.adSetOperations().getAdSetInsight(adSetId);
		System.out.println("Cost per inline click: " + insight.getCostPerInlineLinkClick());
		System.out.println("Cost per inline post engagement: " + insight.getCostPerInlinePostEngagement());
		System.out.println("Cost per total action: " + insight.getCostPerTotalAction());
		System.out.println("Impressions: " + insight.getImpressions());
		System.out.println("Inline link clicks: " + insight.getInlineLinkClicks());
		System.out.println("Inline post engagement: " + insight.getInlinePostEngagement());

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
		System.out.println("************ adSetOperations().createAdSet(accountId, toCreateAdSet) ************");
		String newAdSetId = template.adSetOperations().createAdSet(accountId, toCreateAdSet);
		AdSet createdAdSet = template.adSetOperations().getAdSet(newAdSetId);
		if (!createdAdSet.getName().equals(toCreateAdSet.getName()))
			throw new RuntimeException("Failed to create adSet with proper name!");
		if (!createdAdSet.getCampaignId().equals(toCreateAdSet.getCampaignId()))
			throw new RuntimeException("Failed to create adSet with proper campaing id!");
		if (!createdAdSet.isAutobid()) throw new RuntimeException("Failed to create adSet with autobid!");
		if (createdAdSet.getBillingEvent() != AdSet.BillingEvent.POST_ENGAGEMENT)
			throw new RuntimeException("Failed to create adSet with proper billing event!");
		if (createdAdSet.getConfiguredStatus() != ConfiguredStatus.PAUSED)
			throw new RuntimeException("Failed to create adSet with proper status!");
		if (!createdAdSet.getDailyBudget().equals(toCreateAdSet.getDailyBudget()))
			throw new RuntimeException("Failed to create adSet with proper daily budget!");
		if (createdAdSet.getOptimizationGoal() != AdSet.OptimizationGoal.POST_ENGAGEMENT)
			throw new RuntimeException("Failed to create ad set with proper optimization goal!");
		if (!createdAdSet.getTargeting().getGeoLocations().getCountries().get(0).equals("PL"))
			throw new RuntimeException("Failed to created adset with proper targeting!");
		System.out.println("AdSet created successfully!");

		AdSet toUpdateAdSet = new AdSet();
		toUpdateAdSet.setBillingEvent(AdSet.BillingEvent.LINK_CLICKS);
		toUpdateAdSet.setDailyBudget("6000");
		toUpdateAdSet.setOptimizationGoal(AdSet.OptimizationGoal.LINK_CLICKS);
		toUpdateAdSet.setAutobid(true);
		location.setCountries(Arrays.asList("DE"));
		targeting.setGeoLocations(location);
		toUpdateAdSet.setTargeting(targeting);
		System.out.println("************ adSetOperations().updateAdSet(adSetId, toUpdateAdSet) ************");
		boolean success = template.adSetOperations().updateAdSet(newAdSetId, toUpdateAdSet);
		AdSet updatedAdSet = template.adSetOperations().getAdSet(newAdSetId);
		if (!success) throw new RuntimeException("Failed to update AdSet!");
		if (updatedAdSet.getBillingEvent() != AdSet.BillingEvent.LINK_CLICKS)
			throw new RuntimeException("Failed to update billing event!");
		if (!updatedAdSet.getDailyBudget().equals(toUpdateAdSet.getDailyBudget()))
			throw new RuntimeException("Failed to update daily budget!");
		if (updatedAdSet.getOptimizationGoal() != AdSet.OptimizationGoal.LINK_CLICKS)
			throw new RuntimeException("Failed to update optimization goal!");
		if (!updatedAdSet.getTargeting().getGeoLocations().getCountries().get(0).equals("DE"))
			throw new RuntimeException("Failed to update targeting!");
		System.out.println("AdSet updated successfully");

		System.out.println("************ adSetOperations().deleteAdSet(adSetId) ************");
		template.adSetOperations().deleteAdSet(newAdSetId);
		System.out.println("AdSet deleted successfully!");
	}

}
