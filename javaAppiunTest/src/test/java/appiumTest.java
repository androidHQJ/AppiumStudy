
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    private String root = "/hierarchy/android.widget.FrameLayout" +
            "/android.widget.LinearLayout/android.widget.FrameLayout" +
            "/android.widget.LinearLayout/android.widget.FrameLayout" +
            "/android.widget.RelativeLayout/android.widget.FrameLayout[2]" +
            "/android.widget.LinearLayout/android.widget.RelativeLayout" +
            "/android.support.v4.view.ViewPager/android.widget.RelativeLayout" +
            "/android.view.ViewGroup/android.widget.FrameLayout/android.support.v7.widget.RecyclerView" +
            "/android.widget.LinearLayout[1]/android.widget.RelativeLayout";

    private String adText=root+"/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.TextView[2]";
    private String deviceName1="YAUNW18316005478";
    private String deviceName2="7579c470";

    private Logger logger;
    private WebDriver.Timeouts timeouts;
    private String currentTotalTime="";
    private long timeCount=0;
    private boolean isLogin=true;

    @Before
    public void setup() throws Exception {
        logger = Logger.getLogger("appium");
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.BROWSER_NAME, "");
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", deviceName1); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("appPackage", "com.jifen.qukan");
        cap.setCapability("appActivity", "com.jifen.qkbase.main.MainActivity");
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        cap.setCapability("sessionOverride", true);
        //在此会话之前不要重置应用程序状态。
        cap.setCapability("noReset", true);
        cap.setCapability("fullReset", false);
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        timeouts = driver.manage().timeouts();
    }

    @Test
    public void plus(){
        logger.info("==start==");
        /**
         * Splash
         */
        //权限申请按钮
        MobileElement el = (MobileElement)
                isElementPresent(By.id("com.android.packageinstaller:id/permission_allow_button"));
        while (el!=null){
            el.click();
            el = (MobileElement) isElementPresent(By.id("com.android.packageinstaller:id/permission_allow_button"));
        }

        timeouts.implicitlyWait(1500, TimeUnit.MILLISECONDS);

        /**
         * 首页弹框
         */
        MobileElement el2 = (MobileElement)
                isElementPresent(By.id("com.jifen.qukan:id/a1v"));
        if (el2!=null){
            el2.click();
        }

        if (!isLogin){
            loginHandle();
        }

        videoHandle();
    }

    /**
     * 视频模块
     */
    private void videoHandle() {
        MobileElement el13 = (MobileElement) driver.findElementById("com.jifen.qukan:id/jx");
        el13.click();

        timeouts.implicitlyWait(1500, TimeUnit.MILLISECONDS);

        //判断列表是否是广告类型
        MobileElement elAd = (MobileElement) isElementPresent(By.xpath(adText));
        judgeAdvice(elAd);
    }

    /**
     * 登录逻辑处理
     */
    private void loginHandle() {
        /**
         * 登录页面
         */
        timeouts.implicitlyWait(1500, TimeUnit.MILLISECONDS);

        //隐藏键盘
        (new TouchAction(driver)).tap(PointOption.point(989, 1258)).perform();

        //切换到账号密码登录
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
    }

    /**
     * 自动点击播放
     * @param elAd
     */
    private void judgeAdvice(MobileElement elAd) {
        boolean isAd=elAd!=null&&(elAd.getAttribute("text").equals("广告"));
        if (!isAd) {
            //非广告

            //获取时长
            MobileElement timeTv = (MobileElement) driver.findElementById("com.jifen.qukan:id/a80");
            String duration = timeTv.getAttribute("text");
            String[] split = duration.split(":");
            long time = (Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]));
            logger.info("duration===" + duration + "time===" + time);

            driver.findElementByAccessibilityId("com.jifen.qukan:id/a80");
            //播放按钮
            MobileElement playIv = (MobileElement) isElementPresent(By.id("com.jifen.qukan:id/a6k"));
            if (playIv!=null){
                playIv.click();
            }else {
                timeTv.click();
            }
            //总时长
            timeCount += time;

            /**
             * 在抛出TimeoutException之前这会等待最多10秒钟，或者它找到了元素，在0-10秒之间返回。
             * WebDriverWait默认每500毫秒调用ExpectedCondition直到它成功返回。
             * ExpectedCondition类型的成功返回是布尔值true或非null的返回值。
             */
            WebDriverWait webDriverWait = new WebDriverWait(driver, time*2);
            webDriverWait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    MobileElement webElement = (MobileElement) isElementPresent(By.id("com.jifen.qukan:id/aej"));
                    if (webElement!=null){
                        String value=webElement.getAttribute("text");
                        if (value.equals("重播")){
                            logger.info("findElement==重播");
                            return webElement;
                        }else {
                            logger.info("findElement==正在播放=="+currentTotalTime);
                            return null;
                        }
                    }else {
                        logger.info("findElement==正在播放=="+currentTotalTime);
                        return null;
                    }
                }
            });

            currentTotalTime="当前已播放总时长："+ timeCount +"s";
            logger.info("===播放完毕==="+currentTotalTime);

            continueHandle();
        }else {
            logger.info("===此条是广告===");
            continueHandle();
        }
    }

    /**
     * 滑动继续播放下一个
     */
    private void continueHandle() {
        logger.info("===继续播放下一个===");
        (new TouchAction(driver))
                .press(PointOption.point(542, 885))
                .moveTo(PointOption.point(542, 815))
                .release()
                .perform();
        timeouts.implicitlyWait(1000, TimeUnit.MILLISECONDS);
        MobileElement elAdAgain = (MobileElement) isElementPresent(By.xpath(adText));
        judgeAdvice(elAdAgain);
    }

    /**
     * 判断元素是否存在
     * @param by
     * @return
     */
    private WebElement isElementPresent(By by){
        try{
            return driver.findElement(by);
        }catch(Exception e){
            return null;
        }

    }

    @After
    public void tearDown() {
        driver.quit();
        logger.info("==end==");
    }

}

