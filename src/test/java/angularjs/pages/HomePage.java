package angularjs.pages;

import com.framework.ui.UITestLifecycle;
import com.framework.ui.annotations.Visible;
import com.framework.ui.pages.BasePage;
import com.framework.ui.pages.PageFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.util.concurrent.TimeUnit;

public class HomePage extends BasePage<HomePage> {

    @Name("Develop navigation bar item")
    @Visible
    @FindBy(xpath = "//a[text()='Develop']")
    private WebElement developDropdown;

    @Name("Developer Guide menu item")
    @FindBy(xpath = "//a[text()='Developer Guide']")
    private WebElement developerGuideMenuItem;

    @Step("Open home page")
    public static HomePage open() {
        UITestLifecycle.get().getWebDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        return PageFactory.newInstance(HomePage.class, "https://angularjs.org/");
    }

    @Step("Click developer guide drop down menu")
    public HomePage clickDevelopMenu() {
        developDropdown.click();
        return this;
    }

    @Step("Click developer guide link")
    public DeveloperGuidePage clickDeveloperGuideLink() {
        developerGuideMenuItem.click();
        return PageFactory.newInstance(DeveloperGuidePage.class);
    }

}
