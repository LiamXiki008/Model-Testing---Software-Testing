//Imports
import com.google.gson.Gson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.net.HttpURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


//System Under Test
public class SUT {

    //Webdriver
    static WebDriver browser;

    //Post Request
    public static boolean PostRequest(boolean validPost) throws Exception{


        Gson gson = new Gson(); //Gson object to convert java object to json
        ArrayList<AlertStruct> alerts = new ArrayList<>(); //Arraylist of alerts

        AlertStruct testAlert = new AlertStruct(); //Alert object
       // testAlert
        testAlert = new AlertStruct(
                6,"ASUS TUF Gaming F17 17.3","ASUS TUF Gaming F17 17.3 FHD IPS 144Hz Core i7-11800H (11th Gen) 1TB SSD 16GB RAM RTX3060 (6GB) Win11 (Ex Display)","https://www.scanmalta.com/shop/asus-tuf-gaming-f17-17-3-fhd-ips-144hz-core-i7-11800h-11th-gen-1tb-ssd-16gb-ram-rtx3060-6gb-win10-ex-display.html",
                "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/b084519189a7c7b3054def1f3dcab96f/3/3/3399009-l-a.jpg","aba2df1c-5441-4581-9dc2-5413c9691825",102900);

        //Add alert to arraylist
        if(validPost){
            alerts.add(testAlert);
            alerts.add(testAlert);
        }else {
            //Add invalid alert
            alerts.add(new AlertStruct(0,"","","","","",0));
        }

        //Convert arraylist to json
        for (AlertStruct alert : alerts) {
            String json = gson.toJson(alert);

            //Post request
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
                }catch(IOException e){
                    return false;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //Return true if post request was successful
        return true;
    }

    //Delete Request
    public static int DeleteRequest(String userID){
        //Delete request
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
        }catch (Exception ignored){
        }
        return 0;
    }

    //Sleep function
    public static void sleep(int seconds){
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Login function
    public static boolean LoginFunction(boolean loggedIn){

        //Webdriver
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver.exe");
        driver.get("https://www.marketalertum.com/");
        String result="";

        //Login button and User Id
        By loginButton = By.xpath("//a[@href='/Alerts/Login']");
        By userId = By.id("UserId");

        sleep(3);

        //Click login button
        browser.findElement(loginButton).click();
        sleep(3);

        //Enter user id
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

        //Check if user is logged in
        driver.quit();
        return result.contains("Liam");

    }

    //Logout Function
    public static boolean LogoutFunction(){
        //Webdriver
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver.exe");
        driver.get("https://www.marketalertum.com/");

        //Logout
        boolean loggedIn = LoginFunction(true);
        if(loggedIn){
            By logoutButton = By.xpath("//a[@href='/Home/Logout']");
            browser.findElement(logoutButton).click();
            sleep(1);
        }

        //Check if user is logged out
        String title = driver.getTitle();
        driver.quit();
        return title.contains("Home Page");
    }

    public static boolean isLoggedIn(){
        return  browser.findElement(By.xpath("//a[@href='/Home/Logout']")).isDisplayed();
    }

    public static boolean viewAlerts(boolean loggedIn){
        if(loggedIn) {
            browser.get("https://www.marketalertum.com/Alerts/List");
        }else{
            return false;
        }
        return true;
    }

    public static boolean checkLimit() {
        if (isLoggedIn()) {
            browser.get("https://www.marketalertum.com/Alerts/List");
            List<WebElement> alerts = browser.findElements(By.xpath("//table[@border='1']"));
            System.out.println("Number of alerts: " + alerts.size());

            return alerts.size() >= 5;
        }
        return false;
    }

    public String getEventLog(){
        //Get request
        try {
            URL url = new URL("https://api.marketalertum.com/EventsLog/aba2df1c-5441-4581-9dc2-5413c9691825");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))){
                StringBuilder response = new StringBuilder();
                String respondLine = null;
                while ((respondLine = br.readLine()) != null){
                    response.append(respondLine.trim());
                }
                System.out.println(response.toString());
                return response.toString();
            }catch(IOException e){
                return "Error";
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
