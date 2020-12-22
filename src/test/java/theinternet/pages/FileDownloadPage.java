package theinternet.pages;

import com.framework.ui.annotations.Visible;
import com.framework.ui.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;
import java.util.stream.Stream;

public class FileDownloadPage extends BasePage<FileDownloadPage> {

    @Visible
    @Name("Generic download link")
    @FindBy(css = "div.example a")
    private List<Link> allDownloadLinks;

    @Step("Return all download link names")
    public Stream<String> getDownloadableFileLinkNames() {
        return allDownloadLinks.stream()
                .map(Link::getText);
    }

}
