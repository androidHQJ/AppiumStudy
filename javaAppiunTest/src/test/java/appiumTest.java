
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.text.ParseException;
import java.util.function.Function;
import java.util.logging.Logger;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.sun.tools.doclint.Entity.lambda;
import static com.sun.tools.doclint.Entity.nu;

public class appiumTest {

    private AppiumDriver driver;

    /**
     * {
     * "platformName": "Android",
     * "deviceName": "FLA-AL20",
     * "appPackage": "com.jifen.qukan",
     * "appActivity": "com.jifen.qkbase.main.MainActivity"
     * }
     *
     * @throws Exception
     */

    private static final String root = "/hierarchy/android.widget.FrameLayout" +
            "/android.widget.LinearLayout/android.widget.FrameLayout" +
            "/android.widget.LinearLayout/android.widget.FrameLayout" +
            "/android.widget.RelativeLayout/android.widget.FrameLayout[2]" +
            "/android.widget.LinearLayout/android.widget.RelativeLayout" +
            "/android.support.v4.view.ViewPager/android.widget.RelativeLayout" +
            "/android.view.ViewGroup/android.widget.FrameLayout/android.support.v7.widget.RecyclerView" +
            "/android.widget.LinearLayout[1]/android.widget.RelativeLayout";

    private static final String playFinishRoot = root+
            "/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.TextView[2]";

    private Logger logger;

    @Before
    public void setup() throws Exception {
        logger = Logger.getLogger("appium");
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.BROWSER_NAME, "");
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", "YAUNW18316005478"); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("appPackage", "com.jifen.qukan");
        cap.setCapability("appActivity", "com.jifen.qkbase.main.MainActivity");
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        cap.setCapability("sessionOverride", true);
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
    }

    @Test
    public void plus() throws InterruptedException {
        logger.info("==start==");
        /**
         * Splash
         */
        //权限申请按钮
        for (int i = 0; i < 3; i++) {
            MobileElement el = (MobileElement) driver
                    .findElementById("com.android.packageinstaller:id/permission_allow_button");
            el.click();
        }

        Thread.sleep(1000);

        /**
         * 首页弹框
         */
        MobileElement el2 = (MobileElement) driver.findElementById("com.jifen.qukan:id/a1v");
        if (el2!=null){
            el2.click();
        }

        /**
         * 登录页面
         */
        Thread.sleep(1500);

        //隐藏键盘
        (new TouchAction(driver)).tap(PointOption.point(989, 1258)).perform();

        //账号密码登录
        MobileElement el5 = (MobileElement) driver.findElementById("com.jifen.qukan:id/akb");
        el5.click();

        //输入账号密码
        MobileElement el6 = (MobileElement) driver.findElementById("com.jifen.qukan:id/ad5");
        el6.sendKeys("18351622994");
        MobileElement el8 = (MobileElement) driver.findElementById("com.jifen.qukan:id/ae4");
        el8.sendKeys("heqijun123");

        //登录按钮
        MobileElement el9 = (MobileElement) driver.findElementById("com.jifen.qukan:id/n2");
        el9.click();

        /**
         * 视频模块
         */
        MobileElement el13 = (MobileElement) driver.findElementById("com.jifen.qukan:id/jx");
        el13.click();

        Thread.sleep(1000);

        //判断广告
        MobileElement elVideo = (MobileElement) driver.findElementByXPath(root);
        judgeAdvice(elVideo);
    }

    @After
    public void tearDown() {
        driver.quit();
        logger.info("==end==");
    }

    private void judgeAdvice(MobileElement elVideo) {
        if (elVideo != null) {
            //非广告

            //获取时长
            MobileElement timeTv = (MobileElement) driver.findElementById("com.jifen.qukan:id/a7a");
            String duration = timeTv.getAttribute("text");
            String[] split = duration.split(":");
            long time = (Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]));
            logger.info("duration===" + duration + "time===" + time);

            //播放按钮
            MobileElement playIv = (MobileElement) driver.findElementById("com.jifen.qukan:id/a5v");
            playIv.click();

            /**
             * 在抛出TimeoutException之前这会等待最多10秒钟，或者它找到了元素，在0-10秒之间返回。
             * WebDriverWait默认每500毫秒调用ExpectedCondition直到它成功返回。
             * ExpectedCondition类型的成功返回是布尔值true或非null的返回值。
             */
            WebDriverWait webDriverWait = new WebDriverWait(driver, time*2);
            webDriverWait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    WebElement webElement=webDriver.findElement(By.xpath(playFinishRoot));
                    logger.info("findElement=="+webElement);
                    return webElement;
                }
            });
            logger.info("===播放完毕===");

            continueHandle();
        }else {
            continueHandle();
        }
    }

    private void continueHandle() {
        logger.info("===继续播放下一个===");
        (new TouchAction(driver))
                .press(PointOption.point(503, 1184))
                .moveTo(PointOption.point(503, 429))
                .release()
                .perform();
        MobileElement elVideoAgain = (MobileElement) driver.findElementByXPath(root);
        judgeAdvice(elVideoAgain);
    }
}

