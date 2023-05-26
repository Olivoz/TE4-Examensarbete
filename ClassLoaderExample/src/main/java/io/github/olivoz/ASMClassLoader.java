package io.github.olivoz;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class ASMClassLoader extends URLClassLoader {
    public ASMClassLoader(URL[] jarsToLoad) {
        super(jarsToLoad);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) return loadedClass;

        if (!name.equals("io.github.olivoz.TestClass")) return super.loadClass(name, resolve);

        try {
            return define(name, resolve);
        } catch (IOException | ClassNotFoundException ex) {
            return super.loadClass(name, resolve);
        }
    }

    private Class<?> define(String name, boolean resolve) throws IOException, ClassNotFoundException {
        // loadBytes är en metod som läser in klass
        // Se Github projektet för detaljer
        byte[] bytes = loadBytes(name);

        // ASMVisitor är vår klass som modifierar bytekoden
        // Se nedan eller Github projektet
        byte[] transformedBytes = ASMVisitor.transform(bytes);

        // Om den modifierade klassen ska skrivas till en fil
        // Så kan det skrivas så här:
        try (FileOutputStream out =
                     new FileOutputStream("out.class")
        ) {
            out.write(transformedBytes);
        }


        // defineClass är en inbyggd funktion som definierar klassen
        // så att den senare kan användas
        Class<?> defined = defineClass(name, transformedBytes, 0, transformedBytes.length);
        if (resolve) resolveClass(defined);
        return defined;
    }

    private byte[] loadBytes(String name) throws IOException, ClassNotFoundException {
        if (name == null) throw new ClassNotFoundException();
        String path = name.replace('.', '/') + ".class";

        try (InputStream input = getResourceAsStream(path)) {
            if (input == null) throw new ClassNotFoundException();

            return input.readAllBytes();
        }
    }

}
