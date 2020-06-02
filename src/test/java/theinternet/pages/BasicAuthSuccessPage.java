package theinternet.pages;

import com.framework.ui.annotations.Visible;
import com.framework.ui.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class BasicAuthSuccessPage extends BasePage<BasicAuthSuccessPage> {

    @Visible
    @Name("Header text")
    @FindBy(css = "div.example h3")
    private WebElement headerText;
}
