package pl.sgorecki;

import org.springframework.social.facebook.api.PagedList;
import pl.sgorecki.facebook.marketing.ads.AdCreative;
import pl.sgorecki.facebook.marketing.ads.FacebookAds;

/**
 * @author Sebastian Górecki
 */
public class AdCreativeCheck {
	private AdCreativeCheck() {
		throw new AssertionError();
	}

	public static void run(FacebookAds template, String accountId, String adSetId) {
		System.out.println("******** creativeOperations().getAccountCreatives(accountId) *********");
		PagedList<AdCreative> creatives = template.creativeOperations().getAccountCreatives(accountId);
		creatives.stream().forEach(creative -> System.out.println("Name: " + creative.getName() + ", body: " + creative.getBody() + ", linkUrl: " + creative.getLinkUrl()));

		System.out.println("******** creativeOperations().getAdSetCreatives(adSetId) *********");
		PagedList<AdCreative> adSetCreatives = template.creativeOperations().getAdSetCreatives(adSetId);
		adSetCreatives.stream().forEach(creative -> System.out.println("Name: " + creative.getName() + ", body: " + creative.getBody() + ", linkUrl: " + creative.getLinkUrl()));

		System.out.println("******** creativeOperations().getAdCreative(adCreativeId) *********");
		String adCreativeId = adSetCreatives.stream().findAny().orElseThrow(RuntimeException::new).getId();
		AdCreative creative = template.creativeOperations().getAdCreative(adCreativeId);
		System.out.println("Name: " + creative.getName());
		System.out.println("Object ID: " + creative.getObjectId());
		System.out.println("Type: " + creative.getType());
		System.out.println("Status: " + creative.getStatus());
		System.out.println("Object story ID: " + creative.getObjectStoryId());

		AdCreative toCreateCreative = new AdCreative();
		toCreateCreative.setName(Utils.getRandomString());
		toCreateCreative.setTitle(Utils.getRandomString());
		toCreateCreative.setBody(Utils.getRandomString());
		toCreateCreative.setObjectUrl("http://onet.pl/");
		toCreateCreative.setImageUrl("http://i.tvmaniak.pl/tvmaniak/2011/11/onet-logo1.jpg");
		System.out.println("******** creativeOperations().createAdCreative(accountId, adCreative) *********");
		String newCreativeId = template.creativeOperations().createAdCreative(accountId, toCreateCreative);
		AdCreative createdCreative = template.creativeOperations().getAdCreative(newCreativeId);
		if (!createdCreative.getName().equals(toCreateCreative.getName()))
			throw new RuntimeException("Failed to set proper name to new creative!");
		if (!createdCreative.getTitle().equals(toCreateCreative.getTitle()))
			throw new RuntimeException("Failed to set proper title on new creative!");
		if (!createdCreative.getBody().equals(toCreateCreative.getBody()))
			throw new RuntimeException("Failed to set proper body on new creative!");
		if (!createdCreative.getObjectUrl().equals(toCreateCreative.getObjectUrl()))
			throw new RuntimeException("Failed to set proper object url on new creative!");
		if (createdCreative.getImageUrl().length() == 0)
			throw new RuntimeException("Failed to set proper image url to new creative!");
		System.out.println("AdCreative created successfully!");

		System.out.println("******** creativeOperations().renameAdCreative(creativeId, String) *********");
		String newCreativeName = "Not random creative name #" + Utils.getRandomString();
		boolean success = template.creativeOperations().renameAdCreative(newCreativeId, newCreativeName);
		AdCreative renamedCreative = template.creativeOperations().getAdCreative(newCreativeId);
		if (!success) throw new RuntimeException("Failed to rename creative!");
		if (!renamedCreative.getName().equals(newCreativeName))
			throw new RuntimeException("Failed to rename creative!");
		System.out.println("AdCreative renamed successfully!");

		System.out.println("******** creativeOperations().deleteAdCreative(creativeId) *********");
		template.creativeOperations().deleteAdCreative(newCreativeId);
		System.out.println("AdCreative deleted successfully!");
	}

}
