package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.ExtensionRegistry.ExtensionInfo;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.UnknownFieldSet.Field;
import com.google.protobuf.WireFormat.FieldType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.mortbay.jetty.HttpVersions;

public abstract class AbstractMessage extends AbstractMessageLite implements Message {
    private int memoizedSize = -1;

    public static abstract class Builder<BuilderType extends Builder> extends com.google.protobuf.AbstractMessageLite.Builder<BuilderType> implements com.google.protobuf.Message.Builder {
        private static List<String> findMissingFields(Message message) {
            List<String> arrayList = new ArrayList();
            findMissingFields(message, HttpVersions.HTTP_0_9, arrayList);
            return arrayList;
        }

        private static void findMissingFields(Message message, String str, List<String> list) {
            for (FieldDescriptor fieldDescriptor : message.getDescriptorForType().getFields()) {
                if (fieldDescriptor.isRequired() && !message.hasField(fieldDescriptor)) {
                    list.add(str + fieldDescriptor.getName());
                }
            }
            for (Entry entry : message.getAllFields().entrySet()) {
                FieldDescriptor fieldDescriptor2 = (FieldDescriptor) entry.getKey();
                Object value = entry.getValue();
                if (fieldDescriptor2.getJavaType() == JavaType.MESSAGE) {
                    if (fieldDescriptor2.isRepeated()) {
                        int i = 0;
                        for (Message findMissingFields : (List) value) {
                            findMissingFields(findMissingFields, subMessagePrefix(str, fieldDescriptor2, i), list);
                            i++;
                        }
                    } else if (message.hasField(fieldDescriptor2)) {
                        findMissingFields((Message) value, subMessagePrefix(str, fieldDescriptor2, -1), list);
                    }
                }
            }
        }

        static boolean mergeFieldFrom(CodedInputStream codedInputStream, com.google.protobuf.UnknownFieldSet.Builder builder, ExtensionRegistryLite extensionRegistryLite, com.google.protobuf.Message.Builder builder2, int i) throws IOException {
            Message message = null;
            boolean z = false;
            Descriptor descriptorForType = builder2.getDescriptorForType();
            if (descriptorForType.getOptions().getMessageSetWireFormat() && i == WireFormat.MESSAGE_SET_ITEM_TAG) {
                mergeMessageSetExtensionFromCodedStream(codedInputStream, builder, extensionRegistryLite, builder2);
                return true;
            }
            FieldDescriptor findFieldByNumber;
            boolean z2;
            int tagWireType = WireFormat.getTagWireType(i);
            int tagFieldNumber = WireFormat.getTagFieldNumber(i);
            if (!descriptorForType.isExtensionNumber(tagFieldNumber)) {
                findFieldByNumber = descriptorForType.findFieldByNumber(tagFieldNumber);
            } else if (extensionRegistryLite instanceof ExtensionRegistry) {
                ExtensionInfo findExtensionByNumber = ((ExtensionRegistry) extensionRegistryLite).findExtensionByNumber(descriptorForType, tagFieldNumber);
                if (findExtensionByNumber == null) {
                    findFieldByNumber = null;
                } else {
                    FieldDescriptor fieldDescriptor = findExtensionByNumber.descriptor;
                    Message message2 = findExtensionByNumber.defaultInstance;
                    if (message2 == null && fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                        throw new IllegalStateException("Message-typed extension lacked default instance: " + fieldDescriptor.getFullName());
                    }
                    findFieldByNumber = fieldDescriptor;
                    message = message2;
                }
            } else {
                findFieldByNumber = null;
            }
            if (findFieldByNumber == null) {
                z2 = true;
            } else if (tagWireType == FieldSet.getWireFormatForFieldType(findFieldByNumber.getLiteType(), false)) {
                z2 = false;
            } else if (findFieldByNumber.isPackable() && tagWireType == FieldSet.getWireFormatForFieldType(findFieldByNumber.getLiteType(), true)) {
                z2 = false;
                z = true;
            } else {
                z2 = true;
            }
            if (z2) {
                return builder.mergeFieldFrom(i, codedInputStream);
            }
            if (z) {
                int pushLimit = codedInputStream.pushLimit(codedInputStream.readRawVarint32());
                if (findFieldByNumber.getLiteType() == FieldType.ENUM) {
                    while (codedInputStream.getBytesUntilLimit() > 0) {
                        EnumValueDescriptor findValueByNumber = findFieldByNumber.getEnumType().findValueByNumber(codedInputStream.readEnum());
                        if (findValueByNumber == null) {
                            return true;
                        }
                        builder2.addRepeatedField(findFieldByNumber, findValueByNumber);
                    }
                } else {
                    while (codedInputStream.getBytesUntilLimit() > 0) {
                        builder2.addRepeatedField(findFieldByNumber, FieldSet.readPrimitiveField(codedInputStream, findFieldByNumber.getLiteType()));
                    }
                }
                codedInputStream.popLimit(pushLimit);
            } else {
                Object build;
                Object newBuilderForType;
                com.google.protobuf.Message.Builder newBuilderForField;
                switch (findFieldByNumber.getType()) {
                    case GROUP:
                        if (message != null) {
                            newBuilderForType = message.newBuilderForType();
                        } else {
                            newBuilderForField = builder2.newBuilderForField(findFieldByNumber);
                        }
                        if (!findFieldByNumber.isRepeated()) {
                            newBuilderForType.mergeFrom((Message) builder2.getField(findFieldByNumber));
                        }
                        codedInputStream.readGroup(findFieldByNumber.getNumber(), newBuilderForType, extensionRegistryLite);
                        build = newBuilderForType.build();
                        break;
                    case MESSAGE:
                        if (message != null) {
                            newBuilderForType = message.newBuilderForType();
                        } else {
                            newBuilderForField = builder2.newBuilderForField(findFieldByNumber);
                        }
                        if (!findFieldByNumber.isRepeated()) {
                            newBuilderForType.mergeFrom((Message) builder2.getField(findFieldByNumber));
                        }
                        codedInputStream.readMessage(newBuilderForType, extensionRegistryLite);
                        build = newBuilderForType.build();
                        break;
                    case ENUM:
                        int readEnum = codedInputStream.readEnum();
                        build = findFieldByNumber.getEnumType().findValueByNumber(readEnum);
                        if (build == null) {
                            builder.mergeVarintField(tagFieldNumber, readEnum);
                            return true;
                        }
                        break;
                    default:
                        build = FieldSet.readPrimitiveField(codedInputStream, findFieldByNumber.getLiteType());
                        break;
                }
                if (findFieldByNumber.isRepeated()) {
                    builder2.addRepeatedField(findFieldByNumber, build);
                } else {
                    builder2.setField(findFieldByNumber, build);
                }
            }
            return true;
        }

