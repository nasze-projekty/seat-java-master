package com.framework.common.helper;

import com.framework.ui.pages.BasePage;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wait extends BasePage<Wait> {

    WebDriverWait webDriverWait = new WebDriverWait(driver,10);

    public WebDriverWait wait_for(){
        return new WebDriverWait(driver,10);
    }
}
