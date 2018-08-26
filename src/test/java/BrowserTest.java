import activesupport.MissingRequiredArgument;
import activesupport.system.Properties;
import org.dvsa.testing.lib.browser.Browser;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.junit.*;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;

import java.io.IOException;
import java.net.MalformedURLException;

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
    public void goToLogonPage() throws MalformedURLException {
        Assert.assertThat(Browser.getURL().toString(), new StringContains(myURL.toString()));
    }

    @After
    public void tearDown() {
        Browser.getDriver().close();
    }

}
