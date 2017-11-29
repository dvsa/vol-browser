import org.dvsa.testing.lib.Browser;
import org.dvsa.testing.lib.URI;
import org.dvsa.testing.lib.utils.ApplicationType;
import org.dvsa.testing.lib.utils.Environment;
import org.junit.*;

public class BrowserTest {
    private static String url = URI.build(ApplicationType.EXTERNAL, Environment.PRODUCTION, "auth/login/");

    @BeforeClass
    public static void beforeAll(){
        System.setProperty("browser", "chrome");
    }

    @Before
    public void setUp(){
        Browser.open(url);
    }

    @Test
    public void goToLogonPage(){
        Browser.open(url);
        Assert.assertEquals(url, Browser.getURL());
    }

    @After
    public void tearDown(){
        Browser.close();
    }
}
