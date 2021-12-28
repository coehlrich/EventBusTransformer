package com.wynntils.eventbustransformer;

import net.minecraftforge.eventbus.EventBusEngine;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) throws ZipException, IOException {
        File file = new File(args[0]);
        ZipFile zip = new ZipFile(file);
        File transformed = new File("transformed.jar");
        ZipOutputStream output = new ZipOutputStream(new FileOutputStream(transformed));

        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry next = entries.nextElement();
            output.putNextEntry(next);
            byte[] content = IOUtils.toByteArray(zip.getInputStream(next));
            if (next.getName().endsWith(".class")) {
                Type type = Type.getObjectType(next.getName().replace(".class", ""));
                if (EventBusEngine.INSTANCE.handlesClass(type)) {
                    ClassReader reader = new ClassReader(content);
                    ClassNode node = new ClassNode();
                    reader.accept(node, 0);

                    EventBusEngine.INSTANCE.processClass(node, type);
                    
                    ClassWriter writer = new ClassWriter(0);
                    node.accept(writer);

                    content = writer.toByteArray();
                }
            }

            IOUtils.write(content, output);
            output.closeEntry();
        }
        output.close();
        zip.close();

    }

}
