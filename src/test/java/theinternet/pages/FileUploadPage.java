package theinternet.pages;

import com.framework.ui.annotations.Visible;
import com.framework.ui.pages.BasePage;
import com.framework.ui.pages.PageFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.FileInput;

public class FileUploadPage extends BasePage<FileUploadPage> {

    @Visible
    @Name("Choose Files button")
    @FindBy(css = "input#file-upload")
    private FileInput chooseFileInput;

    @Visible
    @Name("Upload button")
    @FindBy(css = "input#file-submit")
    private WebElement uploadButton;

    @Step("Upload a file by choosing file and then clicking upload")
    public FileUploadSuccessPage uploadFile(String filePath) {
        chooseFileInput.setFileToUpload(filePath);
        uploadButton.click();
        return PageFactory.newInstance(FileUploadSuccessPage.class);
    }

}
