package com.wynntils.eventbustransformer;

import dev.architectury.transformer.transformers.base.ClassEditTransformer;
import net.minecraftforge.eventbus.EventBusEngine;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class EventBusTransform implements ClassEditTransformer {

    private static final long serialVersionUID = -2304913653368586405L;

    @Override
    public dev.architectury.transformer.shadowed.impl.org.objectweb.asm.tree.ClassNode doEdit(String name, dev.architectury.transformer.shadowed.impl.org.objectweb.asm.tree.ClassNode node) {
        Type type = Type.getObjectType(node.name);
        if (EventBusEngine.INSTANCE.handlesClass(type)) {
            dev.architectury.transformer.shadowed.impl.org.objectweb.asm.ClassWriter architecturyClassWriter = new dev.architectury.transformer.shadowed.impl.org.objectweb.asm.ClassWriter(0);
            node.accept(architecturyClassWriter);
            ClassReader normalClassReader = new ClassReader(architecturyClassWriter.toByteArray());
            ClassNode normalNode = new ClassNode();
            normalClassReader.accept(normalNode, 0);

            EventBusEngine.INSTANCE.processClass(normalNode, type);
        }
        return node;
    }

}
