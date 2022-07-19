package ru.netology.testing.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.Until.findObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

const val MODEL_PACKAGE = "ru.netology.testing.uiautomator"

const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)

class ChangeTextTest {

    private lateinit var device: UiDevice
    private val textToSet = "Netology"
    private val textNewActivity: String = "It's text for newActivity"
    private val packageName = MODEL_PACKAGE

    private fun waitForPackage(packageName: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
    }

    @Before
    fun beforeEachTest() {
        // Press home
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()

        // Wait for launcher
        val launcherPackage = device.launcherPackageName
        device.wait(Until.hasObject(By.pkg(launcherPackage)), TIMEOUT)

        // Wait app
        waitForPackage(packageName)
    }

    @Test
    fun testChangeText() {
        device.findObject(By.res(packageName, "userInput")).text = textToSet
        device.findObject(By.res(packageName, "buttonChange")).click()

        val result = device.findObject(By.res(packageName, "textToBeChanged")).text

        assertEquals(result, textToSet)
    }

    @Test
    fun shouldTryInputEmptyText() {
        val textBefore = device.findObject(By.res(packageName, "textToBeChanged")).text

        device.findObject(By.res(packageName, "userInput")).text = " "
        device.findObject(By.res(packageName, "buttonChange")).click()

        val textAfter = device.findObject(By.res(packageName, "textToBeChanged")).text

        assertEquals(textBefore, textAfter)
    }

    @Test
    fun shouldCheckTextOnNewActivity() {
        device.findObject(By.res(packageName, "userInput")).text = textNewActivity
        device.findObject(By.res(packageName, "buttonActivity")).click()

        val textActual = device.wait(findObject(By.res(packageName, "text")), TIMEOUT)
            .text

        assertEquals(textNewActivity, textActual)
    }

}



