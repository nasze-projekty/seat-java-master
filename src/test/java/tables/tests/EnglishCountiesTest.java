package tables.tests;

import com.framework.ui.tests.BaseUITest;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import tables.pages.EnglishCountiesPage;

import static com.google.common.truth.Truth.assertThat;

public class EnglishCountiesTest extends BaseUITest {

    @TmsLink("WIKI-2")
    @Test(description = "Playing with English Counties data")
    public final void exploring_english_counties_data() {

        EnglishCountiesPage countiesPage = EnglishCountiesPage.open();

        assertThat(countiesPage.populationOf("Cornwall"))
                .isAtLeast(550_000);
        // at least two counties have population densities of more than 3000
        assertThat(countiesPage.densities()
                .filter(density -> density > 3000)
                .limit(2)
                .count())
                .isEqualTo(2L);
    }
}
