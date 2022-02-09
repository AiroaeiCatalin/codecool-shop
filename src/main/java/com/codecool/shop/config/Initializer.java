package com.codecool.shop.config;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
//
//        //setting up a new supplier
//        Supplier airGun = new Supplier("Air gun", "Vintage weapons");
//        supplierDataStore.add(airGun);
//        Supplier houseOfGuns = new Supplier("House of guns", "Assault rifles");
//        supplierDataStore.add(houseOfGuns);
//        Supplier modernShot = new Supplier("Modern shot", "Modern weapons");
//        supplierDataStore.add(modernShot);
//
//        //setting up a new product category
//        ProductCategory oldWeapons = new ProductCategory("Old weapons", "Assault rifles", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
//        ProductCategory newWeapons = new ProductCategory("New weapons", "Assault rifles", "A modern touch sensitive smart phone.");
//        productCategoryDataStore.add(oldWeapons);
//        productCategoryDataStore.add(newWeapons);
//
//        //setting up products and printing it
//        productDataStore.add(new Product("StG 44", 49.9f, "USD", "The StG 44 (abbreviation of Sturmgewehr 44, \"assault rifle 44\") is a German selective-fire assault rifle developed during World War II by Hugo Schmeisser.", oldWeapons, airGun));
//        productDataStore.add(new Product("Sturmgewehr 1-5", 479, "USD", "The Volkssturmgewehr is the name of several rifle designs developed by Nazi Germany during the last months of World War II. ", oldWeapons, houseOfGuns));
//        productDataStore.add(new Product("Kar98k", 89, "USD", "The Karabiner 98 kurz, often abbreviated Karabiner 98k, Kar98k or K98k and also incorrectly sometimes referred to as a K98 , is a bolt-action rifle chambered for the 7.92x57mm Mauser cartridge.", oldWeapons, airGun));
//        productDataStore.add(new Product("MP5", 200, "USD", "The MP5 a 9x19mm Parabellum submachine gun, developed in the 1960s by a team of engineers from the German small arms manufacturer Heckler & Koch GmbH (H&K) of Oberndorf am Neckar. ", newWeapons, modernShot));
//        productDataStore.add(new Product("Ak-74u", 300, "USD", "In the early 1970s the Soviet weapons-designer Mikhail Kalashnikov developed the AK-74 as an assault rifle to replace the earlier AKM.", newWeapons, modernShot));
//        productDataStore.add(new Product("M4", 500, "USD", "The M4 Carbine is a 5.56x45mm NATO, air-cooled, gas-operated, direct impingement, magazine-fed, select fire carbine.", newWeapons, modernShot));
    }
}
