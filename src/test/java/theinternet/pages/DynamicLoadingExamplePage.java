package theinternet.pages;

import com.framework.ui.annotations.Invisible;
import com.framework.ui.annotations.Visible;
import com.framework.ui.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.annotations.Timeout;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class DynamicLoadingExamplePage extends BasePage<DynamicLoadingExamplePage> {

    @Visible
    @Name("Start button")
    @FindBy(css = "#start button")
    private Button startButton;

    @Invisible  // make sure it's not there
    @Timeout(0) // used to speed up the wait for Invisible TypifiedElements
    @Name("Hidden element")
    @FindBy(id = "finish")
    private TextBlock dynamicElement;

    @Step("Click Start")
    public DynamicLoadingExamplePage clickStart() {
        startButton.click();
        wait(visibilityOf(dynamicElement.getWrappedElement()));
        return this;
    }

    @Step("Wait for the hidden element to be displayed")
    public DynamicLoadingExamplePage waitForElementToBeDisplayed() {
        wait(visibilityOf(dynamicElement.getWrappedElement()));
        return this;
    }

    public boolean isElementDisplayed() {
        return dynamicElement.isDisplayed();
    }

    public boolean isElementPresent() {
        try {
            return dynamicElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
