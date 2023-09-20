package dev.sebastiano.bundel.onboarding

import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.testharness.TestHarness
import com.karumi.shot.ScreenshotTest
import dev.sebastiano.bundel.R
import dev.sebastiano.bundel.ui.BundelYouTheme
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

/*
* If this test is executed via gradle managed devices, the saved image files will be stored at
* build/outputs/managed_device_android_test_additional_output/debugAndroidTest/managedDevice/[DeviceModel]Api[ApiVersion]/
*/
internal class OnboardingAndroidUiTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val nameRule = TestName()

    @get:Rule
    val permissionsRule = PrepareDevicePermissionsRule()

    private val resources: Resources
        get() = composeTestRule.activity.resources

    @Test
    fun introPage_smallSize_canScroll() {
        composeTestRule.setContent {
            TestHarness(
                darkMode = true,
                size = DpSize(250.dp, 140.dp),
            ) {
                BundelYouTheme {
                    var crashReportingEnabled by remember { mutableStateOf(false) }
                    IntroPage(
                        IntroPageState(crashReportingEnabled) { crashReportingEnabled = !crashReportingEnabled },
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(resources.getString(R.string.onboarding_enable_crashlytics))
            .assertExists("Switch label is missing")
            .assertIsOff()
            .performScrollTo()
            .performClick()
            .assertIsOn()
    }

    @Test
    fun intro_page_should_toggle_state_correctly() {
        composeTestRule.setContent {
            TestHarness(
                size = DpSize(1920.dp, 1080.dp),
            ) {
                BundelYouTheme {
                    var crashReportingEnabled by remember { mutableStateOf(false) }
                    IntroPage(
                        IntroPageState(crashReportingEnabled) { crashReportingEnabled = !crashReportingEnabled },
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(resources.getString(R.string.onboarding_enable_crashlytics))
            .assertExists("Switch label is missing")
            .assertIsOff()
            .performClick()
            .assertIsOn()
            .performClick()
            .assertIsOff()
    }

    @Test
    fun intro_page_should_look_nice() {
        composeTestRule.setContent {
            BundelYouTheme {
                var crashReportingEnabled by remember { mutableStateOf(true) }
                IntroPage(
                    IntroPageState(crashReportingEnabled) { crashReportingEnabled = !crashReportingEnabled },
                )
            }
        }

        compareScreenshot(composeTestRule)
    }

    @Ignore("Yes")
    @Test
    fun should_not_show_back_button_on_first_page() {
        composeTestRule.setContent {
            BundelYouTheme {
                Surface {
//                    OnboardingScreen()
                }
            }
        }

        composeTestRule.onNodeWithText(resources.getString(R.string.back).uppercase())
            .assertDoesNotExist()
    }

    @Ignore("Yes")
    @Test
    fun should_hide_back_button_when_getting_back_to_first_page() {
        composeTestRule.setContent {
            BundelYouTheme {
                Surface {
//                    OnboardingScreen()
                }
            }
        }

        composeTestRule.onNodeWithText(resources.getString(R.string.next).uppercase())
            .assertExists("Next button is missing")
            .performClick()

        composeTestRule.onNodeWithText(resources.getString(R.string.back).uppercase())
            .assertExists()
            .performClick()
            .assertDoesNotExist()
    }
}
