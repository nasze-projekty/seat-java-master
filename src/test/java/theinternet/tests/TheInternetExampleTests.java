package theinternet.tests;

import com.framework.ui.tests.BaseUITest;
import com.google.common.truth.Truth8;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import theinternet.pages.*;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

@Feature("The Internet")
@Test
public class TheInternetExampleTests extends BaseUITest {

    @TmsLink("INT-1")
    @Story("Basic Auth Login")
    public void basic_auth() {

        String pageSource = WelcomePage.open().then()
                .navigateToBasicAuth("admin", "admin")
                .getSource();

        assertThat(pageSource).contains(
                "Congratulations! You must have the proper credentials.");
    }

    @TmsLink("INT-2")
    @Story("Check Checkboxes")
    public void check_boxes() {

        // Navigate to the checkboxes page
        Stream<Boolean> checkboxesStatus =
                WelcomePage.open()
                        .clickCheckboxesLink()
                        .checkAllCheckboxes()
                        .getAllCheckboxCheckedStatus();

        // Assert that all checkboxes are checked
        Truth8.assertThat(checkboxesStatus)
                .doesNotContain(false);
    }

    @TmsLink("INT-3")
    public void drag_and_drop() {

        // Navigate to the checkboxes page
        List<String> headings = WelcomePage.open().then()
                .clickDragAndDropLink()
                .dragAontoB()
                .getListOfHeadings();

        // Assert on the order of the headings
        assertThat(headings)
                .containsExactly("B", "A")
                .inOrder();
    }

    @TmsLink("INT-4")
    public void dropdown() {

        // Navigate to the checkboxes page
        DropdownPage dropdownPage = WelcomePage.open().then().clickDropdownLink();

        // Drag A onto B
        dropdownPage.selectFromDropdown("Option 1");

        // Assert selected
        assertThat(dropdownPage.getSelectedOptionText())
                .isEqualTo("Option 1");
    }

    @TmsLink("INT-5")
    public void file_download() {

        // Navigate to the download page
        FileDownloadPage downloadPage = WelcomePage.open().then().clickFileDownloadLink();

        // Confirm that the some-file.txt file in the list (as other people might be using it!)
        Truth8.assertThat(downloadPage.getDownloadableFileLinkNames())
                .contains("some-file.txt");
    }

    @TmsLink("INT-6")
    public void form_authentication() {

        // Navigate to the form authentication page
        String username = "tomsmith";
        FormAuthenticationPage formAuthenticationPage = WelcomePage
                .open().then()
                .clickFormAuthenticationLink()
                // Log in with the bad password and expect to land where we are
                .login(username, "BadBadPassword", FormAuthenticationPage.class)
                .expectErrorMessage();

        // Log in with the username password provided
        FormAuthenticationSuccessPage successPage = formAuthenticationPage
                .login(username, "SuperSecretPassword!", FormAuthenticationSuccessPage.class);

        // Confirm that we're on the success page
        assertThat(successPage.getSource()).contains("Welcome to the Secure Area");
    }

    @TmsLink("INT-7")
    public void hovers() {

        // Navigate to the hovers page
        HoversPage hoversPage = WelcomePage.open().then().clickHoversLink();

        // Confirm that the caption under the first figure contains expected text
        assertThat(hoversPage.getFirstFigureCaption()).contains("name: user1");
    }

    @TmsLink("INT-8")
    public void javascript_alerts() {

        // Navigate to the javascript alerts page
        JavaScriptAlertsPage javascriptAlerts =
                WelcomePage.open().then().clickJavascriptAlertsLink();

        javascriptAlerts.clickAlertButtonAndAccept();
        assertThat(javascriptAlerts.getResultText())
                .isEqualTo("You successfuly clicked an alert");

        javascriptAlerts.clickAlertButtonAndDismiss();
        assertThat(javascriptAlerts.getResultText())
                .isEqualTo("You successfuly clicked an alert");

        javascriptAlerts.clickConfirmButtonAndAccept();
        assertThat(javascriptAlerts.getResultText())
                .isEqualTo("You clicked: Ok");

        javascriptAlerts.clickConfirmButtonAndDismiss();
        assertThat(javascriptAlerts.getResultText())
                .isEqualTo("You clicked: Cancel");

        String textToEnter = "Blah blah blah";
        javascriptAlerts.clickPromptButtonAndEnterPrompt(textToEnter);
        assertThat(javascriptAlerts.getResultText())
                .isEqualTo("You entered: " + textToEnter);
    }

    @TmsLink("INT-9")
    public void key_presses() {

        // Navigate to the key presses page
        KeyPressesPage keyPressesPage = WelcomePage
                .open()
                .clickKeyPressesLink()
                .enterKeyPress(Keys.ENTER);

        assertThat(keyPressesPage.getResultText())
                .isEqualTo("You entered: " + Keys.ENTER.name());
    }
}
