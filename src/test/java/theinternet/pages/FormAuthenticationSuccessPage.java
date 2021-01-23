package theinternet.pages;

import com.framework.ui.annotations.Visible;
import com.framework.ui.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class FormAuthenticationSuccessPage extends BasePage<FormAuthenticationSuccessPage> {

    @Visible
    @Name("Logout button")
    @FindBy(css = "a[href='/logout']")
    private WebElement logoutButton;

    @Step("Log out")
    public void logout() {
        logoutButton.click();
    }

}