        private static void mergeMessageSetExtensionFromCodedStream(CodedInputStream codedInputStream, com.google.protobuf.UnknownFieldSet.Builder builder, ExtensionRegistryLite extensionRegistryLite, com.google.protobuf.Message.Builder builder2) throws IOException {
            Descriptor descriptorForType = builder2.getDescriptorForType();
            FieldDescriptor fieldDescriptor = null;
            com.google.protobuf.MessageLite.Builder builder3 = null;
            ByteString byteString = null;
            int i = 0;
            while (true) {
                int readTag = codedInputStream.readTag();
                if (readTag == 0) {
                    break;
                } else if (readTag == WireFormat.MESSAGE_SET_TYPE_ID_TAG) {
                    i = codedInputStream.readUInt32();
                    if (i != 0) {
                        ExtensionInfo findExtensionByNumber = extensionRegistryLite instanceof ExtensionRegistry ? ((ExtensionRegistry) extensionRegistryLite).findExtensionByNumber(descriptorForType, i) : null;
                        if (findExtensionByNumber != null) {
                            fieldDescriptor = findExtensionByNumber.descriptor;
                            builder3 = findExtensionByNumber.defaultInstance.newBuilderForType();
                            Message message = (Message) builder2.getField(fieldDescriptor);
                            if (message != null) {
                                builder3.mergeFrom(message);
                            }
                            if (byteString != null) {
                                builder3.mergeFrom(CodedInputStream.newInstance(byteString.newInput()));
                                byteString = null;
                            }
                        } else if (byteString != null) {
                            builder.mergeField(i, Field.newBuilder().addLengthDelimited(byteString).build());
                            byteString = null;
                        }
                    }
                } else if (readTag == WireFormat.MESSAGE_SET_MESSAGE_TAG) {
                    if (i == 0) {
                        byteString = codedInputStream.readBytes();
                    } else if (builder3 == null) {
                        builder.mergeField(i, Field.newBuilder().addLengthDelimited(codedInputStream.readBytes()).build());
                    } else {
                        codedInputStream.readMessage(builder3, extensionRegistryLite);
                    }
                } else if (!codedInputStream.skipField(readTag)) {
                    break;
                }
            }
            codedInputStream.checkLastTagWas(WireFormat.MESSAGE_SET_ITEM_END_TAG);
            if (builder3 != null) {
                builder2.setField(fieldDescriptor, builder3.build());
            }
        }

