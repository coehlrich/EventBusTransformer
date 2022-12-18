module com.wynntils.eventbustransformer {
    exports com.wynntils.eventbustransformer;

    requires net.minecraftforge.eventbus;
    requires org.objectweb.asm;
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires org.objectweb.asm.tree;
    // requires cpw.mods.modlauncher;

    // architectury-transformer
    requires static org.objectweb.asm.commons;

    uses net.minecraftforge.eventbus.IEventBusEngine;
}