import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {
    WebDriver driver;
    ArrayList<String> tabs;
    int i = 1, j, k = 1;
    String fbMsgText = "";
    Boolean elementPresentLoop1, elementPresentLoop2;

    public static void main(String[] args) throws AWTException, InterruptedException, IOException, JSONException {
        System.setProperty("webdriver.chrome.driver", "c:\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--no-proxy-server");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);//to disable save password pop-up
        prefs.put("profile.default_content_setting_values.notifications", 2);//to disable show notification pop-up
        options.setExperimentalOption("prefs", prefs);
        Main classObj = new Main();
        classObj.driver = new ChromeDriver(options);
        classObj.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        String ReplyFromBot, previousFBText = "";

        classObj.openFacebook();

//	  switchToCleverbot();

//	  openCleverbot();


//	  switchToFB();

        classObj.getAllTextsInitially();

        do {
            while (previousFBText != classObj.fbMsgText) {
                previousFBText = classObj.fbMsgText;
//			  switchToCleverbot();
                ReplyFromBot = classObj.getReplyFromBot(classObj.fbMsgText);
//			  switchToFB();
                classObj.sendReplyToFB(ReplyFromBot);
                Thread.sleep(10000);
                classObj.getLatestMsgFromFB();
            }
            Thread.sleep(10000);
            classObj.getLatestMsgFromFB();
        } while (true);
    }


    public void openFacebook() throws AWTException {
//	  //new Tab
//	  Robot r = new Robot();
//	  r.keyPress(KeyEvent.VK_CONTROL);
//	  r.keyPress(KeyEvent.VK_T);
//	  r.keyRelease(KeyEvent.VK_CONTROL);
//	  r.keyRelease(KeyEvent.VK_T);
        driver.get("http://www.facebook.com");
        //login
        driver.findElement(By.name("email")).sendKeys("fbUserEmail");
        driver.findElement(By.name("pass")).sendKeys("password");
        driver.findElement(By.id("loginbutton")).click();
        //open Messenger
        driver.get("https://www.facebook.com/messages/t");
//	  //Store the tab handles
//	  tabs = new ArrayList<String> (driver.getWindowHandles());
    }

    public void openCleverbot() {
        driver.get("http://www.cleverbot.com/");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"avatarform\"]/input[1]")));
    }

    public void switchToFB() {
        driver.switchTo().window(tabs.get(0));
    }

    public void switchToCleverbot() {
        driver.switchTo().window(tabs.get(1));
    }

    public void getAllTextsInitially() {
        WebElement firstEle, secondEle;
        String firstText = "", secondText = "";
        String firstTextPos = "", secondTextPos = "";
        int temp = 0;
        do {
            try {
                firstEle = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div/div/div/span"));
                firstText = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div/div/div/span")).getText();
                firstTextPos = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div/div")).getAttribute("data-tooltip-position");
                System.out.println("1" + fbMsgText);
                elementPresentLoop1 = true;
                temp = 1;
            } catch (NoSuchElementException e) {
                k = 2;
                try {
                    secondEle = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div/div/div/span"));
                    secondText = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div/div/div/span")).getText();
                    secondTextPos = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div/div")).getAttribute("data-tooltip-position");
                    System.out.println("2" + fbMsgText);
                    elementPresentLoop1 = true;
                    temp = 2;
                } catch (NoSuchElementException e1) {
                    elementPresentLoop1 = false;
                }
                if (elementPresentLoop1) {
                    j = 2;
                    do {
                        try {
                            secondText = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div[" + j + "]/div/div/span")).getText();
                            elementPresentLoop2 = true;
                            System.out.println("22" + fbMsgText);
                        } catch (NoSuchElementException e1) {
                            elementPresentLoop2 = false;
                        }
                        j++;
                    } while (elementPresentLoop2);
                }
                k = 1;
            }
            if (elementPresentLoop1 && temp == 1) {
                j = 2;
                do {
                    try {
                        firstText = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[" + k + "]/div[" + j + "]/div/div/span")).getText();
                        elementPresentLoop2 = true;
                        System.out.println("11" + fbMsgText);
                    } catch (NoSuchElementException e) {
                        elementPresentLoop2 = false;
                    }
                    j++;
                } while (elementPresentLoop2);
            }
            i++;
        } while (elementPresentLoop1);
        if (firstTextPos.equals("left")) {
            fbMsgText = firstText;
        } else if (secondTextPos.equals("left")) {
            fbMsgText = secondText;
        }
        System.out.println(fbMsgText);
    }

    public String getReplyFromBot(String inputMsgToBot) {
        String botReply = "   l";
        try {
            JSONObject obj = getJson("https://www.cleverbot.com/getreply?key=CC1ylKVqEiFy2eYiws676DuIGIw");
            String cs = (String) obj.get("cs");
            JSONObject msgObj = getJson("http://www.cleverbot.com/getreply?key=CC1ylKVqEiFy2eYiws676DuIGIw&input=" + fbMsgText.replaceAll(" ", "%20") + "&cs=" + cs + "=ProcessReply");
            botReply = (String) msgObj.get("output");
        } catch (Exception e) {
            System.out.println(e);
        }
        return botReply;
    }

    public void sendReplyToFB(String Reply) {
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/div/div[2]/span/div[2]/div[2]/div[2]/div[1]/div/div/div/div[1]/div/div[2]/div")).sendKeys(Reply + Keys.ENTER);
    }

    public void getLatestMsgFromFB() {
        do {
            try {
                driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[2]/div/div/div/span"));
                fbMsgText = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[2]/div/div/div/span")).getText();
//			  System.out.println("1" + fBMsgText);
                elementPresentLoop1 = true;
            } catch (NoSuchElementException e) {
                System.out.println(i + " " + e);
                elementPresentLoop1 = false;
            }
            if (elementPresentLoop1) {
                j = 2;
                do {
                    try {
                        fbMsgText = driver.findElement(By.xpath("//*[@id=\"js_1\"]/div[" + i + "]/div/div[2]/div[" + j + "]/div/div/span")).getText();
                        elementPresentLoop2 = true;
//					  System.out.println("2" + fBMsgText);
                    } catch (NoSuchElementException e) {
                        elementPresentLoop2 = false;
                    }
                    j++;
                } while (elementPresentLoop2);
                i = i + 2;
            }
        } while (elementPresentLoop1);
    }

    public static JSONObject getJson(String url) {

        InputStream is = null;
        String result = "";
        JSONObject jsonObject = null;

        // HTTP
        try {
            HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
            HttpGet httppost = new HttpGet(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        // Read response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            return null;
        }

        // Convert string to object
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;

    }
}

