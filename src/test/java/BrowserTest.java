import activesupport.MissingRequiredArgument;
import activesupport.file.Files;
import activesupport.system.Properties;
import org.dvsa.testing.lib.browser.Browser;
import org.dvsa.testing.lib.browser.exceptions.UninitialisedDriverException;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.*;

import java.io.IOException;
import java.nio.file.Paths;

public class BrowserTest {

        private static EnvironmentType environmentType;
        private static java.net.URL myURL;

    @BeforeClass
    public static void beforeAll() throws IOException {
        if(System.getProperty("env") == null){
            Properties.writeToConfigPropertyFile("env", "da");
        }

        if(System.getProperty("browser") == null){
            Properties.writeToConfigPropertyFile("browser", "chrome");
        }

        Properties.loadConfigPropertiesFromFile();
        environmentType = EnvironmentType.getEnum(System.getProperty("env"));
        myURL = URL.build(ApplicationType.EXTERNAL, environmentType, "auth/login/");
    }

    @Before
    public void setUp() throws MissingRequiredArgument {
        Browser.open(myURL);
    }

    @Test
    public void goToLogonPage() throws UninitialisedDriverException {
        Assert.assertEquals(myURL.toString(), Browser.getURL());
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteFolderAndItsContent(Paths.get("properties/config.properties"));
    }

}
