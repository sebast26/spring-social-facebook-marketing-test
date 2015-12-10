package pl.sgorecki;

import org.springframework.social.facebook.api.PagedList;
import pl.sgorecki.facebook.marketing.ads.*;

import java.util.Random;
import java.util.UUID;

/**
 * @author Sebastian Górecki
 */
class AdAccountCheck {
	private AdAccountCheck() {
		throw new AssertionError();
	}

	static void run(FacebookAds template, String userId) {
		PagedList<AdAccount> adAccounts = template.accountOperations().getAdAccounts(userId);
		System.out.println("******** accountOperations.getAdAccounts(userId)  *********");
		adAccounts.stream().forEach(adAccount -> System.out.println("Ad account ID: " + adAccount.getId()));

		String accountId = String.valueOf(adAccounts.stream().findAny().orElseThrow(RuntimeException::new).getAccountId());
		System.out.println("******** accountOperations.getAdAccount(accountId)  *********");
		AdAccount adAccount = template.accountOperations().getAdAccount(accountId);
		System.out.println("Ad account ID: " + adAccount.getId());
		System.out.println("Ad account accountId: " + adAccount.getAccountId());
		System.out.println("Ad account name: " + adAccount.getName());
		System.out.println("Ad account spend_cap: " + adAccount.getSpendCap());

		PagedList<AdCampaign> adAccountCampaigns = template.accountOperations().getAdAccountCampaigns(accountId);
		System.out.println("******** accountOperations.getAccountCampaigns(accountId)  *********");
		adAccountCampaigns.stream().forEach(campaign -> System.out.println("Campaign ID: " + campaign.getId() + ", name: " + campaign.getName()));

		PagedList<AdUser> adAccountUsers = template.accountOperations().getAdAccountUsers(accountId);
		System.out.println("******** accountOperations.getAdAccountUsers(accountId)  *********");
		adAccountUsers.stream().forEach(user -> System.out.println("User name: " + user.getName() + ", role: " + user.getRole()));

		AdInsight insight = template.accountOperations().getAdAccountInsight(accountId);
		System.out.println("******** accountOperations.getAdAccountInsight(accountId)  *********");
		System.out.println("Cost per inline click: " + insight.getCostPerInlineLinkClick());
		System.out.println("Cost per inline post engagement: " + insight.getCostPerInlinePostEngagement());
		System.out.println("Cost per total action: " + insight.getCostPerTotalAction());
		System.out.println("Impressions: " + insight.getImpressions());
		System.out.println("Inline link clicks: " + insight.getInlineLinkClicks());
		System.out.println("Inline post engagement: " + insight.getInlinePostEngagement());

		AdAccount toUpdateAdAccount = new AdAccount();
		toUpdateAdAccount.setName(Utils.getRandomString());
		toUpdateAdAccount.setSpendCap(Long.toString(Utils.getRandomLong(50000L, 70000L)));
		boolean success = template.accountOperations().updateAdAccount(accountId, toUpdateAdAccount);
		if (!success) throw new RuntimeException("Failed to update AdAccount!");
		AdAccount updatedAdAccount = template.accountOperations().getAdAccount(accountId);
		if (!updatedAdAccount.getName().equals(toUpdateAdAccount.getName()))
			throw new RuntimeException("Failed to update account name! Current name: " + updatedAdAccount.getName());
		String buggySpendCap = String.valueOf(Long.valueOf(toUpdateAdAccount.getSpendCap()) * 100);
		if (!updatedAdAccount.getSpendCap().equals(buggySpendCap))
			throw new RuntimeException("Failed to update spend cap! Current spend cap: " + updatedAdAccount.getSpendCap());
		System.out.println("************ Ad Account updated successfully! ****************");
	}
}
