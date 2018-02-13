package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class DynamicMessage extends AbstractMessage {
    private final FieldSet<FieldDescriptor> fields;
    private int memoizedSize;
    private final Descriptor type;
    private final UnknownFieldSet unknownFields;

    public static final class Builder extends com.google.protobuf.AbstractMessage.Builder<Builder> {
        private FieldSet<FieldDescriptor> fields;
        private final Descriptor type;
        private UnknownFieldSet unknownFields;

        private Builder(Descriptor descriptor) {
            this.type = descriptor;
            this.fields = FieldSet.newFieldSet();
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        private DynamicMessage buildParsed() throws InvalidProtocolBufferException {
            if (isInitialized()) {
                return buildPartial();
            }
            throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(new DynamicMessage(this.type, this.fields, this.unknownFields)).asInvalidProtocolBufferException();
        }

        private void verifyContainingType(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.getContainingType() != this.type) {
                throw new IllegalArgumentException("FieldDescriptor does not match message type.");
            }
        }

        public Builder addRepeatedField(FieldDescriptor fieldDescriptor, Object obj) {
            verifyContainingType(fieldDescriptor);
            this.fields.addRepeatedField(fieldDescriptor, obj);
            return this;
        }

        public DynamicMessage build() {
            if (this.fields == null || isInitialized()) {
                return buildPartial();
            }
            throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(new DynamicMessage(this.type, this.fields, this.unknownFields));
        }

        public DynamicMessage buildPartial() {
            if (this.fields == null) {
                throw new IllegalStateException("build() has already been called on this Builder.");
            }
            this.fields.makeImmutable();
            DynamicMessage dynamicMessage = new DynamicMessage(this.type, this.fields, this.unknownFields);
            this.fields = null;
            this.unknownFields = null;
            return dynamicMessage;
        }

        public Builder clear() {
            if (this.fields == null) {
                throw new IllegalStateException("Cannot call clear() after build().");
            }
            this.fields.clear();
            return this;
        }

        public Builder clearField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            this.fields.clearField(fieldDescriptor);
            return this;
        }

        public Builder clone() {
            Builder builder = new Builder(this.type);
            builder.fields.mergeFrom(this.fields);
            return builder;
        }

        public Map<FieldDescriptor, Object> getAllFields() {
            return this.fields.getAllFields();
        }

        public DynamicMessage getDefaultInstanceForType() {
            return DynamicMessage.getDefaultInstance(this.type);
        }

        public Descriptor getDescriptorForType() {
            return this.type;
        }

        public Object getField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            Object field = this.fields.getField(fieldDescriptor);
            return field == null ? fieldDescriptor.getJavaType() == JavaType.MESSAGE ? DynamicMessage.getDefaultInstance(fieldDescriptor.getMessageType()) : fieldDescriptor.getDefaultValue() : field;
        }

        public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
            verifyContainingType(fieldDescriptor);
            return this.fields.getRepeatedField(fieldDescriptor, i);
        }

        public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            return this.fields.getRepeatedFieldCount(fieldDescriptor);
        }

        public UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        public boolean hasField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            return this.fields.hasField(fieldDescriptor);
        }

        public boolean isInitialized() {
            return DynamicMessage.isInitialized(this.type, this.fields);
        }

        public Builder mergeFrom(Message message) {
            if (!(message instanceof DynamicMessage)) {
                return (Builder) super.mergeFrom(message);
            }
            DynamicMessage dynamicMessage = (DynamicMessage) message;
            if (dynamicMessage.type != this.type) {
                throw new IllegalArgumentException("mergeFrom(Message) can only merge messages of the same type.");
            }
            this.fields.mergeFrom(dynamicMessage.fields);
            mergeUnknownFields(dynamicMessage.unknownFields);
            return this;
        }

        public Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
            this.unknownFields = UnknownFieldSet.newBuilder(this.unknownFields).mergeFrom(unknownFieldSet).build();
            return this;
        }

        public Builder newBuilderForField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                return new Builder(fieldDescriptor.getMessageType());
            }
            throw new IllegalArgumentException("newBuilderForField is only valid for fields with message type.");
        }

        public Builder setField(FieldDescriptor fieldDescriptor, Object obj) {
            verifyContainingType(fieldDescriptor);
            this.fields.setField(fieldDescriptor, obj);
            return this;
        }

        public Builder setRepeatedField(FieldDescriptor fieldDescriptor, int i, Object obj) {
            verifyContainingType(fieldDescriptor);
            this.fields.setRepeatedField(fieldDescriptor, i, obj);
            return this;
        }

        public Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
            this.unknownFields = unknownFieldSet;
            return this;
        }
    }

    private DynamicMessage(Descriptor descriptor, FieldSet<FieldDescriptor> fieldSet, UnknownFieldSet unknownFieldSet) {
        this.memoizedSize = -1;
        this.type = descriptor;
        this.fields = fieldSet;
        this.unknownFields = unknownFieldSet;
    }

    public static DynamicMessage getDefaultInstance(Descriptor descriptor) {
        return new DynamicMessage(descriptor, FieldSet.emptySet(), UnknownFieldSet.getDefaultInstance());
    }

    private static boolean isInitialized(Descriptor descriptor, FieldSet<FieldDescriptor> fieldSet) {
        for (FieldDescriptor fieldDescriptor : descriptor.getFields()) {
            if (fieldDescriptor.isRequired() && !fieldSet.hasField(fieldDescriptor)) {
                return false;
            }
        }
        return fieldSet.isInitialized();
    }

    public static Builder newBuilder(Descriptor descriptor) {
        return new Builder(descriptor);
    }

    public static Builder newBuilder(Message message) {
        return new Builder(message.getDescriptorForType()).mergeFrom(message);
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, ByteString byteString) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(byteString)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, ByteString byteString, ExtensionRegistry extensionRegistry) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(byteString, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, CodedInputStream codedInputStream) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(codedInputStream)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, CodedInputStream codedInputStream, ExtensionRegistry extensionRegistry) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(codedInputStream, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, InputStream inputStream) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(inputStream)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, InputStream inputStream, ExtensionRegistry extensionRegistry) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(inputStream, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, byte[] bArr) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(bArr)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, byte[] bArr, ExtensionRegistry extensionRegistry) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(bArr, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    private void verifyContainingType(FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor.getContainingType() != this.type) {
            throw new IllegalArgumentException("FieldDescriptor does not match message type.");
        }
    }

    public Map<FieldDescriptor, Object> getAllFields() {
        return this.fields.getAllFields();
    }

    public DynamicMessage getDefaultInstanceForType() {
        return getDefaultInstance(this.type);
    }

    public Descriptor getDescriptorForType() {
        return this.type;
    }

    public Object getField(FieldDescriptor fieldDescriptor) {
        verifyContainingType(fieldDescriptor);
        Object field = this.fields.getField(fieldDescriptor);
        return field == null ? fieldDescriptor.getJavaType() == JavaType.MESSAGE ? getDefaultInstance(fieldDescriptor.getMessageType()) : fieldDescriptor.getDefaultValue() : field;
    }

    public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
        verifyContainingType(fieldDescriptor);
        return this.fields.getRepeatedField(fieldDescriptor, i);
    }

    public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
        verifyContainingType(fieldDescriptor);
        return this.fields.getRepeatedFieldCount(fieldDescriptor);
    }

    public int getSerializedSize() {
        int i = this.memoizedSize;
        if (i == -1) {
            i = this.type.getOptions().getMessageSetWireFormat() ? this.fields.getMessageSetSerializedSize() + this.unknownFields.getSerializedSizeAsMessageSet() : this.fields.getSerializedSize() + this.unknownFields.getSerializedSize();
            this.memoizedSize = i;
        }
        return i;
    }

    public UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    public boolean hasField(FieldDescriptor fieldDescriptor) {
        verifyContainingType(fieldDescriptor);
        return this.fields.hasField(fieldDescriptor);
    }

    public boolean isInitialized() {
        return isInitialized(this.type, this.fields);
    }

    public Builder newBuilderForType() {
        return new Builder(this.type);
    }

    public Builder toBuilder() {
        return newBuilderForType().mergeFrom((Message) this);
    }

    public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
        if (this.type.getOptions().getMessageSetWireFormat()) {
            this.fields.writeMessageSetTo(codedOutputStream);
            this.unknownFields.writeAsMessageSetTo(codedOutputStream);
            return;
        }
        this.fields.writeTo(codedOutputStream);
        this.unknownFields.writeTo(codedOutputStream);
    }
}
