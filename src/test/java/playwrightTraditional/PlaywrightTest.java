package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class PlaywrightTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    public void init() {
        try (Playwright pw = Playwright.create()) {
            playwright = pw;
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            context = browser.newContext(new Browser.NewContextOptions()
                            .setRecordVideoDir(Paths.get("videos/"))
                            .setRecordVideoSize(1280, 720));
            page = context.newPage();
        } catch (Exception e) {
            System.out.println("Error in playwright init");
        }
    }

    @AfterAll
    static void closeAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    void testCaseBookstore() {

        page.navigate("https://depaul.bncollege.com/");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).press("Enter");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("brand JBL (12)")).getByRole(AriaRole.IMG).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
        page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("Color Black (9)")).locator("svg").first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
        page.getByText("Price Over $").click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();

        assertThat(page.getByLabel("main")).containsText("sku 668972707");
        assertThat(page.getByLabel("main").getByRole(AriaRole.HEADING)).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
        assertThat(page.getByLabel("main")).containsText("Adaptive noise cancelling allows awareness of environment when gaming on the go. Light weight, durable, water resist. USB-C dongle for low latency connection < than 30ms.");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();

        // Shopping cart page
        assertThat(page.getByLabel("main")).containsText("Your Shopping Cart");

        assertThat(page.getByLabel("main")).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
        assertThat(page.getByLabel("main")).containsText("$164.98");
        Locator amount = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Quantity, edit and press"));
        assertThat(amount).containsText("1");

        page.getByText("FAST In-Store PickupDePaul").click();
        assertThat(page.getByText("$").nth(1)).containsText("$164.98");
        assertThat(page.getByText("$3.00", new Page.GetByTextOptions().setExact(true))).isVisible();
        assertThat(page.getByText("TBD")).isVisible();
        assertThat(page.getByText("$167.98")).isVisible();

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).fill("TEST");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply Promo Code")).click();

        assertThat(page.getByText("The coupon code entered is")).isVisible();

        // TestCase Create Account Page
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed To Checkout")).first().click();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Create Account"))).isVisible();

        // Contact Information Page
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Contact Information"))).isVisible();

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)")).fill("Joe");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)")).press("Tab");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)")).fill("Smitth");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)")).press("Tab");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)")).fill("test@gmail.com");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)")).press("Tab");
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("United States: +")).press("Tab");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)")).fill("2246055341");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)")).press("Tab");

        assertThat(page.getByText("$164.98").nth(2)).isVisible();
        assertThat(page.getByText("$3.00").nth(3)).isVisible();
        assertThat(page.getByText("TBD").nth(2)).isVisible();
        assertThat(page.getByText("$167.98").nth(1)).isVisible();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

        assertThat(page.getByText("$164.98").nth(2)).isVisible();
        assertThat(page.getByText("$3.00").nth(3)).isVisible();
        assertThat(page.getByText("TBD").nth(2)).isVisible();
        assertThat(page.getByText("$167.98").nth(1)).isVisible();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).nth(1)).isVisible();
        assertThat(page.getByText("$164.98").nth(3)).isVisible();

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to cart")).click();

        // Shopping cart again
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove product JBL Quantum")).click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Your cart is empty"))).isVisible();

        closeAll();

    }
}