package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import java.util.Map;

public interface MessageOrBuilder extends MessageLiteOrBuilder {
    Map<FieldDescriptor, Object> getAllFields();

    Message getDefaultInstanceForType();

    Descriptor getDescriptorForType();

    Object getField(FieldDescriptor fieldDescriptor);

    Object getRepeatedField(FieldDescriptor fieldDescriptor, int i);

    int getRepeatedFieldCount(FieldDescriptor fieldDescriptor);

    UnknownFieldSet getUnknownFields();

    boolean hasField(FieldDescriptor fieldDescriptor);
}
