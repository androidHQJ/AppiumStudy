import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.appium.java_client.events.api.general.ElementEventListener;

public class ElementListener implements ElementEventListener {

    @Override
    public void beforeClickOn(WebElement arg0, WebDriver arg1) {

        //messages.add("Attempt to click on the element");
        System.out.println("准备点击:"+splitElement(arg0));
    }

    @Override
    public void afterClickOn(WebElement arg0, WebDriver arg1) {
        //messages.add("The element was clicked");
        System.out.println("点击:" +splitElement(arg0));

    }

    @Override
    public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
        //messages.add("Attempt to change value of the element");
        System.out.println("准备改变控件:" +splitElement(arg0)+"数值");

    }

    private void deal(WebElement arg0) {
        try{
            WebElement webElement=arg0.findElement(By.id("com.jifen.qukan:id/ajq"));
            String value=webElement.getAttribute("text");
            System.out.println("金币值:" +value);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
        //messages.add("The value of the element was changed");
        System.out.println("控件:" + splitElement(arg0) + "数值已改变");
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver,
                                    CharSequence[] keysToSend) {
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver,
                                   CharSequence[] keysToSend) {
    }

    @Override
    public void beforeGetText(WebElement webElement, WebDriver webDriver) {
    }

    @Override
    public void afterGetText(WebElement webElement, WebDriver webDriver, String s) {
    }

    //获取操作的控件字符串
    private String splitElement(WebElement element) {
        String str = element.toString().split("-> ")[1];
        return str.substring(0, str.length() - 1);
    }
}