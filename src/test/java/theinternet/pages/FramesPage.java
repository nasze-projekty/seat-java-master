package theinternet.pages;

import com.framework.ui.annotations.Visible;
import com.framework.ui.pages.BasePage;
import com.framework.ui.pages.PageFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class FramesPage extends BasePage<FramesPage> {

    @Visible
    @Name("iFrame Link")
    @FindBy(linkText = "iFrame")
    private WebElement iFrameLink;

    @Step("Click iFrame link")
    public IFramePage clickIFrameLink() {
        iFrameLink.click();
        return PageFactory.newInstance(IFramePage.class);
    }

}
