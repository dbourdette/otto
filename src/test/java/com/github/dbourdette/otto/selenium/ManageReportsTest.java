package com.github.dbourdette.otto.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ManageReportsTest {
    public WebDriver driver;

    @Before
    public void init() {
        driver = new FirefoxDriver();

        deleteSource();

        driver.navigate().to("http://admin:tomlechat@localhost:8080");

        driver.findElement(By.linkText("create a new event source")).click();

        driver.findElement(By.id("name")).sendKeys("selenium source");
        driver.findElement(By.id("form")).submit();
    }

    @After
    public void clean() {
        driver.close();
    }

    @Test
    public void createReport() {
        driver.navigate().to("http://admin:tomlechat@localhost:8080");
        driver.findElement(By.linkText("selenium source")).click();

        driver.findElement(By.id("sourceConfiguration")).click();
        driver.findElement(By.linkText("add a report")).click();

        driver.findElement(By.id("title")).sendKeys("selenium source report");
        driver.findElement(By.id("labelAttributes")).sendKeys("slug");
        driver.findElement(By.id("valueAttribute")).sendKeys("hits");
        driver.findElement(By.id("valueAttribute")).sendKeys("hits");
        driver.findElement(By.id("form")).submit();
    }

    private void deleteSource() {
        driver.navigate().to("http://admin:tomlechat@localhost:8080");

        try {
            System.out.println("1");
            driver.findElement(By.linkText("selenium source")).click();
            System.out.println("2");
            driver.findElement(By.id("sourceConfiguration")).click();
            System.out.println("3");
            driver.findElement(By.linkText("delete source")).click();
            System.out.println("4");

            driver.findElement(By.id("deleteForm")).submit();
        } catch (Exception e) {
            // well there was probably no selenium source
        }
    }
}
