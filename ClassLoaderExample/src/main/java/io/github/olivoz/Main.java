package io.github.olivoz;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        File jarFile = new File("./ExampleToLoad-1.0-SNAPSHOT.jar");
        URL[] jarFilesToLoad = {jarFile.toURI().toURL()};

        URLClassLoader jarLoader = new ASMClassLoader(jarFilesToLoad);

        // Hitta och läs in klassen
        Class<?> testClass = Class.forName("io.github.olivoz.TestClass", true, jarLoader);

        // Hitta metoden main(String[] args)
        Method method = testClass.getDeclaredMethod("main", String[].class);


        // Kör metoden main med en tom array som args
        // Första parametern pekar på den instans som koden ska köras i
        // Då det är en statisk funktion så är objektet null
        String[] passedArguments = {};
        method.invoke(null, (Object) passedArguments);

        // Hitta metoden sayHello
        Method sayHelloMethod = testClass.getDeclaredMethod("sayHello");

        // Skapa en ny instans av klassen med standardkonstruktorn som
        // inte tar några parametrar
        Object instance = testClass.getDeclaredConstructor().newInstance();


        // Kör sayHello utan några parametrar
        sayHelloMethod.invoke(instance);
    }
}