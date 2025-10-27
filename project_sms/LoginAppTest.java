import org.junit.Test;
import static org.junit.Assert.*;

public class LoginAppTest {
    @Test
    public void testValidateLogin() {
        LoginApp loginApp = new LoginApp();

        boolean result = loginApp.validateLogin("admin", "123456");

        assertTrue(result); 
    }
}
