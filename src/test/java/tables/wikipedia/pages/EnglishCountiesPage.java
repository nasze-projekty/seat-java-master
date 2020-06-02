package tables.wikipedia.pages;

import com.framework.ui.annotations.Visible;
import com.framework.ui.element.StreamTable;
import com.framework.ui.pages.BasePage;
import com.framework.ui.pages.PageFactory;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class EnglishCountiesPage extends BasePage<EnglishCountiesPage> {

    @Visible
    @CacheLookup
    @FindBy(css = "table.wikitable") // luckily there's only one
    private StreamTable listTable;

    public static EnglishCountiesPage open() {
        return PageFactory.newInstance(EnglishCountiesPage.class,
                "https://en.wikipedia.org/wiki/List_of_ceremonial_counties_of_England");
    }

    public int populationOf(String countyName) {
        Predicate<WebElement> headerLookUp = e -> e.getText().trim().equals("County");
        Predicate<WebElement> lookUpCellMatcher = e -> e.getText().trim().equals(countyName);
        Predicate<WebElement> targetColHeaderLookup = e -> e.getText().trim().startsWith("Population");
        String population = listTable
                .getCellsByLookup(headerLookUp, lookUpCellMatcher, targetColHeaderLookup)
                .findFirst()
                .orElseThrow(NotFoundException::new)
                .getText()
                .replaceAll(",", "");
        return Integer.parseInt(population);
    }

    public Stream<Integer> densities() {
        return listTable
                .getColumn(e -> e.getText().startsWith("Density"))
                .map(WebElement::getText)
                .map(density -> density.replaceAll(",", ""))
                .map(Integer::parseInt);
    }
}
