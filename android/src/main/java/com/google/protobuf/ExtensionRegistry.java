package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ExtensionRegistry extends ExtensionRegistryLite {
    private static final ExtensionRegistry EMPTY = new ExtensionRegistry(true);
    private final Map<String, ExtensionInfo> extensionsByName;
    private final Map<DescriptorIntPair, ExtensionInfo> extensionsByNumber;

    private static final class DescriptorIntPair {
        private final Descriptor descriptor;
        private final int number;

        DescriptorIntPair(Descriptor descriptor, int i) {
            this.descriptor = descriptor;
            this.number = i;
        }

        public boolean equals(Object obj) {
            if (obj instanceof DescriptorIntPair) {
                DescriptorIntPair descriptorIntPair = (DescriptorIntPair) obj;
                if (this.descriptor == descriptorIntPair.descriptor && this.number == descriptorIntPair.number) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return (this.descriptor.hashCode() * 65535) + this.number;
        }
    }

    public static final class ExtensionInfo {
        public final Message defaultInstance;
        public final FieldDescriptor descriptor;

        private ExtensionInfo(FieldDescriptor fieldDescriptor) {
            this.descriptor = fieldDescriptor;
            this.defaultInstance = null;
        }

        private ExtensionInfo(FieldDescriptor fieldDescriptor, Message message) {
            this.descriptor = fieldDescriptor;
            this.defaultInstance = message;
        }
    }

    private ExtensionRegistry() {
        this.extensionsByName = new HashMap();
        this.extensionsByNumber = new HashMap();
    }

    private ExtensionRegistry(ExtensionRegistry extensionRegistry) {
        super((ExtensionRegistryLite) extensionRegistry);
        this.extensionsByName = Collections.unmodifiableMap(extensionRegistry.extensionsByName);
        this.extensionsByNumber = Collections.unmodifiableMap(extensionRegistry.extensionsByNumber);
    }

    private ExtensionRegistry(boolean z) {
        super(ExtensionRegistryLite.getEmptyRegistry());
        this.extensionsByName = Collections.emptyMap();
        this.extensionsByNumber = Collections.emptyMap();
    }

    private void add(ExtensionInfo extensionInfo) {
        if (extensionInfo.descriptor.isExtension()) {
            this.extensionsByName.put(extensionInfo.descriptor.getFullName(), extensionInfo);
            this.extensionsByNumber.put(new DescriptorIntPair(extensionInfo.descriptor.getContainingType(), extensionInfo.descriptor.getNumber()), extensionInfo);
            FieldDescriptor fieldDescriptor = extensionInfo.descriptor;
            if (fieldDescriptor.getContainingType().getOptions().getMessageSetWireFormat() && fieldDescriptor.getType() == Type.MESSAGE && fieldDescriptor.isOptional() && fieldDescriptor.getExtensionScope() == fieldDescriptor.getMessageType()) {
                this.extensionsByName.put(fieldDescriptor.getMessageType().getFullName(), extensionInfo);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("ExtensionRegistry.add() was given a FieldDescriptor for a regular (non-extension) field.");
    }

    public static ExtensionRegistry getEmptyRegistry() {
        return EMPTY;
    }

    public static ExtensionRegistry newInstance() {
        return new ExtensionRegistry();
    }

    public void add(FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
            throw new IllegalArgumentException("ExtensionRegistry.add() must be provided a default instance when adding an embedded message extension.");
        }
        add(new ExtensionInfo(fieldDescriptor, null));
    }

    public void add(FieldDescriptor fieldDescriptor, Message message) {
        if (fieldDescriptor.getJavaType() != JavaType.MESSAGE) {
            throw new IllegalArgumentException("ExtensionRegistry.add() provided a default instance for a non-message extension.");
        }
        add(new ExtensionInfo(fieldDescriptor, message));
    }

    public void add(GeneratedExtension<?, ?> generatedExtension) {
        if (generatedExtension.getDescriptor().getJavaType() != JavaType.MESSAGE) {
            add(new ExtensionInfo(generatedExtension.getDescriptor(), null));
        } else if (generatedExtension.getMessageDefaultInstance() == null) {
            throw new IllegalStateException("Registered message-type extension had null default instance: " + generatedExtension.getDescriptor().getFullName());
        } else {
            add(new ExtensionInfo(generatedExtension.getDescriptor(), generatedExtension.getMessageDefaultInstance()));
        }
    }

    public ExtensionInfo findExtensionByName(String str) {
        return (ExtensionInfo) this.extensionsByName.get(str);
    }

    public ExtensionInfo findExtensionByNumber(Descriptor descriptor, int i) {
        return (ExtensionInfo) this.extensionsByNumber.get(new DescriptorIntPair(descriptor, i));
    }

    public ExtensionRegistry getUnmodifiable() {
        return new ExtensionRegistry(this);
    }
}
