import activesupport.file.Files;
import activesupport.system.Properties;
import org.dvsa.testing.lib.Browser;
import org.dvsa.testing.lib.Environment;
import org.dvsa.testing.lib.URI;
import org.dvsa.testing.lib.utils.ApplicationType;
import org.dvsa.testing.lib.utils.EnvironmentType;
import org.junit.*;

import java.io.IOException;
import java.nio.file.Paths;

public class BrowserTest {

        private static EnvironmentType environmentType;
        private static String URL;

    @BeforeClass
    public static void beforeAll() throws IOException {
        if(System.getProperty("env") == null){
            Properties.writeToConfigPropertyFile("env", "da");
        }

        if(System.getProperty("browser") == null){
            Properties.writeToConfigPropertyFile("browser", "chrome");
        }

        Properties.loadConfigPropertiesFromFile();
        environmentType = Environment.enumType(System.getProperty("env"));
        URL = URI.build(ApplicationType.EXTERNAL, environmentType, "auth/login/");
    }

    @Before
    public void setUp(){
        Browser.open(URL);
    }

    @Test
    public void goToLogonPage(){
        Assert.assertEquals(URL, Browser.getURL());
    }

    @After
    public void tearDown() throws IOException {
        Browser.quit();
        Files.deleteFolderAndItsContent(Paths.get("properties/config.properties"));
    }

}
