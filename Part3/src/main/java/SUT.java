import com.google.gson.Gson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.net.HttpURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class SUT {

    static WebDriver browser;

    public static boolean PostRequest(boolean validPost) throws Exception{
        Gson gson = new Gson();
        boolean successfulPost = false;
        ArrayList<AlertStruct> alerts = new ArrayList<>();

        AlertStruct testAlert = new AlertStruct();
        testAlert = new AlertStruct(
                6,"ASUS TUF Gaming F17 17.3","ASUS TUF Gaming F17 17.3 FHD IPS 144Hz Core i7-11800H (11th Gen) 1TB SSD 16GB RAM RTX3060 (6GB) Win11 (Ex Display)","https://www.scanmalta.com/shop/asus-tuf-gaming-f17-17-3-fhd-ips-144hz-core-i7-11800h-11th-gen-1tb-ssd-16gb-ram-rtx3060-6gb-win10-ex-display.html",
                "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/b084519189a7c7b3054def1f3dcab96f/3/3/3399009-l-a.jpg","aba2df1c-5441-4581-9dc2-5413c9691825",102900);

        if(validPost){
            alerts.add(testAlert);
            alerts.add(testAlert);
        }else {
            alerts.add(new AlertStruct(0,"","","","","",0));
        }

        for (AlertStruct alert : alerts) {
            String json = gson.toJson(alert);
            try {
                URL url = new URL("https://api.marketalertum.com/Alert");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                try(OutputStream os = con.getOutputStream()){
                    byte[] input = json.getBytes("utf-8");
                    os.write(input,0,input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(),"utf-8"))){
                    StringBuilder response = new StringBuilder();
                    String respondLine = null;
                    while ((respondLine = br.readLine()) != null){
                        response.append(respondLine.trim());
                    }
                    System.out.println(response.toString());
                    successfulPost = true;
                }catch(IOException e){
                    return false;
                }

            } catch (MalformedURLException e) {
                successfulPost = false;
                throw new RuntimeException(e);
            } catch (IOException e) {
                successfulPost = false;
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public static int DeleteRequest(String userID){
        try {
           //Delete request
            URL url = new URL("https://api.marketalertum.com/Alert?userId="+userID);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            return responseCode;
        }catch (Exception e){
        }
        return 0;
    }
    public static void sleep(int seconds){
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean LoginFunction(boolean loggedIn){
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver.exe");
        driver.get("https://www.marketalertum.com/");
        String result="";


        By loginButton = By.xpath("//a[@href='/Alerts/Login']");
        By userId = By.id("UserId");

        sleep(3);

        browser.findElement(loginButton).click();

        sleep(3);
        if(loggedIn) {
            browser.findElement(userId).sendKeys("aba2df1c-5441-4581-9dc2-5413c9691825");
            sleep(3);
        }else{
            browser.findElement(userId).sendKeys("invalid");
            sleep(3);
        }
        sleep(2);
        browser.findElement(userId).submit();
        try{
            result = driver.findElement(By.tagName("h1")).getText();
        }catch(Exception e){
            driver.quit();
            return false;
        }

        driver.quit();
        return result.contains("Liam");

    }

    public static boolean LogoutFunction(){
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver.exe");
        driver.get("https://www.marketalertum.com/");

        boolean loggedIn = LoginFunction(true);
        if(loggedIn){
            By logoutButton = By.xpath("//a[@href='/Home/Logout']");
            browser.findElement(logoutButton).click();
            sleep(1);
        }

        String title = driver.getTitle();
        driver.quit();
        return title.contains("Home Page");
    }
}
