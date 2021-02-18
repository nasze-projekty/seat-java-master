# Framework User Guide

- [ABOUT FRAMEWORK](#about-framework)
- [GETTING STARTED](#getting-started)
- [USAGE GUIDE](#usage-guide)
- [WHAT ITS FOR](#what-for)
- [A COUPLE OF COOL THINGS](#cool-thigs)
- [COMMAND LINE OPTIONS](#command-line)
- [LAYERS](#layers)
- [TESTS](#tests)
- [PAGE OBJECTS](#page-objects)
- [ASSERTS](#asserts)
- [WAITS](#waits)
- [IDENTIFYING WEB ELEMENTS](#identifying)
- [SPECIALISED WEBELEMENTS](#specialised)
- [ALLURE REPORTING](#allure)
- [LOGGING](#logging)
- [ZEPHYR FOR JIRA INTEGRATION](#jira-integration)
- [USING CONFIG FILES RATHER THAN COMMAND LINE PARAMS](#using-config-files)
- [SELENIUM GRID](#selenium-grid)
- [MULTIPLE TEST ENVIRONMENTS](#multiple-test-environments)
- [NATIVE CONTROLS](#native-controls)
- [RUNNING VIA BUILD TOOL (E.G. JENKINS)](#running-via-build-tool)
- [CUSTOM BROWSER IMPLEMENTATIONS](#custom-browser-implementations)
- [JAVASCRIPT HACKS](#javascript-hacks)
- [HOVERING WITH CHROMEDRIVER](#hovering-with-chromedriver)
- [AUTOMATICALLY RETRY TESTS ON FAILURE](#automatically-retry-tests-on-failure)

## <a name="about-framework"></a>About Framework

This is a `sample` framework for writing maintainable Selenium and REST API tests that also makes 
integrating with other test things (e.g. JIRA) much easier.

The Framework project is based on Ardesco's [Selenium-Maven-Template][ardesco] and 
Joe VDW's [Bootstrapium][bootstrapium]. We have extended it with some handy extras
for getting started quickly with Selenium, Appium and [Rest Assured][rest-assured].

## <a name="getting-started"></a>Getting Started

After setting up [apache maven][mvn], open the `framework` directory in a 
terminal/command prompt and run `mvn clean verify` to run the example tests using Firefox.

You will need the [geckodriver][geckodriver] on your path if you are using 
Firefox version 48 or above.

### Browsers

You can provide the `-Dbrowser` argument to chose a browser to run the tests in.

#### Drivers

Each browser requires a "driver".

For chrome, [ChromeDriver][chromedriver] needs to be on your path or specified
as an argument:
```
mvn clean verify -Dbrowser=chrome -Dwebdriver.chrome.driver=c:\path\to\chromedriver.exe
```

For Firefox 48 and above, [geckodriver][geckodriver] needs to be on your path or specified
as an argument:
```
mvn clean verify -Dbrowser=firefox -Dwebdriver.gecko.driver=c:\path\to\geckodriver.exe
```

# <a name="usage-guide"></a>Usage Guide


________________________________________
## <a name="what-for"></a>WHAT ITS FOR

Framework provides strong code structure guidelines and contains some nice Java libraries to make writing tests (web, mobile app, API, other tests that we’ve not thought of yet) more consistent and familiar.
It saves reinventing the wheel.
It should mean you spend less time worrying about the plumbing, and more time thinking about the actual tests themselves. Your test projects should also stay structured, clean and focused on the testing, rather than full of helpers and widgets.
________________________________________
## <a name="cool-thigs"></a>A Couple of Cool Things

To whet the appetite, here’s a couple of examples where Framework features make your life easier.

#### @Visible
Say you’re expecting an element on a web page, and you want to wait until it’s there and visible before clicking on it.

##### Vanilla Selenium
```
WebElement button = driver.findElement(By.cssSelector("#search_button");
WebDriverWait wait = new WebDriverWait(driver, 10);
wait(ExpectedConditions.visibilityOf(button));
button.click();
```
You could probably do this with a (longish) one-liner, but if we were trying to make the code nice and maintainable etc.
#### PageFactory
```
// at the top of your class
@FindBy(css="#search_button")
private WebElement button;

// when you want to use it
WebDriverWait wait = new WebDriverWait(driver, 10);
wait(ExpectedConditions.visibilityOf(button));
button.click();
```
Which is nicer, because:
1.	The CSS selector for a control is in one place, rather than repeating it every time you use it. It changes? You update the 1 instance, rather than 5+.
2.	PageFactory removes the need for quite so much driver. action - again reducing the reuse, overlap, repetition, etc
(NB If you’ve never heard of PageFactory, it’s a pattern for using page objects that’s part of the selenium libraries)
 ##### Framework then builds on this:
```
// at the top of your class
@Visible
@FindBy(css="#search_button")
private WebElement button;

// when you want to use it
button.click();
```
By using the `@Visible` annotation, when the page loads you’d automatically wait for the button to be visible. Which further neatens things - particularly when you imagine there might be 10-20 things on a page that we might want to wait for.
There’s also an `@Invisible`, and both works with `List<WebElement>`, `HtmlElements` etc.
________________________________________
## <a name="command-line"></a>Command Line Options
Tests can be executed by running `mvn clean verify`. This can be followed by any properties, in the form `-DpropertyName=value`, you wish to specify.
See also the “Using Config files…” entry for using config files to provide a subset of these params instead
#### General

|Property|Description|Values|Default|
|---|---|---|---|
|`test`|The test class, or comma separated list of test classes, to run. Can include wildcards.|e.g. `MyTest*`|All tests|
|`threads`|The number of threads to use.|e.g. `3`|1|
|`reuseBrowser`|Will re-use existing browsers rather than starting a new instance for each test.|`true` or `false`|`false`|
|`groups`|The TestNG test groups which you wish to run. | e.g. `checkintest`|All groups|
|`build`|The build version or app version to log to Sauce Labs, BrowserStack, or Capture.|e.g. `build-1234`|None|
|`proxy`|Proxy server to be used from Selenium and REST API requests.|`system`,`autodetect`,`direct` or `http://{hostname}:{port}`, e.g.`http://10.3.2.22:80`|None|
|`maxRetryCount`|Additional attempts to retry a failed test.|e.g.`3`| 1 |

#### Browser Properties/Capabilities
|  Property |  Description |  Values | Default  |
|---|---|---|---|
|`browser`|The browser on which you wish to run the tests.|`firefox`, `chrome`, `safari`, `ie`, `opera`, `legacyfirefox`,`electron`,`custom`|`firefox` |
|`maximise`| Maximise browser on opening (if possible).|`true` or `false`|`false`|
|`resolution`|Set browser dimensions to specific setting (if possible).|e.g. `1024x543`|none|
|`chromeUserDataDir`|See `customBrowserImpl` below for preferred method.|e.g. `path/to/chrome_user_data_dir`|none|
|`customBrowserImpl`|Allows users to specify classname (fully qualified name in v3.0.0) of their own browser implementation, for example for specifying a custom set of Capabilities.|e.g. `ChromeIncognitoBrowserImpl` or since v3.0.0 `my.package.ChromeIncognitoBrowserImpl`|None|
|`headless`|Allows users to run Chrome or Firefox in a headless environment|`true` or `false`|`false`|

#### Remote Grids. Devices and Platforms
###### No defaults.
|Property|Description|Values|
|---|---|---|
|`gridURL`|The URL of your Selenium Grid hub.|e.g.`http://localhost:4444/wd/hub` - NB `/wd/hub` is required!|
|`browserStack`|Must be set to `true` if you wish to run on BrowserStack.|`true` or `false`|
|`sauce`|Must be set to true if you wish to run on Sauce Labs.|`true` or `false`|
|`browserVersion`|The browser version on which you wish to run the tests. Only used when running remotely e.g. on Selenium Grid, Sauce Labs or BrowserStack.|e.g. `8.0`|
|`platform`|The platform on which you wish to run the tests. Only used when running remotely. To be specified instead of ‘os’ when running with BrowserStack.|e.g. `windows`, `ios`, `android`, `OSX`|
|`platformVersion`|The platform version on which you wish to run the tests. Only used when running remotely. To be specified instead of ‘os_version’ when running with BrowserStack.|e.g. `5.0`|
|`device`|The device on which you wish to run remote tests. If not using SauceLabs or BrowserStack, can be specified with the `-Dbrowser=chrome` parameter to instigate a Chrome browser emulator of the specified device.|`iPhone`, `iPad`, `iPhone Retina 4-inch`, `Galaxy S4`, etc.|
|`applicationName`|Specify applicationName parameter for a grid run.|e.g. `windows7_32bits_firefox`|
|`videoCaptureUrl`|Enable video capture using Grid plugins. Usage of video capture is generic as possible. All grid plugins, such as Selenium Grid Extras, capture videos by the WebDriver session ID.|e.g. `http://localhost:3000/download_video/%s.mp4`|
|`appPath`|The path to the `apk` file to be used in Sauce Labs. It is also used as the `app` desired capability of the supported [WinAppDriver][winAppDriver].|e.g. `/home/dev/android/build/newapp_1.2.5.apk` for SauceLabs or `Microsoft.WindowsAlarms_8wekyb3d8bbwe!App`, or `C:\Windows\System32\notepad.exe` for the WinAppDriver.|

##### Remote Supported Devices/Platforms
- [BrowserStack][BrowserStack]
- [SauceLabs][SauceLabs]

#### Jira/Zephyr/Test Result Logging Integrations
###### No defaults.

|Property|Description|Values|
|---|---|---|
|`jiraURL`|The base URL of the JIRA instance you want to use|e.g. `http://jira:8080`|
|`jiraUsername`|The JIRA user you want to use|e.g. `JBloggs`|
|`jiraPassword`|The JIRA user’s password|e.g. `password`|
|`jqlQuery`|the JQL query to look up the JIRA tests to run (the results of the query will be looked up against the `@Issue` annotations on tests).|e.g. `(priority=1 and component=Admin) or issueKey=JIRA-123`|
|`jiraResultFieldName`|The Jira field name to attempt to log results to for the specified `@Issue`. The values to change the field to are specified in the Jira config file. Useful if you’re using a Jira field to mark the test result.|e.g. `Test Result`|
|`jiraResultTransition`|If specified, will attempt to transition the `@Issue` specified through the transitions specified in the Jira config. Useful if using a customised Jira workflow for managing test results.|any value|
|`resultVersion`|The ‘Version’ to mark the test execution against in Zephyr for JIRA (requires ZAPI).|e.g. `App v1.1.2`|
|`zapiCycleRegEx`|If the Zephyr test cycle name contains this string test results will be logged against the matching cycles.|e.g. `firefox` or `my-special-cycle`|

#### Spira Integration
###### No defaults.

|Property|Description|Values|
|---|---|---|
|`spiraURL`|The base URL of the Spira instance you want to use|e.g. `http://spira:8080`|

#### Capture Integration
A Capture server is a custom server developed to allow Framework tests to send screenshots for each step they are executing. All three need to be defined for the integration to work correctly.
###### No defaults.

|Property|Description|Values|
|---|---|---|
|`captureURL`|The base URL of the Capture instance you want to automatically send screenshots and step information to.|e.g. `http://capture/`|
|`sutName`|The name of the system under test (SUT) to be presented in the Capture dashboard.|e.g. `My App`|
|`sutVersion`|The release version to appear on the Capture dashboard.|e.g. `1.2.5`|


#### Examples
Running tests using Firefox:
```
mvn clean verify
```

Running web tests using Chrome:
```
mvn clean verify -Dbrowser=chrome
```
Running web tests on Firefox 58.0.1 with Selenium Grid:
```
mvn clean verify -Dbrowser=firefox -DbrowserVersion=58.0.1 -DgridURL=http://localhost:4444/wd/hub
```
Running test methods which match the patterntestM*on Firefox with Selenium Grid and Capture:
```
mvn clean verify -Dtest=TestClass#testM* -Dbrowser=firefox -DgridURL=http://grid:4444/wd/hub -DsutName="My Project" -DsutVersion="0.0.1" -DcaptureURL=http://capture/
```
Running mobile web tests on Chrome, using its device emulation:
```
mvn clean verify -Dbrowser=chrome -Ddevice="Apple iPad 3 / 4"
```
Running mobile web tests on BrowserStack Win XP Firefox 33:
```
export BROWSER_STACK_USERNAME=<username>
export BROWSER_STACK_ACCESS_KEY=<access_key>
mvn clean verify -DbrowserStack=true -Dbrowser=firefox -DbrowserVersion=57.0 -Dplatform=Windows -DplatformVersion=XP
```
Running mobile web tests on Sauce Labs iOS 8.0 iPad Simulator:
```
export SAUCE_USERNAME=<username>
export SAUCE_ACCESS_KEY=<access_key>
mvn clean verify -Dsauce=true -Dplatform=ios -Dbrowser=safari -DplatformVersion=8.0 -Ddevice="iPad Simulator"
```
Running mobile web tests on BrowserStack iOS iPad Air (real device):
```
export BROWSER_STACK_USERNAME=<username>
export BROWSER_STACK_ACCESS_KEY=<access_key>
mvn clean verify -DbrowserStack=true -Dbrowser=ios -Ddevice="iPad Air"
```
NB - platform and platformVersion (os & os_version in BrowserStack config) are not required or supported when running on mobile devices
Running mobile app tests on Sauce Labs Android Emulator:
```
export SAUCE_USERNAME=<username>
export SAUCE_ACCESS_KEY=<access_key>
mvn clean verify -Dsauce=true -Dplatform=android -DappPath=<path_to_.apk>
```
Run regression tests (as marked in JIRA with the label REGRESSION) and log test results against the v1.1.2 version in JIRA:
```
mvn clean
 verify -Dbrowser=firefox -DjiraURL=http://jira:8080 -DjiraResultVersion=v1.1.2 -DjqlQuery="labels=REGRESSION"
```
For full lists of platforms/browsers supported see: [BrowserStack][BrowserStack] and [SauceLabs][SauceLabs] platform lists.
________________________________________
## <a name="layers"></a>Layers

Framework encourages the use of page-object and service-object patterns - writing your test code in such a way that abstracts the functionality of the system you’re testing into it’s own layer. The tests interact with the page (or service) layer and the page (or service) layer interacts with the system under test (via libraries). In essence: the tests do the decision making, the pages or services do the grunt work.
If you build it right, when the service or page that you’re testing changes, but the test does not (e.g. a user still should be able to search for bananas), you just change the one method that conducts that operation, rather than the 15 tests that do banana searches. Likewise, if the id of a button changes following a major UI overhaul, if you’ve organised things right that ID will only be in one place (the relevant page object, in fact) and you can update just that one instance.

                    _________   ____________________
                   |         |  ||                   ||
     _________  /->|  PAGES  |->||                   ||
    |         |/   |_________|  ||   SYSTEM UNDER    ||
    |  TESTS  |     _________   ||       TEST        ||
    |_________|\   |         |  ||                   ||
                \->| SERVICES|->||                   ||
                   |_________|  ||___________________||

## <a name="tests"></a>Tests

### BaseTest
Test classes must extend `com.framework.ui.tests.BaseUITest`. This handles the setup and teardown of the WebDriver / AppiumDriver sessions. If you do not need “drivers” - e.g. you’re writing some API tests, you should extend `com.framework.api.tests.BaseAPITest` instead.
For example:
```
public class DynamicLoadingWebTest extends BaseUITest {
    //...
}
```

### Look - no drivers!
As you can see in the example tests there are no references to WebDriver thanks to Framework. This results in more readable tests which only deal with the services offered by your application and abstracts away the details and mechanics of it. For further explanation see the Page Objects page.
```
@Issue("BBA-5")
@Story("Logins")
@Test(description="Login to the application")
public void validLogin() {
    LandingPage.open().then()
            .clickLoginButton()
            .login("user", "password");
}
```
### Annotations
-	`@Issue` is be used for JIRA & ZAPI reporting, JQL Query executions, and within the Allure report
-	`@Story` utilise Allure’s Features and Stories hierarchy and make larger test suites easier to manage
-	`@Test` contains the description of the test, and can include a number of other details – (`alwaysRun`, `dataProvider`, `dataProviderClass`, `dependsOnGroups`, `dependsOnMethods`, `description`, `enabled`, `expectedExceptions`, `groups`, `invocationCount`, `invocationTimeOut`, `invocationcounts`, `priority`, `successPercentage`, `singleThreaded`, `timeOut`, `threadPoolSize`) see [TestNG Annotations][TestNG Annotations] for further details.
________________________________________
## <a name="page-objects"></a>Page Objects
### Code Structure
#### BasePage
Page objects must extend `com.framework.ui.pages.BasePage<T>`.
Where T is the type of the page you are creating e.g. `LoginPage`.
#### Elements (Fields)
Elements should be `private` and should only be used by this class.
##### Annotations
-	`@Name` allows additional reporting and debugging information to be used later.
-	`@Visible` Framework will wait for the element to be displayed before returning the page.
-	`@InVisible` Framework will wait for the element to NOT be displayed before returning the page.
-	`@FindBy` is a [Selenium annotation.][Selenium annotation].

##### Actions (bottom)

   All `public` methods on the page (the page’s interface) should return either:
   - a Page Object,
   -	built-in Java data types e.g. String or
   -	custom data types or
   -	nothing e.g. `void`

   Nothing Framework or Selenium specific e.g. WebElement should be returned
##### Navigation
If an action on a page results in moving to a new page, then you can instantiate a new Page Object in one of two ways:
-	`return PageFactory.newInstance(NextPage.class);` or
-	`return new NextPagePage().get();`

A common mistake is omitting the `.get()` in the second option which will result in a null pointer exception when trying to use the page object. This is because none of the fields (`WebElement`s) will be initialised.
See the `validLogin()` method below.
If performing an action on a page results in staying on the same page, then you can simply use `return this`; e.g. the `invalidLogin()` method below. If you keep getting `StaleElementReferenceException`s after return `this`, try to return a new page object instead, i.e. `return PageFactory.newInstance(ThisPage.class);`.
##### Annotations
-	`@Step` enables rich reporting of the steps being run. `{0}` and `{1}` (in the example below) will be replaced in reports with the values passed into the method.
#### Example
```
public class LoginPage extends BasePage<LoginPage> {

    @Name("Username text field")
    @Visible
    @FindBy(css="input#inputEmail")
    private WebElement usernameField;

    @Name("Password text field")
    @FindBy(css="input#inputPassword")
    private WebElement passwordField;

    @Step("Enter credentials and return logged in homepage")
    public LoggedInHomepage validLogin(String username, String password) {
        login(username, password);
        return new LoggedInHomepage().get();
    }

    @Step("Enter credentials and expect to stay on the login screen")
    public LoginPage invalidLogin(String username, String password) {
        login(username, password);
        return this;
    }

    @Step("Enter username {0} and password {1} and click login")
    private void login(String username, String password) {
        usernameField.clear();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.sendKeys(password);

        loginButton.click();
    }
}
```
________________________________________
## <a name="asserts"></a>Asserts
##### Tests that don’t assert anything aren’t Tests!
##### Only use asserts in your test layer - NEVER in pages!
Google Truth -`assertThat`

Many assert libraries are available, but we recommend using Google’s [Truth][Truth] library - it’s nice and flexible, produces far more useful assertion errors, and is part of Framework - just `import static com.google.common.truth.Truth.assertThat;` at the top of your test class.
##### Examples
###### Basics
```
assertThat(someInt).isEqualTo(5);
assertThat(aString).isEqualTo("Blah Foo");
assertThat(aString).contains("lah");
assertThat(foo).isNotNull();
```
###### Collections and Maps
```
assertThat(someCollection).contains("a");                       // contains this item
assertThat(someCollection).containsAllOf("a", "b").inOrder();   // contains items in the given order
assertThat(someCollection).containsExactly("a", "b", "c", "d"); // contains all and only these items
assertThat(someCollection).containsNoneOf("q", "r", "s");       // contains none of these items
assertThat(aMap).containsKey("foo");                            // has a key
assertThat(aMap).containsEntry("foo", "bar");                   // has a key, with given value
assertThat(aMap).doesNotContainEntry("foo", "Bar");             // does not have the given entry
```
See [here][Truth] for many more examples.
________________________________________

## <a name="waits"></a>Waits
Only use waits in your `Page Object` - never in your tests
You should not need to do many explicit waits - the `@Visible` and `@Invisible` tags etc., which work on Page Object load, should handle the majority of cases.
However - there are times when you’ll need to wait. For example:
-	we want to click a button, but
-	the button is initially hidden; but made visible by linking the ‘more’ arrow
First we click the ‘more’ arrow, then wait for the button to be visible. We can use `wait` and `ExpectedConditions`, as in the following example:
public class MyPage extends BasePage<MyPage> {
  ```
    @Visible
    @FindBy(id = "more-arrow")
    private WebElement moreArrow;

    // Initially hidden button
    @FindBy(css = "div#button")
    private WebElement myButton;

    public Page clickInitiallyHiddenButton() {
      moreArrow.click();
      wait(ExpectedConditions.visibilityOf(myButton));
      myButton.click();
      return this;
    }
}
```
[ExpectedConditions][ExpectedConditions] has lots of methods to help your waits - e.g. `elementToBeSelected(...)`, `titleContains(...)`, `textToBePresentInElement(...)`, `textToBePresentInElementValue(...)`, etc.
Framework has it’s own extension to `ExpectedConditions`, called `ExtraExpectedConditions`.
________________________________________
## <a name="identifying"></a>Identifying Web Elements
We identify elements on web pages using the `@FindBy` annotation, and passing in selector strings.
Try to pick selectors that are:
-	human readable
-	least likely to change
-	uniquely identify the element(s) you wish to interact with
#### @FindBy(id = “fromButton”)
ID’s are the preferred way to find elements on a page. This is because they are fast and (should be) unique and often human readable.
#### @FindBy(css = “input#fromButton”)
CSS selectors. These are the preferred means of identifying controls after ID. CSS selectors are generally easier to read than xpath, and generally as fast or faster than xpath. See [here][cssgame] for an interactive game to help you learn the syntax.
Developer tools for Firefox and Chrome allow you to identify and test your CSS selectors before you put them into your pages.
#### @FindBy(linkText = “Click here to view”)
`linkText` does exactly what it says on the tin - searches for any links on the page that display the text specified. BEWARE - often the same text will be used in multiple places on a page, so use this with caution.
#### @FindBy(xpath = “//div”)
`xpath` selectors. Best avoided unless everyone on your team knows xpath well. It’s probably better to learn CSS selectors. Xpath can be useful if you need to identify an element based on text within an element, as you can specify “contains”:
e.g.: `//div[contains(.,'Click here')]` will select the div `<div>Click here</div>`
Dev tools in the browsers allow you to test your selector before putting it into your code.
#### Tips
-	Try to select the actual element that you want to perform the operation on.e.g. `<div id="link-button"><a href="blah">Link</a></div>` -> `css="#link-button a"` will be more robust to click than `css="div#link-button"` since it’s actually the ink that you want - e.g. some browsers will treat the entire div as the link, others may not.
-	`Element Visibility` - Selenium will not find hidden controls on the page. This is on purpose, since it’s trying to simulate the activities of a real user. However, if it’s the ONLY way, you can use JavaScript injection to ‘unhide’ controls (see `@ForceVisible`).
-	Auto-Generated tags - the naming conventions of buttons will entirely depend on the framework used. Avoid using auto-generated ‘id’ tags, for example, as these tend to get regenerated when new pages/controls are added.
________________________________________

## <a name="specialised"></a>Specialised WebElements
Page Objects use the `@FindBy` annotation and `PageFactory` to identify, model and create elements on the web page. These are often `WebElement`s, however, because Framework uses the [Yandex HtmlElements library][Yandex HtmlElements library] we can make use of more specialised Objects.
##### HtmlElements Examples
The most useful are the `CheckBox`, `Select`, `Radio`, `Form`, and `FileInput`. Please read the documentation and explore their API in IntelliJ.
#### Tables
There is also a 'Table' which is suitable for small wikipedia, however it is not efficient and sometimes difficult to use.
#### StreamTable
This is why we created `StreamTable`. It utilises two features from Java 8, namely `Stream`s and `Optional`. If you are not familiar with these, please take the time to learn as they will greatly improve the code you are able to write.
`Stream`s are lazy which means they will only do the minimum amount of work required to you the task you set them. This is the largest benefit of `StreamTable`.
##### OptimisedStreamTable
`OptimisedStreamTable` is the same as `StreamTable` however it does not check each element for `isDisplayed` therefore will not work as expected on wikipedia with hidden rows or columns.
`OptimisedStreamTable` is approximately twice as fast as `StreamTable`.
##### AbstractStreamTable
If none of the above Table Objects work for your application then you can create your own Table `HtmlElement` by extending `AbstractStreamTable`.
You then have to implement three of the methods to specialise your new class for your use case.
________________________________________

## <a name="allure"></a>Allure Reporting
You can generate an [Allure][Allure] test report by running:
```
mvn site
```
or, since v3.0.0
```
mvn allure:report
```
Then, open `target/site/allure-maven-plugin.html` to view the report.
NB Either open via IntelliJ (they host it on a local web server for you) or use another browser!
This will interpret `@Step`, `@Issue`, `@Features` and `@Story` annotations, and provide rich reporting on the suite just run.
##### Jenkins
When running tests via Jenkins, the [Allure Jenkins Plugin][Allure Jenkins Plugin] can be added to the Jenkins job, and it will automatically produce and store reports for every run.
________________________________________
## <a name="logging"></a>Logging
#### Logging
Each test run generates a log as defined in the log4j.xml
By default, all contents will push to `Framework.log`, in the `/logs/` folder.
If you want to write to this log (useful for debugging), you should instantiate a new `Logger` in the class from which you’re logging. E.g.:
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
```
public class SomeClass extends BasePageOrBaseTestMaybe {

    private Logger logger = LogManager.getLogger(SomeClass.class);

    public void someMethod() {
        logger.info("I'm logging an info message");
        logger.warn("I'm logging a warn message");
        logger.debug("I'm logging a debug message");
        logger.error("I'm logging an error message");
    }
}
```
________________________________________
## <a name="jira-integration"></a>Zephyr for Jira Integration
#### Test Case Management (TCM) Logging - Zephyr for JIRA
Your tests may have test case equivalents, with manual steps written out, and perhaps linked and traceable to the original stories/tasks/epics/requirements/etc.
When some of these tests are automated and others are run manually, it can be difficult to combine run results to show the overall test status. By annotating or tagging your tests with their Jira ids, and providing your Jira instance details, Framework will log the results of test runs to Jira in real time - making it easier to keep track of execution progress.
##### 1 - Annotate or Tag your tests
```
public class ComponentExampleTest extends BaseTest {

    @TestCaseId("JIRA-1411")
    @Test
    public void myLovelyTest() {
        //some testing
    }
}
```
OR, if using BDD:
```
@TestCaseId:JIRA-1411
Feature: My Lovely Feature that needs testing

@TestCaseId:JIRA-1412
Scenario: Or my lovely scenario
```
##### 2 - Make sure test is in Zephyr for Jira test plan
Make a Zephyr for Jira test plan, and make sure your your test is in a particular version & test cycle. NB: the cycle can’t be ‘Ad Hoc’, and the version can’t be ‘Unscheduled’.
##### 3 - Provide JIRA details at runtime
```
mvn clean verify -DjiraURL=http://some/jira -DjiraUsername=user1 -DjiraPassword=pword -DresultVersion="Version 2.14.8"
# If you have the same test in multiple test cycles, you can specify the cycle with:
# -DzapiCycleRegEx="Some Cycle"
```

You should see the test gets marked as ‘WIP’ on start, and then the result is logged when the test finishes.
________________________________________
## <a name="using-config-files"></a>Using Config Files Rather than Command Line params
Rather than providing all of the details in the command line, you can instead create config files (in your `resources` folder) to store common configurations.
An example would be:
```/resources/config/FirefoxGrid.properties:
applicationName=
appPath=
browser=Firefox
browserStack=
browserVersion=
build=
captureURL=
customBrowserImpl=
device=
gridURL=http://localhost:4444/wd/hub
headless=
jiraPassword=
jiraResultFieldName=
jiraResultTransition=
jiraURL=
jiraUsername=
jqlQuery=
maximise=true
maxRetryCount=2
platform=
platformVersion=
proxy=
resolution=
resultVersion=
reuseBrowser=
sauce=
spiraURL=
sutName=
sutVersion=
threads=
videoCaptureUrl=
zapiCycleRegEx=
```
Colon (:) can also be used as a separator.
`/resources/config/FirefoxGrid.properties`:

```applicationName:
appPath:
browser: Firefox
browserStack:
browserVersion:
build:
captureURL:
customBrowserImpl:
device:
gridURL: http://localhost:4444/wd/hub
headless:
jiraPassword:
jiraResultFieldName:
jiraResultTransition:
jiraURL:
jiraUsername:
jqlQuery:
maximise: true
maxRetryCount: 2
platform:
platformVersion:
proxy:
resolution:
resultVersion:
reuseBrowser:
sauce:
spiraURL:
sutName:
sutVersion:
threads:
videoCaptureUrl:
zapiCycleRegEx:
```
and you’d then use this config file by running:
`mvn clean verify -Dconfig=FirefoxGrid.properties`
________________________________________
## <a name="selenium-grid"></a>Selenium Grid
We’ve already shown that you can use Framework to run your tests on a grid, just by specifying the gridURL parameter at runtime:
`mvn clean verify -DgridURL=http://someurl:4444/wd/hub`
Setting up a grid using the selenium standalone jar is easy enough, but managing larger grids, and updating the dependencies along the way (e.g. DriverServer, jars, paths etc.) on each of your nodes can be a bit of a pain.
The [Selenium Grid Extras][Selenium Grid Extras] project aims to take the pain out of this. You just run (or have a batch script set up to automatically run).
`java -jar SeleniumGridExtras-1.7.1-SNAPSHOT-jar-with-dependencies.jar`
and it handles the rest. It can set up nodes, hubs, or combined hubs and nodes; records videos of the executions, automatically installs and configures all the drivers you’ll need (and auto-update them to the latest versions), kill stale sessions and broken browsers, and even periodically reboot machines to keep your grid up with minimal maintenance.
Download the latest version from [https://github.com/groupon/Selenium-Grid-Extras/releases].
________________________________________
## <a name="multiple-test-environments"></a>Multiple Test Environments
##### Problem
A team has multiple environments for different purpose within the same project (e.g. QA, DevInt, Staging), we often need to parametrise multiple variables (e.g. urls, usernames, passwords).
##### Solution
Store the details for all environments in a properties file, and pass an environment ‘key’ at runtime to tell the code which parameters to use.
###### Create a properties file
The properties file must be in your /resources folder. In your properties file, define the key and value pair for each URL and any other information.
In this case:
-qa points to http://qa.com
-staging points to http://staging.com
```
//config.properties file
qa.baseURL=http://qa.com
qa.username=ninja
qa.password=turtle

staging.baseURL=http://staging.com
staging.username=rick
staging.password=morty

//add more keys as needed
```
##### Create a config property reader
```
public class Config {
    private static final Properties properties = new Properties();
    private static final String environmentPrefix;

    /* initialise data, the key defaults to this when none is specified at command line */
    static {
        initPropertiesFromFile();
        environmentPrefix = System.getProperty("environmentKey", "staging") + ".";
    }

    private static void initPropertiesFromFile() {
        try (InputStream stream =
                     Config.class.getResourceAsStream("/config.properties")) {
            properties.load(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getProperty(String s) {
        return properties.getProperty(environmentPrefix + s);
    }

    /* getters for environment properties */

    public static String getBaseURL() {
        return getProperty("baseURL");
    }

    public static String getUsername() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }
}
```
###### Use the values from the Config in the Page Object/Test
In your test layer:
```
XYZPage xyzPage = XYZPage.open();
xYZPage.typeNameTextBox(Config.getUsername())
        .then().typePasswordTextBox(Config.getPassword())
        .then().clickLoginButton();

```
And in your pages layer:
```
@Step("Navigate to XYZ Page")
public static XYZPage open() {
    return PageFactory.newInstance(XYZPage.class,
            Config.getBaseURL() + "welcome.com");
}
```
Specify which environment to use via command line when executing tests
Choose which environment to run your test in by specifying the corresponding “key”
```
mvn clean verify -DenvironmentKey=staging
```
OR
```
mvn clean verify -DenvironmentKey=qa
```
________________________________________
## <a name="native-controls"></a>Native Controls
#### Alerts/Dialog boxes
This demonstrates the use of the `switchTo().alert()` functionality that selenium uses to interact with javascript alert popups. The page object contains the methods that accept, dismiss, cancel, ok, and enter text to a variety of different native JS popups. These are summarised below:
```
// To accept an alert (e.g. click 'yes' or 'accept' or 'ok'
driver.switchTo().alert().accept();

// To dismiss or cancel an alert (e.g. click 'no' or 'dismiss' or 'cancel')
driver.switchTo().alert().dismiss();

// To enter text on an alert requiring text entry
driver.switchTo().alert().sendKeys(textToEnter);

// To enter authentication on an alert requiring a username & password
Credentials credentials = new UsernamePasswordCredentials("username", "password");
driver.switchTo().alert().authenticationWith(credentials);
```
##### Handling file pickers - e.g. ‘Choose Files’ button
In order to upload a file to a site, the ‘choose files’ input control is used. To a user, this will open a file browser on whatever platform they’re on. Selenium cannot interact with this native control.
In order to get around this, we can SendKeys(/path/to/file) directly on the input control - e.g. `chooseFilesInputButton.sendKeys("/path/to/file");`, which bypasses the file browser that selenium cannot handle.
The example below demonstrates how to perform this on the [Heroku File Upload][File Upload] page. The example page object can be seen below
```
@Visible
@Name("Choose Files button")
@FindBy(css = "input#file-upload")
private WebElement chooseFilesButton;

@Visible
@Name("Upload button")
@FindBy(css = "input#file-submit")
private WebElement uploadButton;

@Step("Upload a file by choosing file and then clicking upload")
public FileUploadSuccessPage uploadFile(File filename) {
  chooseFilesButton.sendKeys(filename.getAbsolutePath());
  uploadButton.click();
  return PageFactory.newInstance(FileUploadSuccessPage.class);
}
```
###### Why not use AutoIt, Sikuli etc.
Running locally, on your machine, with your config etc., a number of solutions may work perfectly well. However, the main problems come when you want to ramp things up - what about:
-	running tests in parallel?
- other browsers - does it behave the same?
-	other OSs - mac dialogs are very different to windows etc.
-	on a grid - more dependencies required (e.g. AutoIt must be present on all your grid nodes)
-	reliability
We want to try and keep our test pack as scalable and multi-platform as possible - although we’re bypassing the OS’s file browser by sending keys directly to the input control here. It’s important to remember our main focus is testing our application - not the browser’s file picker functionality.
________________________________________
## <a name="running-via-build-tool"></a>Running via Build Tool (e.g. Jenkins)
Why bother?
-	Enables Continuous Integration (CI)
-	It encourages good source control practice
-	You can kick stuff off and leave it overnight
-	It can be triggered by other builds
-	It’s much more reliable than custom ‘runner’ scripts
-	You can set default parameters so you don’t mistype things
-	It saves results from previous executions, including Allure reports
##### What you need
-	[Allure Plugin][Allure Plugin]

##### Parameters
-	Set up build parameters for each (or the ones you’ll need) of the command line variables we can pass in
-	e.g. a variable called `BROWSER` which has a dropdown list of firefox, chrome, safari, etc.
-	Pass these parameters into the relevant section of the maven build job - e.g. `mvn test -Dbrowser=$BROWSER`
________________________________________
## <a name="custom-browser-implementations"></a>Custom Browser Implementations
Custom Browser Implementations
Sometimes, one needs to open browsers and devices with certain capabilities and flags set that are not already part of Framework. This is achieved in the WebDriver protocol by `Options` for each browser e.g. `ChromeOptions`, `FirefoxOptions`, these implement the `Capabilities` interface. Framework provides a simple mechanism to set these, whilst continuing to handle the eventual browser instantiation behind the scenes.
The solution is to create a new Java class extending `AbstractDriver` somewhere in your test code, which sets up the `Capabilities` and `WebDriver` as you require them; then reference this class (by name) using the `-DcustomBrowserImpl` parameter at runtime.

###### Running Chrome in Incognito mode example

Example would be to create the class `ChromeIncognitoImpl.java` somewhere in your test code:

```
public class ChromeIncognitoImpl extends ChromeImpl {

    @Override
    public Capabilities getDesiredCapabilities() {
        ChromeOptions chromeOptions = super.getCapabilities();
        chromeOptions.addArguments("--incognito");
        return chromeOptions;
    }

    @Override
    public WebDriver getWebDriver(Capabilities capabilities) {
        return super.getWebDriver(capabilities);
    }
}
```
Then run your tests with:
```
mvn clean verify -DcustomBrowserImpl=ChromeIncognitoImpl
```
Since 3.0.0:
```
mvn clean verify -DcustomBrowserImpl=my.package.ChromeIncognitoImpl
```

###### Using Appium

This can be achieved by implementing a CustomBrowserImpl.

```
import com.framework.ui.driver.AbstractDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;

import static com.framework.common.properties.Property.*;

public class WinAppDriverImpl extends AbstractDriver {

    protected static final Logger logger = LogManager.getLogger();

    @Override
    public Capabilities getCapabilities() {
        logger.info("Creating capabilities for WinAppDriverImpl");
        MutableCapabilities mutableCapabilities = new MutableCapabilities();
        mutableCapabilities.setCapability("platformName", "Windows");
        if (PLATFORM_VERSION.isSpecified()) {
            mutableCapabilities.setCapability(
                    "platform", "Windows " + PLATFORM_VERSION.getValue());
            mutableCapabilities.setCapability(
                    "platformVersion", PLATFORM_VERSION.getValue());
        } else {
            String message = "Platform version needs to be specified when using Windows";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        if (DEVICE.isSpecified()) {
            mutableCapabilities.setCapability("deviceName", DEVICE.getValue());
        } else {
            mutableCapabilities.setCapability("deviceName", "WindowsPC");
        }

        if (APP_PATH.isSpecified()) {
            mutableCapabilities.setCapability("app", APP_PATH.getValue());
        } else {
            mutableCapabilities.setCapability("app", "Root");
        }

        return mutableCapabilities;
    }

    @Override
    public WebDriver getWebDriver(Capabilities capabilities) {
        return new WindowsDriver<WindowsElement>(capabilities);
    }

}
```
You will also need to add the Appium dependency to your `pom.xml` file.
```
<dependency>
  <groupId>io.appium</groupId>
  <artifactId>java-client</artifactId>
  <version>6.1.0</version>
</dependency>
```
If you need access to the Appium Driver you can use the following:
```
if (driver instanceof AppiumDriver) {
    return (AppiumDriver) driver;
} else {
    throw new IllegalStateException(wd + " is not an instance of AppiumDriver");
}
```
Whenever `-DcustomBrowserImpl` is provided, the `-Dbrowser` parameter defaults to custom.
________________________________________
## <a name="javascript-hacks"></a>JavaScript Hacks

#### Warning

`Do NOT use these unless it’s the last remaining option`.

Your tests are supposed to be simulating user actions and user flows, so injecting JavaScript is not to be used unless some weirdness means that Selenium cannot do something that a user can.

###### Example: Un-Hiding Hidden Controls

In order to upload a file to a site, the ‘choose files’ input control is used.


##### Problem

In some cases, the input might be hidden on the page, with a div occluding it:

```
<div id="input-button">
  <input class="hidden" />
</div>
```
SendKeys to the `div` will not have the same effect as SendKeys to the `input`, and because the input control is hidden, Selenium cannot interact with it.

###### Solution

We’ve have added the `@ForceVisible` annotation for this scenario.
```
@ForceVisible
@Name("Upload input")
@FindBy(css="div#upload-button input")
private WebElement uploadInput;

@Step("Upload a file")
public void tryAnUpload(File fileToUpload) {
  //Send the file path directly to the input button via sendKeys
  uploadInput.sendKeys(fileToUpload.getAbsolutePath());
}
```
On page load, Framework will try to un-hide the control with JavaScript. If you’d like to control when it happens, use `visibility.forceVisible(uploadInput)` instead of `@ForceVisible`.


###### Old Solution

The old solution was to use a JavaScript executor in your page object to modify the style of the input such that selenium can interact with it.
The method below demonstrates this.
```
public class MyPage extends BasePage<MyPage> {
    @Name("Upload input")
    @FindBy(css = "div#upload-button input")
    private WebElement uploadInput;

    @Step("Upload a file")
    public void tryAnUpload(File fileToUpload) {
      // CSS Locator for the hidden input button
      String cssSelector = "div#upload-button input";

      // Use JavaScript executor to make the input button visible
      executeJS("document.querySelector('" + cssSelector + "').style.width = '200px'");
      executeJS("document.querySelector('" + cssSelector + "').style.height = '10px'");
      executeJS("document.querySelector('" + cssSelector + "').style.opacity = '100'");

      // Send the file path directly to the input button via sendKeys
      uploadInput.sendKeys(fileToUpload.getAbsolutePath());
    }
}
```
This sort of idea can also be used if you need to do more than `@ForceVisible` or would like to have more control over what happens, it’s just a bit more verbose.


##### Trade Offs


UI automation testing is largely about simulating user’s behaviour as best we can. We could use some other tool (e.g. AutoIt, Sikuli, call JavaScript etc.), but the additional flakiness and complexity this introduces might outweigh the benefit.
It’s about choosing the simplest and most reliable approach to get the job done.
________________________________________
## <a name="hovering-with-chromedriver"></a>Hovering with ChromeDriver
This is a known bug associated with chromedriver’s and Selenium’s interaction with mouseover/hover over elements.
When hovering over an element, it appears to immediately “unhover” as though the mouse was moved away for split second.
```
public SomePage clickOnStuffInsideHoverMenu() {
    Actions builder = new Actions(driver);
    builder.moveToElement(elementThatNeedsToBeMouseOver).perform(); // Mouse over an the hover element,
    somethingLinkUnderTheMouseOverElement.click();   // element that we want to click but only accessible from a mouseover menu
    return PageFactory.newInstance(SomePage.class);
}
```
In this example, we want to click on somethingLinkUnderTheMouseOverElement but its only accessible once we mouse over elementThatNeedsToBeMouseOver.
During execution, the element hovers over elementThatNeedsToBeMouseOver but would immediately close as though the mouse was moved away.
________________________________________
## <a name="automatically-retry-tests-on-failure"></a>Automatically Retry Tests on Failure
If you find yourself with a stubborn test which fails very inf
requently but breaks the build when it does then Framework has a solution.
Add the following to your `@Test` annotation and if the test fails it will be marked as `SKIP` and then retried (up to `maxRetryCount` or a default of once).
```
public class MyTest extends BaseUITest {

    @Test(retryAnalyzer = RetryFlakyTest.class)
    public void test_which_fails_less_than_1_per_cent() {
        // ...
    }
}
```
If your test is failing more than about 1% of the time, you should look to improve its robustness before moving on. If you don’t, or if you abuse the above annotation, or if you are just lazy, then on your head be it!
________________________________________
[winAppDriver]: https://github.com/Microsoft/WinAppDriver
[BrowserStack]:https://www.browserstack.com/list-of-browsers-and-platforms
[SauceLabs]:https://saucelabs.com/platform/supported-browsers-devices
[TestNG Annotations]:https://testng.org/doc/documentation-main.html#annotations
[Selenium annotation]:https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/support/FindBy.html
[Truth]:https://truth.dev/
[ExpectedConditions]:https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/support/ui/ExpectedConditions.html
[cssgame]:https://flukeout.github.io/
[Yandex HtmlElements library]:http://htmlelements.qatools.ru/
[Allure]:http://allure.qatools.ru/
[Allure Jenkins Plugin]:https://plugins.jenkins.io/allure-jenkins-plugin/
[Selenium Grid Extras]:https://github.com/groupon/Selenium-Grid-Extras
[File Upload]:http://the-internet.herokuapp.com/upload
[Allure Plugin]:https://plugins.jenkins.io/allure-jenkins-plugin/
[ardesco]: https://github.com/Ardesco/Selenium-Maven-Template
[bootstrapium]: https://github.com/jvanderwee/bootstrapium
[rest-assured]: http://rest-assured.io/
[mvn]: https://maven.apache.org/download.cgi
[geckodriver]: https://drive.google.com/drive/u/0/folders/1LF07f5wCUUN_kOjHx-4uL4IsQFjfpcqY
[chromedriver]: https://drive.google.com/drive/u/0/folders/1LF07f5wCUUN_kOjHx-4uL4IsQFjfpcqY
[allure]: https://docs.qameta.io/allure/