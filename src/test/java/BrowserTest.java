import activesupport.MissingRequiredArgument;
import activesupport.system.Properties;
import org.dvsa.testing.lib.browser.Browser;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.*;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;

import java.io.IOException;

public class BrowserTest {

        private static java.net.URL myURL;

    @BeforeClass
    public static void beforeAll() throws IOException {
        if(Properties.get("env") == null){
            Properties.set("env", "da");
        }

        if(Properties.get("browser") == null){
            Properties.set("browser", "chrome");
        }

        myURL = URL.build(ApplicationType.EXTERNAL, Properties.get("env"), "auth/login/");
    }

    @Before
    public void setUp() throws MissingRequiredArgument {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        Browser.initialise(Properties.get("browser", true));
        Browser.open(myURL);
    }

    @Test
    public void goToLogonPage() {
        Assert.assertEquals(myURL.toString(), Browser.getURL());
    }

    @After
    public void tearDown() {
        Browser.getDriver().close();
    }

}