        protected static UninitializedMessageException newUninitializedMessageException(Message message) {
            return new UninitializedMessageException(findMissingFields(message));
        }

        private static String subMessagePrefix(String str, FieldDescriptor fieldDescriptor, int i) {
            StringBuilder stringBuilder = new StringBuilder(str);
            if (fieldDescriptor.isExtension()) {
                stringBuilder.append('(').append(fieldDescriptor.getFullName()).append(')');
            } else {
                stringBuilder.append(fieldDescriptor.getName());
            }
            if (i != -1) {
                stringBuilder.append('[').append(i).append(']');
            }
            stringBuilder.append('.');
            return stringBuilder.toString();
        }

        public BuilderType clear() {
            for (Entry key : getAllFields().entrySet()) {
                clearField((FieldDescriptor) key.getKey());
            }
            return this;
        }

        public abstract BuilderType clone();

        public boolean mergeDelimitedFrom(InputStream inputStream) throws IOException {
            return super.mergeDelimitedFrom(inputStream);
        }

        public boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return super.mergeDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public BuilderType mergeFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (Builder) super.mergeFrom(byteString);
        }

        public BuilderType mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (Builder) super.mergeFrom(byteString, extensionRegistryLite);
        }

        public BuilderType mergeFrom(CodedInputStream codedInputStream) throws IOException {
            return mergeFrom(codedInputStream, ExtensionRegistry.getEmptyRegistry());
        }

        public BuilderType mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
            int readTag;
            do {
                readTag = codedInputStream.readTag();
                if (readTag == 0) {
                    break;
                }
            } while (mergeFieldFrom(codedInputStream, newBuilder, extensionRegistryLite, this, readTag));
            setUnknownFields(newBuilder.build());
            return this;
        }

        public BuilderType mergeFrom(Message message) {
            if (message.getDescriptorForType() != getDescriptorForType()) {
                throw new IllegalArgumentException("mergeFrom(Message) can only merge messages of the same type.");
            }
            for (Entry entry : message.getAllFields().entrySet()) {
                FieldDescriptor fieldDescriptor = (FieldDescriptor) entry.getKey();
                if (fieldDescriptor.isRepeated()) {
                    for (Object addRepeatedField : (List) entry.getValue()) {
                        addRepeatedField(fieldDescriptor, addRepeatedField);
                    }
                } else if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                    Message message2 = (Message) getField(fieldDescriptor);
                    if (message2 == message2.getDefaultInstanceForType()) {
                        setField(fieldDescriptor, entry.getValue());
                    } else {
                        setField(fieldDescriptor, message2.newBuilderForType().mergeFrom(message2).mergeFrom((Message) entry.getValue()).build());
                    }
                } else {
                    setField(fieldDescriptor, entry.getValue());
                }
            }
            mergeUnknownFields(message.getUnknownFields());
            return this;
        }

        public BuilderType mergeFrom(InputStream inputStream) throws IOException {
            return (Builder) super.mergeFrom(inputStream);
        }

        public BuilderType mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Builder) super.mergeFrom(inputStream, extensionRegistryLite);
        }

        public BuilderType mergeFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (Builder) super.mergeFrom(bArr);
        }

        public BuilderType mergeFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException {
            return (Builder) super.mergeFrom(bArr, i, i2);
        }

        public BuilderType mergeFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (Builder) super.mergeFrom(bArr, i, i2, extensionRegistryLite);
        }

        public BuilderType mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (Builder) super.mergeFrom(bArr, extensionRegistryLite);
        }

        public BuilderType mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
            setUnknownFields(UnknownFieldSet.newBuilder(getUnknownFields()).mergeFrom(unknownFieldSet).build());
            return this;
        }
    }

    protected static int hashBoolean(boolean z) {
        return z ? 1231 : 1237;
    }

    protected static int hashEnum(EnumLite enumLite) {
        return enumLite.getNumber();
    }

    protected static int hashEnumList(List<? extends EnumLite> list) {
        int i = 1;
        for (EnumLite hashEnum : list) {
            i = hashEnum(hashEnum) + (i * 31);
        }
        return i;
    }

    protected static int hashLong(long j) {
        return (int) ((j >>> 32) ^ j);
    }

    public boolean equals(Object obj) {
        if (obj != this) {
            if (!(obj instanceof Message)) {
                return false;
            }
            Message message = (Message) obj;
            if (getDescriptorForType() != message.getDescriptorForType() || !getAllFields().equals(message.getAllFields())) {
                return false;
            }
            if (!getUnknownFields().equals(message.getUnknownFields())) {
                return false;
            }
        }
        return true;
    }

    public int getSerializedSize() {
        int i = this.memoizedSize;
        if (i == -1) {
            boolean messageSetWireFormat = getDescriptorForType().getOptions().getMessageSetWireFormat();
            int i2 = 0;
            for (Entry entry : getAllFields().entrySet()) {
                FieldDescriptor fieldDescriptor = (FieldDescriptor) entry.getKey();
                Object value = entry.getValue();
                i2 = (messageSetWireFormat && fieldDescriptor.isExtension() && fieldDescriptor.getType() == Type.MESSAGE && !fieldDescriptor.isRepeated()) ? CodedOutputStream.computeMessageSetExtensionSize(fieldDescriptor.getNumber(), (Message) value) + i2 : FieldSet.computeFieldSize(fieldDescriptor, value) + i2;
            }
            UnknownFieldSet unknownFields = getUnknownFields();
            i = messageSetWireFormat ? unknownFields.getSerializedSizeAsMessageSet() + i2 : unknownFields.getSerializedSize() + i2;
            this.memoizedSize = i;
        }
        return i;
    }

    public int hashCode() {
        return (hashFields(getDescriptorForType().hashCode() + 779, getAllFields()) * 29) + getUnknownFields().hashCode();
    }

    protected int hashFields(int i, Map<FieldDescriptor, Object> map) {
        for (Entry entry : map.entrySet()) {
            FieldDescriptor fieldDescriptor = (FieldDescriptor) entry.getKey();
            Object value = entry.getValue();
            int number = (i * 37) + fieldDescriptor.getNumber();
            i = fieldDescriptor.getType() != Type.ENUM ? (number * 53) + value.hashCode() : fieldDescriptor.isRepeated() ? (number * 53) + hashEnumList((List) value) : (number * 53) + hashEnum((EnumLite) value);
        }
        return i;
    }

    public boolean isInitialized() {
        for (FieldDescriptor fieldDescriptor : getDescriptorForType().getFields()) {
            if (fieldDescriptor.isRequired() && !hasField(fieldDescriptor)) {
                return false;
            }
        }
        for (Entry entry : getAllFields().entrySet()) {
            FieldDescriptor fieldDescriptor2 = (FieldDescriptor) entry.getKey();
            if (fieldDescriptor2.getJavaType() == JavaType.MESSAGE) {
                if (fieldDescriptor2.isRepeated()) {
                    for (Message isInitialized : (List) entry.getValue()) {
                        if (!isInitialized.isInitialized()) {
                            return false;
                        }
                    }
                    continue;
                } else if (!((Message) entry.getValue()).isInitialized()) {
                    return false;
                }
            }
        }
        return true;
    }

    public final String toString() {
        return TextFormat.printToString((Message) this);
    }

    public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
        boolean messageSetWireFormat = getDescriptorForType().getOptions().getMessageSetWireFormat();
        for (Entry entry : getAllFields().entrySet()) {
            FieldDescriptor fieldDescriptor = (FieldDescriptor) entry.getKey();
            Object value = entry.getValue();
            if (messageSetWireFormat && fieldDescriptor.isExtension() && fieldDescriptor.getType() == Type.MESSAGE && !fieldDescriptor.isRepeated()) {
                codedOutputStream.writeMessageSetExtension(fieldDescriptor.getNumber(), (Message) value);
            } else {
                FieldSet.writeField(fieldDescriptor, value, codedOutputStream);
            }
        }
        UnknownFieldSet unknownFields = getUnknownFields();
        if (messageSetWireFormat) {
            unknownFields.writeAsMessageSetTo(codedOutputStream);
        } else {
            unknownFields.writeTo(codedOutputStream);
        }
    }
}
