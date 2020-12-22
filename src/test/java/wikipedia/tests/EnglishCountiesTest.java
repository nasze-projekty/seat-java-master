package wikipedia.tests;

import com.framework.ui.tests.BaseUITest;
import org.testng.annotations.Test;
import wikipedia.pages.EnglishCountiesUsingListsPage;

import static com.google.common.truth.Truth.assertThat;

/**
 * This test demonstrates the different between using StreamTable and Lists
 * of WebElements.
 * <p>The trade-off is between readability, maintainability and performance.
 * <p>The List option is slightly longer and slightly more difficult to maintain,
 * especially if your table is changing shape (but not header text), however it
 * is significantly faster.
 */
@Test
public class EnglishCountiesTest extends BaseUITest {

    public void exploring_english_counties_data_with_lists() {
        EnglishCountiesUsingListsPage page = EnglishCountiesUsingListsPage.open();

        assertThat(page.populationOf("Cornwall"))
                .isAtLeast(550_000);
        // at least two counties have population densities of more than 3000
        assertThat(page.densities()
                .filter(density -> density > 3000)
                .limit(2)
                .count())
                .isEqualTo(2L);
    }
}
