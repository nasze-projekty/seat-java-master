package tables.wikipedia.pages;

import com.framework.ui.annotations.Visible;
import com.framework.ui.element.StreamTable;
import com.framework.ui.pages.BasePage;
import com.framework.ui.pages.PageFactory;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import static java.util.stream.Collectors.toList;

public class HighestMountainPage extends BasePage<HighestMountainPage> {

    @Visible
    @CacheLookup
    @FindBy(css = "table.wikitable") // luckily there's only one
    private StreamTable listTable;

    public static HighestMountainPage open() {
        return PageFactory.newInstance(HighestMountainPage.class,
                "https://en.wikipedia.org/wiki/List_of_highest_mountains_on_Earth");
    }

    public String getRankByName(String mountainName) {
        return getText(mountainName, 0);
    }

    public String getFirstAscentByName(String mountainName) {
        return getText(mountainName, 8);
    }

    private String getText(String mountainName, int index) {
        return listTable.getRows()
                .map(row -> row.limit(index + 2).collect(toList()))
                .filter(row -> row.get(1).getText().contains(mountainName))
                .findFirst()
                .orElseThrow(NotFoundException::new)
                .get(index)
                .getText();
    }

    public long mountainsHigherThan(int heightInMeters) {
        return listTable.getColumn(2)
                .map(WebElement::getText)
                .map(this::parseHeight)
                .filter(meters -> meters > heightInMeters)
                .count();
    }

    private int parseHeight(String text) {
        String cleansedText = text
                .replace(",", "")
                .replaceAll("\\[\\d\\]", "");
        return Integer.parseInt(cleansedText);
    }
}
