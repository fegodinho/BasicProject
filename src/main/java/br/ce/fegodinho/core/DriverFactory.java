package br.ce.fegodinho.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import br.ce.fegodinho.core.Propriedades.TipoExecucao;

public class DriverFactory {
	
	//private static WebDriver driver;
	
	private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>() {
		@Override
		protected synchronized WebDriver initialValue() {
			return initDriver();
		}
	};
	
	private DriverFactory() {
		
	}
	
	public static WebDriver getDriver() {
		return threadDriver.get();
	}
	
	public static WebDriver initDriver() {	
		WebDriver driver = null;
		
		if(Propriedades.TIPO_EXECUCAO == TipoExecucao.LOCAL) {		
		switch (Propriedades.BROWSER) {
			case FIREFOX: driver = new FirefoxDriver(); break;
			case CHROME: driver = new ChromeDriver(); break;
			case INTERNET_EXPLORER: driver = new InternetExplorerDriver(); break;
			case MICROSOFT_EDGE: driver = new EdgeDriver(); break;			
			}
		}
		if(Propriedades.TIPO_EXECUCAO == TipoExecucao.GRID) {
			DesiredCapabilities cap = null;
			switch (Propriedades.BROWSER) {
			case FIREFOX: cap = DesiredCapabilities.firefox(); break;
			case CHROME: cap = DesiredCapabilities.chrome(); break;
			case INTERNET_EXPLORER: cap = DesiredCapabilities.internetExplorer(); break;
			case MICROSOFT_EDGE: cap = DesiredCapabilities.edge(); break;			
			}
			try {
				driver = new RemoteWebDriver(new URL("http://192.168.25.5:4444/wd/hub"), cap);
			} catch (MalformedURLException e) {
				System.err.println("Falha na conex�o com o GRID");
				e.printStackTrace();
			}
		}
		if(Propriedades.TIPO_EXECUCAO == TipoExecucao.CLOUD) {
			DesiredCapabilities cap = null;
			switch (Propriedades.BROWSER) {
			case FIREFOX: cap = DesiredCapabilities.firefox(); break;
			case CHROME: cap = DesiredCapabilities.chrome(); break;
			case INTERNET_EXPLORER: cap = DesiredCapabilities.internetExplorer();
									cap.setCapability("platform", "Windows 7");
									cap.setCapability("version", "10.0");			
			break;
			case MICROSOFT_EDGE: cap = DesiredCapabilities.edge(); break;			
			}
			try {
				driver = new RemoteWebDriver(new URL("http://fegodinho:172fcd87-6b7d-4c8d-8693-e20c903affa4@ondemand.saucelabs.com:80/wd/hub"), cap);
				//URL= http://usuario_saucelabs:chave_saucelabs@URLsaucelabs
			} catch (MalformedURLException e) {
				System.err.println("Falha na conex�o com a Saucelabs");
				e.printStackTrace();
			}
		}
		
		driver.manage().window().setSize(new Dimension(1200, 765));			
		return driver;
	}
	
	public static void killDriver() {
		WebDriver driver = getDriver();
		if (driver != null) {
		driver.quit();
		driver=null;
		}
		if(threadDriver != null) {
			threadDriver.remove();
		}
	}	
}