package com.github.dbourdette.otto.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ManageUsersTest {
    public WebDriver driver;

    @Before
    public void init() {
        driver = new FirefoxDriver();

        driver.navigate().to("http://admin:tomlechat@localhost:8080/users");

        try {
            driver.findElement(By.linkText("selenium user"));

            doDeleteUser();
        } catch (Exception e) {
            // well there was probably no selenium user
        }
    }

    @After
    public void clean() {
        driver.close();
    }

    @Test
    public void createUser() {
        driver.findElement(By.linkText("Add user")).click();

        driver.findElement(By.id("username")).sendKeys("selenium user");
        driver.findElement(By.id("sources")).sendKeys("source");
        driver.findElement(By.id("form")).submit();

        Assert.assertEquals("user selenium user has been created", driver.findElement(By.className("message")).getText());
    }

    @Test
    public void deleteUser() {
        createUser();

        doDeleteUser();
    }

    @Test
    public void updateUser() {
        createUser();

        driver.navigate().to("http://localhost:8080/users");

        driver.findElement(By.linkText("selenium user")).click();

        driver.findElement(By.id("sources")).sendKeys("source,anothersource");

        driver.findElement(By.id("form")).submit();

        Assert.assertEquals("user selenium user has been updated", driver.findElement(By.className("message")).getText());
    }

    private void doDeleteUser() {
        driver.navigate().to("http://localhost:8080/users");

        driver.findElement(By.linkText("selenium user")).click();

        driver.findElement(By.id("deleteForm")).submit();
    }
}
