package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.GeneratedMessage.ExtendableBuilder;
import com.google.protobuf.GeneratedMessage.ExtendableMessage;
import com.google.protobuf.GeneratedMessage.ExtendableMessageOrBuilder;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;
import com.google.protobuf.Internal.EnumLiteMap;
import com.parrot.ardronetool.vlib.VideoCodecType;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.jetty.HttpVersions;

public final class DescriptorProtos {
    private static FileDescriptor descriptor;
    private static Descriptor f276x1458f8d;
    private static FieldAccessorTable f277xf366d90b;
    private static Descriptor internal_static_google_protobuf_DescriptorProto_descriptor;
    private static FieldAccessorTable f278x907d0bf0;
    private static Descriptor internal_static_google_protobuf_EnumDescriptorProto_descriptor;
    private static FieldAccessorTable f279x9945f651;
    private static Descriptor internal_static_google_protobuf_EnumOptions_descriptor;
    private static FieldAccessorTable internal_static_google_protobuf_EnumOptions_fieldAccessorTable;
    private static Descriptor f280xf18031a8;
    private static FieldAccessorTable f281xfb173026;
    private static Descriptor internal_static_google_protobuf_EnumValueOptions_descriptor;
    private static FieldAccessorTable f282xdf65a421;
    private static Descriptor internal_static_google_protobuf_FieldDescriptorProto_descriptor;
    private static FieldAccessorTable f283x4734b330;
    private static Descriptor internal_static_google_protobuf_FieldOptions_descriptor;
    private static FieldAccessorTable internal_static_google_protobuf_FieldOptions_fieldAccessorTable;
    private static Descriptor internal_static_google_protobuf_FileDescriptorProto_descriptor;
    private static FieldAccessorTable f284x4b52780c;
    private static Descriptor internal_static_google_protobuf_FileDescriptorSet_descriptor;
    private static FieldAccessorTable f285x15a6a952;
    private static Descriptor internal_static_google_protobuf_FileOptions_descriptor;
    private static FieldAccessorTable internal_static_google_protobuf_FileOptions_fieldAccessorTable;
    private static Descriptor internal_static_google_protobuf_MessageOptions_descriptor;
    private static FieldAccessorTable f286x9c0b3d38;
    private static Descriptor internal_static_google_protobuf_MethodDescriptorProto_descriptor;
    private static FieldAccessorTable f287xc5331ef1;
    private static Descriptor internal_static_google_protobuf_MethodOptions_descriptor;
    private static FieldAccessorTable internal_static_google_protobuf_MethodOptions_fieldAccessorTable;
    private static Descriptor f288x158c73ed;
    private static FieldAccessorTable f289xee335d6b;
    private static Descriptor internal_static_google_protobuf_ServiceOptions_descriptor;
    private static FieldAccessorTable f290x371e666;
    private static Descriptor f291xb210d08d;
    private static FieldAccessorTable f292x9611a0b;
    private static Descriptor internal_static_google_protobuf_SourceCodeInfo_descriptor;
    private static FieldAccessorTable f293x532209f9;
    private static Descriptor f294xb111d23c;
    private static FieldAccessorTable f295x1698fcba;
    private static Descriptor internal_static_google_protobuf_UninterpretedOption_descriptor;
    private static FieldAccessorTable f296x2101041;

    static final class C09401 implements InternalDescriptorAssigner {
        C09401() {
        }

        public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
            DescriptorProtos.descriptor = fileDescriptor;
            DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(0);
            DescriptorProtos.f285x15a6a952 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor, new String[]{"File"}, FileDescriptorSet.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(1);
            DescriptorProtos.f284x4b52780c = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor, new String[]{"Name", "Package", "Dependency", "MessageType", "EnumType", "Service", "Extension", "Options", "SourceCodeInfo"}, FileDescriptorProto.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(2);
            DescriptorProtos.f278x907d0bf0 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor, new String[]{"Name", "Field", "Extension", "NestedType", "EnumType", "ExtensionRange", "Options"}, DescriptorProto.class, Builder.class);
            DescriptorProtos.f276x1458f8d = (Descriptor) DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor.getNestedTypes().get(0);
            DescriptorProtos.f277xf366d90b = new FieldAccessorTable(DescriptorProtos.f276x1458f8d, new String[]{"Start", "End"}, ExtensionRange.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(3);
            DescriptorProtos.f283x4734b330 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor, new String[]{"Name", "Number", "Label", "Type", "TypeName", "Extendee", "DefaultValue", "Options"}, FieldDescriptorProto.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(4);
            DescriptorProtos.f279x9945f651 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor, new String[]{"Name", "Value", "Options"}, EnumDescriptorProto.class, Builder.class);
            DescriptorProtos.f280xf18031a8 = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(5);
            DescriptorProtos.f281xfb173026 = new FieldAccessorTable(DescriptorProtos.f280xf18031a8, new String[]{"Name", "Number", "Options"}, EnumValueDescriptorProto.class, Builder.class);
            DescriptorProtos.f288x158c73ed = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(6);
            DescriptorProtos.f289xee335d6b = new FieldAccessorTable(DescriptorProtos.f288x158c73ed, new String[]{"Name", "Method", "Options"}, ServiceDescriptorProto.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(7);
            DescriptorProtos.f287xc5331ef1 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor, new String[]{"Name", "InputType", "OutputType", "Options"}, MethodDescriptorProto.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(8);
            DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor, new String[]{"JavaPackage", "JavaOuterClassname", "JavaMultipleFiles", "JavaGenerateEqualsAndHash", "OptimizeFor", "CcGenericServices", "JavaGenericServices", "PyGenericServices", "UninterpretedOption"}, FileOptions.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(9);
            DescriptorProtos.f286x9c0b3d38 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor, new String[]{"MessageSetWireFormat", "NoStandardDescriptorAccessor", "UninterpretedOption"}, MessageOptions.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(10);
            DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor, new String[]{"Ctype", "Packed", "Deprecated", "ExperimentalMapKey", "UninterpretedOption"}, FieldOptions.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(11);
            DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor, new String[]{"UninterpretedOption"}, EnumOptions.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(12);
            DescriptorProtos.f282xdf65a421 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor, new String[]{"UninterpretedOption"}, EnumValueOptions.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(13);
            DescriptorProtos.f290x371e666 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor, new String[]{"UninterpretedOption"}, ServiceOptions.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(14);
            DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor, new String[]{"UninterpretedOption"}, MethodOptions.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(15);
            DescriptorProtos.f296x2101041 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor, new String[]{"Name", "IdentifierValue", "PositiveIntValue", "NegativeIntValue", "DoubleValue", "StringValue", "AggregateValue"}, UninterpretedOption.class, Builder.class);
            DescriptorProtos.f294xb111d23c = (Descriptor) DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor.getNestedTypes().get(0);
            DescriptorProtos.f295x1698fcba = new FieldAccessorTable(DescriptorProtos.f294xb111d23c, new String[]{"NamePart", "IsExtension"}, NamePart.class, Builder.class);
            DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor = (Descriptor) DescriptorProtos.getDescriptor().getMessageTypes().get(16);
            DescriptorProtos.f293x532209f9 = new FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor, new String[]{"Location"}, SourceCodeInfo.class, Builder.class);
            DescriptorProtos.f291xb210d08d = (Descriptor) DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor.getNestedTypes().get(0);
            DescriptorProtos.f292x9611a0b = new FieldAccessorTable(DescriptorProtos.f291xb210d08d, new String[]{"Path", "Span"}, Location.class, Builder.class);
            return null;
        }
    }

    public interface DescriptorProtoOrBuilder extends MessageOrBuilder {
        EnumDescriptorProto getEnumType(int i);

        int getEnumTypeCount();

        List<EnumDescriptorProto> getEnumTypeList();

        EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int i);

        List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList();

        FieldDescriptorProto getExtension(int i);

        int getExtensionCount();

        List<FieldDescriptorProto> getExtensionList();

        FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int i);

        List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList();

        ExtensionRange getExtensionRange(int i);

        int getExtensionRangeCount();

        List<ExtensionRange> getExtensionRangeList();

        ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int i);

        List<? extends ExtensionRangeOrBuilder> getExtensionRangeOrBuilderList();

        FieldDescriptorProto getField(int i);

        int getFieldCount();

        List<FieldDescriptorProto> getFieldList();

        FieldDescriptorProtoOrBuilder getFieldOrBuilder(int i);

        List<? extends FieldDescriptorProtoOrBuilder> getFieldOrBuilderList();

        String getName();

        DescriptorProto getNestedType(int i);

        int getNestedTypeCount();

        List<DescriptorProto> getNestedTypeList();

        DescriptorProtoOrBuilder getNestedTypeOrBuilder(int i);

        List<? extends DescriptorProtoOrBuilder> getNestedTypeOrBuilderList();

        MessageOptions getOptions();

        MessageOptionsOrBuilder getOptionsOrBuilder();

        boolean hasName();

        boolean hasOptions();
    }

    public static final class DescriptorProto extends GeneratedMessage implements DescriptorProtoOrBuilder {
        public static final int ENUM_TYPE_FIELD_NUMBER = 4;
        public static final int EXTENSION_FIELD_NUMBER = 6;
        public static final int EXTENSION_RANGE_FIELD_NUMBER = 5;
        public static final int FIELD_FIELD_NUMBER = 2;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int NESTED_TYPE_FIELD_NUMBER = 3;
        public static final int OPTIONS_FIELD_NUMBER = 7;
        private static final DescriptorProto defaultInstance = new DescriptorProto(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private List<EnumDescriptorProto> enumType_;
        private List<ExtensionRange> extensionRange_;
        private List<FieldDescriptorProto> extension_;
        private List<FieldDescriptorProto> field_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private Object name_;
        private List<DescriptorProto> nestedType_;
        private MessageOptions options_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements DescriptorProtoOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<EnumDescriptorProto, Builder, EnumDescriptorProtoOrBuilder> enumTypeBuilder_;
            private List<EnumDescriptorProto> enumType_;
            private RepeatedFieldBuilder<FieldDescriptorProto, Builder, FieldDescriptorProtoOrBuilder> extensionBuilder_;
            private RepeatedFieldBuilder<ExtensionRange, Builder, ExtensionRangeOrBuilder> extensionRangeBuilder_;
            private List<ExtensionRange> extensionRange_;
            private List<FieldDescriptorProto> extension_;
            private RepeatedFieldBuilder<FieldDescriptorProto, Builder, FieldDescriptorProtoOrBuilder> fieldBuilder_;
            private List<FieldDescriptorProto> field_;
            private Object name_;
            private RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> nestedTypeBuilder_;
            private List<DescriptorProto> nestedType_;
            private SingleFieldBuilder<MessageOptions, Builder, MessageOptionsOrBuilder> optionsBuilder_;
            private MessageOptions options_;

            private Builder() {
                this.name_ = HttpVersions.HTTP_0_9;
                this.field_ = Collections.emptyList();
                this.extension_ = Collections.emptyList();
                this.nestedType_ = Collections.emptyList();
                this.enumType_ = Collections.emptyList();
                this.extensionRange_ = Collections.emptyList();
                this.options_ = MessageOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = HttpVersions.HTTP_0_9;
                this.field_ = Collections.emptyList();
                this.extension_ = Collections.emptyList();
                this.nestedType_ = Collections.emptyList();
                this.enumType_ = Collections.emptyList();
                this.extensionRange_ = Collections.emptyList();
                this.options_ = MessageOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private DescriptorProto buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureEnumTypeIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.enumType_ = new ArrayList(this.enumType_);
                    this.bitField0_ |= 16;
                }
            }

            private void ensureExtensionIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.extension_ = new ArrayList(this.extension_);
                    this.bitField0_ |= 4;
                }
            }

            private void ensureExtensionRangeIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.extensionRange_ = new ArrayList(this.extensionRange_);
                    this.bitField0_ |= 32;
                }
            }

            private void ensureFieldIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.field_ = new ArrayList(this.field_);
                    this.bitField0_ |= 2;
                }
            }

            private void ensureNestedTypeIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.nestedType_ = new ArrayList(this.nestedType_);
                    this.bitField0_ |= 8;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
            }

            private RepeatedFieldBuilder<EnumDescriptorProto, Builder, EnumDescriptorProtoOrBuilder> getEnumTypeFieldBuilder() {
                if (this.enumTypeBuilder_ == null) {
                    this.enumTypeBuilder_ = new RepeatedFieldBuilder(this.enumType_, (this.bitField0_ & 16) == 16, getParentForChildren(), isClean());
                    this.enumType_ = null;
                }
                return this.enumTypeBuilder_;
            }

            private RepeatedFieldBuilder<FieldDescriptorProto, Builder, FieldDescriptorProtoOrBuilder> getExtensionFieldBuilder() {
                if (this.extensionBuilder_ == null) {
                    this.extensionBuilder_ = new RepeatedFieldBuilder(this.extension_, (this.bitField0_ & 4) == 4, getParentForChildren(), isClean());
                    this.extension_ = null;
                }
                return this.extensionBuilder_;
            }

            private RepeatedFieldBuilder<ExtensionRange, Builder, ExtensionRangeOrBuilder> getExtensionRangeFieldBuilder() {
                if (this.extensionRangeBuilder_ == null) {
                    this.extensionRangeBuilder_ = new RepeatedFieldBuilder(this.extensionRange_, (this.bitField0_ & 32) == 32, getParentForChildren(), isClean());
                    this.extensionRange_ = null;
                }
                return this.extensionRangeBuilder_;
            }

            private RepeatedFieldBuilder<FieldDescriptorProto, Builder, FieldDescriptorProtoOrBuilder> getFieldFieldBuilder() {
                if (this.fieldBuilder_ == null) {
                    this.fieldBuilder_ = new RepeatedFieldBuilder(this.field_, (this.bitField0_ & 2) == 2, getParentForChildren(), isClean());
                    this.field_ = null;
                }
                return this.fieldBuilder_;
            }

            private RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> getNestedTypeFieldBuilder() {
                if (this.nestedTypeBuilder_ == null) {
                    this.nestedTypeBuilder_ = new RepeatedFieldBuilder(this.nestedType_, (this.bitField0_ & 8) == 8, getParentForChildren(), isClean());
                    this.nestedType_ = null;
                }
                return this.nestedTypeBuilder_;
            }

            private SingleFieldBuilder<MessageOptions, Builder, MessageOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, getParentForChildren(), isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getFieldFieldBuilder();
                    getExtensionFieldBuilder();
                    getNestedTypeFieldBuilder();
                    getEnumTypeFieldBuilder();
                    getExtensionRangeFieldBuilder();
                    getOptionsFieldBuilder();
                }
            }

            public Builder addAllEnumType(Iterable<? extends EnumDescriptorProto> iterable) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.enumType_);
                    onChanged();
                } else {
                    this.enumTypeBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addAllExtension(Iterable<? extends FieldDescriptorProto> iterable) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.extension_);
                    onChanged();
                } else {
                    this.extensionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addAllExtensionRange(Iterable<? extends ExtensionRange> iterable) {
                if (this.extensionRangeBuilder_ == null) {
                    ensureExtensionRangeIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.extensionRange_);
                    onChanged();
                } else {
                    this.extensionRangeBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addAllField(Iterable<? extends FieldDescriptorProto> iterable) {
                if (this.fieldBuilder_ == null) {
                    ensureFieldIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.field_);
                    onChanged();
                } else {
                    this.fieldBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addAllNestedType(Iterable<? extends DescriptorProto> iterable) {
                if (this.nestedTypeBuilder_ == null) {
                    ensureNestedTypeIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.nestedType_);
                    onChanged();
                } else {
                    this.nestedTypeBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addEnumType(int i, Builder builder) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(i, builder.build());
                    onChanged();
                } else {
                    this.enumTypeBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addEnumType(int i, EnumDescriptorProto enumDescriptorProto) {
                if (this.enumTypeBuilder_ != null) {
                    this.enumTypeBuilder_.addMessage(i, enumDescriptorProto);
                } else if (enumDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(i, enumDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addEnumType(Builder builder) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(builder.build());
                    onChanged();
                } else {
                    this.enumTypeBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addEnumType(EnumDescriptorProto enumDescriptorProto) {
                if (this.enumTypeBuilder_ != null) {
                    this.enumTypeBuilder_.addMessage(enumDescriptorProto);
                } else if (enumDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(enumDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addEnumTypeBuilder() {
                return (Builder) getEnumTypeFieldBuilder().addBuilder(EnumDescriptorProto.getDefaultInstance());
            }

            public Builder addEnumTypeBuilder(int i) {
                return (Builder) getEnumTypeFieldBuilder().addBuilder(i, EnumDescriptorProto.getDefaultInstance());
            }

            public Builder addExtension(int i, Builder builder) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.add(i, builder.build());
                    onChanged();
                } else {
                    this.extensionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addExtension(int i, FieldDescriptorProto fieldDescriptorProto) {
                if (this.extensionBuilder_ != null) {
                    this.extensionBuilder_.addMessage(i, fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionIsMutable();
                    this.extension_.add(i, fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addExtension(Builder builder) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.add(builder.build());
                    onChanged();
                } else {
                    this.extensionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addExtension(FieldDescriptorProto fieldDescriptorProto) {
                if (this.extensionBuilder_ != null) {
                    this.extensionBuilder_.addMessage(fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionIsMutable();
                    this.extension_.add(fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addExtensionBuilder() {
                return (Builder) getExtensionFieldBuilder().addBuilder(FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addExtensionBuilder(int i) {
                return (Builder) getExtensionFieldBuilder().addBuilder(i, FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addExtensionRange(int i, Builder builder) {
                if (this.extensionRangeBuilder_ == null) {
                    ensureExtensionRangeIsMutable();
                    this.extensionRange_.add(i, builder.build());
                    onChanged();
                } else {
                    this.extensionRangeBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addExtensionRange(int i, ExtensionRange extensionRange) {
                if (this.extensionRangeBuilder_ != null) {
                    this.extensionRangeBuilder_.addMessage(i, extensionRange);
                } else if (extensionRange == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionRangeIsMutable();
                    this.extensionRange_.add(i, extensionRange);
                    onChanged();
                }
                return this;
            }

            public Builder addExtensionRange(Builder builder) {
                if (this.extensionRangeBuilder_ == null) {
                    ensureExtensionRangeIsMutable();
                    this.extensionRange_.add(builder.build());
                    onChanged();
                } else {
                    this.extensionRangeBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addExtensionRange(ExtensionRange extensionRange) {
                if (this.extensionRangeBuilder_ != null) {
                    this.extensionRangeBuilder_.addMessage(extensionRange);
                } else if (extensionRange == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionRangeIsMutable();
                    this.extensionRange_.add(extensionRange);
                    onChanged();
                }
                return this;
            }

            public Builder addExtensionRangeBuilder() {
                return (Builder) getExtensionRangeFieldBuilder().addBuilder(ExtensionRange.getDefaultInstance());
            }

            public Builder addExtensionRangeBuilder(int i) {
                return (Builder) getExtensionRangeFieldBuilder().addBuilder(i, ExtensionRange.getDefaultInstance());
            }

            public Builder addField(int i, Builder builder) {
                if (this.fieldBuilder_ == null) {
                    ensureFieldIsMutable();
                    this.field_.add(i, builder.build());
                    onChanged();
                } else {
                    this.fieldBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addField(int i, FieldDescriptorProto fieldDescriptorProto) {
                if (this.fieldBuilder_ != null) {
                    this.fieldBuilder_.addMessage(i, fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureFieldIsMutable();
                    this.field_.add(i, fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addField(Builder builder) {
                if (this.fieldBuilder_ == null) {
                    ensureFieldIsMutable();
                    this.field_.add(builder.build());
                    onChanged();
                } else {
                    this.fieldBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addField(FieldDescriptorProto fieldDescriptorProto) {
                if (this.fieldBuilder_ != null) {
                    this.fieldBuilder_.addMessage(fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureFieldIsMutable();
                    this.field_.add(fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addFieldBuilder() {
                return (Builder) getFieldFieldBuilder().addBuilder(FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addFieldBuilder(int i) {
                return (Builder) getFieldFieldBuilder().addBuilder(i, FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addNestedType(int i, Builder builder) {
                if (this.nestedTypeBuilder_ == null) {
                    ensureNestedTypeIsMutable();
                    this.nestedType_.add(i, builder.build());
                    onChanged();
                } else {
                    this.nestedTypeBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addNestedType(int i, DescriptorProto descriptorProto) {
                if (this.nestedTypeBuilder_ != null) {
                    this.nestedTypeBuilder_.addMessage(i, descriptorProto);
                } else if (descriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureNestedTypeIsMutable();
                    this.nestedType_.add(i, descriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addNestedType(Builder builder) {
                if (this.nestedTypeBuilder_ == null) {
                    ensureNestedTypeIsMutable();
                    this.nestedType_.add(builder.build());
                    onChanged();
                } else {
                    this.nestedTypeBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addNestedType(DescriptorProto descriptorProto) {
                if (this.nestedTypeBuilder_ != null) {
                    this.nestedTypeBuilder_.addMessage(descriptorProto);
                } else if (descriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureNestedTypeIsMutable();
                    this.nestedType_.add(descriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addNestedTypeBuilder() {
                return (Builder) getNestedTypeFieldBuilder().addBuilder(DescriptorProto.getDefaultInstance());
            }

            public Builder addNestedTypeBuilder(int i) {
                return (Builder) getNestedTypeFieldBuilder().addBuilder(i, DescriptorProto.getDefaultInstance());
            }

            public DescriptorProto build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public DescriptorProto buildPartial() {
                int i = 1;
                DescriptorProto descriptorProto = new DescriptorProto();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                descriptorProto.name_ = this.name_;
                if (this.fieldBuilder_ == null) {
                    if ((this.bitField0_ & 2) == 2) {
                        this.field_ = Collections.unmodifiableList(this.field_);
                        this.bitField0_ &= -3;
                    }
                    descriptorProto.field_ = this.field_;
                } else {
                    descriptorProto.field_ = this.fieldBuilder_.build();
                }
                if (this.extensionBuilder_ == null) {
                    if ((this.bitField0_ & 4) == 4) {
                        this.extension_ = Collections.unmodifiableList(this.extension_);
                        this.bitField0_ &= -5;
                    }
                    descriptorProto.extension_ = this.extension_;
                } else {
                    descriptorProto.extension_ = this.extensionBuilder_.build();
                }
                if (this.nestedTypeBuilder_ == null) {
                    if ((this.bitField0_ & 8) == 8) {
                        this.nestedType_ = Collections.unmodifiableList(this.nestedType_);
                        this.bitField0_ &= -9;
                    }
                    descriptorProto.nestedType_ = this.nestedType_;
                } else {
                    descriptorProto.nestedType_ = this.nestedTypeBuilder_.build();
                }
                if (this.enumTypeBuilder_ == null) {
                    if ((this.bitField0_ & 16) == 16) {
                        this.enumType_ = Collections.unmodifiableList(this.enumType_);
                        this.bitField0_ &= -17;
                    }
                    descriptorProto.enumType_ = this.enumType_;
                } else {
                    descriptorProto.enumType_ = this.enumTypeBuilder_.build();
                }
                if (this.extensionRangeBuilder_ == null) {
                    if ((this.bitField0_ & 32) == 32) {
                        this.extensionRange_ = Collections.unmodifiableList(this.extensionRange_);
                        this.bitField0_ &= -33;
                    }
                    descriptorProto.extensionRange_ = this.extensionRange_;
                } else {
                    descriptorProto.extensionRange_ = this.extensionRangeBuilder_.build();
                }
                int i3 = (i2 & 64) == 64 ? i | 2 : i;
                if (this.optionsBuilder_ == null) {
                    descriptorProto.options_ = this.options_;
                } else {
                    descriptorProto.options_ = (MessageOptions) this.optionsBuilder_.build();
                }
                descriptorProto.bitField0_ = i3;
                onBuilt();
                return descriptorProto;
            }

            public Builder clear() {
                super.clear();
                this.name_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                if (this.fieldBuilder_ == null) {
                    this.field_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.fieldBuilder_.clear();
                }
                if (this.extensionBuilder_ == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                } else {
                    this.extensionBuilder_.clear();
                }
                if (this.nestedTypeBuilder_ == null) {
                    this.nestedType_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                } else {
                    this.nestedTypeBuilder_.clear();
                }
                if (this.enumTypeBuilder_ == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                } else {
                    this.enumTypeBuilder_.clear();
                }
                if (this.extensionRangeBuilder_ == null) {
                    this.extensionRange_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                } else {
                    this.extensionRangeBuilder_.clear();
                }
                if (this.optionsBuilder_ == null) {
                    this.options_ = MessageOptions.getDefaultInstance();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -65;
                return this;
            }

            public Builder clearEnumType() {
                if (this.enumTypeBuilder_ == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                    onChanged();
                } else {
                    this.enumTypeBuilder_.clear();
                }
                return this;
            }

            public Builder clearExtension() {
                if (this.extensionBuilder_ == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    onChanged();
                } else {
                    this.extensionBuilder_.clear();
                }
                return this;
            }

            public Builder clearExtensionRange() {
                if (this.extensionRangeBuilder_ == null) {
                    this.extensionRange_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                    onChanged();
                } else {
                    this.extensionRangeBuilder_.clear();
                }
                return this;
            }

            public Builder clearField() {
                if (this.fieldBuilder_ == null) {
                    this.field_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.fieldBuilder_.clear();
                }
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = DescriptorProto.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder clearNestedType() {
                if (this.nestedTypeBuilder_ == null) {
                    this.nestedType_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                    onChanged();
                } else {
                    this.nestedTypeBuilder_.clear();
                }
                return this;
            }

            public Builder clearOptions() {
                if (this.optionsBuilder_ == null) {
                    this.options_ = MessageOptions.getDefaultInstance();
                    onChanged();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -65;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public DescriptorProto getDefaultInstanceForType() {
                return DescriptorProto.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return DescriptorProto.getDescriptor();
            }

            public EnumDescriptorProto getEnumType(int i) {
                return this.enumTypeBuilder_ == null ? (EnumDescriptorProto) this.enumType_.get(i) : (EnumDescriptorProto) this.enumTypeBuilder_.getMessage(i);
            }

            public Builder getEnumTypeBuilder(int i) {
                return (Builder) getEnumTypeFieldBuilder().getBuilder(i);
            }

            public List<Builder> getEnumTypeBuilderList() {
                return getEnumTypeFieldBuilder().getBuilderList();
            }

            public int getEnumTypeCount() {
                return this.enumTypeBuilder_ == null ? this.enumType_.size() : this.enumTypeBuilder_.getCount();
            }

            public List<EnumDescriptorProto> getEnumTypeList() {
                return this.enumTypeBuilder_ == null ? Collections.unmodifiableList(this.enumType_) : this.enumTypeBuilder_.getMessageList();
            }

            public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int i) {
                return this.enumTypeBuilder_ == null ? (EnumDescriptorProtoOrBuilder) this.enumType_.get(i) : (EnumDescriptorProtoOrBuilder) this.enumTypeBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
                return this.enumTypeBuilder_ != null ? this.enumTypeBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.enumType_);
            }

            public FieldDescriptorProto getExtension(int i) {
                return this.extensionBuilder_ == null ? (FieldDescriptorProto) this.extension_.get(i) : (FieldDescriptorProto) this.extensionBuilder_.getMessage(i);
            }

            public Builder getExtensionBuilder(int i) {
                return (Builder) getExtensionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getExtensionBuilderList() {
                return getExtensionFieldBuilder().getBuilderList();
            }

            public int getExtensionCount() {
                return this.extensionBuilder_ == null ? this.extension_.size() : this.extensionBuilder_.getCount();
            }

            public List<FieldDescriptorProto> getExtensionList() {
                return this.extensionBuilder_ == null ? Collections.unmodifiableList(this.extension_) : this.extensionBuilder_.getMessageList();
            }

            public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int i) {
                return this.extensionBuilder_ == null ? (FieldDescriptorProtoOrBuilder) this.extension_.get(i) : (FieldDescriptorProtoOrBuilder) this.extensionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
                return this.extensionBuilder_ != null ? this.extensionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.extension_);
            }

            public ExtensionRange getExtensionRange(int i) {
                return this.extensionRangeBuilder_ == null ? (ExtensionRange) this.extensionRange_.get(i) : (ExtensionRange) this.extensionRangeBuilder_.getMessage(i);
            }

            public Builder getExtensionRangeBuilder(int i) {
                return (Builder) getExtensionRangeFieldBuilder().getBuilder(i);
            }

            public List<Builder> getExtensionRangeBuilderList() {
                return getExtensionRangeFieldBuilder().getBuilderList();
            }

            public int getExtensionRangeCount() {
                return this.extensionRangeBuilder_ == null ? this.extensionRange_.size() : this.extensionRangeBuilder_.getCount();
            }

            public List<ExtensionRange> getExtensionRangeList() {
                return this.extensionRangeBuilder_ == null ? Collections.unmodifiableList(this.extensionRange_) : this.extensionRangeBuilder_.getMessageList();
            }

            public ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int i) {
                return this.extensionRangeBuilder_ == null ? (ExtensionRangeOrBuilder) this.extensionRange_.get(i) : (ExtensionRangeOrBuilder) this.extensionRangeBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends ExtensionRangeOrBuilder> getExtensionRangeOrBuilderList() {
                return this.extensionRangeBuilder_ != null ? this.extensionRangeBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.extensionRange_);
            }

            public FieldDescriptorProto getField(int i) {
                return this.fieldBuilder_ == null ? (FieldDescriptorProto) this.field_.get(i) : (FieldDescriptorProto) this.fieldBuilder_.getMessage(i);
            }

            public Builder getFieldBuilder(int i) {
                return (Builder) getFieldFieldBuilder().getBuilder(i);
            }

            public List<Builder> getFieldBuilderList() {
                return getFieldFieldBuilder().getBuilderList();
            }

            public int getFieldCount() {
                return this.fieldBuilder_ == null ? this.field_.size() : this.fieldBuilder_.getCount();
            }

            public List<FieldDescriptorProto> getFieldList() {
                return this.fieldBuilder_ == null ? Collections.unmodifiableList(this.field_) : this.fieldBuilder_.getMessageList();
            }

            public FieldDescriptorProtoOrBuilder getFieldOrBuilder(int i) {
                return this.fieldBuilder_ == null ? (FieldDescriptorProtoOrBuilder) this.field_.get(i) : (FieldDescriptorProtoOrBuilder) this.fieldBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends FieldDescriptorProtoOrBuilder> getFieldOrBuilderList() {
                return this.fieldBuilder_ != null ? this.fieldBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.field_);
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = toStringUtf8;
                return toStringUtf8;
            }

            public DescriptorProto getNestedType(int i) {
                return this.nestedTypeBuilder_ == null ? (DescriptorProto) this.nestedType_.get(i) : (DescriptorProto) this.nestedTypeBuilder_.getMessage(i);
            }

            public Builder getNestedTypeBuilder(int i) {
                return (Builder) getNestedTypeFieldBuilder().getBuilder(i);
            }

            public List<Builder> getNestedTypeBuilderList() {
                return getNestedTypeFieldBuilder().getBuilderList();
            }

            public int getNestedTypeCount() {
                return this.nestedTypeBuilder_ == null ? this.nestedType_.size() : this.nestedTypeBuilder_.getCount();
            }

            public List<DescriptorProto> getNestedTypeList() {
                return this.nestedTypeBuilder_ == null ? Collections.unmodifiableList(this.nestedType_) : this.nestedTypeBuilder_.getMessageList();
            }

            public DescriptorProtoOrBuilder getNestedTypeOrBuilder(int i) {
                return this.nestedTypeBuilder_ == null ? (DescriptorProtoOrBuilder) this.nestedType_.get(i) : (DescriptorProtoOrBuilder) this.nestedTypeBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends DescriptorProtoOrBuilder> getNestedTypeOrBuilderList() {
                return this.nestedTypeBuilder_ != null ? this.nestedTypeBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.nestedType_);
            }

            public MessageOptions getOptions() {
                return this.optionsBuilder_ == null ? this.options_ : (MessageOptions) this.optionsBuilder_.getMessage();
            }

            public Builder getOptionsBuilder() {
                this.bitField0_ |= 64;
                onChanged();
                return (Builder) getOptionsFieldBuilder().getBuilder();
            }

            public MessageOptionsOrBuilder getOptionsOrBuilder() {
                return this.optionsBuilder_ != null ? (MessageOptionsOrBuilder) this.optionsBuilder_.getMessageOrBuilder() : this.options_;
            }

            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasOptions() {
                return (this.bitField0_ & 64) == 64;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f278x907d0bf0;
            }

            public final boolean isInitialized() {
                int i;
                for (i = 0; i < getFieldCount(); i++) {
                    if (!getField(i).isInitialized()) {
                        return false;
                    }
                }
                for (i = 0; i < getExtensionCount(); i++) {
                    if (!getExtension(i).isInitialized()) {
                        return false;
                    }
                }
                for (i = 0; i < getNestedTypeCount(); i++) {
                    if (!getNestedType(i).isInitialized()) {
                        return false;
                    }
                }
                for (i = 0; i < getEnumTypeCount(); i++) {
                    if (!getEnumType(i).isInitialized()) {
                        return false;
                    }
                }
                return !hasOptions() || getOptions().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    Object newBuilder2;
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.name_ = codedInputStream.readBytes();
                            continue;
                        case 18:
                            newBuilder2 = FieldDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addField(newBuilder2.buildPartial());
                            continue;
                        case 26:
                            newBuilder2 = DescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addNestedType(newBuilder2.buildPartial());
                            continue;
                        case 34:
                            newBuilder2 = EnumDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addEnumType(newBuilder2.buildPartial());
                            continue;
                        case 42:
                            newBuilder2 = ExtensionRange.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addExtensionRange(newBuilder2.buildPartial());
                            continue;
                        case 50:
                            newBuilder2 = FieldDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addExtension(newBuilder2.buildPartial());
                            continue;
                        case 58:
                            newBuilder2 = MessageOptions.newBuilder();
                            if (hasOptions()) {
                                newBuilder2.mergeFrom(getOptions());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setOptions(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(DescriptorProto descriptorProto) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (descriptorProto != DescriptorProto.getDefaultInstance()) {
                    if (descriptorProto.hasName()) {
                        setName(descriptorProto.getName());
                    }
                    if (this.fieldBuilder_ == null) {
                        if (!descriptorProto.field_.isEmpty()) {
                            if (this.field_.isEmpty()) {
                                this.field_ = descriptorProto.field_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureFieldIsMutable();
                                this.field_.addAll(descriptorProto.field_);
                            }
                            onChanged();
                        }
                    } else if (!descriptorProto.field_.isEmpty()) {
                        if (this.fieldBuilder_.isEmpty()) {
                            this.fieldBuilder_.dispose();
                            this.fieldBuilder_ = null;
                            this.field_ = descriptorProto.field_;
                            this.bitField0_ &= -3;
                            this.fieldBuilder_ = GeneratedMessage.alwaysUseFieldBuilders ? getFieldFieldBuilder() : null;
                        } else {
                            this.fieldBuilder_.addAllMessages(descriptorProto.field_);
                        }
                    }
                    if (this.extensionBuilder_ == null) {
                        if (!descriptorProto.extension_.isEmpty()) {
                            if (this.extension_.isEmpty()) {
                                this.extension_ = descriptorProto.extension_;
                                this.bitField0_ &= -5;
                            } else {
                                ensureExtensionIsMutable();
                                this.extension_.addAll(descriptorProto.extension_);
                            }
                            onChanged();
                        }
                    } else if (!descriptorProto.extension_.isEmpty()) {
                        if (this.extensionBuilder_.isEmpty()) {
                            this.extensionBuilder_.dispose();
                            this.extensionBuilder_ = null;
                            this.extension_ = descriptorProto.extension_;
                            this.bitField0_ &= -5;
                            this.extensionBuilder_ = GeneratedMessage.alwaysUseFieldBuilders ? getExtensionFieldBuilder() : null;
                        } else {
                            this.extensionBuilder_.addAllMessages(descriptorProto.extension_);
                        }
                    }
                    if (this.nestedTypeBuilder_ == null) {
                        if (!descriptorProto.nestedType_.isEmpty()) {
                            if (this.nestedType_.isEmpty()) {
                                this.nestedType_ = descriptorProto.nestedType_;
                                this.bitField0_ &= -9;
                            } else {
                                ensureNestedTypeIsMutable();
                                this.nestedType_.addAll(descriptorProto.nestedType_);
                            }
                            onChanged();
                        }
                    } else if (!descriptorProto.nestedType_.isEmpty()) {
                        if (this.nestedTypeBuilder_.isEmpty()) {
                            this.nestedTypeBuilder_.dispose();
                            this.nestedTypeBuilder_ = null;
                            this.nestedType_ = descriptorProto.nestedType_;
                            this.bitField0_ &= -9;
                            this.nestedTypeBuilder_ = GeneratedMessage.alwaysUseFieldBuilders ? getNestedTypeFieldBuilder() : null;
                        } else {
                            this.nestedTypeBuilder_.addAllMessages(descriptorProto.nestedType_);
                        }
                    }
                    if (this.enumTypeBuilder_ == null) {
                        if (!descriptorProto.enumType_.isEmpty()) {
                            if (this.enumType_.isEmpty()) {
                                this.enumType_ = descriptorProto.enumType_;
                                this.bitField0_ &= -17;
                            } else {
                                ensureEnumTypeIsMutable();
                                this.enumType_.addAll(descriptorProto.enumType_);
                            }
                            onChanged();
                        }
                    } else if (!descriptorProto.enumType_.isEmpty()) {
                        if (this.enumTypeBuilder_.isEmpty()) {
                            this.enumTypeBuilder_.dispose();
                            this.enumTypeBuilder_ = null;
                            this.enumType_ = descriptorProto.enumType_;
                            this.bitField0_ &= -17;
                            this.enumTypeBuilder_ = GeneratedMessage.alwaysUseFieldBuilders ? getEnumTypeFieldBuilder() : null;
                        } else {
                            this.enumTypeBuilder_.addAllMessages(descriptorProto.enumType_);
                        }
                    }
                    if (this.extensionRangeBuilder_ == null) {
                        if (!descriptorProto.extensionRange_.isEmpty()) {
                            if (this.extensionRange_.isEmpty()) {
                                this.extensionRange_ = descriptorProto.extensionRange_;
                                this.bitField0_ &= -33;
                            } else {
                                ensureExtensionRangeIsMutable();
                                this.extensionRange_.addAll(descriptorProto.extensionRange_);
                            }
                            onChanged();
                        }
                    } else if (!descriptorProto.extensionRange_.isEmpty()) {
                        if (this.extensionRangeBuilder_.isEmpty()) {
                            this.extensionRangeBuilder_.dispose();
                            this.extensionRangeBuilder_ = null;
                            this.extensionRange_ = descriptorProto.extensionRange_;
                            this.bitField0_ &= -33;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getExtensionRangeFieldBuilder();
                            }
                            this.extensionRangeBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.extensionRangeBuilder_.addAllMessages(descriptorProto.extensionRange_);
                        }
                    }
                    if (descriptorProto.hasOptions()) {
                        mergeOptions(descriptorProto.getOptions());
                    }
                    mergeUnknownFields(descriptorProto.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof DescriptorProto) {
                    return mergeFrom((DescriptorProto) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(MessageOptions messageOptions) {
                if (this.optionsBuilder_ == null) {
                    if ((this.bitField0_ & 64) != 64 || this.options_ == MessageOptions.getDefaultInstance()) {
                        this.options_ = messageOptions;
                    } else {
                        this.options_ = MessageOptions.newBuilder(this.options_).mergeFrom(messageOptions).buildPartial();
                    }
                    onChanged();
                } else {
                    this.optionsBuilder_.mergeFrom(messageOptions);
                }
                this.bitField0_ |= 64;
                return this;
            }

            public Builder removeEnumType(int i) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.remove(i);
                    onChanged();
                } else {
                    this.enumTypeBuilder_.remove(i);
                }
                return this;
            }

            public Builder removeExtension(int i) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.remove(i);
                    onChanged();
                } else {
                    this.extensionBuilder_.remove(i);
                }
                return this;
            }

            public Builder removeExtensionRange(int i) {
                if (this.extensionRangeBuilder_ == null) {
                    ensureExtensionRangeIsMutable();
                    this.extensionRange_.remove(i);
                    onChanged();
                } else {
                    this.extensionRangeBuilder_.remove(i);
                }
                return this;
            }

            public Builder removeField(int i) {
                if (this.fieldBuilder_ == null) {
                    ensureFieldIsMutable();
                    this.field_.remove(i);
                    onChanged();
                } else {
                    this.fieldBuilder_.remove(i);
                }
                return this;
            }

            public Builder removeNestedType(int i) {
                if (this.nestedTypeBuilder_ == null) {
                    ensureNestedTypeIsMutable();
                    this.nestedType_.remove(i);
                    onChanged();
                } else {
                    this.nestedTypeBuilder_.remove(i);
                }
                return this;
            }

            public Builder setEnumType(int i, Builder builder) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.set(i, builder.build());
                    onChanged();
                } else {
                    this.enumTypeBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setEnumType(int i, EnumDescriptorProto enumDescriptorProto) {
                if (this.enumTypeBuilder_ != null) {
                    this.enumTypeBuilder_.setMessage(i, enumDescriptorProto);
                } else if (enumDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureEnumTypeIsMutable();
                    this.enumType_.set(i, enumDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setExtension(int i, Builder builder) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.set(i, builder.build());
                    onChanged();
                } else {
                    this.extensionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setExtension(int i, FieldDescriptorProto fieldDescriptorProto) {
                if (this.extensionBuilder_ != null) {
                    this.extensionBuilder_.setMessage(i, fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionIsMutable();
                    this.extension_.set(i, fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setExtensionRange(int i, Builder builder) {
                if (this.extensionRangeBuilder_ == null) {
                    ensureExtensionRangeIsMutable();
                    this.extensionRange_.set(i, builder.build());
                    onChanged();
                } else {
                    this.extensionRangeBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setExtensionRange(int i, ExtensionRange extensionRange) {
                if (this.extensionRangeBuilder_ != null) {
                    this.extensionRangeBuilder_.setMessage(i, extensionRange);
                } else if (extensionRange == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionRangeIsMutable();
                    this.extensionRange_.set(i, extensionRange);
                    onChanged();
                }
                return this;
            }

            public Builder setField(int i, Builder builder) {
                if (this.fieldBuilder_ == null) {
                    ensureFieldIsMutable();
                    this.field_.set(i, builder.build());
                    onChanged();
                } else {
                    this.fieldBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setField(int i, FieldDescriptorProto fieldDescriptorProto) {
                if (this.fieldBuilder_ != null) {
                    this.fieldBuilder_.setMessage(i, fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureFieldIsMutable();
                    this.field_.set(i, fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = str;
                onChanged();
                return this;
            }

            void setName(ByteString byteString) {
                this.bitField0_ |= 1;
                this.name_ = byteString;
                onChanged();
            }

            public Builder setNestedType(int i, Builder builder) {
                if (this.nestedTypeBuilder_ == null) {
                    ensureNestedTypeIsMutable();
                    this.nestedType_.set(i, builder.build());
                    onChanged();
                } else {
                    this.nestedTypeBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setNestedType(int i, DescriptorProto descriptorProto) {
                if (this.nestedTypeBuilder_ != null) {
                    this.nestedTypeBuilder_.setMessage(i, descriptorProto);
                } else if (descriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureNestedTypeIsMutable();
                    this.nestedType_.set(i, descriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setOptions(Builder builder) {
                if (this.optionsBuilder_ == null) {
                    this.options_ = builder.build();
                    onChanged();
                } else {
                    this.optionsBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 64;
                return this;
            }

            public Builder setOptions(MessageOptions messageOptions) {
                if (this.optionsBuilder_ != null) {
                    this.optionsBuilder_.setMessage(messageOptions);
                } else if (messageOptions == null) {
                    throw new NullPointerException();
                } else {
                    this.options_ = messageOptions;
                    onChanged();
                }
                this.bitField0_ |= 64;
                return this;
            }
        }

        public interface ExtensionRangeOrBuilder extends MessageOrBuilder {
            int getEnd();

            int getStart();

            boolean hasEnd();

            boolean hasStart();
        }

        public static final class ExtensionRange extends GeneratedMessage implements ExtensionRangeOrBuilder {
            public static final int END_FIELD_NUMBER = 2;
            public static final int START_FIELD_NUMBER = 1;
            private static final ExtensionRange defaultInstance = new ExtensionRange(true);
            private static final long serialVersionUID = 0;
            private int bitField0_;
            private int end_;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private int start_;

            public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements ExtensionRangeOrBuilder {
                private int bitField0_;
                private int end_;
                private int start_;

                private Builder() {
                    maybeForceBuilderInitialization();
                }

                private Builder(BuilderParent builderParent) {
                    super(builderParent);
                    maybeForceBuilderInitialization();
                }

                private ExtensionRange buildParsed() throws InvalidProtocolBufferException {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
                }

                private static Builder create() {
                    return new Builder();
                }

                public static final Descriptor getDescriptor() {
                    return DescriptorProtos.f276x1458f8d;
                }

                private void maybeForceBuilderInitialization() {
                    if (!GeneratedMessage.alwaysUseFieldBuilders) {
                    }
                }

                public ExtensionRange build() {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
                }

                public ExtensionRange buildPartial() {
                    int i = 1;
                    ExtensionRange extensionRange = new ExtensionRange();
                    int i2 = this.bitField0_;
                    if ((i2 & 1) != 1) {
                        i = 0;
                    }
                    extensionRange.start_ = this.start_;
                    if ((i2 & 2) == 2) {
                        i |= 2;
                    }
                    extensionRange.end_ = this.end_;
                    extensionRange.bitField0_ = i;
                    onBuilt();
                    return extensionRange;
                }

                public Builder clear() {
                    super.clear();
                    this.start_ = 0;
                    this.bitField0_ &= -2;
                    this.end_ = 0;
                    this.bitField0_ &= -3;
                    return this;
                }

                public Builder clearEnd() {
                    this.bitField0_ &= -3;
                    this.end_ = 0;
                    onChanged();
                    return this;
                }

                public Builder clearStart() {
                    this.bitField0_ &= -2;
                    this.start_ = 0;
                    onChanged();
                    return this;
                }

                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                public ExtensionRange getDefaultInstanceForType() {
                    return ExtensionRange.getDefaultInstance();
                }

                public Descriptor getDescriptorForType() {
                    return ExtensionRange.getDescriptor();
                }

                public int getEnd() {
                    return this.end_;
                }

                public int getStart() {
                    return this.start_;
                }

                public boolean hasEnd() {
                    return (this.bitField0_ & 2) == 2;
                }

                public boolean hasStart() {
                    return (this.bitField0_ & 1) == 1;
                }

                protected FieldAccessorTable internalGetFieldAccessorTable() {
                    return DescriptorProtos.f277xf366d90b;
                }

                public final boolean isInitialized() {
                    return true;
                }

                public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                    while (true) {
                        int readTag = codedInputStream.readTag();
                        switch (readTag) {
                            case 0:
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            case 8:
                                this.bitField0_ |= 1;
                                this.start_ = codedInputStream.readInt32();
                                continue;
                            case 16:
                                this.bitField0_ |= 2;
                                this.end_ = codedInputStream.readInt32();
                                continue;
                            default:
                                if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                    setUnknownFields(newBuilder.build());
                                    onChanged();
                                    break;
                                }
                                continue;
                        }
                        return this;
                    }
                }

                public Builder mergeFrom(ExtensionRange extensionRange) {
                    if (extensionRange != ExtensionRange.getDefaultInstance()) {
                        if (extensionRange.hasStart()) {
                            setStart(extensionRange.getStart());
                        }
                        if (extensionRange.hasEnd()) {
                            setEnd(extensionRange.getEnd());
                        }
                        mergeUnknownFields(extensionRange.getUnknownFields());
                    }
                    return this;
                }

                public Builder mergeFrom(Message message) {
                    if (message instanceof ExtensionRange) {
                        return mergeFrom((ExtensionRange) message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder setEnd(int i) {
                    this.bitField0_ |= 2;
                    this.end_ = i;
                    onChanged();
                    return this;
                }

                public Builder setStart(int i) {
                    this.bitField0_ |= 1;
                    this.start_ = i;
                    onChanged();
                    return this;
                }
            }

            static {
                defaultInstance.initFields();
            }

            private ExtensionRange(Builder builder) {
                super(builder);
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            private ExtensionRange(boolean z) {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            public static ExtensionRange getDefaultInstance() {
                return defaultInstance;
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.f276x1458f8d;
            }

            private void initFields() {
                this.start_ = 0;
                this.end_ = 0;
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public static Builder newBuilder(ExtensionRange extensionRange) {
                return newBuilder().mergeFrom(extensionRange);
            }

            public static ExtensionRange parseDelimitedFrom(InputStream inputStream) throws IOException {
                Builder newBuilder = newBuilder();
                return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
            }

            public static ExtensionRange parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Builder newBuilder = newBuilder();
                return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
            }

            public static ExtensionRange parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
            }

            public static ExtensionRange parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
            }

            public static ExtensionRange parseFrom(CodedInputStream codedInputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
            }

            public static ExtensionRange parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
            }

            public static ExtensionRange parseFrom(InputStream inputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
            }

            public static ExtensionRange parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
            }

            public static ExtensionRange parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
            }

            public static ExtensionRange parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
            }

            public ExtensionRange getDefaultInstanceForType() {
                return defaultInstance;
            }

            public int getEnd() {
                return this.end_;
            }

            public int getSerializedSize() {
                int i = this.memoizedSerializedSize;
                if (i != -1) {
                    return i;
                }
                i = 0;
                if ((this.bitField0_ & 1) == 1) {
                    i = CodedOutputStream.computeInt32Size(1, this.start_) + 0;
                }
                if ((this.bitField0_ & 2) == 2) {
                    i += CodedOutputStream.computeInt32Size(2, this.end_);
                }
                i += getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = i;
                return i;
            }

            public int getStart() {
                return this.start_;
            }

            public boolean hasEnd() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasStart() {
                return (this.bitField0_ & 1) == 1;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f277xf366d90b;
            }

            public final boolean isInitialized() {
                byte b = this.memoizedIsInitialized;
                if (b != (byte) -1) {
                    return b == (byte) 1;
                } else {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
            }

            public Builder newBuilderForType() {
                return newBuilder();
            }

            protected Builder newBuilderForType(BuilderParent builderParent) {
                return new Builder(builderParent);
            }

            public Builder toBuilder() {
                return newBuilder(this);
            }

            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                getSerializedSize();
                if ((this.bitField0_ & 1) == 1) {
                    codedOutputStream.writeInt32(1, this.start_);
                }
                if ((this.bitField0_ & 2) == 2) {
                    codedOutputStream.writeInt32(2, this.end_);
                }
                getUnknownFields().writeTo(codedOutputStream);
            }
        }

        static {
            defaultInstance.initFields();
        }

        private DescriptorProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private DescriptorProto(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static DescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = HttpVersions.HTTP_0_9;
            this.field_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.nestedType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.extensionRange_ = Collections.emptyList();
            this.options_ = MessageOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(DescriptorProto descriptorProto) {
            return newBuilder().mergeFrom(descriptorProto);
        }

        public static DescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static DescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static DescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static DescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static DescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static DescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static DescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static DescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static DescriptorProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static DescriptorProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public DescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public EnumDescriptorProto getEnumType(int i) {
            return (EnumDescriptorProto) this.enumType_.get(i);
        }

        public int getEnumTypeCount() {
            return this.enumType_.size();
        }

        public List<EnumDescriptorProto> getEnumTypeList() {
            return this.enumType_;
        }

        public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int i) {
            return (EnumDescriptorProtoOrBuilder) this.enumType_.get(i);
        }

        public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
            return this.enumType_;
        }

        public FieldDescriptorProto getExtension(int i) {
            return (FieldDescriptorProto) this.extension_.get(i);
        }

        public int getExtensionCount() {
            return this.extension_.size();
        }

        public List<FieldDescriptorProto> getExtensionList() {
            return this.extension_;
        }

        public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int i) {
            return (FieldDescriptorProtoOrBuilder) this.extension_.get(i);
        }

        public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
            return this.extension_;
        }

        public ExtensionRange getExtensionRange(int i) {
            return (ExtensionRange) this.extensionRange_.get(i);
        }

        public int getExtensionRangeCount() {
            return this.extensionRange_.size();
        }

        public List<ExtensionRange> getExtensionRangeList() {
            return this.extensionRange_;
        }

        public ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int i) {
            return (ExtensionRangeOrBuilder) this.extensionRange_.get(i);
        }

        public List<? extends ExtensionRangeOrBuilder> getExtensionRangeOrBuilderList() {
            return this.extensionRange_;
        }

        public FieldDescriptorProto getField(int i) {
            return (FieldDescriptorProto) this.field_.get(i);
        }

        public int getFieldCount() {
            return this.field_.size();
        }

        public List<FieldDescriptorProto> getFieldList() {
            return this.field_;
        }

        public FieldDescriptorProtoOrBuilder getFieldOrBuilder(int i) {
            return (FieldDescriptorProtoOrBuilder) this.field_.get(i);
        }

        public List<? extends FieldDescriptorProtoOrBuilder> getFieldOrBuilderList() {
            return this.field_;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public DescriptorProto getNestedType(int i) {
            return (DescriptorProto) this.nestedType_.get(i);
        }

        public int getNestedTypeCount() {
            return this.nestedType_.size();
        }

        public List<DescriptorProto> getNestedTypeList() {
            return this.nestedType_;
        }

        public DescriptorProtoOrBuilder getNestedTypeOrBuilder(int i) {
            return (DescriptorProtoOrBuilder) this.nestedType_.get(i);
        }

        public List<? extends DescriptorProtoOrBuilder> getNestedTypeOrBuilderList() {
            return this.nestedType_;
        }

        public MessageOptions getOptions() {
            return this.options_;
        }

        public MessageOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public int getSerializedSize() {
            int i = 0;
            int i2 = this.memoizedSerializedSize;
            if (i2 != -1) {
                return i2;
            }
            int i3;
            int computeBytesSize = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBytesSize(1, getNameBytes()) + 0 : 0;
            for (i3 = 0; i3 < this.field_.size(); i3++) {
                computeBytesSize += CodedOutputStream.computeMessageSize(2, (MessageLite) this.field_.get(i3));
            }
            for (i3 = 0; i3 < this.nestedType_.size(); i3++) {
                computeBytesSize += CodedOutputStream.computeMessageSize(3, (MessageLite) this.nestedType_.get(i3));
            }
            for (i3 = 0; i3 < this.enumType_.size(); i3++) {
                computeBytesSize += CodedOutputStream.computeMessageSize(4, (MessageLite) this.enumType_.get(i3));
            }
            for (i3 = 0; i3 < this.extensionRange_.size(); i3++) {
                computeBytesSize += CodedOutputStream.computeMessageSize(5, (MessageLite) this.extensionRange_.get(i3));
            }
            while (i < this.extension_.size()) {
                computeBytesSize += CodedOutputStream.computeMessageSize(6, (MessageLite) this.extension_.get(i));
                i++;
            }
            if ((this.bitField0_ & 2) == 2) {
                computeBytesSize += CodedOutputStream.computeMessageSize(7, this.options_);
            }
            i2 = getUnknownFields().getSerializedSize() + computeBytesSize;
            this.memoizedSerializedSize = i2;
            return i2;
        }

        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasOptions() {
            return (this.bitField0_ & 2) == 2;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f278x907d0bf0;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getFieldCount()) {
                    if (getField(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                i = 0;
                while (i < getExtensionCount()) {
                    if (getExtension(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                i = 0;
                while (i < getNestedTypeCount()) {
                    if (getNestedType(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                i = 0;
                while (i < getEnumTypeCount()) {
                    if (getEnumType(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (!hasOptions() || getOptions().isInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            int i;
            int i2 = 0;
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getNameBytes());
            }
            for (i = 0; i < this.field_.size(); i++) {
                codedOutputStream.writeMessage(2, (MessageLite) this.field_.get(i));
            }
            for (i = 0; i < this.nestedType_.size(); i++) {
                codedOutputStream.writeMessage(3, (MessageLite) this.nestedType_.get(i));
            }
            for (i = 0; i < this.enumType_.size(); i++) {
                codedOutputStream.writeMessage(4, (MessageLite) this.enumType_.get(i));
            }
            for (i = 0; i < this.extensionRange_.size(); i++) {
                codedOutputStream.writeMessage(5, (MessageLite) this.extensionRange_.get(i));
            }
            while (i2 < this.extension_.size()) {
                codedOutputStream.writeMessage(6, (MessageLite) this.extension_.get(i2));
                i2++;
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeMessage(7, this.options_);
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface EnumDescriptorProtoOrBuilder extends MessageOrBuilder {
        String getName();

        EnumOptions getOptions();

        EnumOptionsOrBuilder getOptionsOrBuilder();

        EnumValueDescriptorProto getValue(int i);

        int getValueCount();

        List<EnumValueDescriptorProto> getValueList();

        EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int i);

        List<? extends EnumValueDescriptorProtoOrBuilder> getValueOrBuilderList();

        boolean hasName();

        boolean hasOptions();
    }

    public static final class EnumDescriptorProto extends GeneratedMessage implements EnumDescriptorProtoOrBuilder {
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 3;
        public static final int VALUE_FIELD_NUMBER = 2;
        private static final EnumDescriptorProto defaultInstance = new EnumDescriptorProto(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private Object name_;
        private EnumOptions options_;
        private List<EnumValueDescriptorProto> value_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements EnumDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object name_;
            private SingleFieldBuilder<EnumOptions, Builder, EnumOptionsOrBuilder> optionsBuilder_;
            private EnumOptions options_;
            private RepeatedFieldBuilder<EnumValueDescriptorProto, Builder, EnumValueDescriptorProtoOrBuilder> valueBuilder_;
            private List<EnumValueDescriptorProto> value_;

            private Builder() {
                this.name_ = HttpVersions.HTTP_0_9;
                this.value_ = Collections.emptyList();
                this.options_ = EnumOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = HttpVersions.HTTP_0_9;
                this.value_ = Collections.emptyList();
                this.options_ = EnumOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private EnumDescriptorProto buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureValueIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.value_ = new ArrayList(this.value_);
                    this.bitField0_ |= 2;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
            }

            private SingleFieldBuilder<EnumOptions, Builder, EnumOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, getParentForChildren(), isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private RepeatedFieldBuilder<EnumValueDescriptorProto, Builder, EnumValueDescriptorProtoOrBuilder> getValueFieldBuilder() {
                if (this.valueBuilder_ == null) {
                    this.valueBuilder_ = new RepeatedFieldBuilder(this.value_, (this.bitField0_ & 2) == 2, getParentForChildren(), isClean());
                    this.value_ = null;
                }
                return this.valueBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getValueFieldBuilder();
                    getOptionsFieldBuilder();
                }
            }

            public Builder addAllValue(Iterable<? extends EnumValueDescriptorProto> iterable) {
                if (this.valueBuilder_ == null) {
                    ensureValueIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.value_);
                    onChanged();
                } else {
                    this.valueBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addValue(int i, Builder builder) {
                if (this.valueBuilder_ == null) {
                    ensureValueIsMutable();
                    this.value_.add(i, builder.build());
                    onChanged();
                } else {
                    this.valueBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addValue(int i, EnumValueDescriptorProto enumValueDescriptorProto) {
                if (this.valueBuilder_ != null) {
                    this.valueBuilder_.addMessage(i, enumValueDescriptorProto);
                } else if (enumValueDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureValueIsMutable();
                    this.value_.add(i, enumValueDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addValue(Builder builder) {
                if (this.valueBuilder_ == null) {
                    ensureValueIsMutable();
                    this.value_.add(builder.build());
                    onChanged();
                } else {
                    this.valueBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addValue(EnumValueDescriptorProto enumValueDescriptorProto) {
                if (this.valueBuilder_ != null) {
                    this.valueBuilder_.addMessage(enumValueDescriptorProto);
                } else if (enumValueDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureValueIsMutable();
                    this.value_.add(enumValueDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addValueBuilder() {
                return (Builder) getValueFieldBuilder().addBuilder(EnumValueDescriptorProto.getDefaultInstance());
            }

            public Builder addValueBuilder(int i) {
                return (Builder) getValueFieldBuilder().addBuilder(i, EnumValueDescriptorProto.getDefaultInstance());
            }

            public EnumDescriptorProto build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public EnumDescriptorProto buildPartial() {
                int i = 1;
                EnumDescriptorProto enumDescriptorProto = new EnumDescriptorProto();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                enumDescriptorProto.name_ = this.name_;
                if (this.valueBuilder_ == null) {
                    if ((this.bitField0_ & 2) == 2) {
                        this.value_ = Collections.unmodifiableList(this.value_);
                        this.bitField0_ &= -3;
                    }
                    enumDescriptorProto.value_ = this.value_;
                } else {
                    enumDescriptorProto.value_ = this.valueBuilder_.build();
                }
                int i3 = (i2 & 4) == 4 ? i | 2 : i;
                if (this.optionsBuilder_ == null) {
                    enumDescriptorProto.options_ = this.options_;
                } else {
                    enumDescriptorProto.options_ = (EnumOptions) this.optionsBuilder_.build();
                }
                enumDescriptorProto.bitField0_ = i3;
                onBuilt();
                return enumDescriptorProto;
            }

            public Builder clear() {
                super.clear();
                this.name_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                if (this.valueBuilder_ == null) {
                    this.value_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.valueBuilder_.clear();
                }
                if (this.optionsBuilder_ == null) {
                    this.options_ = EnumOptions.getDefaultInstance();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = EnumDescriptorProto.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder clearOptions() {
                if (this.optionsBuilder_ == null) {
                    this.options_ = EnumOptions.getDefaultInstance();
                    onChanged();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearValue() {
                if (this.valueBuilder_ == null) {
                    this.value_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.valueBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public EnumDescriptorProto getDefaultInstanceForType() {
                return EnumDescriptorProto.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return EnumDescriptorProto.getDescriptor();
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = toStringUtf8;
                return toStringUtf8;
            }

            public EnumOptions getOptions() {
                return this.optionsBuilder_ == null ? this.options_ : (EnumOptions) this.optionsBuilder_.getMessage();
            }

            public Builder getOptionsBuilder() {
                this.bitField0_ |= 4;
                onChanged();
                return (Builder) getOptionsFieldBuilder().getBuilder();
            }

            public EnumOptionsOrBuilder getOptionsOrBuilder() {
                return this.optionsBuilder_ != null ? (EnumOptionsOrBuilder) this.optionsBuilder_.getMessageOrBuilder() : this.options_;
            }

            public EnumValueDescriptorProto getValue(int i) {
                return this.valueBuilder_ == null ? (EnumValueDescriptorProto) this.value_.get(i) : (EnumValueDescriptorProto) this.valueBuilder_.getMessage(i);
            }

            public Builder getValueBuilder(int i) {
                return (Builder) getValueFieldBuilder().getBuilder(i);
            }

            public List<Builder> getValueBuilderList() {
                return getValueFieldBuilder().getBuilderList();
            }

            public int getValueCount() {
                return this.valueBuilder_ == null ? this.value_.size() : this.valueBuilder_.getCount();
            }

            public List<EnumValueDescriptorProto> getValueList() {
                return this.valueBuilder_ == null ? Collections.unmodifiableList(this.value_) : this.valueBuilder_.getMessageList();
            }

            public EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int i) {
                return this.valueBuilder_ == null ? (EnumValueDescriptorProtoOrBuilder) this.value_.get(i) : (EnumValueDescriptorProtoOrBuilder) this.valueBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends EnumValueDescriptorProtoOrBuilder> getValueOrBuilderList() {
                return this.valueBuilder_ != null ? this.valueBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.value_);
            }

            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasOptions() {
                return (this.bitField0_ & 4) == 4;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f279x9945f651;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getValueCount(); i++) {
                    if (!getValue(i).isInitialized()) {
                        return false;
                    }
                }
                return !hasOptions() || getOptions().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    Object newBuilder2;
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.name_ = codedInputStream.readBytes();
                            continue;
                        case 18:
                            newBuilder2 = EnumValueDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addValue(newBuilder2.buildPartial());
                            continue;
                        case 26:
                            newBuilder2 = EnumOptions.newBuilder();
                            if (hasOptions()) {
                                newBuilder2.mergeFrom(getOptions());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setOptions(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(EnumDescriptorProto enumDescriptorProto) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (enumDescriptorProto != EnumDescriptorProto.getDefaultInstance()) {
                    if (enumDescriptorProto.hasName()) {
                        setName(enumDescriptorProto.getName());
                    }
                    if (this.valueBuilder_ == null) {
                        if (!enumDescriptorProto.value_.isEmpty()) {
                            if (this.value_.isEmpty()) {
                                this.value_ = enumDescriptorProto.value_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureValueIsMutable();
                                this.value_.addAll(enumDescriptorProto.value_);
                            }
                            onChanged();
                        }
                    } else if (!enumDescriptorProto.value_.isEmpty()) {
                        if (this.valueBuilder_.isEmpty()) {
                            this.valueBuilder_.dispose();
                            this.valueBuilder_ = null;
                            this.value_ = enumDescriptorProto.value_;
                            this.bitField0_ &= -3;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getValueFieldBuilder();
                            }
                            this.valueBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.valueBuilder_.addAllMessages(enumDescriptorProto.value_);
                        }
                    }
                    if (enumDescriptorProto.hasOptions()) {
                        mergeOptions(enumDescriptorProto.getOptions());
                    }
                    mergeUnknownFields(enumDescriptorProto.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof EnumDescriptorProto) {
                    return mergeFrom((EnumDescriptorProto) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(EnumOptions enumOptions) {
                if (this.optionsBuilder_ == null) {
                    if ((this.bitField0_ & 4) != 4 || this.options_ == EnumOptions.getDefaultInstance()) {
                        this.options_ = enumOptions;
                    } else {
                        this.options_ = EnumOptions.newBuilder(this.options_).mergeFrom(enumOptions).buildPartial();
                    }
                    onChanged();
                } else {
                    this.optionsBuilder_.mergeFrom(enumOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder removeValue(int i) {
                if (this.valueBuilder_ == null) {
                    ensureValueIsMutable();
                    this.value_.remove(i);
                    onChanged();
                } else {
                    this.valueBuilder_.remove(i);
                }
                return this;
            }

            public Builder setName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = str;
                onChanged();
                return this;
            }

            void setName(ByteString byteString) {
                this.bitField0_ |= 1;
                this.name_ = byteString;
                onChanged();
            }

            public Builder setOptions(Builder builder) {
                if (this.optionsBuilder_ == null) {
                    this.options_ = builder.build();
                    onChanged();
                } else {
                    this.optionsBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setOptions(EnumOptions enumOptions) {
                if (this.optionsBuilder_ != null) {
                    this.optionsBuilder_.setMessage(enumOptions);
                } else if (enumOptions == null) {
                    throw new NullPointerException();
                } else {
                    this.options_ = enumOptions;
                    onChanged();
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setValue(int i, Builder builder) {
                if (this.valueBuilder_ == null) {
                    ensureValueIsMutable();
                    this.value_.set(i, builder.build());
                    onChanged();
                } else {
                    this.valueBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setValue(int i, EnumValueDescriptorProto enumValueDescriptorProto) {
                if (this.valueBuilder_ != null) {
                    this.valueBuilder_.setMessage(i, enumValueDescriptorProto);
                } else if (enumValueDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureValueIsMutable();
                    this.value_.set(i, enumValueDescriptorProto);
                    onChanged();
                }
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private EnumDescriptorProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private EnumDescriptorProto(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static EnumDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = HttpVersions.HTTP_0_9;
            this.value_ = Collections.emptyList();
            this.options_ = EnumOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumDescriptorProto enumDescriptorProto) {
            return newBuilder().mergeFrom(enumDescriptorProto);
        }

        public static EnumDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static EnumDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static EnumDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static EnumDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static EnumDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static EnumDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static EnumDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static EnumDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static EnumDescriptorProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static EnumDescriptorProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public EnumDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public EnumOptions getOptions() {
            return this.options_;
        }

        public EnumOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public int getSerializedSize() {
            int i = 0;
            int i2 = this.memoizedSerializedSize;
            if (i2 != -1) {
                return i2;
            }
            int computeBytesSize = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBytesSize(1, getNameBytes()) + 0 : 0;
            while (i < this.value_.size()) {
                i++;
                computeBytesSize = CodedOutputStream.computeMessageSize(2, (MessageLite) this.value_.get(i)) + computeBytesSize;
            }
            if ((this.bitField0_ & 2) == 2) {
                computeBytesSize += CodedOutputStream.computeMessageSize(3, this.options_);
            }
            i2 = getUnknownFields().getSerializedSize() + computeBytesSize;
            this.memoizedSerializedSize = i2;
            return i2;
        }

        public EnumValueDescriptorProto getValue(int i) {
            return (EnumValueDescriptorProto) this.value_.get(i);
        }

        public int getValueCount() {
            return this.value_.size();
        }

        public List<EnumValueDescriptorProto> getValueList() {
            return this.value_;
        }

        public EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int i) {
            return (EnumValueDescriptorProtoOrBuilder) this.value_.get(i);
        }

        public List<? extends EnumValueDescriptorProtoOrBuilder> getValueOrBuilderList() {
            return this.value_;
        }

        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasOptions() {
            return (this.bitField0_ & 2) == 2;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f279x9945f651;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getValueCount()) {
                    if (getValue(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (!hasOptions() || getOptions().isInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getNameBytes());
            }
            for (int i = 0; i < this.value_.size(); i++) {
                codedOutputStream.writeMessage(2, (MessageLite) this.value_.get(i));
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeMessage(3, this.options_);
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface EnumOptionsOrBuilder extends ExtendableMessageOrBuilder<EnumOptions> {
        UninterpretedOption getUninterpretedOption(int i);

        int getUninterpretedOptionCount();

        List<UninterpretedOption> getUninterpretedOptionList();

        UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i);

        List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();
    }

    public static final class EnumOptions extends ExtendableMessage<EnumOptions> implements EnumOptionsOrBuilder {
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final EnumOptions defaultInstance = new EnumOptions(true);
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<UninterpretedOption> uninterpretedOption_;

        public static final class Builder extends ExtendableBuilder<EnumOptions, Builder> implements EnumOptionsOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_;

            private Builder() {
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private EnumOptions buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                boolean z = true;
                if (this.uninterpretedOptionBuilder_ == null) {
                    List list = this.uninterpretedOption_;
                    if ((this.bitField0_ & 1) != 1) {
                        z = false;
                    }
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, z, getParentForChildren(), isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.uninterpretedOption_);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOption(Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOptionBuilder() {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public Builder addUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(i, UninterpretedOption.getDefaultInstance());
            }

            public EnumOptions build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public EnumOptions buildPartial() {
                EnumOptions enumOptions = new EnumOptions();
                int i = this.bitField0_;
                if (this.uninterpretedOptionBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -2;
                    }
                    enumOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    enumOptions.uninterpretedOption_ = this.uninterpretedOptionBuilder_.build();
                }
                onBuilt();
                return enumOptions;
            }

            public Builder clear() {
                super.clear();
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clearUninterpretedOption() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public EnumOptions getDefaultInstanceForType() {
                return EnumOptions.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return EnumOptions.getDescriptor();
            }

            public UninterpretedOption getUninterpretedOption(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOption) this.uninterpretedOption_.get(i) : (UninterpretedOption) this.uninterpretedOptionBuilder_.getMessage(i);
            }

            public Builder getUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getUninterpretedOptionBuilderList() {
                return getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            public int getUninterpretedOptionCount() {
                return this.uninterpretedOptionBuilder_ == null ? this.uninterpretedOption_.size() : this.uninterpretedOptionBuilder_.getCount();
            }

            public List<UninterpretedOption> getUninterpretedOptionList() {
                return this.uninterpretedOptionBuilder_ == null ? Collections.unmodifiableList(this.uninterpretedOption_) : this.uninterpretedOptionBuilder_.getMessageList();
            }

            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i) : (UninterpretedOptionOrBuilder) this.uninterpretedOptionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                return this.uninterpretedOptionBuilder_ != null ? this.uninterpretedOptionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getUninterpretedOptionCount(); i++) {
                    if (!getUninterpretedOption(i).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 7994:
                            Object newBuilder2 = UninterpretedOption.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addUninterpretedOption(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(EnumOptions enumOptions) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (enumOptions != EnumOptions.getDefaultInstance()) {
                    if (this.uninterpretedOptionBuilder_ == null) {
                        if (!enumOptions.uninterpretedOption_.isEmpty()) {
                            if (this.uninterpretedOption_.isEmpty()) {
                                this.uninterpretedOption_ = enumOptions.uninterpretedOption_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureUninterpretedOptionIsMutable();
                                this.uninterpretedOption_.addAll(enumOptions.uninterpretedOption_);
                            }
                            onChanged();
                        }
                    } else if (!enumOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOptionBuilder_.isEmpty()) {
                            this.uninterpretedOptionBuilder_.dispose();
                            this.uninterpretedOptionBuilder_ = null;
                            this.uninterpretedOption_ = enumOptions.uninterpretedOption_;
                            this.bitField0_ &= -2;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getUninterpretedOptionFieldBuilder();
                            }
                            this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.uninterpretedOptionBuilder_.addAllMessages(enumOptions.uninterpretedOption_);
                        }
                    }
                    mergeExtensionFields(enumOptions);
                    mergeUnknownFields(enumOptions.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof EnumOptions) {
                    return mergeFrom((EnumOptions) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int i) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(i);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.remove(i);
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.setMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private EnumOptions(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private EnumOptions(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static EnumOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
        }

        private void initFields() {
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumOptions enumOptions) {
            return newBuilder().mergeFrom(enumOptions);
        }

        public static EnumOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static EnumOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static EnumOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static EnumOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static EnumOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static EnumOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static EnumOptions parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static EnumOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static EnumOptions parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static EnumOptions parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public EnumOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeMessageSize;
            i = 0;
            int i2 = 0;
            while (i2 < this.uninterpretedOption_.size()) {
                computeMessageSize = CodedOutputStream.computeMessageSize(999, (MessageLite) this.uninterpretedOption_.get(i2)) + i;
                i2++;
                i = computeMessageSize;
            }
            computeMessageSize = (extensionsSerializedSize() + i) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = computeMessageSize;
            return computeMessageSize;
        }

        public UninterpretedOption getUninterpretedOption(int i) {
            return (UninterpretedOption) this.uninterpretedOption_.get(i);
        }

        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
            return (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i);
        }

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getUninterpretedOptionCount()) {
                    if (getUninterpretedOption(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            ExtensionWriter newExtensionWriter = newExtensionWriter();
            for (int i = 0; i < this.uninterpretedOption_.size(); i++) {
                codedOutputStream.writeMessage(999, (MessageLite) this.uninterpretedOption_.get(i));
            }
            newExtensionWriter.writeUntil(536870912, codedOutputStream);
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface EnumValueDescriptorProtoOrBuilder extends MessageOrBuilder {
        String getName();

        int getNumber();

        EnumValueOptions getOptions();

        EnumValueOptionsOrBuilder getOptionsOrBuilder();

        boolean hasName();

        boolean hasNumber();

        boolean hasOptions();
    }

    public static final class EnumValueDescriptorProto extends GeneratedMessage implements EnumValueDescriptorProtoOrBuilder {
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int NUMBER_FIELD_NUMBER = 2;
        public static final int OPTIONS_FIELD_NUMBER = 3;
        private static final EnumValueDescriptorProto defaultInstance = new EnumValueDescriptorProto(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private Object name_;
        private int number_;
        private EnumValueOptions options_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements EnumValueDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object name_;
            private int number_;
            private SingleFieldBuilder<EnumValueOptions, Builder, EnumValueOptionsOrBuilder> optionsBuilder_;
            private EnumValueOptions options_;

            private Builder() {
                this.name_ = HttpVersions.HTTP_0_9;
                this.options_ = EnumValueOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = HttpVersions.HTTP_0_9;
                this.options_ = EnumValueOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private EnumValueDescriptorProto buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.f280xf18031a8;
            }

            private SingleFieldBuilder<EnumValueOptions, Builder, EnumValueOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, getParentForChildren(), isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getOptionsFieldBuilder();
                }
            }

            public EnumValueDescriptorProto build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public EnumValueDescriptorProto buildPartial() {
                int i = 1;
                EnumValueDescriptorProto enumValueDescriptorProto = new EnumValueDescriptorProto();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                enumValueDescriptorProto.name_ = this.name_;
                if ((i2 & 2) == 2) {
                    i |= 2;
                }
                enumValueDescriptorProto.number_ = this.number_;
                int i3 = (i2 & 4) == 4 ? i | 4 : i;
                if (this.optionsBuilder_ == null) {
                    enumValueDescriptorProto.options_ = this.options_;
                } else {
                    enumValueDescriptorProto.options_ = (EnumValueOptions) this.optionsBuilder_.build();
                }
                enumValueDescriptorProto.bitField0_ = i3;
                onBuilt();
                return enumValueDescriptorProto;
            }

            public Builder clear() {
                super.clear();
                this.name_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                this.number_ = 0;
                this.bitField0_ &= -3;
                if (this.optionsBuilder_ == null) {
                    this.options_ = EnumValueOptions.getDefaultInstance();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = EnumValueDescriptorProto.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder clearNumber() {
                this.bitField0_ &= -3;
                this.number_ = 0;
                onChanged();
                return this;
            }

            public Builder clearOptions() {
                if (this.optionsBuilder_ == null) {
                    this.options_ = EnumValueOptions.getDefaultInstance();
                    onChanged();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public EnumValueDescriptorProto getDefaultInstanceForType() {
                return EnumValueDescriptorProto.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return EnumValueDescriptorProto.getDescriptor();
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = toStringUtf8;
                return toStringUtf8;
            }

            public int getNumber() {
                return this.number_;
            }

            public EnumValueOptions getOptions() {
                return this.optionsBuilder_ == null ? this.options_ : (EnumValueOptions) this.optionsBuilder_.getMessage();
            }

            public Builder getOptionsBuilder() {
                this.bitField0_ |= 4;
                onChanged();
                return (Builder) getOptionsFieldBuilder().getBuilder();
            }

            public EnumValueOptionsOrBuilder getOptionsOrBuilder() {
                return this.optionsBuilder_ != null ? (EnumValueOptionsOrBuilder) this.optionsBuilder_.getMessageOrBuilder() : this.options_;
            }

            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasNumber() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasOptions() {
                return (this.bitField0_ & 4) == 4;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f281xfb173026;
            }

            public final boolean isInitialized() {
                return !hasOptions() || getOptions().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.name_ = codedInputStream.readBytes();
                            continue;
                        case 16:
                            this.bitField0_ |= 2;
                            this.number_ = codedInputStream.readInt32();
                            continue;
                        case 26:
                            Object newBuilder2 = EnumValueOptions.newBuilder();
                            if (hasOptions()) {
                                newBuilder2.mergeFrom(getOptions());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setOptions(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(EnumValueDescriptorProto enumValueDescriptorProto) {
                if (enumValueDescriptorProto != EnumValueDescriptorProto.getDefaultInstance()) {
                    if (enumValueDescriptorProto.hasName()) {
                        setName(enumValueDescriptorProto.getName());
                    }
                    if (enumValueDescriptorProto.hasNumber()) {
                        setNumber(enumValueDescriptorProto.getNumber());
                    }
                    if (enumValueDescriptorProto.hasOptions()) {
                        mergeOptions(enumValueDescriptorProto.getOptions());
                    }
                    mergeUnknownFields(enumValueDescriptorProto.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof EnumValueDescriptorProto) {
                    return mergeFrom((EnumValueDescriptorProto) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(EnumValueOptions enumValueOptions) {
                if (this.optionsBuilder_ == null) {
                    if ((this.bitField0_ & 4) != 4 || this.options_ == EnumValueOptions.getDefaultInstance()) {
                        this.options_ = enumValueOptions;
                    } else {
                        this.options_ = EnumValueOptions.newBuilder(this.options_).mergeFrom(enumValueOptions).buildPartial();
                    }
                    onChanged();
                } else {
                    this.optionsBuilder_.mergeFrom(enumValueOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = str;
                onChanged();
                return this;
            }

            void setName(ByteString byteString) {
                this.bitField0_ |= 1;
                this.name_ = byteString;
                onChanged();
            }

            public Builder setNumber(int i) {
                this.bitField0_ |= 2;
                this.number_ = i;
                onChanged();
                return this;
            }

            public Builder setOptions(Builder builder) {
                if (this.optionsBuilder_ == null) {
                    this.options_ = builder.build();
                    onChanged();
                } else {
                    this.optionsBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setOptions(EnumValueOptions enumValueOptions) {
                if (this.optionsBuilder_ != null) {
                    this.optionsBuilder_.setMessage(enumValueOptions);
                } else if (enumValueOptions == null) {
                    throw new NullPointerException();
                } else {
                    this.options_ = enumValueOptions;
                    onChanged();
                }
                this.bitField0_ |= 4;
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private EnumValueDescriptorProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private EnumValueDescriptorProto(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static EnumValueDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.f280xf18031a8;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = HttpVersions.HTTP_0_9;
            this.number_ = 0;
            this.options_ = EnumValueOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumValueDescriptorProto enumValueDescriptorProto) {
            return newBuilder().mergeFrom(enumValueDescriptorProto);
        }

        public static EnumValueDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static EnumValueDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static EnumValueDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static EnumValueDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static EnumValueDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static EnumValueDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static EnumValueDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static EnumValueDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static EnumValueDescriptorProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static EnumValueDescriptorProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public EnumValueDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public int getNumber() {
            return this.number_;
        }

        public EnumValueOptions getOptions() {
            return this.options_;
        }

        public EnumValueOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            i = 0;
            if ((this.bitField0_ & 1) == 1) {
                i = CodedOutputStream.computeBytesSize(1, getNameBytes()) + 0;
            }
            if ((this.bitField0_ & 2) == 2) {
                i += CodedOutputStream.computeInt32Size(2, this.number_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i += CodedOutputStream.computeMessageSize(3, this.options_);
            }
            i += getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = i;
            return i;
        }

        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasNumber() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean hasOptions() {
            return (this.bitField0_ & 4) == 4;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f281xfb173026;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                if (!hasOptions() || getOptions().isInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeInt32(2, this.number_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeMessage(3, this.options_);
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface EnumValueOptionsOrBuilder extends ExtendableMessageOrBuilder<EnumValueOptions> {
        UninterpretedOption getUninterpretedOption(int i);

        int getUninterpretedOptionCount();

        List<UninterpretedOption> getUninterpretedOptionList();

        UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i);

        List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();
    }

    public static final class EnumValueOptions extends ExtendableMessage<EnumValueOptions> implements EnumValueOptionsOrBuilder {
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final EnumValueOptions defaultInstance = new EnumValueOptions(true);
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<UninterpretedOption> uninterpretedOption_;

        public static final class Builder extends ExtendableBuilder<EnumValueOptions, Builder> implements EnumValueOptionsOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_;

            private Builder() {
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private EnumValueOptions buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                boolean z = true;
                if (this.uninterpretedOptionBuilder_ == null) {
                    List list = this.uninterpretedOption_;
                    if ((this.bitField0_ & 1) != 1) {
                        z = false;
                    }
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, z, getParentForChildren(), isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.uninterpretedOption_);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOption(Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOptionBuilder() {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public Builder addUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(i, UninterpretedOption.getDefaultInstance());
            }

            public EnumValueOptions build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public EnumValueOptions buildPartial() {
                EnumValueOptions enumValueOptions = new EnumValueOptions();
                int i = this.bitField0_;
                if (this.uninterpretedOptionBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -2;
                    }
                    enumValueOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    enumValueOptions.uninterpretedOption_ = this.uninterpretedOptionBuilder_.build();
                }
                onBuilt();
                return enumValueOptions;
            }

            public Builder clear() {
                super.clear();
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clearUninterpretedOption() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public EnumValueOptions getDefaultInstanceForType() {
                return EnumValueOptions.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return EnumValueOptions.getDescriptor();
            }

            public UninterpretedOption getUninterpretedOption(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOption) this.uninterpretedOption_.get(i) : (UninterpretedOption) this.uninterpretedOptionBuilder_.getMessage(i);
            }

            public Builder getUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getUninterpretedOptionBuilderList() {
                return getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            public int getUninterpretedOptionCount() {
                return this.uninterpretedOptionBuilder_ == null ? this.uninterpretedOption_.size() : this.uninterpretedOptionBuilder_.getCount();
            }

            public List<UninterpretedOption> getUninterpretedOptionList() {
                return this.uninterpretedOptionBuilder_ == null ? Collections.unmodifiableList(this.uninterpretedOption_) : this.uninterpretedOptionBuilder_.getMessageList();
            }

            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i) : (UninterpretedOptionOrBuilder) this.uninterpretedOptionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                return this.uninterpretedOptionBuilder_ != null ? this.uninterpretedOptionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f282xdf65a421;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getUninterpretedOptionCount(); i++) {
                    if (!getUninterpretedOption(i).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 7994:
                            Object newBuilder2 = UninterpretedOption.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addUninterpretedOption(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(EnumValueOptions enumValueOptions) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (enumValueOptions != EnumValueOptions.getDefaultInstance()) {
                    if (this.uninterpretedOptionBuilder_ == null) {
                        if (!enumValueOptions.uninterpretedOption_.isEmpty()) {
                            if (this.uninterpretedOption_.isEmpty()) {
                                this.uninterpretedOption_ = enumValueOptions.uninterpretedOption_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureUninterpretedOptionIsMutable();
                                this.uninterpretedOption_.addAll(enumValueOptions.uninterpretedOption_);
                            }
                            onChanged();
                        }
                    } else if (!enumValueOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOptionBuilder_.isEmpty()) {
                            this.uninterpretedOptionBuilder_.dispose();
                            this.uninterpretedOptionBuilder_ = null;
                            this.uninterpretedOption_ = enumValueOptions.uninterpretedOption_;
                            this.bitField0_ &= -2;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getUninterpretedOptionFieldBuilder();
                            }
                            this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.uninterpretedOptionBuilder_.addAllMessages(enumValueOptions.uninterpretedOption_);
                        }
                    }
                    mergeExtensionFields(enumValueOptions);
                    mergeUnknownFields(enumValueOptions.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof EnumValueOptions) {
                    return mergeFrom((EnumValueOptions) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int i) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(i);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.remove(i);
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.setMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private EnumValueOptions(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private EnumValueOptions(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static EnumValueOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
        }

        private void initFields() {
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumValueOptions enumValueOptions) {
            return newBuilder().mergeFrom(enumValueOptions);
        }

        public static EnumValueOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static EnumValueOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static EnumValueOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static EnumValueOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static EnumValueOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static EnumValueOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static EnumValueOptions parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static EnumValueOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static EnumValueOptions parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static EnumValueOptions parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public EnumValueOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeMessageSize;
            i = 0;
            int i2 = 0;
            while (i2 < this.uninterpretedOption_.size()) {
                computeMessageSize = CodedOutputStream.computeMessageSize(999, (MessageLite) this.uninterpretedOption_.get(i2)) + i;
                i2++;
                i = computeMessageSize;
            }
            computeMessageSize = (extensionsSerializedSize() + i) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = computeMessageSize;
            return computeMessageSize;
        }

        public UninterpretedOption getUninterpretedOption(int i) {
            return (UninterpretedOption) this.uninterpretedOption_.get(i);
        }

        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
            return (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i);
        }

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f282xdf65a421;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getUninterpretedOptionCount()) {
                    if (getUninterpretedOption(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            ExtensionWriter newExtensionWriter = newExtensionWriter();
            for (int i = 0; i < this.uninterpretedOption_.size(); i++) {
                codedOutputStream.writeMessage(999, (MessageLite) this.uninterpretedOption_.get(i));
            }
            newExtensionWriter.writeUntil(536870912, codedOutputStream);
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface FieldDescriptorProtoOrBuilder extends MessageOrBuilder {
        String getDefaultValue();

        String getExtendee();

        Label getLabel();

        String getName();

        int getNumber();

        FieldOptions getOptions();

        FieldOptionsOrBuilder getOptionsOrBuilder();

        Type getType();

        String getTypeName();

        boolean hasDefaultValue();

        boolean hasExtendee();

        boolean hasLabel();

        boolean hasName();

        boolean hasNumber();

        boolean hasOptions();

        boolean hasType();

        boolean hasTypeName();
    }

    public static final class FieldDescriptorProto extends GeneratedMessage implements FieldDescriptorProtoOrBuilder {
        public static final int DEFAULT_VALUE_FIELD_NUMBER = 7;
        public static final int EXTENDEE_FIELD_NUMBER = 2;
        public static final int LABEL_FIELD_NUMBER = 4;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int NUMBER_FIELD_NUMBER = 3;
        public static final int OPTIONS_FIELD_NUMBER = 8;
        public static final int TYPE_FIELD_NUMBER = 5;
        public static final int TYPE_NAME_FIELD_NUMBER = 6;
        private static final FieldDescriptorProto defaultInstance = new FieldDescriptorProto(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private Object defaultValue_;
        private Object extendee_;
        private Label label_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private Object name_;
        private int number_;
        private FieldOptions options_;
        private Object typeName_;
        private Type type_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements FieldDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object defaultValue_;
            private Object extendee_;
            private Label label_;
            private Object name_;
            private int number_;
            private SingleFieldBuilder<FieldOptions, Builder, FieldOptionsOrBuilder> optionsBuilder_;
            private FieldOptions options_;
            private Object typeName_;
            private Type type_;

            private Builder() {
                this.name_ = HttpVersions.HTTP_0_9;
                this.label_ = Label.LABEL_OPTIONAL;
                this.type_ = Type.TYPE_DOUBLE;
                this.typeName_ = HttpVersions.HTTP_0_9;
                this.extendee_ = HttpVersions.HTTP_0_9;
                this.defaultValue_ = HttpVersions.HTTP_0_9;
                this.options_ = FieldOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = HttpVersions.HTTP_0_9;
                this.label_ = Label.LABEL_OPTIONAL;
                this.type_ = Type.TYPE_DOUBLE;
                this.typeName_ = HttpVersions.HTTP_0_9;
                this.extendee_ = HttpVersions.HTTP_0_9;
                this.defaultValue_ = HttpVersions.HTTP_0_9;
                this.options_ = FieldOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private FieldDescriptorProto buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
            }

            private SingleFieldBuilder<FieldOptions, Builder, FieldOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, getParentForChildren(), isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getOptionsFieldBuilder();
                }
            }

            public FieldDescriptorProto build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public FieldDescriptorProto buildPartial() {
                int i = 1;
                FieldDescriptorProto fieldDescriptorProto = new FieldDescriptorProto();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                fieldDescriptorProto.name_ = this.name_;
                if ((i2 & 2) == 2) {
                    i |= 2;
                }
                fieldDescriptorProto.number_ = this.number_;
                if ((i2 & 4) == 4) {
                    i |= 4;
                }
                fieldDescriptorProto.label_ = this.label_;
                if ((i2 & 8) == 8) {
                    i |= 8;
                }
                fieldDescriptorProto.type_ = this.type_;
                if ((i2 & 16) == 16) {
                    i |= 16;
                }
                fieldDescriptorProto.typeName_ = this.typeName_;
                if ((i2 & 32) == 32) {
                    i |= 32;
                }
                fieldDescriptorProto.extendee_ = this.extendee_;
                if ((i2 & 64) == 64) {
                    i |= 64;
                }
                fieldDescriptorProto.defaultValue_ = this.defaultValue_;
                int i3 = (i2 & 128) == 128 ? i | 128 : i;
                if (this.optionsBuilder_ == null) {
                    fieldDescriptorProto.options_ = this.options_;
                } else {
                    fieldDescriptorProto.options_ = (FieldOptions) this.optionsBuilder_.build();
                }
                fieldDescriptorProto.bitField0_ = i3;
                onBuilt();
                return fieldDescriptorProto;
            }

            public Builder clear() {
                super.clear();
                this.name_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                this.number_ = 0;
                this.bitField0_ &= -3;
                this.label_ = Label.LABEL_OPTIONAL;
                this.bitField0_ &= -5;
                this.type_ = Type.TYPE_DOUBLE;
                this.bitField0_ &= -9;
                this.typeName_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -17;
                this.extendee_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -33;
                this.defaultValue_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -65;
                if (this.optionsBuilder_ == null) {
                    this.options_ = FieldOptions.getDefaultInstance();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -129;
                return this;
            }

            public Builder clearDefaultValue() {
                this.bitField0_ &= -65;
                this.defaultValue_ = FieldDescriptorProto.getDefaultInstance().getDefaultValue();
                onChanged();
                return this;
            }

            public Builder clearExtendee() {
                this.bitField0_ &= -33;
                this.extendee_ = FieldDescriptorProto.getDefaultInstance().getExtendee();
                onChanged();
                return this;
            }

            public Builder clearLabel() {
                this.bitField0_ &= -5;
                this.label_ = Label.LABEL_OPTIONAL;
                onChanged();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = FieldDescriptorProto.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder clearNumber() {
                this.bitField0_ &= -3;
                this.number_ = 0;
                onChanged();
                return this;
            }

            public Builder clearOptions() {
                if (this.optionsBuilder_ == null) {
                    this.options_ = FieldOptions.getDefaultInstance();
                    onChanged();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -129;
                return this;
            }

            public Builder clearType() {
                this.bitField0_ &= -9;
                this.type_ = Type.TYPE_DOUBLE;
                onChanged();
                return this;
            }

            public Builder clearTypeName() {
                this.bitField0_ &= -17;
                this.typeName_ = FieldDescriptorProto.getDefaultInstance().getTypeName();
                onChanged();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public FieldDescriptorProto getDefaultInstanceForType() {
                return FieldDescriptorProto.getDefaultInstance();
            }

            public String getDefaultValue() {
                Object obj = this.defaultValue_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.defaultValue_ = toStringUtf8;
                return toStringUtf8;
            }

            public Descriptor getDescriptorForType() {
                return FieldDescriptorProto.getDescriptor();
            }

            public String getExtendee() {
                Object obj = this.extendee_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.extendee_ = toStringUtf8;
                return toStringUtf8;
            }

            public Label getLabel() {
                return this.label_;
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = toStringUtf8;
                return toStringUtf8;
            }

            public int getNumber() {
                return this.number_;
            }

            public FieldOptions getOptions() {
                return this.optionsBuilder_ == null ? this.options_ : (FieldOptions) this.optionsBuilder_.getMessage();
            }

            public Builder getOptionsBuilder() {
                this.bitField0_ |= 128;
                onChanged();
                return (Builder) getOptionsFieldBuilder().getBuilder();
            }

            public FieldOptionsOrBuilder getOptionsOrBuilder() {
                return this.optionsBuilder_ != null ? (FieldOptionsOrBuilder) this.optionsBuilder_.getMessageOrBuilder() : this.options_;
            }

            public Type getType() {
                return this.type_;
            }

            public String getTypeName() {
                Object obj = this.typeName_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.typeName_ = toStringUtf8;
                return toStringUtf8;
            }

            public boolean hasDefaultValue() {
                return (this.bitField0_ & 64) == 64;
            }

            public boolean hasExtendee() {
                return (this.bitField0_ & 32) == 32;
            }

            public boolean hasLabel() {
                return (this.bitField0_ & 4) == 4;
            }

            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasNumber() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasOptions() {
                return (this.bitField0_ & 128) == 128;
            }

            public boolean hasType() {
                return (this.bitField0_ & 8) == 8;
            }

            public boolean hasTypeName() {
                return (this.bitField0_ & 16) == 16;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f283x4734b330;
            }

            public final boolean isInitialized() {
                return !hasOptions() || getOptions().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.name_ = codedInputStream.readBytes();
                            continue;
                        case 18:
                            this.bitField0_ |= 32;
                            this.extendee_ = codedInputStream.readBytes();
                            continue;
                        case 24:
                            this.bitField0_ |= 2;
                            this.number_ = codedInputStream.readInt32();
                            continue;
                        case 32:
                            readTag = codedInputStream.readEnum();
                            Label valueOf = Label.valueOf(readTag);
                            if (valueOf != null) {
                                this.bitField0_ |= 4;
                                this.label_ = valueOf;
                                break;
                            }
                            newBuilder.mergeVarintField(4, readTag);
                            continue;
                        case 40:
                            readTag = codedInputStream.readEnum();
                            Type valueOf2 = Type.valueOf(readTag);
                            if (valueOf2 != null) {
                                this.bitField0_ |= 8;
                                this.type_ = valueOf2;
                                break;
                            }
                            newBuilder.mergeVarintField(5, readTag);
                            continue;
                        case 50:
                            this.bitField0_ |= 16;
                            this.typeName_ = codedInputStream.readBytes();
                            continue;
                        case 58:
                            this.bitField0_ |= 64;
                            this.defaultValue_ = codedInputStream.readBytes();
                            continue;
                        case 66:
                            Object newBuilder2 = FieldOptions.newBuilder();
                            if (hasOptions()) {
                                newBuilder2.mergeFrom(getOptions());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setOptions(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(FieldDescriptorProto fieldDescriptorProto) {
                if (fieldDescriptorProto != FieldDescriptorProto.getDefaultInstance()) {
                    if (fieldDescriptorProto.hasName()) {
                        setName(fieldDescriptorProto.getName());
                    }
                    if (fieldDescriptorProto.hasNumber()) {
                        setNumber(fieldDescriptorProto.getNumber());
                    }
                    if (fieldDescriptorProto.hasLabel()) {
                        setLabel(fieldDescriptorProto.getLabel());
                    }
                    if (fieldDescriptorProto.hasType()) {
                        setType(fieldDescriptorProto.getType());
                    }
                    if (fieldDescriptorProto.hasTypeName()) {
                        setTypeName(fieldDescriptorProto.getTypeName());
                    }
                    if (fieldDescriptorProto.hasExtendee()) {
                        setExtendee(fieldDescriptorProto.getExtendee());
                    }
                    if (fieldDescriptorProto.hasDefaultValue()) {
                        setDefaultValue(fieldDescriptorProto.getDefaultValue());
                    }
                    if (fieldDescriptorProto.hasOptions()) {
                        mergeOptions(fieldDescriptorProto.getOptions());
                    }
                    mergeUnknownFields(fieldDescriptorProto.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof FieldDescriptorProto) {
                    return mergeFrom((FieldDescriptorProto) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(FieldOptions fieldOptions) {
                if (this.optionsBuilder_ == null) {
                    if ((this.bitField0_ & 128) != 128 || this.options_ == FieldOptions.getDefaultInstance()) {
                        this.options_ = fieldOptions;
                    } else {
                        this.options_ = FieldOptions.newBuilder(this.options_).mergeFrom(fieldOptions).buildPartial();
                    }
                    onChanged();
                } else {
                    this.optionsBuilder_.mergeFrom(fieldOptions);
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder setDefaultValue(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 64;
                this.defaultValue_ = str;
                onChanged();
                return this;
            }

            void setDefaultValue(ByteString byteString) {
                this.bitField0_ |= 64;
                this.defaultValue_ = byteString;
                onChanged();
            }

            public Builder setExtendee(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 32;
                this.extendee_ = str;
                onChanged();
                return this;
            }

            void setExtendee(ByteString byteString) {
                this.bitField0_ |= 32;
                this.extendee_ = byteString;
                onChanged();
            }

            public Builder setLabel(Label label) {
                if (label == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.label_ = label;
                onChanged();
                return this;
            }

            public Builder setName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = str;
                onChanged();
                return this;
            }

            void setName(ByteString byteString) {
                this.bitField0_ |= 1;
                this.name_ = byteString;
                onChanged();
            }

            public Builder setNumber(int i) {
                this.bitField0_ |= 2;
                this.number_ = i;
                onChanged();
                return this;
            }

            public Builder setOptions(Builder builder) {
                if (this.optionsBuilder_ == null) {
                    this.options_ = builder.build();
                    onChanged();
                } else {
                    this.optionsBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder setOptions(FieldOptions fieldOptions) {
                if (this.optionsBuilder_ != null) {
                    this.optionsBuilder_.setMessage(fieldOptions);
                } else if (fieldOptions == null) {
                    throw new NullPointerException();
                } else {
                    this.options_ = fieldOptions;
                    onChanged();
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder setType(Type type) {
                if (type == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.type_ = type;
                onChanged();
                return this;
            }

            public Builder setTypeName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 16;
                this.typeName_ = str;
                onChanged();
                return this;
            }

            void setTypeName(ByteString byteString) {
                this.bitField0_ |= 16;
                this.typeName_ = byteString;
                onChanged();
            }
        }

        public enum Label implements ProtocolMessageEnum {
            LABEL_OPTIONAL(0, 1),
            LABEL_REQUIRED(1, 2),
            LABEL_REPEATED(2, 3);
            
            public static final int LABEL_OPTIONAL_VALUE = 1;
            public static final int LABEL_REPEATED_VALUE = 3;
            public static final int LABEL_REQUIRED_VALUE = 2;
            private static final Label[] VALUES = null;
            private static EnumLiteMap<Label> internalValueMap;
            private final int index;
            private final int value;

            static final class C09411 implements EnumLiteMap<Label> {
                C09411() {
                }

                public Label findValueByNumber(int i) {
                    return Label.valueOf(i);
                }
            }

            static {
                internalValueMap = new C09411();
                VALUES = new Label[]{LABEL_OPTIONAL, LABEL_REQUIRED, LABEL_REPEATED};
            }

            private Label(int i, int i2) {
                this.index = i;
                this.value = i2;
            }

            public static final EnumDescriptor getDescriptor() {
                return (EnumDescriptor) FieldDescriptorProto.getDescriptor().getEnumTypes().get(1);
            }

            public static EnumLiteMap<Label> internalGetValueMap() {
                return internalValueMap;
            }

            public static Label valueOf(int i) {
                switch (i) {
                    case 1:
                        return LABEL_OPTIONAL;
                    case 2:
                        return LABEL_REQUIRED;
                    case 3:
                        return LABEL_REPEATED;
                    default:
                        return null;
                }
            }

            public static Label valueOf(EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public final EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public final int getNumber() {
                return this.value;
            }

            public final EnumValueDescriptor getValueDescriptor() {
                return (EnumValueDescriptor) getDescriptor().getValues().get(this.index);
            }
        }

        public enum Type implements ProtocolMessageEnum {
            TYPE_DOUBLE(0, 1),
            TYPE_FLOAT(1, 2),
            TYPE_INT64(2, 3),
            TYPE_UINT64(3, 4),
            TYPE_INT32(4, 5),
            TYPE_FIXED64(5, 6),
            TYPE_FIXED32(6, 7),
            TYPE_BOOL(7, 8),
            TYPE_STRING(8, 9),
            TYPE_GROUP(9, 10),
            TYPE_MESSAGE(10, 11),
            TYPE_BYTES(11, 12),
            TYPE_UINT32(12, 13),
            TYPE_ENUM(13, 14),
            TYPE_SFIXED32(14, 15),
            TYPE_SFIXED64(15, 16),
            TYPE_SINT32(16, 17),
            TYPE_SINT64(17, 18);
            
            public static final int TYPE_BOOL_VALUE = 8;
            public static final int TYPE_BYTES_VALUE = 12;
            public static final int TYPE_DOUBLE_VALUE = 1;
            public static final int TYPE_ENUM_VALUE = 14;
            public static final int TYPE_FIXED32_VALUE = 7;
            public static final int TYPE_FIXED64_VALUE = 6;
            public static final int TYPE_FLOAT_VALUE = 2;
            public static final int TYPE_GROUP_VALUE = 10;
            public static final int TYPE_INT32_VALUE = 5;
            public static final int TYPE_INT64_VALUE = 3;
            public static final int TYPE_MESSAGE_VALUE = 11;
            public static final int TYPE_SFIXED32_VALUE = 15;
            public static final int TYPE_SFIXED64_VALUE = 16;
            public static final int TYPE_SINT32_VALUE = 17;
            public static final int TYPE_SINT64_VALUE = 18;
            public static final int TYPE_STRING_VALUE = 9;
            public static final int TYPE_UINT32_VALUE = 13;
            public static final int TYPE_UINT64_VALUE = 4;
            private static final Type[] VALUES = null;
            private static EnumLiteMap<Type> internalValueMap;
            private final int index;
            private final int value;

            static final class C09421 implements EnumLiteMap<Type> {
                C09421() {
                }

                public Type findValueByNumber(int i) {
                    return Type.valueOf(i);
                }
            }

            static {
                internalValueMap = new C09421();
                VALUES = new Type[]{TYPE_DOUBLE, TYPE_FLOAT, TYPE_INT64, TYPE_UINT64, TYPE_INT32, TYPE_FIXED64, TYPE_FIXED32, TYPE_BOOL, TYPE_STRING, TYPE_GROUP, TYPE_MESSAGE, TYPE_BYTES, TYPE_UINT32, TYPE_ENUM, TYPE_SFIXED32, TYPE_SFIXED64, TYPE_SINT32, TYPE_SINT64};
            }

            private Type(int i, int i2) {
                this.index = i;
                this.value = i2;
            }

            public static final EnumDescriptor getDescriptor() {
                return (EnumDescriptor) FieldDescriptorProto.getDescriptor().getEnumTypes().get(0);
            }

            public static EnumLiteMap<Type> internalGetValueMap() {
                return internalValueMap;
            }

            public static Type valueOf(int i) {
                switch (i) {
                    case 1:
                        return TYPE_DOUBLE;
                    case 2:
                        return TYPE_FLOAT;
                    case 3:
                        return TYPE_INT64;
                    case 4:
                        return TYPE_UINT64;
                    case 5:
                        return TYPE_INT32;
                    case 6:
                        return TYPE_FIXED64;
                    case 7:
                        return TYPE_FIXED32;
                    case 8:
                        return TYPE_BOOL;
                    case 9:
                        return TYPE_STRING;
                    case 10:
                        return TYPE_GROUP;
                    case 11:
                        return TYPE_MESSAGE;
                    case 12:
                        return TYPE_BYTES;
                    case 13:
                        return TYPE_UINT32;
                    case 14:
                        return TYPE_ENUM;
                    case 15:
                        return TYPE_SFIXED32;
                    case 16:
                        return TYPE_SFIXED64;
                    case 17:
                        return TYPE_SINT32;
                    case 18:
                        return TYPE_SINT64;
                    default:
                        return null;
                }
            }

            public static Type valueOf(EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public final EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public final int getNumber() {
                return this.value;
            }

            public final EnumValueDescriptor getValueDescriptor() {
                return (EnumValueDescriptor) getDescriptor().getValues().get(this.index);
            }
        }

        static {
            defaultInstance.initFields();
        }

        private FieldDescriptorProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private FieldDescriptorProto(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static FieldDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        private ByteString getDefaultValueBytes() {
            Object obj = this.defaultValue_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.defaultValue_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
        }

        private ByteString getExtendeeBytes() {
            Object obj = this.extendee_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.extendee_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private ByteString getTypeNameBytes() {
            Object obj = this.typeName_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.typeName_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = HttpVersions.HTTP_0_9;
            this.number_ = 0;
            this.label_ = Label.LABEL_OPTIONAL;
            this.type_ = Type.TYPE_DOUBLE;
            this.typeName_ = HttpVersions.HTTP_0_9;
            this.extendee_ = HttpVersions.HTTP_0_9;
            this.defaultValue_ = HttpVersions.HTTP_0_9;
            this.options_ = FieldOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FieldDescriptorProto fieldDescriptorProto) {
            return newBuilder().mergeFrom(fieldDescriptorProto);
        }

        public static FieldDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static FieldDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static FieldDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static FieldDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static FieldDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static FieldDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static FieldDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static FieldDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static FieldDescriptorProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static FieldDescriptorProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public FieldDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public String getDefaultValue() {
            Object obj = this.defaultValue_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.defaultValue_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public String getExtendee() {
            Object obj = this.extendee_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.extendee_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public Label getLabel() {
            return this.label_;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public int getNumber() {
            return this.number_;
        }

        public FieldOptions getOptions() {
            return this.options_;
        }

        public FieldOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            i = 0;
            if ((this.bitField0_ & 1) == 1) {
                i = CodedOutputStream.computeBytesSize(1, getNameBytes()) + 0;
            }
            if ((this.bitField0_ & 32) == 32) {
                i += CodedOutputStream.computeBytesSize(2, getExtendeeBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i += CodedOutputStream.computeInt32Size(3, this.number_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i += CodedOutputStream.computeEnumSize(4, this.label_.getNumber());
            }
            if ((this.bitField0_ & 8) == 8) {
                i += CodedOutputStream.computeEnumSize(5, this.type_.getNumber());
            }
            if ((this.bitField0_ & 16) == 16) {
                i += CodedOutputStream.computeBytesSize(6, getTypeNameBytes());
            }
            if ((this.bitField0_ & 64) == 64) {
                i += CodedOutputStream.computeBytesSize(7, getDefaultValueBytes());
            }
            if ((this.bitField0_ & 128) == 128) {
                i += CodedOutputStream.computeMessageSize(8, this.options_);
            }
            i += getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = i;
            return i;
        }

        public Type getType() {
            return this.type_;
        }

        public String getTypeName() {
            Object obj = this.typeName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.typeName_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public boolean hasDefaultValue() {
            return (this.bitField0_ & 64) == 64;
        }

        public boolean hasExtendee() {
            return (this.bitField0_ & 32) == 32;
        }

        public boolean hasLabel() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasNumber() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean hasOptions() {
            return (this.bitField0_ & 128) == 128;
        }

        public boolean hasType() {
            return (this.bitField0_ & 8) == 8;
        }

        public boolean hasTypeName() {
            return (this.bitField0_ & 16) == 16;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f283x4734b330;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                if (!hasOptions() || getOptions().isInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getNameBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeBytes(2, getExtendeeBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeInt32(3, this.number_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeEnum(4, this.label_.getNumber());
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeEnum(5, this.type_.getNumber());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(6, getTypeNameBytes());
            }
            if ((this.bitField0_ & 64) == 64) {
                codedOutputStream.writeBytes(7, getDefaultValueBytes());
            }
            if ((this.bitField0_ & 128) == 128) {
                codedOutputStream.writeMessage(8, this.options_);
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface FieldOptionsOrBuilder extends ExtendableMessageOrBuilder<FieldOptions> {
        CType getCtype();

        boolean getDeprecated();

        String getExperimentalMapKey();

        boolean getPacked();

        UninterpretedOption getUninterpretedOption(int i);

        int getUninterpretedOptionCount();

        List<UninterpretedOption> getUninterpretedOptionList();

        UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i);

        List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();

        boolean hasCtype();

        boolean hasDeprecated();

        boolean hasExperimentalMapKey();

        boolean hasPacked();
    }

    public static final class FieldOptions extends ExtendableMessage<FieldOptions> implements FieldOptionsOrBuilder {
        public static final int CTYPE_FIELD_NUMBER = 1;
        public static final int DEPRECATED_FIELD_NUMBER = 3;
        public static final int EXPERIMENTAL_MAP_KEY_FIELD_NUMBER = 9;
        public static final int PACKED_FIELD_NUMBER = 2;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final FieldOptions defaultInstance = new FieldOptions(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private CType ctype_;
        private boolean deprecated_;
        private Object experimentalMapKey_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private boolean packed_;
        private List<UninterpretedOption> uninterpretedOption_;

        public static final class Builder extends ExtendableBuilder<FieldOptions, Builder> implements FieldOptionsOrBuilder {
            private int bitField0_;
            private CType ctype_;
            private boolean deprecated_;
            private Object experimentalMapKey_;
            private boolean packed_;
            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_;

            private Builder() {
                this.ctype_ = CType.STRING;
                this.experimentalMapKey_ = HttpVersions.HTTP_0_9;
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.ctype_ = CType.STRING;
                this.experimentalMapKey_ = HttpVersions.HTTP_0_9;
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private FieldOptions buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
                    this.bitField0_ |= 16;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(this.uninterpretedOption_, (this.bitField0_ & 16) == 16, getParentForChildren(), isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.uninterpretedOption_);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOption(Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOptionBuilder() {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public Builder addUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(i, UninterpretedOption.getDefaultInstance());
            }

            public FieldOptions build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public FieldOptions buildPartial() {
                int i = 1;
                FieldOptions fieldOptions = new FieldOptions();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                fieldOptions.ctype_ = this.ctype_;
                if ((i2 & 2) == 2) {
                    i |= 2;
                }
                fieldOptions.packed_ = this.packed_;
                if ((i2 & 4) == 4) {
                    i |= 4;
                }
                fieldOptions.deprecated_ = this.deprecated_;
                if ((i2 & 8) == 8) {
                    i |= 8;
                }
                fieldOptions.experimentalMapKey_ = this.experimentalMapKey_;
                if (this.uninterpretedOptionBuilder_ == null) {
                    if ((this.bitField0_ & 16) == 16) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -17;
                    }
                    fieldOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    fieldOptions.uninterpretedOption_ = this.uninterpretedOptionBuilder_.build();
                }
                fieldOptions.bitField0_ = i;
                onBuilt();
                return fieldOptions;
            }

            public Builder clear() {
                super.clear();
                this.ctype_ = CType.STRING;
                this.bitField0_ &= -2;
                this.packed_ = false;
                this.bitField0_ &= -3;
                this.deprecated_ = false;
                this.bitField0_ &= -5;
                this.experimentalMapKey_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -9;
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clearCtype() {
                this.bitField0_ &= -2;
                this.ctype_ = CType.STRING;
                onChanged();
                return this;
            }

            public Builder clearDeprecated() {
                this.bitField0_ &= -5;
                this.deprecated_ = false;
                onChanged();
                return this;
            }

            public Builder clearExperimentalMapKey() {
                this.bitField0_ &= -9;
                this.experimentalMapKey_ = FieldOptions.getDefaultInstance().getExperimentalMapKey();
                onChanged();
                return this;
            }

            public Builder clearPacked() {
                this.bitField0_ &= -3;
                this.packed_ = false;
                onChanged();
                return this;
            }

            public Builder clearUninterpretedOption() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public CType getCtype() {
                return this.ctype_;
            }

            public FieldOptions getDefaultInstanceForType() {
                return FieldOptions.getDefaultInstance();
            }

            public boolean getDeprecated() {
                return this.deprecated_;
            }

            public Descriptor getDescriptorForType() {
                return FieldOptions.getDescriptor();
            }

            public String getExperimentalMapKey() {
                Object obj = this.experimentalMapKey_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.experimentalMapKey_ = toStringUtf8;
                return toStringUtf8;
            }

            public boolean getPacked() {
                return this.packed_;
            }

            public UninterpretedOption getUninterpretedOption(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOption) this.uninterpretedOption_.get(i) : (UninterpretedOption) this.uninterpretedOptionBuilder_.getMessage(i);
            }

            public Builder getUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getUninterpretedOptionBuilderList() {
                return getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            public int getUninterpretedOptionCount() {
                return this.uninterpretedOptionBuilder_ == null ? this.uninterpretedOption_.size() : this.uninterpretedOptionBuilder_.getCount();
            }

            public List<UninterpretedOption> getUninterpretedOptionList() {
                return this.uninterpretedOptionBuilder_ == null ? Collections.unmodifiableList(this.uninterpretedOption_) : this.uninterpretedOptionBuilder_.getMessageList();
            }

            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i) : (UninterpretedOptionOrBuilder) this.uninterpretedOptionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                return this.uninterpretedOptionBuilder_ != null ? this.uninterpretedOptionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
            }

            public boolean hasCtype() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasDeprecated() {
                return (this.bitField0_ & 4) == 4;
            }

            public boolean hasExperimentalMapKey() {
                return (this.bitField0_ & 8) == 8;
            }

            public boolean hasPacked() {
                return (this.bitField0_ & 2) == 2;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getUninterpretedOptionCount(); i++) {
                    if (!getUninterpretedOption(i).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 8:
                            readTag = codedInputStream.readEnum();
                            CType valueOf = CType.valueOf(readTag);
                            if (valueOf != null) {
                                this.bitField0_ |= 1;
                                this.ctype_ = valueOf;
                                break;
                            }
                            newBuilder.mergeVarintField(1, readTag);
                            continue;
                        case 16:
                            this.bitField0_ |= 2;
                            this.packed_ = codedInputStream.readBool();
                            continue;
                        case 24:
                            this.bitField0_ |= 4;
                            this.deprecated_ = codedInputStream.readBool();
                            continue;
                        case 74:
                            this.bitField0_ |= 8;
                            this.experimentalMapKey_ = codedInputStream.readBytes();
                            continue;
                        case 7994:
                            Object newBuilder2 = UninterpretedOption.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addUninterpretedOption(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(FieldOptions fieldOptions) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (fieldOptions != FieldOptions.getDefaultInstance()) {
                    if (fieldOptions.hasCtype()) {
                        setCtype(fieldOptions.getCtype());
                    }
                    if (fieldOptions.hasPacked()) {
                        setPacked(fieldOptions.getPacked());
                    }
                    if (fieldOptions.hasDeprecated()) {
                        setDeprecated(fieldOptions.getDeprecated());
                    }
                    if (fieldOptions.hasExperimentalMapKey()) {
                        setExperimentalMapKey(fieldOptions.getExperimentalMapKey());
                    }
                    if (this.uninterpretedOptionBuilder_ == null) {
                        if (!fieldOptions.uninterpretedOption_.isEmpty()) {
                            if (this.uninterpretedOption_.isEmpty()) {
                                this.uninterpretedOption_ = fieldOptions.uninterpretedOption_;
                                this.bitField0_ &= -17;
                            } else {
                                ensureUninterpretedOptionIsMutable();
                                this.uninterpretedOption_.addAll(fieldOptions.uninterpretedOption_);
                            }
                            onChanged();
                        }
                    } else if (!fieldOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOptionBuilder_.isEmpty()) {
                            this.uninterpretedOptionBuilder_.dispose();
                            this.uninterpretedOptionBuilder_ = null;
                            this.uninterpretedOption_ = fieldOptions.uninterpretedOption_;
                            this.bitField0_ &= -17;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getUninterpretedOptionFieldBuilder();
                            }
                            this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.uninterpretedOptionBuilder_.addAllMessages(fieldOptions.uninterpretedOption_);
                        }
                    }
                    mergeExtensionFields(fieldOptions);
                    mergeUnknownFields(fieldOptions.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof FieldOptions) {
                    return mergeFrom((FieldOptions) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int i) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(i);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.remove(i);
                }
                return this;
            }

            public Builder setCtype(CType cType) {
                if (cType == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.ctype_ = cType;
                onChanged();
                return this;
            }

            public Builder setDeprecated(boolean z) {
                this.bitField0_ |= 4;
                this.deprecated_ = z;
                onChanged();
                return this;
            }

            public Builder setExperimentalMapKey(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.experimentalMapKey_ = str;
                onChanged();
                return this;
            }

            void setExperimentalMapKey(ByteString byteString) {
                this.bitField0_ |= 8;
                this.experimentalMapKey_ = byteString;
                onChanged();
            }

            public Builder setPacked(boolean z) {
                this.bitField0_ |= 2;
                this.packed_ = z;
                onChanged();
                return this;
            }

            public Builder setUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.setMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }
        }

        public enum CType implements ProtocolMessageEnum {
            STRING(0, 0),
            CORD(1, 1),
            STRING_PIECE(2, 2);
            
            public static final int CORD_VALUE = 1;
            public static final int STRING_PIECE_VALUE = 2;
            public static final int STRING_VALUE = 0;
            private static final CType[] VALUES = null;
            private static EnumLiteMap<CType> internalValueMap;
            private final int index;
            private final int value;

            static final class C09431 implements EnumLiteMap<CType> {
                C09431() {
                }

                public CType findValueByNumber(int i) {
                    return CType.valueOf(i);
                }
            }

            static {
                internalValueMap = new C09431();
                VALUES = new CType[]{STRING, CORD, STRING_PIECE};
            }

            private CType(int i, int i2) {
                this.index = i;
                this.value = i2;
            }

            public static final EnumDescriptor getDescriptor() {
                return (EnumDescriptor) FieldOptions.getDescriptor().getEnumTypes().get(0);
            }

            public static EnumLiteMap<CType> internalGetValueMap() {
                return internalValueMap;
            }

            public static CType valueOf(int i) {
                switch (i) {
                    case 0:
                        return STRING;
                    case 1:
                        return CORD;
                    case 2:
                        return STRING_PIECE;
                    default:
                        return null;
                }
            }

            public static CType valueOf(EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public final EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public final int getNumber() {
                return this.value;
            }

            public final EnumValueDescriptor getValueDescriptor() {
                return (EnumValueDescriptor) getDescriptor().getValues().get(this.index);
            }
        }

        static {
            defaultInstance.initFields();
        }

        private FieldOptions(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private FieldOptions(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static FieldOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
        }

        private ByteString getExperimentalMapKeyBytes() {
            Object obj = this.experimentalMapKey_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.experimentalMapKey_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.ctype_ = CType.STRING;
            this.packed_ = false;
            this.deprecated_ = false;
            this.experimentalMapKey_ = HttpVersions.HTTP_0_9;
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FieldOptions fieldOptions) {
            return newBuilder().mergeFrom(fieldOptions);
        }

        public static FieldOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static FieldOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static FieldOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static FieldOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static FieldOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static FieldOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static FieldOptions parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static FieldOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static FieldOptions parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static FieldOptions parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public CType getCtype() {
            return this.ctype_;
        }

        public FieldOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean getDeprecated() {
            return this.deprecated_;
        }

        public String getExperimentalMapKey() {
            Object obj = this.experimentalMapKey_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.experimentalMapKey_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public boolean getPacked() {
            return this.packed_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            i = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeEnumSize(1, this.ctype_.getNumber()) + 0 : 0;
            if ((this.bitField0_ & 2) == 2) {
                i += CodedOutputStream.computeBoolSize(2, this.packed_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i += CodedOutputStream.computeBoolSize(3, this.deprecated_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i += CodedOutputStream.computeBytesSize(9, getExperimentalMapKeyBytes());
            }
            int i2 = 0;
            int i3 = i;
            while (i2 < this.uninterpretedOption_.size()) {
                i = CodedOutputStream.computeMessageSize(999, (MessageLite) this.uninterpretedOption_.get(i2)) + i3;
                i2++;
                i3 = i;
            }
            i = (extensionsSerializedSize() + i3) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = i;
            return i;
        }

        public UninterpretedOption getUninterpretedOption(int i) {
            return (UninterpretedOption) this.uninterpretedOption_.get(i);
        }

        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
            return (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i);
        }

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        public boolean hasCtype() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasDeprecated() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean hasExperimentalMapKey() {
            return (this.bitField0_ & 8) == 8;
        }

        public boolean hasPacked() {
            return (this.bitField0_ & 2) == 2;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getUninterpretedOptionCount()) {
                    if (getUninterpretedOption(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            ExtensionWriter newExtensionWriter = newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeEnum(1, this.ctype_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBool(2, this.packed_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(3, this.deprecated_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBytes(9, getExperimentalMapKeyBytes());
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); i++) {
                codedOutputStream.writeMessage(999, (MessageLite) this.uninterpretedOption_.get(i));
            }
            newExtensionWriter.writeUntil(536870912, codedOutputStream);
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface FileDescriptorProtoOrBuilder extends MessageOrBuilder {
        String getDependency(int i);

        int getDependencyCount();

        List<String> getDependencyList();

        EnumDescriptorProto getEnumType(int i);

        int getEnumTypeCount();

        List<EnumDescriptorProto> getEnumTypeList();

        EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int i);

        List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList();

        FieldDescriptorProto getExtension(int i);

        int getExtensionCount();

        List<FieldDescriptorProto> getExtensionList();

        FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int i);

        List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList();

        DescriptorProto getMessageType(int i);

        int getMessageTypeCount();

        List<DescriptorProto> getMessageTypeList();

        DescriptorProtoOrBuilder getMessageTypeOrBuilder(int i);

        List<? extends DescriptorProtoOrBuilder> getMessageTypeOrBuilderList();

        String getName();

        FileOptions getOptions();

        FileOptionsOrBuilder getOptionsOrBuilder();

        String getPackage();

        ServiceDescriptorProto getService(int i);

        int getServiceCount();

        List<ServiceDescriptorProto> getServiceList();

        ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int i);

        List<? extends ServiceDescriptorProtoOrBuilder> getServiceOrBuilderList();

        SourceCodeInfo getSourceCodeInfo();

        SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder();

        boolean hasName();

        boolean hasOptions();

        boolean hasPackage();

        boolean hasSourceCodeInfo();
    }

    public static final class FileDescriptorProto extends GeneratedMessage implements FileDescriptorProtoOrBuilder {
        public static final int DEPENDENCY_FIELD_NUMBER = 3;
        public static final int ENUM_TYPE_FIELD_NUMBER = 5;
        public static final int EXTENSION_FIELD_NUMBER = 7;
        public static final int MESSAGE_TYPE_FIELD_NUMBER = 4;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 8;
        public static final int PACKAGE_FIELD_NUMBER = 2;
        public static final int SERVICE_FIELD_NUMBER = 6;
        public static final int SOURCE_CODE_INFO_FIELD_NUMBER = 9;
        private static final FileDescriptorProto defaultInstance = new FileDescriptorProto(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private LazyStringList dependency_;
        private List<EnumDescriptorProto> enumType_;
        private List<FieldDescriptorProto> extension_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<DescriptorProto> messageType_;
        private Object name_;
        private FileOptions options_;
        private Object package_;
        private List<ServiceDescriptorProto> service_;
        private SourceCodeInfo sourceCodeInfo_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements FileDescriptorProtoOrBuilder {
            private int bitField0_;
            private LazyStringList dependency_;
            private RepeatedFieldBuilder<EnumDescriptorProto, Builder, EnumDescriptorProtoOrBuilder> enumTypeBuilder_;
            private List<EnumDescriptorProto> enumType_;
            private RepeatedFieldBuilder<FieldDescriptorProto, Builder, FieldDescriptorProtoOrBuilder> extensionBuilder_;
            private List<FieldDescriptorProto> extension_;
            private RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> messageTypeBuilder_;
            private List<DescriptorProto> messageType_;
            private Object name_;
            private SingleFieldBuilder<FileOptions, Builder, FileOptionsOrBuilder> optionsBuilder_;
            private FileOptions options_;
            private Object package_;
            private RepeatedFieldBuilder<ServiceDescriptorProto, Builder, ServiceDescriptorProtoOrBuilder> serviceBuilder_;
            private List<ServiceDescriptorProto> service_;
            private SingleFieldBuilder<SourceCodeInfo, Builder, SourceCodeInfoOrBuilder> sourceCodeInfoBuilder_;
            private SourceCodeInfo sourceCodeInfo_;

            private Builder() {
                this.name_ = HttpVersions.HTTP_0_9;
                this.package_ = HttpVersions.HTTP_0_9;
                this.dependency_ = LazyStringArrayList.EMPTY;
                this.messageType_ = Collections.emptyList();
                this.enumType_ = Collections.emptyList();
                this.service_ = Collections.emptyList();
                this.extension_ = Collections.emptyList();
                this.options_ = FileOptions.getDefaultInstance();
                this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = HttpVersions.HTTP_0_9;
                this.package_ = HttpVersions.HTTP_0_9;
                this.dependency_ = LazyStringArrayList.EMPTY;
                this.messageType_ = Collections.emptyList();
                this.enumType_ = Collections.emptyList();
                this.service_ = Collections.emptyList();
                this.extension_ = Collections.emptyList();
                this.options_ = FileOptions.getDefaultInstance();
                this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private FileDescriptorProto buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureDependencyIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.dependency_ = new LazyStringArrayList(this.dependency_);
                    this.bitField0_ |= 4;
                }
            }

            private void ensureEnumTypeIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.enumType_ = new ArrayList(this.enumType_);
                    this.bitField0_ |= 16;
                }
            }

            private void ensureExtensionIsMutable() {
                if ((this.bitField0_ & 64) != 64) {
                    this.extension_ = new ArrayList(this.extension_);
                    this.bitField0_ |= 64;
                }
            }

            private void ensureMessageTypeIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.messageType_ = new ArrayList(this.messageType_);
                    this.bitField0_ |= 8;
                }
            }

            private void ensureServiceIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.service_ = new ArrayList(this.service_);
                    this.bitField0_ |= 32;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
            }

            private RepeatedFieldBuilder<EnumDescriptorProto, Builder, EnumDescriptorProtoOrBuilder> getEnumTypeFieldBuilder() {
                if (this.enumTypeBuilder_ == null) {
                    this.enumTypeBuilder_ = new RepeatedFieldBuilder(this.enumType_, (this.bitField0_ & 16) == 16, getParentForChildren(), isClean());
                    this.enumType_ = null;
                }
                return this.enumTypeBuilder_;
            }

            private RepeatedFieldBuilder<FieldDescriptorProto, Builder, FieldDescriptorProtoOrBuilder> getExtensionFieldBuilder() {
                if (this.extensionBuilder_ == null) {
                    this.extensionBuilder_ = new RepeatedFieldBuilder(this.extension_, (this.bitField0_ & 64) == 64, getParentForChildren(), isClean());
                    this.extension_ = null;
                }
                return this.extensionBuilder_;
            }

            private RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> getMessageTypeFieldBuilder() {
                if (this.messageTypeBuilder_ == null) {
                    this.messageTypeBuilder_ = new RepeatedFieldBuilder(this.messageType_, (this.bitField0_ & 8) == 8, getParentForChildren(), isClean());
                    this.messageType_ = null;
                }
                return this.messageTypeBuilder_;
            }

            private SingleFieldBuilder<FileOptions, Builder, FileOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, getParentForChildren(), isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private RepeatedFieldBuilder<ServiceDescriptorProto, Builder, ServiceDescriptorProtoOrBuilder> getServiceFieldBuilder() {
                if (this.serviceBuilder_ == null) {
                    this.serviceBuilder_ = new RepeatedFieldBuilder(this.service_, (this.bitField0_ & 32) == 32, getParentForChildren(), isClean());
                    this.service_ = null;
                }
                return this.serviceBuilder_;
            }

            private SingleFieldBuilder<SourceCodeInfo, Builder, SourceCodeInfoOrBuilder> getSourceCodeInfoFieldBuilder() {
                if (this.sourceCodeInfoBuilder_ == null) {
                    this.sourceCodeInfoBuilder_ = new SingleFieldBuilder(this.sourceCodeInfo_, getParentForChildren(), isClean());
                    this.sourceCodeInfo_ = null;
                }
                return this.sourceCodeInfoBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getMessageTypeFieldBuilder();
                    getEnumTypeFieldBuilder();
                    getServiceFieldBuilder();
                    getExtensionFieldBuilder();
                    getOptionsFieldBuilder();
                    getSourceCodeInfoFieldBuilder();
                }
            }

            public Builder addAllDependency(Iterable<String> iterable) {
                ensureDependencyIsMutable();
                com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.dependency_);
                onChanged();
                return this;
            }

            public Builder addAllEnumType(Iterable<? extends EnumDescriptorProto> iterable) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.enumType_);
                    onChanged();
                } else {
                    this.enumTypeBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addAllExtension(Iterable<? extends FieldDescriptorProto> iterable) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.extension_);
                    onChanged();
                } else {
                    this.extensionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addAllMessageType(Iterable<? extends DescriptorProto> iterable) {
                if (this.messageTypeBuilder_ == null) {
                    ensureMessageTypeIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.messageType_);
                    onChanged();
                } else {
                    this.messageTypeBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addAllService(Iterable<? extends ServiceDescriptorProto> iterable) {
                if (this.serviceBuilder_ == null) {
                    ensureServiceIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.service_);
                    onChanged();
                } else {
                    this.serviceBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addDependency(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                ensureDependencyIsMutable();
                this.dependency_.add(str);
                onChanged();
                return this;
            }

            void addDependency(ByteString byteString) {
                ensureDependencyIsMutable();
                this.dependency_.add(byteString);
                onChanged();
            }

            public Builder addEnumType(int i, Builder builder) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(i, builder.build());
                    onChanged();
                } else {
                    this.enumTypeBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addEnumType(int i, EnumDescriptorProto enumDescriptorProto) {
                if (this.enumTypeBuilder_ != null) {
                    this.enumTypeBuilder_.addMessage(i, enumDescriptorProto);
                } else if (enumDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(i, enumDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addEnumType(Builder builder) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(builder.build());
                    onChanged();
                } else {
                    this.enumTypeBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addEnumType(EnumDescriptorProto enumDescriptorProto) {
                if (this.enumTypeBuilder_ != null) {
                    this.enumTypeBuilder_.addMessage(enumDescriptorProto);
                } else if (enumDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureEnumTypeIsMutable();
                    this.enumType_.add(enumDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addEnumTypeBuilder() {
                return (Builder) getEnumTypeFieldBuilder().addBuilder(EnumDescriptorProto.getDefaultInstance());
            }

            public Builder addEnumTypeBuilder(int i) {
                return (Builder) getEnumTypeFieldBuilder().addBuilder(i, EnumDescriptorProto.getDefaultInstance());
            }

            public Builder addExtension(int i, Builder builder) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.add(i, builder.build());
                    onChanged();
                } else {
                    this.extensionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addExtension(int i, FieldDescriptorProto fieldDescriptorProto) {
                if (this.extensionBuilder_ != null) {
                    this.extensionBuilder_.addMessage(i, fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionIsMutable();
                    this.extension_.add(i, fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addExtension(Builder builder) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.add(builder.build());
                    onChanged();
                } else {
                    this.extensionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addExtension(FieldDescriptorProto fieldDescriptorProto) {
                if (this.extensionBuilder_ != null) {
                    this.extensionBuilder_.addMessage(fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionIsMutable();
                    this.extension_.add(fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addExtensionBuilder() {
                return (Builder) getExtensionFieldBuilder().addBuilder(FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addExtensionBuilder(int i) {
                return (Builder) getExtensionFieldBuilder().addBuilder(i, FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addMessageType(int i, Builder builder) {
                if (this.messageTypeBuilder_ == null) {
                    ensureMessageTypeIsMutable();
                    this.messageType_.add(i, builder.build());
                    onChanged();
                } else {
                    this.messageTypeBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addMessageType(int i, DescriptorProto descriptorProto) {
                if (this.messageTypeBuilder_ != null) {
                    this.messageTypeBuilder_.addMessage(i, descriptorProto);
                } else if (descriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureMessageTypeIsMutable();
                    this.messageType_.add(i, descriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addMessageType(Builder builder) {
                if (this.messageTypeBuilder_ == null) {
                    ensureMessageTypeIsMutable();
                    this.messageType_.add(builder.build());
                    onChanged();
                } else {
                    this.messageTypeBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addMessageType(DescriptorProto descriptorProto) {
                if (this.messageTypeBuilder_ != null) {
                    this.messageTypeBuilder_.addMessage(descriptorProto);
                } else if (descriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureMessageTypeIsMutable();
                    this.messageType_.add(descriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addMessageTypeBuilder() {
                return (Builder) getMessageTypeFieldBuilder().addBuilder(DescriptorProto.getDefaultInstance());
            }

            public Builder addMessageTypeBuilder(int i) {
                return (Builder) getMessageTypeFieldBuilder().addBuilder(i, DescriptorProto.getDefaultInstance());
            }

            public Builder addService(int i, Builder builder) {
                if (this.serviceBuilder_ == null) {
                    ensureServiceIsMutable();
                    this.service_.add(i, builder.build());
                    onChanged();
                } else {
                    this.serviceBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addService(int i, ServiceDescriptorProto serviceDescriptorProto) {
                if (this.serviceBuilder_ != null) {
                    this.serviceBuilder_.addMessage(i, serviceDescriptorProto);
                } else if (serviceDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureServiceIsMutable();
                    this.service_.add(i, serviceDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addService(Builder builder) {
                if (this.serviceBuilder_ == null) {
                    ensureServiceIsMutable();
                    this.service_.add(builder.build());
                    onChanged();
                } else {
                    this.serviceBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addService(ServiceDescriptorProto serviceDescriptorProto) {
                if (this.serviceBuilder_ != null) {
                    this.serviceBuilder_.addMessage(serviceDescriptorProto);
                } else if (serviceDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureServiceIsMutable();
                    this.service_.add(serviceDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addServiceBuilder() {
                return (Builder) getServiceFieldBuilder().addBuilder(ServiceDescriptorProto.getDefaultInstance());
            }

            public Builder addServiceBuilder(int i) {
                return (Builder) getServiceFieldBuilder().addBuilder(i, ServiceDescriptorProto.getDefaultInstance());
            }

            public FileDescriptorProto build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public FileDescriptorProto buildPartial() {
                int i = 1;
                FileDescriptorProto fileDescriptorProto = new FileDescriptorProto();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                fileDescriptorProto.name_ = this.name_;
                if ((i2 & 2) == 2) {
                    i |= 2;
                }
                fileDescriptorProto.package_ = this.package_;
                if ((this.bitField0_ & 4) == 4) {
                    this.dependency_ = new UnmodifiableLazyStringList(this.dependency_);
                    this.bitField0_ &= -5;
                }
                fileDescriptorProto.dependency_ = this.dependency_;
                if (this.messageTypeBuilder_ == null) {
                    if ((this.bitField0_ & 8) == 8) {
                        this.messageType_ = Collections.unmodifiableList(this.messageType_);
                        this.bitField0_ &= -9;
                    }
                    fileDescriptorProto.messageType_ = this.messageType_;
                } else {
                    fileDescriptorProto.messageType_ = this.messageTypeBuilder_.build();
                }
                if (this.enumTypeBuilder_ == null) {
                    if ((this.bitField0_ & 16) == 16) {
                        this.enumType_ = Collections.unmodifiableList(this.enumType_);
                        this.bitField0_ &= -17;
                    }
                    fileDescriptorProto.enumType_ = this.enumType_;
                } else {
                    fileDescriptorProto.enumType_ = this.enumTypeBuilder_.build();
                }
                if (this.serviceBuilder_ == null) {
                    if ((this.bitField0_ & 32) == 32) {
                        this.service_ = Collections.unmodifiableList(this.service_);
                        this.bitField0_ &= -33;
                    }
                    fileDescriptorProto.service_ = this.service_;
                } else {
                    fileDescriptorProto.service_ = this.serviceBuilder_.build();
                }
                if (this.extensionBuilder_ == null) {
                    if ((this.bitField0_ & 64) == 64) {
                        this.extension_ = Collections.unmodifiableList(this.extension_);
                        this.bitField0_ &= -65;
                    }
                    fileDescriptorProto.extension_ = this.extension_;
                } else {
                    fileDescriptorProto.extension_ = this.extensionBuilder_.build();
                }
                int i3 = (i2 & 128) == 128 ? i | 4 : i;
                if (this.optionsBuilder_ == null) {
                    fileDescriptorProto.options_ = this.options_;
                } else {
                    fileDescriptorProto.options_ = (FileOptions) this.optionsBuilder_.build();
                }
                if ((i2 & 256) == 256) {
                    i3 |= 8;
                }
                if (this.sourceCodeInfoBuilder_ == null) {
                    fileDescriptorProto.sourceCodeInfo_ = this.sourceCodeInfo_;
                } else {
                    fileDescriptorProto.sourceCodeInfo_ = (SourceCodeInfo) this.sourceCodeInfoBuilder_.build();
                }
                fileDescriptorProto.bitField0_ = i3;
                onBuilt();
                return fileDescriptorProto;
            }

            public Builder clear() {
                super.clear();
                this.name_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                this.package_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -3;
                this.dependency_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -5;
                if (this.messageTypeBuilder_ == null) {
                    this.messageType_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                } else {
                    this.messageTypeBuilder_.clear();
                }
                if (this.enumTypeBuilder_ == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                } else {
                    this.enumTypeBuilder_.clear();
                }
                if (this.serviceBuilder_ == null) {
                    this.service_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                } else {
                    this.serviceBuilder_.clear();
                }
                if (this.extensionBuilder_ == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                } else {
                    this.extensionBuilder_.clear();
                }
                if (this.optionsBuilder_ == null) {
                    this.options_ = FileOptions.getDefaultInstance();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -129;
                if (this.sourceCodeInfoBuilder_ == null) {
                    this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
                } else {
                    this.sourceCodeInfoBuilder_.clear();
                }
                this.bitField0_ &= -257;
                return this;
            }

            public Builder clearDependency() {
                this.dependency_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -5;
                onChanged();
                return this;
            }

            public Builder clearEnumType() {
                if (this.enumTypeBuilder_ == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                    onChanged();
                } else {
                    this.enumTypeBuilder_.clear();
                }
                return this;
            }

            public Builder clearExtension() {
                if (this.extensionBuilder_ == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                    onChanged();
                } else {
                    this.extensionBuilder_.clear();
                }
                return this;
            }

            public Builder clearMessageType() {
                if (this.messageTypeBuilder_ == null) {
                    this.messageType_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                    onChanged();
                } else {
                    this.messageTypeBuilder_.clear();
                }
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = FileDescriptorProto.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder clearOptions() {
                if (this.optionsBuilder_ == null) {
                    this.options_ = FileOptions.getDefaultInstance();
                    onChanged();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -129;
                return this;
            }

            public Builder clearPackage() {
                this.bitField0_ &= -3;
                this.package_ = FileDescriptorProto.getDefaultInstance().getPackage();
                onChanged();
                return this;
            }

            public Builder clearService() {
                if (this.serviceBuilder_ == null) {
                    this.service_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                    onChanged();
                } else {
                    this.serviceBuilder_.clear();
                }
                return this;
            }

            public Builder clearSourceCodeInfo() {
                if (this.sourceCodeInfoBuilder_ == null) {
                    this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
                    onChanged();
                } else {
                    this.sourceCodeInfoBuilder_.clear();
                }
                this.bitField0_ &= -257;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public FileDescriptorProto getDefaultInstanceForType() {
                return FileDescriptorProto.getDefaultInstance();
            }

            public String getDependency(int i) {
                return (String) this.dependency_.get(i);
            }

            public int getDependencyCount() {
                return this.dependency_.size();
            }

            public List<String> getDependencyList() {
                return Collections.unmodifiableList(this.dependency_);
            }

            public Descriptor getDescriptorForType() {
                return FileDescriptorProto.getDescriptor();
            }

            public EnumDescriptorProto getEnumType(int i) {
                return this.enumTypeBuilder_ == null ? (EnumDescriptorProto) this.enumType_.get(i) : (EnumDescriptorProto) this.enumTypeBuilder_.getMessage(i);
            }

            public Builder getEnumTypeBuilder(int i) {
                return (Builder) getEnumTypeFieldBuilder().getBuilder(i);
            }

            public List<Builder> getEnumTypeBuilderList() {
                return getEnumTypeFieldBuilder().getBuilderList();
            }

            public int getEnumTypeCount() {
                return this.enumTypeBuilder_ == null ? this.enumType_.size() : this.enumTypeBuilder_.getCount();
            }

            public List<EnumDescriptorProto> getEnumTypeList() {
                return this.enumTypeBuilder_ == null ? Collections.unmodifiableList(this.enumType_) : this.enumTypeBuilder_.getMessageList();
            }

            public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int i) {
                return this.enumTypeBuilder_ == null ? (EnumDescriptorProtoOrBuilder) this.enumType_.get(i) : (EnumDescriptorProtoOrBuilder) this.enumTypeBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
                return this.enumTypeBuilder_ != null ? this.enumTypeBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.enumType_);
            }

            public FieldDescriptorProto getExtension(int i) {
                return this.extensionBuilder_ == null ? (FieldDescriptorProto) this.extension_.get(i) : (FieldDescriptorProto) this.extensionBuilder_.getMessage(i);
            }

            public Builder getExtensionBuilder(int i) {
                return (Builder) getExtensionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getExtensionBuilderList() {
                return getExtensionFieldBuilder().getBuilderList();
            }

            public int getExtensionCount() {
                return this.extensionBuilder_ == null ? this.extension_.size() : this.extensionBuilder_.getCount();
            }

            public List<FieldDescriptorProto> getExtensionList() {
                return this.extensionBuilder_ == null ? Collections.unmodifiableList(this.extension_) : this.extensionBuilder_.getMessageList();
            }

            public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int i) {
                return this.extensionBuilder_ == null ? (FieldDescriptorProtoOrBuilder) this.extension_.get(i) : (FieldDescriptorProtoOrBuilder) this.extensionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
                return this.extensionBuilder_ != null ? this.extensionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.extension_);
            }

            public DescriptorProto getMessageType(int i) {
                return this.messageTypeBuilder_ == null ? (DescriptorProto) this.messageType_.get(i) : (DescriptorProto) this.messageTypeBuilder_.getMessage(i);
            }

            public Builder getMessageTypeBuilder(int i) {
                return (Builder) getMessageTypeFieldBuilder().getBuilder(i);
            }

            public List<Builder> getMessageTypeBuilderList() {
                return getMessageTypeFieldBuilder().getBuilderList();
            }

            public int getMessageTypeCount() {
                return this.messageTypeBuilder_ == null ? this.messageType_.size() : this.messageTypeBuilder_.getCount();
            }

            public List<DescriptorProto> getMessageTypeList() {
                return this.messageTypeBuilder_ == null ? Collections.unmodifiableList(this.messageType_) : this.messageTypeBuilder_.getMessageList();
            }

            public DescriptorProtoOrBuilder getMessageTypeOrBuilder(int i) {
                return this.messageTypeBuilder_ == null ? (DescriptorProtoOrBuilder) this.messageType_.get(i) : (DescriptorProtoOrBuilder) this.messageTypeBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends DescriptorProtoOrBuilder> getMessageTypeOrBuilderList() {
                return this.messageTypeBuilder_ != null ? this.messageTypeBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.messageType_);
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = toStringUtf8;
                return toStringUtf8;
            }

            public FileOptions getOptions() {
                return this.optionsBuilder_ == null ? this.options_ : (FileOptions) this.optionsBuilder_.getMessage();
            }

            public Builder getOptionsBuilder() {
                this.bitField0_ |= 128;
                onChanged();
                return (Builder) getOptionsFieldBuilder().getBuilder();
            }

            public FileOptionsOrBuilder getOptionsOrBuilder() {
                return this.optionsBuilder_ != null ? (FileOptionsOrBuilder) this.optionsBuilder_.getMessageOrBuilder() : this.options_;
            }

            public String getPackage() {
                Object obj = this.package_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.package_ = toStringUtf8;
                return toStringUtf8;
            }

            public ServiceDescriptorProto getService(int i) {
                return this.serviceBuilder_ == null ? (ServiceDescriptorProto) this.service_.get(i) : (ServiceDescriptorProto) this.serviceBuilder_.getMessage(i);
            }

            public Builder getServiceBuilder(int i) {
                return (Builder) getServiceFieldBuilder().getBuilder(i);
            }

            public List<Builder> getServiceBuilderList() {
                return getServiceFieldBuilder().getBuilderList();
            }

            public int getServiceCount() {
                return this.serviceBuilder_ == null ? this.service_.size() : this.serviceBuilder_.getCount();
            }

            public List<ServiceDescriptorProto> getServiceList() {
                return this.serviceBuilder_ == null ? Collections.unmodifiableList(this.service_) : this.serviceBuilder_.getMessageList();
            }

            public ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int i) {
                return this.serviceBuilder_ == null ? (ServiceDescriptorProtoOrBuilder) this.service_.get(i) : (ServiceDescriptorProtoOrBuilder) this.serviceBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends ServiceDescriptorProtoOrBuilder> getServiceOrBuilderList() {
                return this.serviceBuilder_ != null ? this.serviceBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.service_);
            }

            public SourceCodeInfo getSourceCodeInfo() {
                return this.sourceCodeInfoBuilder_ == null ? this.sourceCodeInfo_ : (SourceCodeInfo) this.sourceCodeInfoBuilder_.getMessage();
            }

            public Builder getSourceCodeInfoBuilder() {
                this.bitField0_ |= 256;
                onChanged();
                return (Builder) getSourceCodeInfoFieldBuilder().getBuilder();
            }

            public SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
                return this.sourceCodeInfoBuilder_ != null ? (SourceCodeInfoOrBuilder) this.sourceCodeInfoBuilder_.getMessageOrBuilder() : this.sourceCodeInfo_;
            }

            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasOptions() {
                return (this.bitField0_ & 128) == 128;
            }

            public boolean hasPackage() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasSourceCodeInfo() {
                return (this.bitField0_ & 256) == 256;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f284x4b52780c;
            }

            public final boolean isInitialized() {
                int i;
                for (i = 0; i < getMessageTypeCount(); i++) {
                    if (!getMessageType(i).isInitialized()) {
                        return false;
                    }
                }
                for (i = 0; i < getEnumTypeCount(); i++) {
                    if (!getEnumType(i).isInitialized()) {
                        return false;
                    }
                }
                for (i = 0; i < getServiceCount(); i++) {
                    if (!getService(i).isInitialized()) {
                        return false;
                    }
                }
                for (i = 0; i < getExtensionCount(); i++) {
                    if (!getExtension(i).isInitialized()) {
                        return false;
                    }
                }
                return !hasOptions() || getOptions().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    Object newBuilder2;
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.name_ = codedInputStream.readBytes();
                            continue;
                        case 18:
                            this.bitField0_ |= 2;
                            this.package_ = codedInputStream.readBytes();
                            continue;
                        case 26:
                            ensureDependencyIsMutable();
                            this.dependency_.add(codedInputStream.readBytes());
                            continue;
                        case 34:
                            newBuilder2 = DescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addMessageType(newBuilder2.buildPartial());
                            continue;
                        case 42:
                            newBuilder2 = EnumDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addEnumType(newBuilder2.buildPartial());
                            continue;
                        case 50:
                            newBuilder2 = ServiceDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addService(newBuilder2.buildPartial());
                            continue;
                        case 58:
                            newBuilder2 = FieldDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addExtension(newBuilder2.buildPartial());
                            continue;
                        case 66:
                            newBuilder2 = FileOptions.newBuilder();
                            if (hasOptions()) {
                                newBuilder2.mergeFrom(getOptions());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setOptions(newBuilder2.buildPartial());
                            continue;
                        case 74:
                            newBuilder2 = SourceCodeInfo.newBuilder();
                            if (hasSourceCodeInfo()) {
                                newBuilder2.mergeFrom(getSourceCodeInfo());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setSourceCodeInfo(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(FileDescriptorProto fileDescriptorProto) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (fileDescriptorProto != FileDescriptorProto.getDefaultInstance()) {
                    if (fileDescriptorProto.hasName()) {
                        setName(fileDescriptorProto.getName());
                    }
                    if (fileDescriptorProto.hasPackage()) {
                        setPackage(fileDescriptorProto.getPackage());
                    }
                    if (!fileDescriptorProto.dependency_.isEmpty()) {
                        if (this.dependency_.isEmpty()) {
                            this.dependency_ = fileDescriptorProto.dependency_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureDependencyIsMutable();
                            this.dependency_.addAll(fileDescriptorProto.dependency_);
                        }
                        onChanged();
                    }
                    if (this.messageTypeBuilder_ == null) {
                        if (!fileDescriptorProto.messageType_.isEmpty()) {
                            if (this.messageType_.isEmpty()) {
                                this.messageType_ = fileDescriptorProto.messageType_;
                                this.bitField0_ &= -9;
                            } else {
                                ensureMessageTypeIsMutable();
                                this.messageType_.addAll(fileDescriptorProto.messageType_);
                            }
                            onChanged();
                        }
                    } else if (!fileDescriptorProto.messageType_.isEmpty()) {
                        if (this.messageTypeBuilder_.isEmpty()) {
                            this.messageTypeBuilder_.dispose();
                            this.messageTypeBuilder_ = null;
                            this.messageType_ = fileDescriptorProto.messageType_;
                            this.bitField0_ &= -9;
                            this.messageTypeBuilder_ = GeneratedMessage.alwaysUseFieldBuilders ? getMessageTypeFieldBuilder() : null;
                        } else {
                            this.messageTypeBuilder_.addAllMessages(fileDescriptorProto.messageType_);
                        }
                    }
                    if (this.enumTypeBuilder_ == null) {
                        if (!fileDescriptorProto.enumType_.isEmpty()) {
                            if (this.enumType_.isEmpty()) {
                                this.enumType_ = fileDescriptorProto.enumType_;
                                this.bitField0_ &= -17;
                            } else {
                                ensureEnumTypeIsMutable();
                                this.enumType_.addAll(fileDescriptorProto.enumType_);
                            }
                            onChanged();
                        }
                    } else if (!fileDescriptorProto.enumType_.isEmpty()) {
                        if (this.enumTypeBuilder_.isEmpty()) {
                            this.enumTypeBuilder_.dispose();
                            this.enumTypeBuilder_ = null;
                            this.enumType_ = fileDescriptorProto.enumType_;
                            this.bitField0_ &= -17;
                            this.enumTypeBuilder_ = GeneratedMessage.alwaysUseFieldBuilders ? getEnumTypeFieldBuilder() : null;
                        } else {
                            this.enumTypeBuilder_.addAllMessages(fileDescriptorProto.enumType_);
                        }
                    }
                    if (this.serviceBuilder_ == null) {
                        if (!fileDescriptorProto.service_.isEmpty()) {
                            if (this.service_.isEmpty()) {
                                this.service_ = fileDescriptorProto.service_;
                                this.bitField0_ &= -33;
                            } else {
                                ensureServiceIsMutable();
                                this.service_.addAll(fileDescriptorProto.service_);
                            }
                            onChanged();
                        }
                    } else if (!fileDescriptorProto.service_.isEmpty()) {
                        if (this.serviceBuilder_.isEmpty()) {
                            this.serviceBuilder_.dispose();
                            this.serviceBuilder_ = null;
                            this.service_ = fileDescriptorProto.service_;
                            this.bitField0_ &= -33;
                            this.serviceBuilder_ = GeneratedMessage.alwaysUseFieldBuilders ? getServiceFieldBuilder() : null;
                        } else {
                            this.serviceBuilder_.addAllMessages(fileDescriptorProto.service_);
                        }
                    }
                    if (this.extensionBuilder_ == null) {
                        if (!fileDescriptorProto.extension_.isEmpty()) {
                            if (this.extension_.isEmpty()) {
                                this.extension_ = fileDescriptorProto.extension_;
                                this.bitField0_ &= -65;
                            } else {
                                ensureExtensionIsMutable();
                                this.extension_.addAll(fileDescriptorProto.extension_);
                            }
                            onChanged();
                        }
                    } else if (!fileDescriptorProto.extension_.isEmpty()) {
                        if (this.extensionBuilder_.isEmpty()) {
                            this.extensionBuilder_.dispose();
                            this.extensionBuilder_ = null;
                            this.extension_ = fileDescriptorProto.extension_;
                            this.bitField0_ &= -65;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getExtensionFieldBuilder();
                            }
                            this.extensionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.extensionBuilder_.addAllMessages(fileDescriptorProto.extension_);
                        }
                    }
                    if (fileDescriptorProto.hasOptions()) {
                        mergeOptions(fileDescriptorProto.getOptions());
                    }
                    if (fileDescriptorProto.hasSourceCodeInfo()) {
                        mergeSourceCodeInfo(fileDescriptorProto.getSourceCodeInfo());
                    }
                    mergeUnknownFields(fileDescriptorProto.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof FileDescriptorProto) {
                    return mergeFrom((FileDescriptorProto) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(FileOptions fileOptions) {
                if (this.optionsBuilder_ == null) {
                    if ((this.bitField0_ & 128) != 128 || this.options_ == FileOptions.getDefaultInstance()) {
                        this.options_ = fileOptions;
                    } else {
                        this.options_ = FileOptions.newBuilder(this.options_).mergeFrom(fileOptions).buildPartial();
                    }
                    onChanged();
                } else {
                    this.optionsBuilder_.mergeFrom(fileOptions);
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder mergeSourceCodeInfo(SourceCodeInfo sourceCodeInfo) {
                if (this.sourceCodeInfoBuilder_ == null) {
                    if ((this.bitField0_ & 256) != 256 || this.sourceCodeInfo_ == SourceCodeInfo.getDefaultInstance()) {
                        this.sourceCodeInfo_ = sourceCodeInfo;
                    } else {
                        this.sourceCodeInfo_ = SourceCodeInfo.newBuilder(this.sourceCodeInfo_).mergeFrom(sourceCodeInfo).buildPartial();
                    }
                    onChanged();
                } else {
                    this.sourceCodeInfoBuilder_.mergeFrom(sourceCodeInfo);
                }
                this.bitField0_ |= 256;
                return this;
            }

            public Builder removeEnumType(int i) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.remove(i);
                    onChanged();
                } else {
                    this.enumTypeBuilder_.remove(i);
                }
                return this;
            }

            public Builder removeExtension(int i) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.remove(i);
                    onChanged();
                } else {
                    this.extensionBuilder_.remove(i);
                }
                return this;
            }

            public Builder removeMessageType(int i) {
                if (this.messageTypeBuilder_ == null) {
                    ensureMessageTypeIsMutable();
                    this.messageType_.remove(i);
                    onChanged();
                } else {
                    this.messageTypeBuilder_.remove(i);
                }
                return this;
            }

            public Builder removeService(int i) {
                if (this.serviceBuilder_ == null) {
                    ensureServiceIsMutable();
                    this.service_.remove(i);
                    onChanged();
                } else {
                    this.serviceBuilder_.remove(i);
                }
                return this;
            }

            public Builder setDependency(int i, String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                ensureDependencyIsMutable();
                this.dependency_.set(i, str);
                onChanged();
                return this;
            }

            public Builder setEnumType(int i, Builder builder) {
                if (this.enumTypeBuilder_ == null) {
                    ensureEnumTypeIsMutable();
                    this.enumType_.set(i, builder.build());
                    onChanged();
                } else {
                    this.enumTypeBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setEnumType(int i, EnumDescriptorProto enumDescriptorProto) {
                if (this.enumTypeBuilder_ != null) {
                    this.enumTypeBuilder_.setMessage(i, enumDescriptorProto);
                } else if (enumDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureEnumTypeIsMutable();
                    this.enumType_.set(i, enumDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setExtension(int i, Builder builder) {
                if (this.extensionBuilder_ == null) {
                    ensureExtensionIsMutable();
                    this.extension_.set(i, builder.build());
                    onChanged();
                } else {
                    this.extensionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setExtension(int i, FieldDescriptorProto fieldDescriptorProto) {
                if (this.extensionBuilder_ != null) {
                    this.extensionBuilder_.setMessage(i, fieldDescriptorProto);
                } else if (fieldDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureExtensionIsMutable();
                    this.extension_.set(i, fieldDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setMessageType(int i, Builder builder) {
                if (this.messageTypeBuilder_ == null) {
                    ensureMessageTypeIsMutable();
                    this.messageType_.set(i, builder.build());
                    onChanged();
                } else {
                    this.messageTypeBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setMessageType(int i, DescriptorProto descriptorProto) {
                if (this.messageTypeBuilder_ != null) {
                    this.messageTypeBuilder_.setMessage(i, descriptorProto);
                } else if (descriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureMessageTypeIsMutable();
                    this.messageType_.set(i, descriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = str;
                onChanged();
                return this;
            }

            void setName(ByteString byteString) {
                this.bitField0_ |= 1;
                this.name_ = byteString;
                onChanged();
            }

            public Builder setOptions(Builder builder) {
                if (this.optionsBuilder_ == null) {
                    this.options_ = builder.build();
                    onChanged();
                } else {
                    this.optionsBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder setOptions(FileOptions fileOptions) {
                if (this.optionsBuilder_ != null) {
                    this.optionsBuilder_.setMessage(fileOptions);
                } else if (fileOptions == null) {
                    throw new NullPointerException();
                } else {
                    this.options_ = fileOptions;
                    onChanged();
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder setPackage(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.package_ = str;
                onChanged();
                return this;
            }

            void setPackage(ByteString byteString) {
                this.bitField0_ |= 2;
                this.package_ = byteString;
                onChanged();
            }

            public Builder setService(int i, Builder builder) {
                if (this.serviceBuilder_ == null) {
                    ensureServiceIsMutable();
                    this.service_.set(i, builder.build());
                    onChanged();
                } else {
                    this.serviceBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setService(int i, ServiceDescriptorProto serviceDescriptorProto) {
                if (this.serviceBuilder_ != null) {
                    this.serviceBuilder_.setMessage(i, serviceDescriptorProto);
                } else if (serviceDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureServiceIsMutable();
                    this.service_.set(i, serviceDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setSourceCodeInfo(Builder builder) {
                if (this.sourceCodeInfoBuilder_ == null) {
                    this.sourceCodeInfo_ = builder.build();
                    onChanged();
                } else {
                    this.sourceCodeInfoBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 256;
                return this;
            }

            public Builder setSourceCodeInfo(SourceCodeInfo sourceCodeInfo) {
                if (this.sourceCodeInfoBuilder_ != null) {
                    this.sourceCodeInfoBuilder_.setMessage(sourceCodeInfo);
                } else if (sourceCodeInfo == null) {
                    throw new NullPointerException();
                } else {
                    this.sourceCodeInfo_ = sourceCodeInfo;
                    onChanged();
                }
                this.bitField0_ |= 256;
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private FileDescriptorProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private FileDescriptorProto(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static FileDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private ByteString getPackageBytes() {
            Object obj = this.package_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.package_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = HttpVersions.HTTP_0_9;
            this.package_ = HttpVersions.HTTP_0_9;
            this.dependency_ = LazyStringArrayList.EMPTY;
            this.messageType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.service_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.options_ = FileOptions.getDefaultInstance();
            this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FileDescriptorProto fileDescriptorProto) {
            return newBuilder().mergeFrom(fileDescriptorProto);
        }

        public static FileDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static FileDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static FileDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static FileDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static FileDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static FileDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static FileDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static FileDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static FileDescriptorProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static FileDescriptorProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public FileDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public String getDependency(int i) {
            return (String) this.dependency_.get(i);
        }

        public int getDependencyCount() {
            return this.dependency_.size();
        }

        public List<String> getDependencyList() {
            return this.dependency_;
        }

        public EnumDescriptorProto getEnumType(int i) {
            return (EnumDescriptorProto) this.enumType_.get(i);
        }

        public int getEnumTypeCount() {
            return this.enumType_.size();
        }

        public List<EnumDescriptorProto> getEnumTypeList() {
            return this.enumType_;
        }

        public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int i) {
            return (EnumDescriptorProtoOrBuilder) this.enumType_.get(i);
        }

        public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
            return this.enumType_;
        }

        public FieldDescriptorProto getExtension(int i) {
            return (FieldDescriptorProto) this.extension_.get(i);
        }

        public int getExtensionCount() {
            return this.extension_.size();
        }

        public List<FieldDescriptorProto> getExtensionList() {
            return this.extension_;
        }

        public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int i) {
            return (FieldDescriptorProtoOrBuilder) this.extension_.get(i);
        }

        public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
            return this.extension_;
        }

        public DescriptorProto getMessageType(int i) {
            return (DescriptorProto) this.messageType_.get(i);
        }

        public int getMessageTypeCount() {
            return this.messageType_.size();
        }

        public List<DescriptorProto> getMessageTypeList() {
            return this.messageType_;
        }

        public DescriptorProtoOrBuilder getMessageTypeOrBuilder(int i) {
            return (DescriptorProtoOrBuilder) this.messageType_.get(i);
        }

        public List<? extends DescriptorProtoOrBuilder> getMessageTypeOrBuilderList() {
            return this.messageType_;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public FileOptions getOptions() {
            return this.options_;
        }

        public FileOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public String getPackage() {
            Object obj = this.package_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.package_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public int getSerializedSize() {
            int i = 0;
            int i2 = this.memoizedSerializedSize;
            if (i2 != -1) {
                return i2;
            }
            int i3;
            i2 = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBytesSize(1, getNameBytes()) + 0 : 0;
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBytesSize(2, getPackageBytes());
            }
            int i4 = 0;
            for (i3 = 0; i3 < this.dependency_.size(); i3++) {
                i4 += CodedOutputStream.computeBytesSizeNoTag(this.dependency_.getByteString(i3));
            }
            i2 = (i2 + i4) + (getDependencyList().size() * 1);
            i3 = i2;
            for (i4 = 0; i4 < this.messageType_.size(); i4++) {
                i3 += CodedOutputStream.computeMessageSize(4, (MessageLite) this.messageType_.get(i4));
            }
            for (i4 = 0; i4 < this.enumType_.size(); i4++) {
                i3 += CodedOutputStream.computeMessageSize(5, (MessageLite) this.enumType_.get(i4));
            }
            for (i4 = 0; i4 < this.service_.size(); i4++) {
                i3 += CodedOutputStream.computeMessageSize(6, (MessageLite) this.service_.get(i4));
            }
            while (i < this.extension_.size()) {
                i3 += CodedOutputStream.computeMessageSize(7, (MessageLite) this.extension_.get(i));
                i++;
            }
            if ((this.bitField0_ & 4) == 4) {
                i3 += CodedOutputStream.computeMessageSize(8, this.options_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i3 += CodedOutputStream.computeMessageSize(9, this.sourceCodeInfo_);
            }
            i2 = getUnknownFields().getSerializedSize() + i3;
            this.memoizedSerializedSize = i2;
            return i2;
        }

        public ServiceDescriptorProto getService(int i) {
            return (ServiceDescriptorProto) this.service_.get(i);
        }

        public int getServiceCount() {
            return this.service_.size();
        }

        public List<ServiceDescriptorProto> getServiceList() {
            return this.service_;
        }

        public ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int i) {
            return (ServiceDescriptorProtoOrBuilder) this.service_.get(i);
        }

        public List<? extends ServiceDescriptorProtoOrBuilder> getServiceOrBuilderList() {
            return this.service_;
        }

        public SourceCodeInfo getSourceCodeInfo() {
            return this.sourceCodeInfo_;
        }

        public SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
            return this.sourceCodeInfo_;
        }

        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasOptions() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean hasPackage() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean hasSourceCodeInfo() {
            return (this.bitField0_ & 8) == 8;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f284x4b52780c;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getMessageTypeCount()) {
                    if (getMessageType(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                i = 0;
                while (i < getEnumTypeCount()) {
                    if (getEnumType(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                i = 0;
                while (i < getServiceCount()) {
                    if (getService(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                i = 0;
                while (i < getExtensionCount()) {
                    if (getExtension(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (!hasOptions() || getOptions().isInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            int i;
            int i2 = 0;
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getPackageBytes());
            }
            for (int i3 = 0; i3 < this.dependency_.size(); i3++) {
                codedOutputStream.writeBytes(3, this.dependency_.getByteString(i3));
            }
            for (i = 0; i < this.messageType_.size(); i++) {
                codedOutputStream.writeMessage(4, (MessageLite) this.messageType_.get(i));
            }
            for (i = 0; i < this.enumType_.size(); i++) {
                codedOutputStream.writeMessage(5, (MessageLite) this.enumType_.get(i));
            }
            for (i = 0; i < this.service_.size(); i++) {
                codedOutputStream.writeMessage(6, (MessageLite) this.service_.get(i));
            }
            while (i2 < this.extension_.size()) {
                codedOutputStream.writeMessage(7, (MessageLite) this.extension_.get(i2));
                i2++;
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeMessage(8, this.options_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeMessage(9, this.sourceCodeInfo_);
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface FileDescriptorSetOrBuilder extends MessageOrBuilder {
        FileDescriptorProto getFile(int i);

        int getFileCount();

        List<FileDescriptorProto> getFileList();

        FileDescriptorProtoOrBuilder getFileOrBuilder(int i);

        List<? extends FileDescriptorProtoOrBuilder> getFileOrBuilderList();
    }

    public static final class FileDescriptorSet extends GeneratedMessage implements FileDescriptorSetOrBuilder {
        public static final int FILE_FIELD_NUMBER = 1;
        private static final FileDescriptorSet defaultInstance = new FileDescriptorSet(true);
        private static final long serialVersionUID = 0;
        private List<FileDescriptorProto> file_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements FileDescriptorSetOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<FileDescriptorProto, Builder, FileDescriptorProtoOrBuilder> fileBuilder_;
            private List<FileDescriptorProto> file_;

            private Builder() {
                this.file_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.file_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private FileDescriptorSet buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureFileIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.file_ = new ArrayList(this.file_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
            }

            private RepeatedFieldBuilder<FileDescriptorProto, Builder, FileDescriptorProtoOrBuilder> getFileFieldBuilder() {
                boolean z = true;
                if (this.fileBuilder_ == null) {
                    List list = this.file_;
                    if ((this.bitField0_ & 1) != 1) {
                        z = false;
                    }
                    this.fileBuilder_ = new RepeatedFieldBuilder(list, z, getParentForChildren(), isClean());
                    this.file_ = null;
                }
                return this.fileBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getFileFieldBuilder();
                }
            }

            public Builder addAllFile(Iterable<? extends FileDescriptorProto> iterable) {
                if (this.fileBuilder_ == null) {
                    ensureFileIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.file_);
                    onChanged();
                } else {
                    this.fileBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addFile(int i, Builder builder) {
                if (this.fileBuilder_ == null) {
                    ensureFileIsMutable();
                    this.file_.add(i, builder.build());
                    onChanged();
                } else {
                    this.fileBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addFile(int i, FileDescriptorProto fileDescriptorProto) {
                if (this.fileBuilder_ != null) {
                    this.fileBuilder_.addMessage(i, fileDescriptorProto);
                } else if (fileDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureFileIsMutable();
                    this.file_.add(i, fileDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addFile(Builder builder) {
                if (this.fileBuilder_ == null) {
                    ensureFileIsMutable();
                    this.file_.add(builder.build());
                    onChanged();
                } else {
                    this.fileBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addFile(FileDescriptorProto fileDescriptorProto) {
                if (this.fileBuilder_ != null) {
                    this.fileBuilder_.addMessage(fileDescriptorProto);
                } else if (fileDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureFileIsMutable();
                    this.file_.add(fileDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addFileBuilder() {
                return (Builder) getFileFieldBuilder().addBuilder(FileDescriptorProto.getDefaultInstance());
            }

            public Builder addFileBuilder(int i) {
                return (Builder) getFileFieldBuilder().addBuilder(i, FileDescriptorProto.getDefaultInstance());
            }

            public FileDescriptorSet build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public FileDescriptorSet buildPartial() {
                FileDescriptorSet fileDescriptorSet = new FileDescriptorSet();
                int i = this.bitField0_;
                if (this.fileBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.file_ = Collections.unmodifiableList(this.file_);
                        this.bitField0_ &= -2;
                    }
                    fileDescriptorSet.file_ = this.file_;
                } else {
                    fileDescriptorSet.file_ = this.fileBuilder_.build();
                }
                onBuilt();
                return fileDescriptorSet;
            }

            public Builder clear() {
                super.clear();
                if (this.fileBuilder_ == null) {
                    this.file_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.fileBuilder_.clear();
                }
                return this;
            }

            public Builder clearFile() {
                if (this.fileBuilder_ == null) {
                    this.file_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.fileBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public FileDescriptorSet getDefaultInstanceForType() {
                return FileDescriptorSet.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return FileDescriptorSet.getDescriptor();
            }

            public FileDescriptorProto getFile(int i) {
                return this.fileBuilder_ == null ? (FileDescriptorProto) this.file_.get(i) : (FileDescriptorProto) this.fileBuilder_.getMessage(i);
            }

            public Builder getFileBuilder(int i) {
                return (Builder) getFileFieldBuilder().getBuilder(i);
            }

            public List<Builder> getFileBuilderList() {
                return getFileFieldBuilder().getBuilderList();
            }

            public int getFileCount() {
                return this.fileBuilder_ == null ? this.file_.size() : this.fileBuilder_.getCount();
            }

            public List<FileDescriptorProto> getFileList() {
                return this.fileBuilder_ == null ? Collections.unmodifiableList(this.file_) : this.fileBuilder_.getMessageList();
            }

            public FileDescriptorProtoOrBuilder getFileOrBuilder(int i) {
                return this.fileBuilder_ == null ? (FileDescriptorProtoOrBuilder) this.file_.get(i) : (FileDescriptorProtoOrBuilder) this.fileBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends FileDescriptorProtoOrBuilder> getFileOrBuilderList() {
                return this.fileBuilder_ != null ? this.fileBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.file_);
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f285x15a6a952;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getFileCount(); i++) {
                    if (!getFile(i).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            Object newBuilder2 = FileDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addFile(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(FileDescriptorSet fileDescriptorSet) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (fileDescriptorSet != FileDescriptorSet.getDefaultInstance()) {
                    if (this.fileBuilder_ == null) {
                        if (!fileDescriptorSet.file_.isEmpty()) {
                            if (this.file_.isEmpty()) {
                                this.file_ = fileDescriptorSet.file_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureFileIsMutable();
                                this.file_.addAll(fileDescriptorSet.file_);
                            }
                            onChanged();
                        }
                    } else if (!fileDescriptorSet.file_.isEmpty()) {
                        if (this.fileBuilder_.isEmpty()) {
                            this.fileBuilder_.dispose();
                            this.fileBuilder_ = null;
                            this.file_ = fileDescriptorSet.file_;
                            this.bitField0_ &= -2;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getFileFieldBuilder();
                            }
                            this.fileBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.fileBuilder_.addAllMessages(fileDescriptorSet.file_);
                        }
                    }
                    mergeUnknownFields(fileDescriptorSet.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof FileDescriptorSet) {
                    return mergeFrom((FileDescriptorSet) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeFile(int i) {
                if (this.fileBuilder_ == null) {
                    ensureFileIsMutable();
                    this.file_.remove(i);
                    onChanged();
                } else {
                    this.fileBuilder_.remove(i);
                }
                return this;
            }

            public Builder setFile(int i, Builder builder) {
                if (this.fileBuilder_ == null) {
                    ensureFileIsMutable();
                    this.file_.set(i, builder.build());
                    onChanged();
                } else {
                    this.fileBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setFile(int i, FileDescriptorProto fileDescriptorProto) {
                if (this.fileBuilder_ != null) {
                    this.fileBuilder_.setMessage(i, fileDescriptorProto);
                } else if (fileDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureFileIsMutable();
                    this.file_.set(i, fileDescriptorProto);
                    onChanged();
                }
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private FileDescriptorSet(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private FileDescriptorSet(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static FileDescriptorSet getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
        }

        private void initFields() {
            this.file_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FileDescriptorSet fileDescriptorSet) {
            return newBuilder().mergeFrom(fileDescriptorSet);
        }

        public static FileDescriptorSet parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static FileDescriptorSet parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static FileDescriptorSet parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static FileDescriptorSet parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static FileDescriptorSet parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static FileDescriptorSet parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static FileDescriptorSet parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static FileDescriptorSet parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static FileDescriptorSet parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static FileDescriptorSet parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public FileDescriptorSet getDefaultInstanceForType() {
            return defaultInstance;
        }

        public FileDescriptorProto getFile(int i) {
            return (FileDescriptorProto) this.file_.get(i);
        }

        public int getFileCount() {
            return this.file_.size();
        }

        public List<FileDescriptorProto> getFileList() {
            return this.file_;
        }

        public FileDescriptorProtoOrBuilder getFileOrBuilder(int i) {
            return (FileDescriptorProtoOrBuilder) this.file_.get(i);
        }

        public List<? extends FileDescriptorProtoOrBuilder> getFileOrBuilderList() {
            return this.file_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeMessageSize;
            i = 0;
            int i2 = 0;
            while (i2 < this.file_.size()) {
                computeMessageSize = CodedOutputStream.computeMessageSize(1, (MessageLite) this.file_.get(i2)) + i;
                i2++;
                i = computeMessageSize;
            }
            computeMessageSize = getUnknownFields().getSerializedSize() + i;
            this.memoizedSerializedSize = computeMessageSize;
            return computeMessageSize;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f285x15a6a952;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getFileCount()) {
                    if (getFile(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                this.memoizedIsInitialized = (byte) 1;
                return true;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.file_.size(); i++) {
                codedOutputStream.writeMessage(1, (MessageLite) this.file_.get(i));
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface FileOptionsOrBuilder extends ExtendableMessageOrBuilder<FileOptions> {
        boolean getCcGenericServices();

        boolean getJavaGenerateEqualsAndHash();

        boolean getJavaGenericServices();

        boolean getJavaMultipleFiles();

        String getJavaOuterClassname();

        String getJavaPackage();

        OptimizeMode getOptimizeFor();

        boolean getPyGenericServices();

        UninterpretedOption getUninterpretedOption(int i);

        int getUninterpretedOptionCount();

        List<UninterpretedOption> getUninterpretedOptionList();

        UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i);

        List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();

        boolean hasCcGenericServices();

        boolean hasJavaGenerateEqualsAndHash();

        boolean hasJavaGenericServices();

        boolean hasJavaMultipleFiles();

        boolean hasJavaOuterClassname();

        boolean hasJavaPackage();

        boolean hasOptimizeFor();

        boolean hasPyGenericServices();
    }

    public static final class FileOptions extends ExtendableMessage<FileOptions> implements FileOptionsOrBuilder {
        public static final int CC_GENERIC_SERVICES_FIELD_NUMBER = 16;
        public static final int JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER = 20;
        public static final int JAVA_GENERIC_SERVICES_FIELD_NUMBER = 17;
        public static final int JAVA_MULTIPLE_FILES_FIELD_NUMBER = 10;
        public static final int JAVA_OUTER_CLASSNAME_FIELD_NUMBER = 8;
        public static final int JAVA_PACKAGE_FIELD_NUMBER = 1;
        public static final int OPTIMIZE_FOR_FIELD_NUMBER = 9;
        public static final int PY_GENERIC_SERVICES_FIELD_NUMBER = 18;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final FileOptions defaultInstance = new FileOptions(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private boolean ccGenericServices_;
        private boolean javaGenerateEqualsAndHash_;
        private boolean javaGenericServices_;
        private boolean javaMultipleFiles_;
        private Object javaOuterClassname_;
        private Object javaPackage_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private OptimizeMode optimizeFor_;
        private boolean pyGenericServices_;
        private List<UninterpretedOption> uninterpretedOption_;

        public static final class Builder extends ExtendableBuilder<FileOptions, Builder> implements FileOptionsOrBuilder {
            private int bitField0_;
            private boolean ccGenericServices_;
            private boolean javaGenerateEqualsAndHash_;
            private boolean javaGenericServices_;
            private boolean javaMultipleFiles_;
            private Object javaOuterClassname_;
            private Object javaPackage_;
            private OptimizeMode optimizeFor_;
            private boolean pyGenericServices_;
            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_;

            private Builder() {
                this.javaPackage_ = HttpVersions.HTTP_0_9;
                this.javaOuterClassname_ = HttpVersions.HTTP_0_9;
                this.optimizeFor_ = OptimizeMode.SPEED;
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.javaPackage_ = HttpVersions.HTTP_0_9;
                this.javaOuterClassname_ = HttpVersions.HTTP_0_9;
                this.optimizeFor_ = OptimizeMode.SPEED;
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private FileOptions buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 256) != 256) {
                    this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
                    this.bitField0_ |= 256;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(this.uninterpretedOption_, (this.bitField0_ & 256) == 256, getParentForChildren(), isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.uninterpretedOption_);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOption(Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOptionBuilder() {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public Builder addUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(i, UninterpretedOption.getDefaultInstance());
            }

            public FileOptions build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public FileOptions buildPartial() {
                int i = 1;
                FileOptions fileOptions = new FileOptions();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                fileOptions.javaPackage_ = this.javaPackage_;
                if ((i2 & 2) == 2) {
                    i |= 2;
                }
                fileOptions.javaOuterClassname_ = this.javaOuterClassname_;
                if ((i2 & 4) == 4) {
                    i |= 4;
                }
                fileOptions.javaMultipleFiles_ = this.javaMultipleFiles_;
                if ((i2 & 8) == 8) {
                    i |= 8;
                }
                fileOptions.javaGenerateEqualsAndHash_ = this.javaGenerateEqualsAndHash_;
                if ((i2 & 16) == 16) {
                    i |= 16;
                }
                fileOptions.optimizeFor_ = this.optimizeFor_;
                if ((i2 & 32) == 32) {
                    i |= 32;
                }
                fileOptions.ccGenericServices_ = this.ccGenericServices_;
                if ((i2 & 64) == 64) {
                    i |= 64;
                }
                fileOptions.javaGenericServices_ = this.javaGenericServices_;
                if ((i2 & 128) == 128) {
                    i |= 128;
                }
                fileOptions.pyGenericServices_ = this.pyGenericServices_;
                if (this.uninterpretedOptionBuilder_ == null) {
                    if ((this.bitField0_ & 256) == 256) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -257;
                    }
                    fileOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    fileOptions.uninterpretedOption_ = this.uninterpretedOptionBuilder_.build();
                }
                fileOptions.bitField0_ = i;
                onBuilt();
                return fileOptions;
            }

            public Builder clear() {
                super.clear();
                this.javaPackage_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                this.javaOuterClassname_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -3;
                this.javaMultipleFiles_ = false;
                this.bitField0_ &= -5;
                this.javaGenerateEqualsAndHash_ = false;
                this.bitField0_ &= -9;
                this.optimizeFor_ = OptimizeMode.SPEED;
                this.bitField0_ &= -17;
                this.ccGenericServices_ = false;
                this.bitField0_ &= -33;
                this.javaGenericServices_ = false;
                this.bitField0_ &= -65;
                this.pyGenericServices_ = false;
                this.bitField0_ &= -129;
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -257;
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clearCcGenericServices() {
                this.bitField0_ &= -33;
                this.ccGenericServices_ = false;
                onChanged();
                return this;
            }

            public Builder clearJavaGenerateEqualsAndHash() {
                this.bitField0_ &= -9;
                this.javaGenerateEqualsAndHash_ = false;
                onChanged();
                return this;
            }

            public Builder clearJavaGenericServices() {
                this.bitField0_ &= -65;
                this.javaGenericServices_ = false;
                onChanged();
                return this;
            }

            public Builder clearJavaMultipleFiles() {
                this.bitField0_ &= -5;
                this.javaMultipleFiles_ = false;
                onChanged();
                return this;
            }

            public Builder clearJavaOuterClassname() {
                this.bitField0_ &= -3;
                this.javaOuterClassname_ = FileOptions.getDefaultInstance().getJavaOuterClassname();
                onChanged();
                return this;
            }

            public Builder clearJavaPackage() {
                this.bitField0_ &= -2;
                this.javaPackage_ = FileOptions.getDefaultInstance().getJavaPackage();
                onChanged();
                return this;
            }

            public Builder clearOptimizeFor() {
                this.bitField0_ &= -17;
                this.optimizeFor_ = OptimizeMode.SPEED;
                onChanged();
                return this;
            }

            public Builder clearPyGenericServices() {
                this.bitField0_ &= -129;
                this.pyGenericServices_ = false;
                onChanged();
                return this;
            }

            public Builder clearUninterpretedOption() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -257;
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public boolean getCcGenericServices() {
                return this.ccGenericServices_;
            }

            public FileOptions getDefaultInstanceForType() {
                return FileOptions.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return FileOptions.getDescriptor();
            }

            public boolean getJavaGenerateEqualsAndHash() {
                return this.javaGenerateEqualsAndHash_;
            }

            public boolean getJavaGenericServices() {
                return this.javaGenericServices_;
            }

            public boolean getJavaMultipleFiles() {
                return this.javaMultipleFiles_;
            }

            public String getJavaOuterClassname() {
                Object obj = this.javaOuterClassname_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.javaOuterClassname_ = toStringUtf8;
                return toStringUtf8;
            }

            public String getJavaPackage() {
                Object obj = this.javaPackage_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.javaPackage_ = toStringUtf8;
                return toStringUtf8;
            }

            public OptimizeMode getOptimizeFor() {
                return this.optimizeFor_;
            }

            public boolean getPyGenericServices() {
                return this.pyGenericServices_;
            }

            public UninterpretedOption getUninterpretedOption(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOption) this.uninterpretedOption_.get(i) : (UninterpretedOption) this.uninterpretedOptionBuilder_.getMessage(i);
            }

            public Builder getUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getUninterpretedOptionBuilderList() {
                return getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            public int getUninterpretedOptionCount() {
                return this.uninterpretedOptionBuilder_ == null ? this.uninterpretedOption_.size() : this.uninterpretedOptionBuilder_.getCount();
            }

            public List<UninterpretedOption> getUninterpretedOptionList() {
                return this.uninterpretedOptionBuilder_ == null ? Collections.unmodifiableList(this.uninterpretedOption_) : this.uninterpretedOptionBuilder_.getMessageList();
            }

            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i) : (UninterpretedOptionOrBuilder) this.uninterpretedOptionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                return this.uninterpretedOptionBuilder_ != null ? this.uninterpretedOptionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
            }

            public boolean hasCcGenericServices() {
                return (this.bitField0_ & 32) == 32;
            }

            public boolean hasJavaGenerateEqualsAndHash() {
                return (this.bitField0_ & 8) == 8;
            }

            public boolean hasJavaGenericServices() {
                return (this.bitField0_ & 64) == 64;
            }

            public boolean hasJavaMultipleFiles() {
                return (this.bitField0_ & 4) == 4;
            }

            public boolean hasJavaOuterClassname() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasJavaPackage() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasOptimizeFor() {
                return (this.bitField0_ & 16) == 16;
            }

            public boolean hasPyGenericServices() {
                return (this.bitField0_ & 128) == 128;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getUninterpretedOptionCount(); i++) {
                    if (!getUninterpretedOption(i).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.javaPackage_ = codedInputStream.readBytes();
                            continue;
                        case 66:
                            this.bitField0_ |= 2;
                            this.javaOuterClassname_ = codedInputStream.readBytes();
                            continue;
                        case 72:
                            readTag = codedInputStream.readEnum();
                            OptimizeMode valueOf = OptimizeMode.valueOf(readTag);
                            if (valueOf != null) {
                                this.bitField0_ |= 16;
                                this.optimizeFor_ = valueOf;
                                break;
                            }
                            newBuilder.mergeVarintField(9, readTag);
                            continue;
                        case ExifTagConstants.FLASH_VALUE_OFF_RED_EYE_REDUCTION /*80*/:
                            this.bitField0_ |= 4;
                            this.javaMultipleFiles_ = codedInputStream.readBool();
                            continue;
                        case 128:
                            this.bitField0_ |= 32;
                            this.ccGenericServices_ = codedInputStream.readBool();
                            continue;
                        case VideoCodecType.MP4_360P_H264_360P_CODEC /*136*/:
                            this.bitField0_ |= 64;
                            this.javaGenericServices_ = codedInputStream.readBool();
                            continue;
                        case 144:
                            this.bitField0_ |= 128;
                            this.pyGenericServices_ = codedInputStream.readBool();
                            continue;
                        case 160:
                            this.bitField0_ |= 8;
                            this.javaGenerateEqualsAndHash_ = codedInputStream.readBool();
                            continue;
                        case 7994:
                            Object newBuilder2 = UninterpretedOption.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addUninterpretedOption(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(FileOptions fileOptions) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (fileOptions != FileOptions.getDefaultInstance()) {
                    if (fileOptions.hasJavaPackage()) {
                        setJavaPackage(fileOptions.getJavaPackage());
                    }
                    if (fileOptions.hasJavaOuterClassname()) {
                        setJavaOuterClassname(fileOptions.getJavaOuterClassname());
                    }
                    if (fileOptions.hasJavaMultipleFiles()) {
                        setJavaMultipleFiles(fileOptions.getJavaMultipleFiles());
                    }
                    if (fileOptions.hasJavaGenerateEqualsAndHash()) {
                        setJavaGenerateEqualsAndHash(fileOptions.getJavaGenerateEqualsAndHash());
                    }
                    if (fileOptions.hasOptimizeFor()) {
                        setOptimizeFor(fileOptions.getOptimizeFor());
                    }
                    if (fileOptions.hasCcGenericServices()) {
                        setCcGenericServices(fileOptions.getCcGenericServices());
                    }
                    if (fileOptions.hasJavaGenericServices()) {
                        setJavaGenericServices(fileOptions.getJavaGenericServices());
                    }
                    if (fileOptions.hasPyGenericServices()) {
                        setPyGenericServices(fileOptions.getPyGenericServices());
                    }
                    if (this.uninterpretedOptionBuilder_ == null) {
                        if (!fileOptions.uninterpretedOption_.isEmpty()) {
                            if (this.uninterpretedOption_.isEmpty()) {
                                this.uninterpretedOption_ = fileOptions.uninterpretedOption_;
                                this.bitField0_ &= -257;
                            } else {
                                ensureUninterpretedOptionIsMutable();
                                this.uninterpretedOption_.addAll(fileOptions.uninterpretedOption_);
                            }
                            onChanged();
                        }
                    } else if (!fileOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOptionBuilder_.isEmpty()) {
                            this.uninterpretedOptionBuilder_.dispose();
                            this.uninterpretedOptionBuilder_ = null;
                            this.uninterpretedOption_ = fileOptions.uninterpretedOption_;
                            this.bitField0_ &= -257;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getUninterpretedOptionFieldBuilder();
                            }
                            this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.uninterpretedOptionBuilder_.addAllMessages(fileOptions.uninterpretedOption_);
                        }
                    }
                    mergeExtensionFields(fileOptions);
                    mergeUnknownFields(fileOptions.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof FileOptions) {
                    return mergeFrom((FileOptions) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int i) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(i);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.remove(i);
                }
                return this;
            }

            public Builder setCcGenericServices(boolean z) {
                this.bitField0_ |= 32;
                this.ccGenericServices_ = z;
                onChanged();
                return this;
            }

            public Builder setJavaGenerateEqualsAndHash(boolean z) {
                this.bitField0_ |= 8;
                this.javaGenerateEqualsAndHash_ = z;
                onChanged();
                return this;
            }

            public Builder setJavaGenericServices(boolean z) {
                this.bitField0_ |= 64;
                this.javaGenericServices_ = z;
                onChanged();
                return this;
            }

            public Builder setJavaMultipleFiles(boolean z) {
                this.bitField0_ |= 4;
                this.javaMultipleFiles_ = z;
                onChanged();
                return this;
            }

            public Builder setJavaOuterClassname(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.javaOuterClassname_ = str;
                onChanged();
                return this;
            }

            void setJavaOuterClassname(ByteString byteString) {
                this.bitField0_ |= 2;
                this.javaOuterClassname_ = byteString;
                onChanged();
            }

            public Builder setJavaPackage(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.javaPackage_ = str;
                onChanged();
                return this;
            }

            void setJavaPackage(ByteString byteString) {
                this.bitField0_ |= 1;
                this.javaPackage_ = byteString;
                onChanged();
            }

            public Builder setOptimizeFor(OptimizeMode optimizeMode) {
                if (optimizeMode == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 16;
                this.optimizeFor_ = optimizeMode;
                onChanged();
                return this;
            }

            public Builder setPyGenericServices(boolean z) {
                this.bitField0_ |= 128;
                this.pyGenericServices_ = z;
                onChanged();
                return this;
            }

            public Builder setUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.setMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }
        }

        public enum OptimizeMode implements ProtocolMessageEnum {
            SPEED(0, 1),
            CODE_SIZE(1, 2),
            LITE_RUNTIME(2, 3);
            
            public static final int CODE_SIZE_VALUE = 2;
            public static final int LITE_RUNTIME_VALUE = 3;
            public static final int SPEED_VALUE = 1;
            private static final OptimizeMode[] VALUES = null;
            private static EnumLiteMap<OptimizeMode> internalValueMap;
            private final int index;
            private final int value;

            static final class C09441 implements EnumLiteMap<OptimizeMode> {
                C09441() {
                }

                public OptimizeMode findValueByNumber(int i) {
                    return OptimizeMode.valueOf(i);
                }
            }

            static {
                internalValueMap = new C09441();
                VALUES = new OptimizeMode[]{SPEED, CODE_SIZE, LITE_RUNTIME};
            }

            private OptimizeMode(int i, int i2) {
                this.index = i;
                this.value = i2;
            }

            public static final EnumDescriptor getDescriptor() {
                return (EnumDescriptor) FileOptions.getDescriptor().getEnumTypes().get(0);
            }

            public static EnumLiteMap<OptimizeMode> internalGetValueMap() {
                return internalValueMap;
            }

            public static OptimizeMode valueOf(int i) {
                switch (i) {
                    case 1:
                        return SPEED;
                    case 2:
                        return CODE_SIZE;
                    case 3:
                        return LITE_RUNTIME;
                    default:
                        return null;
                }
            }

            public static OptimizeMode valueOf(EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public final EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public final int getNumber() {
                return this.value;
            }

            public final EnumValueDescriptor getValueDescriptor() {
                return (EnumValueDescriptor) getDescriptor().getValues().get(this.index);
            }
        }

        static {
            defaultInstance.initFields();
        }

        private FileOptions(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private FileOptions(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static FileOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
        }

        private ByteString getJavaOuterClassnameBytes() {
            Object obj = this.javaOuterClassname_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.javaOuterClassname_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private ByteString getJavaPackageBytes() {
            Object obj = this.javaPackage_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.javaPackage_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.javaPackage_ = HttpVersions.HTTP_0_9;
            this.javaOuterClassname_ = HttpVersions.HTTP_0_9;
            this.javaMultipleFiles_ = false;
            this.javaGenerateEqualsAndHash_ = false;
            this.optimizeFor_ = OptimizeMode.SPEED;
            this.ccGenericServices_ = false;
            this.javaGenericServices_ = false;
            this.pyGenericServices_ = false;
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FileOptions fileOptions) {
            return newBuilder().mergeFrom(fileOptions);
        }

        public static FileOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static FileOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static FileOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static FileOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static FileOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static FileOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static FileOptions parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static FileOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static FileOptions parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static FileOptions parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public boolean getCcGenericServices() {
            return this.ccGenericServices_;
        }

        public FileOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean getJavaGenerateEqualsAndHash() {
            return this.javaGenerateEqualsAndHash_;
        }

        public boolean getJavaGenericServices() {
            return this.javaGenericServices_;
        }

        public boolean getJavaMultipleFiles() {
            return this.javaMultipleFiles_;
        }

        public String getJavaOuterClassname() {
            Object obj = this.javaOuterClassname_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.javaOuterClassname_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public String getJavaPackage() {
            Object obj = this.javaPackage_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.javaPackage_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public OptimizeMode getOptimizeFor() {
            return this.optimizeFor_;
        }

        public boolean getPyGenericServices() {
            return this.pyGenericServices_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            i = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBytesSize(1, getJavaPackageBytes()) + 0 : 0;
            if ((this.bitField0_ & 2) == 2) {
                i += CodedOutputStream.computeBytesSize(8, getJavaOuterClassnameBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                i += CodedOutputStream.computeEnumSize(9, this.optimizeFor_.getNumber());
            }
            if ((this.bitField0_ & 4) == 4) {
                i += CodedOutputStream.computeBoolSize(10, this.javaMultipleFiles_);
            }
            if ((this.bitField0_ & 32) == 32) {
                i += CodedOutputStream.computeBoolSize(16, this.ccGenericServices_);
            }
            if ((this.bitField0_ & 64) == 64) {
                i += CodedOutputStream.computeBoolSize(17, this.javaGenericServices_);
            }
            if ((this.bitField0_ & 128) == 128) {
                i += CodedOutputStream.computeBoolSize(18, this.pyGenericServices_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i += CodedOutputStream.computeBoolSize(20, this.javaGenerateEqualsAndHash_);
            }
            int i2 = 0;
            int i3 = i;
            while (i2 < this.uninterpretedOption_.size()) {
                i = CodedOutputStream.computeMessageSize(999, (MessageLite) this.uninterpretedOption_.get(i2)) + i3;
                i2++;
                i3 = i;
            }
            i = (extensionsSerializedSize() + i3) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = i;
            return i;
        }

        public UninterpretedOption getUninterpretedOption(int i) {
            return (UninterpretedOption) this.uninterpretedOption_.get(i);
        }

        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
            return (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i);
        }

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        public boolean hasCcGenericServices() {
            return (this.bitField0_ & 32) == 32;
        }

        public boolean hasJavaGenerateEqualsAndHash() {
            return (this.bitField0_ & 8) == 8;
        }

        public boolean hasJavaGenericServices() {
            return (this.bitField0_ & 64) == 64;
        }

        public boolean hasJavaMultipleFiles() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean hasJavaOuterClassname() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean hasJavaPackage() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasOptimizeFor() {
            return (this.bitField0_ & 16) == 16;
        }

        public boolean hasPyGenericServices() {
            return (this.bitField0_ & 128) == 128;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getUninterpretedOptionCount()) {
                    if (getUninterpretedOption(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            ExtensionWriter newExtensionWriter = newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getJavaPackageBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(8, getJavaOuterClassnameBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeEnum(9, this.optimizeFor_.getNumber());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(10, this.javaMultipleFiles_);
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeBool(16, this.ccGenericServices_);
            }
            if ((this.bitField0_ & 64) == 64) {
                codedOutputStream.writeBool(17, this.javaGenericServices_);
            }
            if ((this.bitField0_ & 128) == 128) {
                codedOutputStream.writeBool(18, this.pyGenericServices_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBool(20, this.javaGenerateEqualsAndHash_);
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); i++) {
                codedOutputStream.writeMessage(999, (MessageLite) this.uninterpretedOption_.get(i));
            }
            newExtensionWriter.writeUntil(536870912, codedOutputStream);
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface MessageOptionsOrBuilder extends ExtendableMessageOrBuilder<MessageOptions> {
        boolean getMessageSetWireFormat();

        boolean getNoStandardDescriptorAccessor();

        UninterpretedOption getUninterpretedOption(int i);

        int getUninterpretedOptionCount();

        List<UninterpretedOption> getUninterpretedOptionList();

        UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i);

        List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();

        boolean hasMessageSetWireFormat();

        boolean hasNoStandardDescriptorAccessor();
    }

    public static final class MessageOptions extends ExtendableMessage<MessageOptions> implements MessageOptionsOrBuilder {
        public static final int MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER = 1;
        public static final int NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER = 2;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final MessageOptions defaultInstance = new MessageOptions(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private boolean messageSetWireFormat_;
        private boolean noStandardDescriptorAccessor_;
        private List<UninterpretedOption> uninterpretedOption_;

        public static final class Builder extends ExtendableBuilder<MessageOptions, Builder> implements MessageOptionsOrBuilder {
            private int bitField0_;
            private boolean messageSetWireFormat_;
            private boolean noStandardDescriptorAccessor_;
            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_;

            private Builder() {
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private MessageOptions buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
                    this.bitField0_ |= 4;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(this.uninterpretedOption_, (this.bitField0_ & 4) == 4, getParentForChildren(), isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.uninterpretedOption_);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOption(Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOptionBuilder() {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public Builder addUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(i, UninterpretedOption.getDefaultInstance());
            }

            public MessageOptions build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public MessageOptions buildPartial() {
                int i = 1;
                MessageOptions messageOptions = new MessageOptions();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                messageOptions.messageSetWireFormat_ = this.messageSetWireFormat_;
                if ((i2 & 2) == 2) {
                    i |= 2;
                }
                messageOptions.noStandardDescriptorAccessor_ = this.noStandardDescriptorAccessor_;
                if (this.uninterpretedOptionBuilder_ == null) {
                    if ((this.bitField0_ & 4) == 4) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -5;
                    }
                    messageOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    messageOptions.uninterpretedOption_ = this.uninterpretedOptionBuilder_.build();
                }
                messageOptions.bitField0_ = i;
                onBuilt();
                return messageOptions;
            }

            public Builder clear() {
                super.clear();
                this.messageSetWireFormat_ = false;
                this.bitField0_ &= -2;
                this.noStandardDescriptorAccessor_ = false;
                this.bitField0_ &= -3;
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clearMessageSetWireFormat() {
                this.bitField0_ &= -2;
                this.messageSetWireFormat_ = false;
                onChanged();
                return this;
            }

            public Builder clearNoStandardDescriptorAccessor() {
                this.bitField0_ &= -3;
                this.noStandardDescriptorAccessor_ = false;
                onChanged();
                return this;
            }

            public Builder clearUninterpretedOption() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public MessageOptions getDefaultInstanceForType() {
                return MessageOptions.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return MessageOptions.getDescriptor();
            }

            public boolean getMessageSetWireFormat() {
                return this.messageSetWireFormat_;
            }

            public boolean getNoStandardDescriptorAccessor() {
                return this.noStandardDescriptorAccessor_;
            }

            public UninterpretedOption getUninterpretedOption(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOption) this.uninterpretedOption_.get(i) : (UninterpretedOption) this.uninterpretedOptionBuilder_.getMessage(i);
            }

            public Builder getUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getUninterpretedOptionBuilderList() {
                return getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            public int getUninterpretedOptionCount() {
                return this.uninterpretedOptionBuilder_ == null ? this.uninterpretedOption_.size() : this.uninterpretedOptionBuilder_.getCount();
            }

            public List<UninterpretedOption> getUninterpretedOptionList() {
                return this.uninterpretedOptionBuilder_ == null ? Collections.unmodifiableList(this.uninterpretedOption_) : this.uninterpretedOptionBuilder_.getMessageList();
            }

            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i) : (UninterpretedOptionOrBuilder) this.uninterpretedOptionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                return this.uninterpretedOptionBuilder_ != null ? this.uninterpretedOptionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
            }

            public boolean hasMessageSetWireFormat() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasNoStandardDescriptorAccessor() {
                return (this.bitField0_ & 2) == 2;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f286x9c0b3d38;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getUninterpretedOptionCount(); i++) {
                    if (!getUninterpretedOption(i).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 8:
                            this.bitField0_ |= 1;
                            this.messageSetWireFormat_ = codedInputStream.readBool();
                            continue;
                        case 16:
                            this.bitField0_ |= 2;
                            this.noStandardDescriptorAccessor_ = codedInputStream.readBool();
                            continue;
                        case 7994:
                            Object newBuilder2 = UninterpretedOption.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addUninterpretedOption(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(MessageOptions messageOptions) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (messageOptions != MessageOptions.getDefaultInstance()) {
                    if (messageOptions.hasMessageSetWireFormat()) {
                        setMessageSetWireFormat(messageOptions.getMessageSetWireFormat());
                    }
                    if (messageOptions.hasNoStandardDescriptorAccessor()) {
                        setNoStandardDescriptorAccessor(messageOptions.getNoStandardDescriptorAccessor());
                    }
                    if (this.uninterpretedOptionBuilder_ == null) {
                        if (!messageOptions.uninterpretedOption_.isEmpty()) {
                            if (this.uninterpretedOption_.isEmpty()) {
                                this.uninterpretedOption_ = messageOptions.uninterpretedOption_;
                                this.bitField0_ &= -5;
                            } else {
                                ensureUninterpretedOptionIsMutable();
                                this.uninterpretedOption_.addAll(messageOptions.uninterpretedOption_);
                            }
                            onChanged();
                        }
                    } else if (!messageOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOptionBuilder_.isEmpty()) {
                            this.uninterpretedOptionBuilder_.dispose();
                            this.uninterpretedOptionBuilder_ = null;
                            this.uninterpretedOption_ = messageOptions.uninterpretedOption_;
                            this.bitField0_ &= -5;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getUninterpretedOptionFieldBuilder();
                            }
                            this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.uninterpretedOptionBuilder_.addAllMessages(messageOptions.uninterpretedOption_);
                        }
                    }
                    mergeExtensionFields(messageOptions);
                    mergeUnknownFields(messageOptions.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof MessageOptions) {
                    return mergeFrom((MessageOptions) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int i) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(i);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.remove(i);
                }
                return this;
            }

            public Builder setMessageSetWireFormat(boolean z) {
                this.bitField0_ |= 1;
                this.messageSetWireFormat_ = z;
                onChanged();
                return this;
            }

            public Builder setNoStandardDescriptorAccessor(boolean z) {
                this.bitField0_ |= 2;
                this.noStandardDescriptorAccessor_ = z;
                onChanged();
                return this;
            }

            public Builder setUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.setMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private MessageOptions(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private MessageOptions(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static MessageOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
        }

        private void initFields() {
            this.messageSetWireFormat_ = false;
            this.noStandardDescriptorAccessor_ = false;
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(MessageOptions messageOptions) {
            return newBuilder().mergeFrom(messageOptions);
        }

        public static MessageOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static MessageOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static MessageOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static MessageOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static MessageOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static MessageOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static MessageOptions parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static MessageOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static MessageOptions parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static MessageOptions parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public MessageOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean getMessageSetWireFormat() {
            return this.messageSetWireFormat_;
        }

        public boolean getNoStandardDescriptorAccessor() {
            return this.noStandardDescriptorAccessor_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            i = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBoolSize(1, this.messageSetWireFormat_) + 0 : 0;
            if ((this.bitField0_ & 2) == 2) {
                i += CodedOutputStream.computeBoolSize(2, this.noStandardDescriptorAccessor_);
            }
            int i2 = 0;
            int i3 = i;
            while (i2 < this.uninterpretedOption_.size()) {
                i = CodedOutputStream.computeMessageSize(999, (MessageLite) this.uninterpretedOption_.get(i2)) + i3;
                i2++;
                i3 = i;
            }
            i = (extensionsSerializedSize() + i3) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = i;
            return i;
        }

        public UninterpretedOption getUninterpretedOption(int i) {
            return (UninterpretedOption) this.uninterpretedOption_.get(i);
        }

        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
            return (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i);
        }

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        public boolean hasMessageSetWireFormat() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasNoStandardDescriptorAccessor() {
            return (this.bitField0_ & 2) == 2;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f286x9c0b3d38;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getUninterpretedOptionCount()) {
                    if (getUninterpretedOption(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            ExtensionWriter newExtensionWriter = newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBool(1, this.messageSetWireFormat_);
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBool(2, this.noStandardDescriptorAccessor_);
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); i++) {
                codedOutputStream.writeMessage(999, (MessageLite) this.uninterpretedOption_.get(i));
            }
            newExtensionWriter.writeUntil(536870912, codedOutputStream);
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface MethodDescriptorProtoOrBuilder extends MessageOrBuilder {
        String getInputType();

        String getName();

        MethodOptions getOptions();

        MethodOptionsOrBuilder getOptionsOrBuilder();

        String getOutputType();

        boolean hasInputType();

        boolean hasName();

        boolean hasOptions();

        boolean hasOutputType();
    }

    public static final class MethodDescriptorProto extends GeneratedMessage implements MethodDescriptorProtoOrBuilder {
        public static final int INPUT_TYPE_FIELD_NUMBER = 2;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 4;
        public static final int OUTPUT_TYPE_FIELD_NUMBER = 3;
        private static final MethodDescriptorProto defaultInstance = new MethodDescriptorProto(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private Object inputType_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private Object name_;
        private MethodOptions options_;
        private Object outputType_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements MethodDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object inputType_;
            private Object name_;
            private SingleFieldBuilder<MethodOptions, Builder, MethodOptionsOrBuilder> optionsBuilder_;
            private MethodOptions options_;
            private Object outputType_;

            private Builder() {
                this.name_ = HttpVersions.HTTP_0_9;
                this.inputType_ = HttpVersions.HTTP_0_9;
                this.outputType_ = HttpVersions.HTTP_0_9;
                this.options_ = MethodOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = HttpVersions.HTTP_0_9;
                this.inputType_ = HttpVersions.HTTP_0_9;
                this.outputType_ = HttpVersions.HTTP_0_9;
                this.options_ = MethodOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private MethodDescriptorProto buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
            }

            private SingleFieldBuilder<MethodOptions, Builder, MethodOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, getParentForChildren(), isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getOptionsFieldBuilder();
                }
            }

            public MethodDescriptorProto build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public MethodDescriptorProto buildPartial() {
                int i = 1;
                MethodDescriptorProto methodDescriptorProto = new MethodDescriptorProto();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                methodDescriptorProto.name_ = this.name_;
                if ((i2 & 2) == 2) {
                    i |= 2;
                }
                methodDescriptorProto.inputType_ = this.inputType_;
                if ((i2 & 4) == 4) {
                    i |= 4;
                }
                methodDescriptorProto.outputType_ = this.outputType_;
                int i3 = (i2 & 8) == 8 ? i | 8 : i;
                if (this.optionsBuilder_ == null) {
                    methodDescriptorProto.options_ = this.options_;
                } else {
                    methodDescriptorProto.options_ = (MethodOptions) this.optionsBuilder_.build();
                }
                methodDescriptorProto.bitField0_ = i3;
                onBuilt();
                return methodDescriptorProto;
            }

            public Builder clear() {
                super.clear();
                this.name_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                this.inputType_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -3;
                this.outputType_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -5;
                if (this.optionsBuilder_ == null) {
                    this.options_ = MethodOptions.getDefaultInstance();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -9;
                return this;
            }

            public Builder clearInputType() {
                this.bitField0_ &= -3;
                this.inputType_ = MethodDescriptorProto.getDefaultInstance().getInputType();
                onChanged();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = MethodDescriptorProto.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder clearOptions() {
                if (this.optionsBuilder_ == null) {
                    this.options_ = MethodOptions.getDefaultInstance();
                    onChanged();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -9;
                return this;
            }

            public Builder clearOutputType() {
                this.bitField0_ &= -5;
                this.outputType_ = MethodDescriptorProto.getDefaultInstance().getOutputType();
                onChanged();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public MethodDescriptorProto getDefaultInstanceForType() {
                return MethodDescriptorProto.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return MethodDescriptorProto.getDescriptor();
            }

            public String getInputType() {
                Object obj = this.inputType_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.inputType_ = toStringUtf8;
                return toStringUtf8;
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = toStringUtf8;
                return toStringUtf8;
            }

            public MethodOptions getOptions() {
                return this.optionsBuilder_ == null ? this.options_ : (MethodOptions) this.optionsBuilder_.getMessage();
            }

            public Builder getOptionsBuilder() {
                this.bitField0_ |= 8;
                onChanged();
                return (Builder) getOptionsFieldBuilder().getBuilder();
            }

            public MethodOptionsOrBuilder getOptionsOrBuilder() {
                return this.optionsBuilder_ != null ? (MethodOptionsOrBuilder) this.optionsBuilder_.getMessageOrBuilder() : this.options_;
            }

            public String getOutputType() {
                Object obj = this.outputType_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.outputType_ = toStringUtf8;
                return toStringUtf8;
            }

            public boolean hasInputType() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasOptions() {
                return (this.bitField0_ & 8) == 8;
            }

            public boolean hasOutputType() {
                return (this.bitField0_ & 4) == 4;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f287xc5331ef1;
            }

            public final boolean isInitialized() {
                return !hasOptions() || getOptions().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.name_ = codedInputStream.readBytes();
                            continue;
                        case 18:
                            this.bitField0_ |= 2;
                            this.inputType_ = codedInputStream.readBytes();
                            continue;
                        case 26:
                            this.bitField0_ |= 4;
                            this.outputType_ = codedInputStream.readBytes();
                            continue;
                        case 34:
                            Object newBuilder2 = MethodOptions.newBuilder();
                            if (hasOptions()) {
                                newBuilder2.mergeFrom(getOptions());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setOptions(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(MethodDescriptorProto methodDescriptorProto) {
                if (methodDescriptorProto != MethodDescriptorProto.getDefaultInstance()) {
                    if (methodDescriptorProto.hasName()) {
                        setName(methodDescriptorProto.getName());
                    }
                    if (methodDescriptorProto.hasInputType()) {
                        setInputType(methodDescriptorProto.getInputType());
                    }
                    if (methodDescriptorProto.hasOutputType()) {
                        setOutputType(methodDescriptorProto.getOutputType());
                    }
                    if (methodDescriptorProto.hasOptions()) {
                        mergeOptions(methodDescriptorProto.getOptions());
                    }
                    mergeUnknownFields(methodDescriptorProto.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof MethodDescriptorProto) {
                    return mergeFrom((MethodDescriptorProto) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(MethodOptions methodOptions) {
                if (this.optionsBuilder_ == null) {
                    if ((this.bitField0_ & 8) != 8 || this.options_ == MethodOptions.getDefaultInstance()) {
                        this.options_ = methodOptions;
                    } else {
                        this.options_ = MethodOptions.newBuilder(this.options_).mergeFrom(methodOptions).buildPartial();
                    }
                    onChanged();
                } else {
                    this.optionsBuilder_.mergeFrom(methodOptions);
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder setInputType(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.inputType_ = str;
                onChanged();
                return this;
            }

            void setInputType(ByteString byteString) {
                this.bitField0_ |= 2;
                this.inputType_ = byteString;
                onChanged();
            }

            public Builder setName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = str;
                onChanged();
                return this;
            }

            void setName(ByteString byteString) {
                this.bitField0_ |= 1;
                this.name_ = byteString;
                onChanged();
            }

            public Builder setOptions(Builder builder) {
                if (this.optionsBuilder_ == null) {
                    this.options_ = builder.build();
                    onChanged();
                } else {
                    this.optionsBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder setOptions(MethodOptions methodOptions) {
                if (this.optionsBuilder_ != null) {
                    this.optionsBuilder_.setMessage(methodOptions);
                } else if (methodOptions == null) {
                    throw new NullPointerException();
                } else {
                    this.options_ = methodOptions;
                    onChanged();
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder setOutputType(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.outputType_ = str;
                onChanged();
                return this;
            }

            void setOutputType(ByteString byteString) {
                this.bitField0_ |= 4;
                this.outputType_ = byteString;
                onChanged();
            }
        }

        static {
            defaultInstance.initFields();
        }

        private MethodDescriptorProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private MethodDescriptorProto(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static MethodDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
        }

        private ByteString getInputTypeBytes() {
            Object obj = this.inputType_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.inputType_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private ByteString getOutputTypeBytes() {
            Object obj = this.outputType_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.outputType_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = HttpVersions.HTTP_0_9;
            this.inputType_ = HttpVersions.HTTP_0_9;
            this.outputType_ = HttpVersions.HTTP_0_9;
            this.options_ = MethodOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(MethodDescriptorProto methodDescriptorProto) {
            return newBuilder().mergeFrom(methodDescriptorProto);
        }

        public static MethodDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static MethodDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static MethodDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static MethodDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static MethodDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static MethodDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static MethodDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static MethodDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static MethodDescriptorProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static MethodDescriptorProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public MethodDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public String getInputType() {
            Object obj = this.inputType_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.inputType_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public MethodOptions getOptions() {
            return this.options_;
        }

        public MethodOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public String getOutputType() {
            Object obj = this.outputType_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.outputType_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            i = 0;
            if ((this.bitField0_ & 1) == 1) {
                i = CodedOutputStream.computeBytesSize(1, getNameBytes()) + 0;
            }
            if ((this.bitField0_ & 2) == 2) {
                i += CodedOutputStream.computeBytesSize(2, getInputTypeBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                i += CodedOutputStream.computeBytesSize(3, getOutputTypeBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                i += CodedOutputStream.computeMessageSize(4, this.options_);
            }
            i += getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = i;
            return i;
        }

        public boolean hasInputType() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasOptions() {
            return (this.bitField0_ & 8) == 8;
        }

        public boolean hasOutputType() {
            return (this.bitField0_ & 4) == 4;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f287xc5331ef1;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                if (!hasOptions() || getOptions().isInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getInputTypeBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBytes(3, getOutputTypeBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeMessage(4, this.options_);
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface MethodOptionsOrBuilder extends ExtendableMessageOrBuilder<MethodOptions> {
        UninterpretedOption getUninterpretedOption(int i);

        int getUninterpretedOptionCount();

        List<UninterpretedOption> getUninterpretedOptionList();

        UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i);

        List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();
    }

    public static final class MethodOptions extends ExtendableMessage<MethodOptions> implements MethodOptionsOrBuilder {
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final MethodOptions defaultInstance = new MethodOptions(true);
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<UninterpretedOption> uninterpretedOption_;

        public static final class Builder extends ExtendableBuilder<MethodOptions, Builder> implements MethodOptionsOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_;

            private Builder() {
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private MethodOptions buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                boolean z = true;
                if (this.uninterpretedOptionBuilder_ == null) {
                    List list = this.uninterpretedOption_;
                    if ((this.bitField0_ & 1) != 1) {
                        z = false;
                    }
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, z, getParentForChildren(), isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.uninterpretedOption_);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOption(Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOptionBuilder() {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public Builder addUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(i, UninterpretedOption.getDefaultInstance());
            }

            public MethodOptions build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public MethodOptions buildPartial() {
                MethodOptions methodOptions = new MethodOptions();
                int i = this.bitField0_;
                if (this.uninterpretedOptionBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -2;
                    }
                    methodOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    methodOptions.uninterpretedOption_ = this.uninterpretedOptionBuilder_.build();
                }
                onBuilt();
                return methodOptions;
            }

            public Builder clear() {
                super.clear();
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clearUninterpretedOption() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public MethodOptions getDefaultInstanceForType() {
                return MethodOptions.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return MethodOptions.getDescriptor();
            }

            public UninterpretedOption getUninterpretedOption(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOption) this.uninterpretedOption_.get(i) : (UninterpretedOption) this.uninterpretedOptionBuilder_.getMessage(i);
            }

            public Builder getUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getUninterpretedOptionBuilderList() {
                return getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            public int getUninterpretedOptionCount() {
                return this.uninterpretedOptionBuilder_ == null ? this.uninterpretedOption_.size() : this.uninterpretedOptionBuilder_.getCount();
            }

            public List<UninterpretedOption> getUninterpretedOptionList() {
                return this.uninterpretedOptionBuilder_ == null ? Collections.unmodifiableList(this.uninterpretedOption_) : this.uninterpretedOptionBuilder_.getMessageList();
            }

            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i) : (UninterpretedOptionOrBuilder) this.uninterpretedOptionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                return this.uninterpretedOptionBuilder_ != null ? this.uninterpretedOptionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getUninterpretedOptionCount(); i++) {
                    if (!getUninterpretedOption(i).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 7994:
                            Object newBuilder2 = UninterpretedOption.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addUninterpretedOption(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(MethodOptions methodOptions) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (methodOptions != MethodOptions.getDefaultInstance()) {
                    if (this.uninterpretedOptionBuilder_ == null) {
                        if (!methodOptions.uninterpretedOption_.isEmpty()) {
                            if (this.uninterpretedOption_.isEmpty()) {
                                this.uninterpretedOption_ = methodOptions.uninterpretedOption_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureUninterpretedOptionIsMutable();
                                this.uninterpretedOption_.addAll(methodOptions.uninterpretedOption_);
                            }
                            onChanged();
                        }
                    } else if (!methodOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOptionBuilder_.isEmpty()) {
                            this.uninterpretedOptionBuilder_.dispose();
                            this.uninterpretedOptionBuilder_ = null;
                            this.uninterpretedOption_ = methodOptions.uninterpretedOption_;
                            this.bitField0_ &= -2;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getUninterpretedOptionFieldBuilder();
                            }
                            this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.uninterpretedOptionBuilder_.addAllMessages(methodOptions.uninterpretedOption_);
                        }
                    }
                    mergeExtensionFields(methodOptions);
                    mergeUnknownFields(methodOptions.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof MethodOptions) {
                    return mergeFrom((MethodOptions) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int i) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(i);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.remove(i);
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.setMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private MethodOptions(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private MethodOptions(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static MethodOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
        }

        private void initFields() {
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(MethodOptions methodOptions) {
            return newBuilder().mergeFrom(methodOptions);
        }

        public static MethodOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static MethodOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static MethodOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static MethodOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static MethodOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static MethodOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static MethodOptions parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static MethodOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static MethodOptions parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static MethodOptions parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public MethodOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeMessageSize;
            i = 0;
            int i2 = 0;
            while (i2 < this.uninterpretedOption_.size()) {
                computeMessageSize = CodedOutputStream.computeMessageSize(999, (MessageLite) this.uninterpretedOption_.get(i2)) + i;
                i2++;
                i = computeMessageSize;
            }
            computeMessageSize = (extensionsSerializedSize() + i) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = computeMessageSize;
            return computeMessageSize;
        }

        public UninterpretedOption getUninterpretedOption(int i) {
            return (UninterpretedOption) this.uninterpretedOption_.get(i);
        }

        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
            return (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i);
        }

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getUninterpretedOptionCount()) {
                    if (getUninterpretedOption(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            ExtensionWriter newExtensionWriter = newExtensionWriter();
            for (int i = 0; i < this.uninterpretedOption_.size(); i++) {
                codedOutputStream.writeMessage(999, (MessageLite) this.uninterpretedOption_.get(i));
            }
            newExtensionWriter.writeUntil(536870912, codedOutputStream);
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface ServiceDescriptorProtoOrBuilder extends MessageOrBuilder {
        MethodDescriptorProto getMethod(int i);

        int getMethodCount();

        List<MethodDescriptorProto> getMethodList();

        MethodDescriptorProtoOrBuilder getMethodOrBuilder(int i);

        List<? extends MethodDescriptorProtoOrBuilder> getMethodOrBuilderList();

        String getName();

        ServiceOptions getOptions();

        ServiceOptionsOrBuilder getOptionsOrBuilder();

        boolean hasName();

        boolean hasOptions();
    }

    public static final class ServiceDescriptorProto extends GeneratedMessage implements ServiceDescriptorProtoOrBuilder {
        public static final int METHOD_FIELD_NUMBER = 2;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 3;
        private static final ServiceDescriptorProto defaultInstance = new ServiceDescriptorProto(true);
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<MethodDescriptorProto> method_;
        private Object name_;
        private ServiceOptions options_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements ServiceDescriptorProtoOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<MethodDescriptorProto, Builder, MethodDescriptorProtoOrBuilder> methodBuilder_;
            private List<MethodDescriptorProto> method_;
            private Object name_;
            private SingleFieldBuilder<ServiceOptions, Builder, ServiceOptionsOrBuilder> optionsBuilder_;
            private ServiceOptions options_;

            private Builder() {
                this.name_ = HttpVersions.HTTP_0_9;
                this.method_ = Collections.emptyList();
                this.options_ = ServiceOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = HttpVersions.HTTP_0_9;
                this.method_ = Collections.emptyList();
                this.options_ = ServiceOptions.getDefaultInstance();
                maybeForceBuilderInitialization();
            }

            private ServiceDescriptorProto buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureMethodIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.method_ = new ArrayList(this.method_);
                    this.bitField0_ |= 2;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.f288x158c73ed;
            }

            private RepeatedFieldBuilder<MethodDescriptorProto, Builder, MethodDescriptorProtoOrBuilder> getMethodFieldBuilder() {
                if (this.methodBuilder_ == null) {
                    this.methodBuilder_ = new RepeatedFieldBuilder(this.method_, (this.bitField0_ & 2) == 2, getParentForChildren(), isClean());
                    this.method_ = null;
                }
                return this.methodBuilder_;
            }

            private SingleFieldBuilder<ServiceOptions, Builder, ServiceOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, getParentForChildren(), isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getMethodFieldBuilder();
                    getOptionsFieldBuilder();
                }
            }

            public Builder addAllMethod(Iterable<? extends MethodDescriptorProto> iterable) {
                if (this.methodBuilder_ == null) {
                    ensureMethodIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.method_);
                    onChanged();
                } else {
                    this.methodBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addMethod(int i, Builder builder) {
                if (this.methodBuilder_ == null) {
                    ensureMethodIsMutable();
                    this.method_.add(i, builder.build());
                    onChanged();
                } else {
                    this.methodBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addMethod(int i, MethodDescriptorProto methodDescriptorProto) {
                if (this.methodBuilder_ != null) {
                    this.methodBuilder_.addMessage(i, methodDescriptorProto);
                } else if (methodDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureMethodIsMutable();
                    this.method_.add(i, methodDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addMethod(Builder builder) {
                if (this.methodBuilder_ == null) {
                    ensureMethodIsMutable();
                    this.method_.add(builder.build());
                    onChanged();
                } else {
                    this.methodBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addMethod(MethodDescriptorProto methodDescriptorProto) {
                if (this.methodBuilder_ != null) {
                    this.methodBuilder_.addMessage(methodDescriptorProto);
                } else if (methodDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureMethodIsMutable();
                    this.method_.add(methodDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder addMethodBuilder() {
                return (Builder) getMethodFieldBuilder().addBuilder(MethodDescriptorProto.getDefaultInstance());
            }

            public Builder addMethodBuilder(int i) {
                return (Builder) getMethodFieldBuilder().addBuilder(i, MethodDescriptorProto.getDefaultInstance());
            }

            public ServiceDescriptorProto build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public ServiceDescriptorProto buildPartial() {
                int i = 1;
                ServiceDescriptorProto serviceDescriptorProto = new ServiceDescriptorProto();
                int i2 = this.bitField0_;
                if ((i2 & 1) != 1) {
                    i = 0;
                }
                serviceDescriptorProto.name_ = this.name_;
                if (this.methodBuilder_ == null) {
                    if ((this.bitField0_ & 2) == 2) {
                        this.method_ = Collections.unmodifiableList(this.method_);
                        this.bitField0_ &= -3;
                    }
                    serviceDescriptorProto.method_ = this.method_;
                } else {
                    serviceDescriptorProto.method_ = this.methodBuilder_.build();
                }
                int i3 = (i2 & 4) == 4 ? i | 2 : i;
                if (this.optionsBuilder_ == null) {
                    serviceDescriptorProto.options_ = this.options_;
                } else {
                    serviceDescriptorProto.options_ = (ServiceOptions) this.optionsBuilder_.build();
                }
                serviceDescriptorProto.bitField0_ = i3;
                onBuilt();
                return serviceDescriptorProto;
            }

            public Builder clear() {
                super.clear();
                this.name_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -2;
                if (this.methodBuilder_ == null) {
                    this.method_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.methodBuilder_.clear();
                }
                if (this.optionsBuilder_ == null) {
                    this.options_ = ServiceOptions.getDefaultInstance();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearMethod() {
                if (this.methodBuilder_ == null) {
                    this.method_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.methodBuilder_.clear();
                }
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = ServiceDescriptorProto.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder clearOptions() {
                if (this.optionsBuilder_ == null) {
                    this.options_ = ServiceOptions.getDefaultInstance();
                    onChanged();
                } else {
                    this.optionsBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public ServiceDescriptorProto getDefaultInstanceForType() {
                return ServiceDescriptorProto.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return ServiceDescriptorProto.getDescriptor();
            }

            public MethodDescriptorProto getMethod(int i) {
                return this.methodBuilder_ == null ? (MethodDescriptorProto) this.method_.get(i) : (MethodDescriptorProto) this.methodBuilder_.getMessage(i);
            }

            public Builder getMethodBuilder(int i) {
                return (Builder) getMethodFieldBuilder().getBuilder(i);
            }

            public List<Builder> getMethodBuilderList() {
                return getMethodFieldBuilder().getBuilderList();
            }

            public int getMethodCount() {
                return this.methodBuilder_ == null ? this.method_.size() : this.methodBuilder_.getCount();
            }

            public List<MethodDescriptorProto> getMethodList() {
                return this.methodBuilder_ == null ? Collections.unmodifiableList(this.method_) : this.methodBuilder_.getMessageList();
            }

            public MethodDescriptorProtoOrBuilder getMethodOrBuilder(int i) {
                return this.methodBuilder_ == null ? (MethodDescriptorProtoOrBuilder) this.method_.get(i) : (MethodDescriptorProtoOrBuilder) this.methodBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends MethodDescriptorProtoOrBuilder> getMethodOrBuilderList() {
                return this.methodBuilder_ != null ? this.methodBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.method_);
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = toStringUtf8;
                return toStringUtf8;
            }

            public ServiceOptions getOptions() {
                return this.optionsBuilder_ == null ? this.options_ : (ServiceOptions) this.optionsBuilder_.getMessage();
            }

            public Builder getOptionsBuilder() {
                this.bitField0_ |= 4;
                onChanged();
                return (Builder) getOptionsFieldBuilder().getBuilder();
            }

            public ServiceOptionsOrBuilder getOptionsOrBuilder() {
                return this.optionsBuilder_ != null ? (ServiceOptionsOrBuilder) this.optionsBuilder_.getMessageOrBuilder() : this.options_;
            }

            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean hasOptions() {
                return (this.bitField0_ & 4) == 4;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f289xee335d6b;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getMethodCount(); i++) {
                    if (!getMethod(i).isInitialized()) {
                        return false;
                    }
                }
                return !hasOptions() || getOptions().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    Object newBuilder2;
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            this.bitField0_ |= 1;
                            this.name_ = codedInputStream.readBytes();
                            continue;
                        case 18:
                            newBuilder2 = MethodDescriptorProto.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addMethod(newBuilder2.buildPartial());
                            continue;
                        case 26:
                            newBuilder2 = ServiceOptions.newBuilder();
                            if (hasOptions()) {
                                newBuilder2.mergeFrom(getOptions());
                            }
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            setOptions(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(ServiceDescriptorProto serviceDescriptorProto) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (serviceDescriptorProto != ServiceDescriptorProto.getDefaultInstance()) {
                    if (serviceDescriptorProto.hasName()) {
                        setName(serviceDescriptorProto.getName());
                    }
                    if (this.methodBuilder_ == null) {
                        if (!serviceDescriptorProto.method_.isEmpty()) {
                            if (this.method_.isEmpty()) {
                                this.method_ = serviceDescriptorProto.method_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureMethodIsMutable();
                                this.method_.addAll(serviceDescriptorProto.method_);
                            }
                            onChanged();
                        }
                    } else if (!serviceDescriptorProto.method_.isEmpty()) {
                        if (this.methodBuilder_.isEmpty()) {
                            this.methodBuilder_.dispose();
                            this.methodBuilder_ = null;
                            this.method_ = serviceDescriptorProto.method_;
                            this.bitField0_ &= -3;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getMethodFieldBuilder();
                            }
                            this.methodBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.methodBuilder_.addAllMessages(serviceDescriptorProto.method_);
                        }
                    }
                    if (serviceDescriptorProto.hasOptions()) {
                        mergeOptions(serviceDescriptorProto.getOptions());
                    }
                    mergeUnknownFields(serviceDescriptorProto.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof ServiceDescriptorProto) {
                    return mergeFrom((ServiceDescriptorProto) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(ServiceOptions serviceOptions) {
                if (this.optionsBuilder_ == null) {
                    if ((this.bitField0_ & 4) != 4 || this.options_ == ServiceOptions.getDefaultInstance()) {
                        this.options_ = serviceOptions;
                    } else {
                        this.options_ = ServiceOptions.newBuilder(this.options_).mergeFrom(serviceOptions).buildPartial();
                    }
                    onChanged();
                } else {
                    this.optionsBuilder_.mergeFrom(serviceOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder removeMethod(int i) {
                if (this.methodBuilder_ == null) {
                    ensureMethodIsMutable();
                    this.method_.remove(i);
                    onChanged();
                } else {
                    this.methodBuilder_.remove(i);
                }
                return this;
            }

            public Builder setMethod(int i, Builder builder) {
                if (this.methodBuilder_ == null) {
                    ensureMethodIsMutable();
                    this.method_.set(i, builder.build());
                    onChanged();
                } else {
                    this.methodBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setMethod(int i, MethodDescriptorProto methodDescriptorProto) {
                if (this.methodBuilder_ != null) {
                    this.methodBuilder_.setMessage(i, methodDescriptorProto);
                } else if (methodDescriptorProto == null) {
                    throw new NullPointerException();
                } else {
                    ensureMethodIsMutable();
                    this.method_.set(i, methodDescriptorProto);
                    onChanged();
                }
                return this;
            }

            public Builder setName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = str;
                onChanged();
                return this;
            }

            void setName(ByteString byteString) {
                this.bitField0_ |= 1;
                this.name_ = byteString;
                onChanged();
            }

            public Builder setOptions(Builder builder) {
                if (this.optionsBuilder_ == null) {
                    this.options_ = builder.build();
                    onChanged();
                } else {
                    this.optionsBuilder_.setMessage(builder.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setOptions(ServiceOptions serviceOptions) {
                if (this.optionsBuilder_ != null) {
                    this.optionsBuilder_.setMessage(serviceOptions);
                } else if (serviceOptions == null) {
                    throw new NullPointerException();
                } else {
                    this.options_ = serviceOptions;
                    onChanged();
                }
                this.bitField0_ |= 4;
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private ServiceDescriptorProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private ServiceDescriptorProto(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static ServiceDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.f288x158c73ed;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = HttpVersions.HTTP_0_9;
            this.method_ = Collections.emptyList();
            this.options_ = ServiceOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(ServiceDescriptorProto serviceDescriptorProto) {
            return newBuilder().mergeFrom(serviceDescriptorProto);
        }

        public static ServiceDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static ServiceDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static ServiceDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static ServiceDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static ServiceDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static ServiceDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static ServiceDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static ServiceDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static ServiceDescriptorProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static ServiceDescriptorProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public ServiceDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public MethodDescriptorProto getMethod(int i) {
            return (MethodDescriptorProto) this.method_.get(i);
        }

        public int getMethodCount() {
            return this.method_.size();
        }

        public List<MethodDescriptorProto> getMethodList() {
            return this.method_;
        }

        public MethodDescriptorProtoOrBuilder getMethodOrBuilder(int i) {
            return (MethodDescriptorProtoOrBuilder) this.method_.get(i);
        }

        public List<? extends MethodDescriptorProtoOrBuilder> getMethodOrBuilderList() {
            return this.method_;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public ServiceOptions getOptions() {
            return this.options_;
        }

        public ServiceOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public int getSerializedSize() {
            int i = 0;
            int i2 = this.memoizedSerializedSize;
            if (i2 != -1) {
                return i2;
            }
            int computeBytesSize = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBytesSize(1, getNameBytes()) + 0 : 0;
            while (i < this.method_.size()) {
                i++;
                computeBytesSize = CodedOutputStream.computeMessageSize(2, (MessageLite) this.method_.get(i)) + computeBytesSize;
            }
            if ((this.bitField0_ & 2) == 2) {
                computeBytesSize += CodedOutputStream.computeMessageSize(3, this.options_);
            }
            i2 = getUnknownFields().getSerializedSize() + computeBytesSize;
            this.memoizedSerializedSize = i2;
            return i2;
        }

        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasOptions() {
            return (this.bitField0_ & 2) == 2;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f289xee335d6b;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getMethodCount()) {
                    if (getMethod(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (!hasOptions() || getOptions().isInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getNameBytes());
            }
            for (int i = 0; i < this.method_.size(); i++) {
                codedOutputStream.writeMessage(2, (MessageLite) this.method_.get(i));
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeMessage(3, this.options_);
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface ServiceOptionsOrBuilder extends ExtendableMessageOrBuilder<ServiceOptions> {
        UninterpretedOption getUninterpretedOption(int i);

        int getUninterpretedOptionCount();

        List<UninterpretedOption> getUninterpretedOptionList();

        UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i);

        List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();
    }

    public static final class ServiceOptions extends ExtendableMessage<ServiceOptions> implements ServiceOptionsOrBuilder {
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final ServiceOptions defaultInstance = new ServiceOptions(true);
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<UninterpretedOption> uninterpretedOption_;

        public static final class Builder extends ExtendableBuilder<ServiceOptions, Builder> implements ServiceOptionsOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_;

            private Builder() {
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.uninterpretedOption_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private ServiceOptions buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                boolean z = true;
                if (this.uninterpretedOptionBuilder_ == null) {
                    List list = this.uninterpretedOption_;
                    if ((this.bitField0_ & 1) != 1) {
                        z = false;
                    }
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, z, getParentForChildren(), isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.uninterpretedOption_);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOption(Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.addMessage(uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(uninterpretedOption);
                    onChanged();
                }
                return this;
            }

            public Builder addUninterpretedOptionBuilder() {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public Builder addUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().addBuilder(i, UninterpretedOption.getDefaultInstance());
            }

            public ServiceOptions build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public ServiceOptions buildPartial() {
                ServiceOptions serviceOptions = new ServiceOptions();
                int i = this.bitField0_;
                if (this.uninterpretedOptionBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -2;
                    }
                    serviceOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    serviceOptions.uninterpretedOption_ = this.uninterpretedOptionBuilder_.build();
                }
                onBuilt();
                return serviceOptions;
            }

            public Builder clear() {
                super.clear();
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clearUninterpretedOption() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public ServiceOptions getDefaultInstanceForType() {
                return ServiceOptions.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return ServiceOptions.getDescriptor();
            }

            public UninterpretedOption getUninterpretedOption(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOption) this.uninterpretedOption_.get(i) : (UninterpretedOption) this.uninterpretedOptionBuilder_.getMessage(i);
            }

            public Builder getUninterpretedOptionBuilder(int i) {
                return (Builder) getUninterpretedOptionFieldBuilder().getBuilder(i);
            }

            public List<Builder> getUninterpretedOptionBuilderList() {
                return getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            public int getUninterpretedOptionCount() {
                return this.uninterpretedOptionBuilder_ == null ? this.uninterpretedOption_.size() : this.uninterpretedOptionBuilder_.getCount();
            }

            public List<UninterpretedOption> getUninterpretedOptionList() {
                return this.uninterpretedOptionBuilder_ == null ? Collections.unmodifiableList(this.uninterpretedOption_) : this.uninterpretedOptionBuilder_.getMessageList();
            }

            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
                return this.uninterpretedOptionBuilder_ == null ? (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i) : (UninterpretedOptionOrBuilder) this.uninterpretedOptionBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                return this.uninterpretedOptionBuilder_ != null ? this.uninterpretedOptionBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f290x371e666;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getUninterpretedOptionCount(); i++) {
                    if (!getUninterpretedOption(i).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 7994:
                            Object newBuilder2 = UninterpretedOption.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addUninterpretedOption(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(ServiceOptions serviceOptions) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (serviceOptions != ServiceOptions.getDefaultInstance()) {
                    if (this.uninterpretedOptionBuilder_ == null) {
                        if (!serviceOptions.uninterpretedOption_.isEmpty()) {
                            if (this.uninterpretedOption_.isEmpty()) {
                                this.uninterpretedOption_ = serviceOptions.uninterpretedOption_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureUninterpretedOptionIsMutable();
                                this.uninterpretedOption_.addAll(serviceOptions.uninterpretedOption_);
                            }
                            onChanged();
                        }
                    } else if (!serviceOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOptionBuilder_.isEmpty()) {
                            this.uninterpretedOptionBuilder_.dispose();
                            this.uninterpretedOptionBuilder_ = null;
                            this.uninterpretedOption_ = serviceOptions.uninterpretedOption_;
                            this.bitField0_ &= -2;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getUninterpretedOptionFieldBuilder();
                            }
                            this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.uninterpretedOptionBuilder_.addAllMessages(serviceOptions.uninterpretedOption_);
                        }
                    }
                    mergeExtensionFields(serviceOptions);
                    mergeUnknownFields(serviceOptions.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof ServiceOptions) {
                    return mergeFrom((ServiceOptions) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int i) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(i);
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.remove(i);
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, Builder builder) {
                if (this.uninterpretedOptionBuilder_ == null) {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, builder.build());
                    onChanged();
                } else {
                    this.uninterpretedOptionBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setUninterpretedOption(int i, UninterpretedOption uninterpretedOption) {
                if (this.uninterpretedOptionBuilder_ != null) {
                    this.uninterpretedOptionBuilder_.setMessage(i, uninterpretedOption);
                } else if (uninterpretedOption == null) {
                    throw new NullPointerException();
                } else {
                    ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(i, uninterpretedOption);
                    onChanged();
                }
                return this;
            }
        }

        static {
            defaultInstance.initFields();
        }

        private ServiceOptions(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private ServiceOptions(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static ServiceOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
        }

        private void initFields() {
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(ServiceOptions serviceOptions) {
            return newBuilder().mergeFrom(serviceOptions);
        }

        public static ServiceOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static ServiceOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static ServiceOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static ServiceOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static ServiceOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static ServiceOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static ServiceOptions parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static ServiceOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static ServiceOptions parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static ServiceOptions parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public ServiceOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeMessageSize;
            i = 0;
            int i2 = 0;
            while (i2 < this.uninterpretedOption_.size()) {
                computeMessageSize = CodedOutputStream.computeMessageSize(999, (MessageLite) this.uninterpretedOption_.get(i2)) + i;
                i2++;
                i = computeMessageSize;
            }
            computeMessageSize = (extensionsSerializedSize() + i) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = computeMessageSize;
            return computeMessageSize;
        }

        public UninterpretedOption getUninterpretedOption(int i) {
            return (UninterpretedOption) this.uninterpretedOption_.get(i);
        }

        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int i) {
            return (UninterpretedOptionOrBuilder) this.uninterpretedOption_.get(i);
        }

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f290x371e666;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getUninterpretedOptionCount()) {
                    if (getUninterpretedOption(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            ExtensionWriter newExtensionWriter = newExtensionWriter();
            for (int i = 0; i < this.uninterpretedOption_.size(); i++) {
                codedOutputStream.writeMessage(999, (MessageLite) this.uninterpretedOption_.get(i));
            }
            newExtensionWriter.writeUntil(536870912, codedOutputStream);
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface SourceCodeInfoOrBuilder extends MessageOrBuilder {
        Location getLocation(int i);

        int getLocationCount();

        List<Location> getLocationList();

        LocationOrBuilder getLocationOrBuilder(int i);

        List<? extends LocationOrBuilder> getLocationOrBuilderList();
    }

    public static final class SourceCodeInfo extends GeneratedMessage implements SourceCodeInfoOrBuilder {
        public static final int LOCATION_FIELD_NUMBER = 1;
        private static final SourceCodeInfo defaultInstance = new SourceCodeInfo(true);
        private static final long serialVersionUID = 0;
        private List<Location> location_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements SourceCodeInfoOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<Location, Builder, LocationOrBuilder> locationBuilder_;
            private List<Location> location_;

            private Builder() {
                this.location_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.location_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private SourceCodeInfo buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureLocationIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.location_ = new ArrayList(this.location_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
            }

            private RepeatedFieldBuilder<Location, Builder, LocationOrBuilder> getLocationFieldBuilder() {
                boolean z = true;
                if (this.locationBuilder_ == null) {
                    List list = this.location_;
                    if ((this.bitField0_ & 1) != 1) {
                        z = false;
                    }
                    this.locationBuilder_ = new RepeatedFieldBuilder(list, z, getParentForChildren(), isClean());
                    this.location_ = null;
                }
                return this.locationBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getLocationFieldBuilder();
                }
            }

            public Builder addAllLocation(Iterable<? extends Location> iterable) {
                if (this.locationBuilder_ == null) {
                    ensureLocationIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.location_);
                    onChanged();
                } else {
                    this.locationBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addLocation(int i, Builder builder) {
                if (this.locationBuilder_ == null) {
                    ensureLocationIsMutable();
                    this.location_.add(i, builder.build());
                    onChanged();
                } else {
                    this.locationBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addLocation(int i, Location location) {
                if (this.locationBuilder_ != null) {
                    this.locationBuilder_.addMessage(i, location);
                } else if (location == null) {
                    throw new NullPointerException();
                } else {
                    ensureLocationIsMutable();
                    this.location_.add(i, location);
                    onChanged();
                }
                return this;
            }

            public Builder addLocation(Builder builder) {
                if (this.locationBuilder_ == null) {
                    ensureLocationIsMutable();
                    this.location_.add(builder.build());
                    onChanged();
                } else {
                    this.locationBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addLocation(Location location) {
                if (this.locationBuilder_ != null) {
                    this.locationBuilder_.addMessage(location);
                } else if (location == null) {
                    throw new NullPointerException();
                } else {
                    ensureLocationIsMutable();
                    this.location_.add(location);
                    onChanged();
                }
                return this;
            }

            public Builder addLocationBuilder() {
                return (Builder) getLocationFieldBuilder().addBuilder(Location.getDefaultInstance());
            }

            public Builder addLocationBuilder(int i) {
                return (Builder) getLocationFieldBuilder().addBuilder(i, Location.getDefaultInstance());
            }

            public SourceCodeInfo build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public SourceCodeInfo buildPartial() {
                SourceCodeInfo sourceCodeInfo = new SourceCodeInfo();
                int i = this.bitField0_;
                if (this.locationBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.location_ = Collections.unmodifiableList(this.location_);
                        this.bitField0_ &= -2;
                    }
                    sourceCodeInfo.location_ = this.location_;
                } else {
                    sourceCodeInfo.location_ = this.locationBuilder_.build();
                }
                onBuilt();
                return sourceCodeInfo;
            }

            public Builder clear() {
                super.clear();
                if (this.locationBuilder_ == null) {
                    this.location_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.locationBuilder_.clear();
                }
                return this;
            }

            public Builder clearLocation() {
                if (this.locationBuilder_ == null) {
                    this.location_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.locationBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public SourceCodeInfo getDefaultInstanceForType() {
                return SourceCodeInfo.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return SourceCodeInfo.getDescriptor();
            }

            public Location getLocation(int i) {
                return this.locationBuilder_ == null ? (Location) this.location_.get(i) : (Location) this.locationBuilder_.getMessage(i);
            }

            public Builder getLocationBuilder(int i) {
                return (Builder) getLocationFieldBuilder().getBuilder(i);
            }

            public List<Builder> getLocationBuilderList() {
                return getLocationFieldBuilder().getBuilderList();
            }

            public int getLocationCount() {
                return this.locationBuilder_ == null ? this.location_.size() : this.locationBuilder_.getCount();
            }

            public List<Location> getLocationList() {
                return this.locationBuilder_ == null ? Collections.unmodifiableList(this.location_) : this.locationBuilder_.getMessageList();
            }

            public LocationOrBuilder getLocationOrBuilder(int i) {
                return this.locationBuilder_ == null ? (LocationOrBuilder) this.location_.get(i) : (LocationOrBuilder) this.locationBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends LocationOrBuilder> getLocationOrBuilderList() {
                return this.locationBuilder_ != null ? this.locationBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.location_);
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f293x532209f9;
            }

            public final boolean isInitialized() {
                return true;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 10:
                            Object newBuilder2 = Location.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addLocation(newBuilder2.buildPartial());
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(SourceCodeInfo sourceCodeInfo) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (sourceCodeInfo != SourceCodeInfo.getDefaultInstance()) {
                    if (this.locationBuilder_ == null) {
                        if (!sourceCodeInfo.location_.isEmpty()) {
                            if (this.location_.isEmpty()) {
                                this.location_ = sourceCodeInfo.location_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureLocationIsMutable();
                                this.location_.addAll(sourceCodeInfo.location_);
                            }
                            onChanged();
                        }
                    } else if (!sourceCodeInfo.location_.isEmpty()) {
                        if (this.locationBuilder_.isEmpty()) {
                            this.locationBuilder_.dispose();
                            this.locationBuilder_ = null;
                            this.location_ = sourceCodeInfo.location_;
                            this.bitField0_ &= -2;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getLocationFieldBuilder();
                            }
                            this.locationBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.locationBuilder_.addAllMessages(sourceCodeInfo.location_);
                        }
                    }
                    mergeUnknownFields(sourceCodeInfo.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof SourceCodeInfo) {
                    return mergeFrom((SourceCodeInfo) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeLocation(int i) {
                if (this.locationBuilder_ == null) {
                    ensureLocationIsMutable();
                    this.location_.remove(i);
                    onChanged();
                } else {
                    this.locationBuilder_.remove(i);
                }
                return this;
            }

            public Builder setLocation(int i, Builder builder) {
                if (this.locationBuilder_ == null) {
                    ensureLocationIsMutable();
                    this.location_.set(i, builder.build());
                    onChanged();
                } else {
                    this.locationBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setLocation(int i, Location location) {
                if (this.locationBuilder_ != null) {
                    this.locationBuilder_.setMessage(i, location);
                } else if (location == null) {
                    throw new NullPointerException();
                } else {
                    ensureLocationIsMutable();
                    this.location_.set(i, location);
                    onChanged();
                }
                return this;
            }
        }

        public interface LocationOrBuilder extends MessageOrBuilder {
            int getPath(int i);

            int getPathCount();

            List<Integer> getPathList();

            int getSpan(int i);

            int getSpanCount();

            List<Integer> getSpanList();
        }

        public static final class Location extends GeneratedMessage implements LocationOrBuilder {
            public static final int PATH_FIELD_NUMBER = 1;
            public static final int SPAN_FIELD_NUMBER = 2;
            private static final Location defaultInstance = new Location(true);
            private static final long serialVersionUID = 0;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private int pathMemoizedSerializedSize;
            private List<Integer> path_;
            private int spanMemoizedSerializedSize;
            private List<Integer> span_;

            public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements LocationOrBuilder {
                private int bitField0_;
                private List<Integer> path_;
                private List<Integer> span_;

                private Builder() {
                    this.path_ = Collections.emptyList();
                    this.span_ = Collections.emptyList();
                    maybeForceBuilderInitialization();
                }

                private Builder(BuilderParent builderParent) {
                    super(builderParent);
                    this.path_ = Collections.emptyList();
                    this.span_ = Collections.emptyList();
                    maybeForceBuilderInitialization();
                }

                private Location buildParsed() throws InvalidProtocolBufferException {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
                }

                private static Builder create() {
                    return new Builder();
                }

                private void ensurePathIsMutable() {
                    if ((this.bitField0_ & 1) != 1) {
                        this.path_ = new ArrayList(this.path_);
                        this.bitField0_ |= 1;
                    }
                }

                private void ensureSpanIsMutable() {
                    if ((this.bitField0_ & 2) != 2) {
                        this.span_ = new ArrayList(this.span_);
                        this.bitField0_ |= 2;
                    }
                }

                public static final Descriptor getDescriptor() {
                    return DescriptorProtos.f291xb210d08d;
                }

                private void maybeForceBuilderInitialization() {
                    if (!GeneratedMessage.alwaysUseFieldBuilders) {
                    }
                }

                public Builder addAllPath(Iterable<? extends Integer> iterable) {
                    ensurePathIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.path_);
                    onChanged();
                    return this;
                }

                public Builder addAllSpan(Iterable<? extends Integer> iterable) {
                    ensureSpanIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.span_);
                    onChanged();
                    return this;
                }

                public Builder addPath(int i) {
                    ensurePathIsMutable();
                    this.path_.add(Integer.valueOf(i));
                    onChanged();
                    return this;
                }

                public Builder addSpan(int i) {
                    ensureSpanIsMutable();
                    this.span_.add(Integer.valueOf(i));
                    onChanged();
                    return this;
                }

                public Location build() {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
                }

                public Location buildPartial() {
                    Location location = new Location();
                    int i = this.bitField0_;
                    if ((this.bitField0_ & 1) == 1) {
                        this.path_ = Collections.unmodifiableList(this.path_);
                        this.bitField0_ &= -2;
                    }
                    location.path_ = this.path_;
                    if ((this.bitField0_ & 2) == 2) {
                        this.span_ = Collections.unmodifiableList(this.span_);
                        this.bitField0_ &= -3;
                    }
                    location.span_ = this.span_;
                    onBuilt();
                    return location;
                }

                public Builder clear() {
                    super.clear();
                    this.path_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.span_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    return this;
                }

                public Builder clearPath() {
                    this.path_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                    return this;
                }

                public Builder clearSpan() {
                    this.span_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                    return this;
                }

                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                public Location getDefaultInstanceForType() {
                    return Location.getDefaultInstance();
                }

                public Descriptor getDescriptorForType() {
                    return Location.getDescriptor();
                }

                public int getPath(int i) {
                    return ((Integer) this.path_.get(i)).intValue();
                }

                public int getPathCount() {
                    return this.path_.size();
                }

                public List<Integer> getPathList() {
                    return Collections.unmodifiableList(this.path_);
                }

                public int getSpan(int i) {
                    return ((Integer) this.span_.get(i)).intValue();
                }

                public int getSpanCount() {
                    return this.span_.size();
                }

                public List<Integer> getSpanList() {
                    return Collections.unmodifiableList(this.span_);
                }

                protected FieldAccessorTable internalGetFieldAccessorTable() {
                    return DescriptorProtos.f292x9611a0b;
                }

                public final boolean isInitialized() {
                    return true;
                }

                public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                    while (true) {
                        int readTag = codedInputStream.readTag();
                        switch (readTag) {
                            case 0:
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            case 8:
                                ensurePathIsMutable();
                                this.path_.add(Integer.valueOf(codedInputStream.readInt32()));
                                continue;
                            case 10:
                                readTag = codedInputStream.pushLimit(codedInputStream.readRawVarint32());
                                while (codedInputStream.getBytesUntilLimit() > 0) {
                                    addPath(codedInputStream.readInt32());
                                }
                                codedInputStream.popLimit(readTag);
                                continue;
                            case 16:
                                ensureSpanIsMutable();
                                this.span_.add(Integer.valueOf(codedInputStream.readInt32()));
                                continue;
                            case 18:
                                readTag = codedInputStream.pushLimit(codedInputStream.readRawVarint32());
                                while (codedInputStream.getBytesUntilLimit() > 0) {
                                    addSpan(codedInputStream.readInt32());
                                }
                                codedInputStream.popLimit(readTag);
                                continue;
                            default:
                                if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                    setUnknownFields(newBuilder.build());
                                    onChanged();
                                    break;
                                }
                                continue;
                        }
                        return this;
                    }
                }

                public Builder mergeFrom(Location location) {
                    if (location != Location.getDefaultInstance()) {
                        if (!location.path_.isEmpty()) {
                            if (this.path_.isEmpty()) {
                                this.path_ = location.path_;
                                this.bitField0_ &= -2;
                            } else {
                                ensurePathIsMutable();
                                this.path_.addAll(location.path_);
                            }
                            onChanged();
                        }
                        if (!location.span_.isEmpty()) {
                            if (this.span_.isEmpty()) {
                                this.span_ = location.span_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureSpanIsMutable();
                                this.span_.addAll(location.span_);
                            }
                            onChanged();
                        }
                        mergeUnknownFields(location.getUnknownFields());
                    }
                    return this;
                }

                public Builder mergeFrom(Message message) {
                    if (message instanceof Location) {
                        return mergeFrom((Location) message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder setPath(int i, int i2) {
                    ensurePathIsMutable();
                    this.path_.set(i, Integer.valueOf(i2));
                    onChanged();
                    return this;
                }

                public Builder setSpan(int i, int i2) {
                    ensureSpanIsMutable();
                    this.span_.set(i, Integer.valueOf(i2));
                    onChanged();
                    return this;
                }
            }

            static {
                defaultInstance.initFields();
            }

            private Location(Builder builder) {
                super(builder);
                this.pathMemoizedSerializedSize = -1;
                this.spanMemoizedSerializedSize = -1;
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            private Location(boolean z) {
                this.pathMemoizedSerializedSize = -1;
                this.spanMemoizedSerializedSize = -1;
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            public static Location getDefaultInstance() {
                return defaultInstance;
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.f291xb210d08d;
            }

            private void initFields() {
                this.path_ = Collections.emptyList();
                this.span_ = Collections.emptyList();
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public static Builder newBuilder(Location location) {
                return newBuilder().mergeFrom(location);
            }

            public static Location parseDelimitedFrom(InputStream inputStream) throws IOException {
                Builder newBuilder = newBuilder();
                return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
            }

            public static Location parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Builder newBuilder = newBuilder();
                return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
            }

            public static Location parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
            }

            public static Location parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
            }

            public static Location parseFrom(CodedInputStream codedInputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
            }

            public static Location parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
            }

            public static Location parseFrom(InputStream inputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
            }

            public static Location parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
            }

            public static Location parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
            }

            public static Location parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
            }

            public Location getDefaultInstanceForType() {
                return defaultInstance;
            }

            public int getPath(int i) {
                return ((Integer) this.path_.get(i)).intValue();
            }

            public int getPathCount() {
                return this.path_.size();
            }

            public List<Integer> getPathList() {
                return this.path_;
            }

            public int getSerializedSize() {
                int i = 0;
                int i2 = this.memoizedSerializedSize;
                if (i2 != -1) {
                    return i2;
                }
                int i3 = 0;
                int i4 = 0;
                while (i4 < this.path_.size()) {
                    i2 = CodedOutputStream.computeInt32SizeNoTag(((Integer) this.path_.get(i4)).intValue()) + i3;
                    i4++;
                    i3 = i2;
                }
                i2 = i3 + 0;
                i4 = !getPathList().isEmpty() ? (i2 + 1) + CodedOutputStream.computeInt32SizeNoTag(i3) : i2;
                this.pathMemoizedSerializedSize = i3;
                i3 = 0;
                while (i < this.span_.size()) {
                    i++;
                    i3 = CodedOutputStream.computeInt32SizeNoTag(((Integer) this.span_.get(i)).intValue()) + i3;
                }
                i2 = i4 + i3;
                if (!getSpanList().isEmpty()) {
                    i2 = (i2 + 1) + CodedOutputStream.computeInt32SizeNoTag(i3);
                }
                this.spanMemoizedSerializedSize = i3;
                i2 += getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = i2;
                return i2;
            }

            public int getSpan(int i) {
                return ((Integer) this.span_.get(i)).intValue();
            }

            public int getSpanCount() {
                return this.span_.size();
            }

            public List<Integer> getSpanList() {
                return this.span_;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f292x9611a0b;
            }

            public final boolean isInitialized() {
                byte b = this.memoizedIsInitialized;
                if (b != (byte) -1) {
                    return b == (byte) 1;
                } else {
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }
            }

            public Builder newBuilderForType() {
                return newBuilder();
            }

            protected Builder newBuilderForType(BuilderParent builderParent) {
                return new Builder(builderParent);
            }

            public Builder toBuilder() {
                return newBuilder(this);
            }

            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                int i = 0;
                getSerializedSize();
                if (getPathList().size() > 0) {
                    codedOutputStream.writeRawVarint32(10);
                    codedOutputStream.writeRawVarint32(this.pathMemoizedSerializedSize);
                }
                for (int i2 = 0; i2 < this.path_.size(); i2++) {
                    codedOutputStream.writeInt32NoTag(((Integer) this.path_.get(i2)).intValue());
                }
                if (getSpanList().size() > 0) {
                    codedOutputStream.writeRawVarint32(18);
                    codedOutputStream.writeRawVarint32(this.spanMemoizedSerializedSize);
                }
                while (i < this.span_.size()) {
                    codedOutputStream.writeInt32NoTag(((Integer) this.span_.get(i)).intValue());
                    i++;
                }
                getUnknownFields().writeTo(codedOutputStream);
            }
        }

        static {
            defaultInstance.initFields();
        }

        private SourceCodeInfo(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private SourceCodeInfo(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        public static SourceCodeInfo getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
        }

        private void initFields() {
            this.location_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(SourceCodeInfo sourceCodeInfo) {
            return newBuilder().mergeFrom(sourceCodeInfo);
        }

        public static SourceCodeInfo parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static SourceCodeInfo parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static SourceCodeInfo parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static SourceCodeInfo parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static SourceCodeInfo parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static SourceCodeInfo parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static SourceCodeInfo parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static SourceCodeInfo parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static SourceCodeInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static SourceCodeInfo parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public SourceCodeInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        public Location getLocation(int i) {
            return (Location) this.location_.get(i);
        }

        public int getLocationCount() {
            return this.location_.size();
        }

        public List<Location> getLocationList() {
            return this.location_;
        }

        public LocationOrBuilder getLocationOrBuilder(int i) {
            return (LocationOrBuilder) this.location_.get(i);
        }

        public List<? extends LocationOrBuilder> getLocationOrBuilderList() {
            return this.location_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeMessageSize;
            i = 0;
            int i2 = 0;
            while (i2 < this.location_.size()) {
                computeMessageSize = CodedOutputStream.computeMessageSize(1, (MessageLite) this.location_.get(i2)) + i;
                i2++;
                i = computeMessageSize;
            }
            computeMessageSize = getUnknownFields().getSerializedSize() + i;
            this.memoizedSerializedSize = computeMessageSize;
            return computeMessageSize;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f293x532209f9;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                this.memoizedIsInitialized = (byte) 1;
                return true;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.location_.size(); i++) {
                codedOutputStream.writeMessage(1, (MessageLite) this.location_.get(i));
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    public interface UninterpretedOptionOrBuilder extends MessageOrBuilder {
        String getAggregateValue();

        double getDoubleValue();

        String getIdentifierValue();

        NamePart getName(int i);

        int getNameCount();

        List<NamePart> getNameList();

        NamePartOrBuilder getNameOrBuilder(int i);

        List<? extends NamePartOrBuilder> getNameOrBuilderList();

        long getNegativeIntValue();

        long getPositiveIntValue();

        ByteString getStringValue();

        boolean hasAggregateValue();

        boolean hasDoubleValue();

        boolean hasIdentifierValue();

        boolean hasNegativeIntValue();

        boolean hasPositiveIntValue();

        boolean hasStringValue();
    }

    public static final class UninterpretedOption extends GeneratedMessage implements UninterpretedOptionOrBuilder {
        public static final int AGGREGATE_VALUE_FIELD_NUMBER = 8;
        public static final int DOUBLE_VALUE_FIELD_NUMBER = 6;
        public static final int IDENTIFIER_VALUE_FIELD_NUMBER = 3;
        public static final int NAME_FIELD_NUMBER = 2;
        public static final int NEGATIVE_INT_VALUE_FIELD_NUMBER = 5;
        public static final int POSITIVE_INT_VALUE_FIELD_NUMBER = 4;
        public static final int STRING_VALUE_FIELD_NUMBER = 7;
        private static final UninterpretedOption defaultInstance = new UninterpretedOption(true);
        private static final long serialVersionUID = 0;
        private Object aggregateValue_;
        private int bitField0_;
        private double doubleValue_;
        private Object identifierValue_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<NamePart> name_;
        private long negativeIntValue_;
        private long positiveIntValue_;
        private ByteString stringValue_;

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements UninterpretedOptionOrBuilder {
            private Object aggregateValue_;
            private int bitField0_;
            private double doubleValue_;
            private Object identifierValue_;
            private RepeatedFieldBuilder<NamePart, Builder, NamePartOrBuilder> nameBuilder_;
            private List<NamePart> name_;
            private long negativeIntValue_;
            private long positiveIntValue_;
            private ByteString stringValue_;

            private Builder() {
                this.name_ = Collections.emptyList();
                this.identifierValue_ = HttpVersions.HTTP_0_9;
                this.stringValue_ = ByteString.EMPTY;
                this.aggregateValue_ = HttpVersions.HTTP_0_9;
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent builderParent) {
                super(builderParent);
                this.name_ = Collections.emptyList();
                this.identifierValue_ = HttpVersions.HTTP_0_9;
                this.stringValue_ = ByteString.EMPTY;
                this.aggregateValue_ = HttpVersions.HTTP_0_9;
                maybeForceBuilderInitialization();
            }

            private UninterpretedOption buildParsed() throws InvalidProtocolBufferException {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureNameIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.name_ = new ArrayList(this.name_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
            }

            private RepeatedFieldBuilder<NamePart, Builder, NamePartOrBuilder> getNameFieldBuilder() {
                boolean z = true;
                if (this.nameBuilder_ == null) {
                    List list = this.name_;
                    if ((this.bitField0_ & 1) != 1) {
                        z = false;
                    }
                    this.nameBuilder_ = new RepeatedFieldBuilder(list, z, getParentForChildren(), isClean());
                    this.name_ = null;
                }
                return this.nameBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    getNameFieldBuilder();
                }
            }

            public Builder addAllName(Iterable<? extends NamePart> iterable) {
                if (this.nameBuilder_ == null) {
                    ensureNameIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(iterable, this.name_);
                    onChanged();
                } else {
                    this.nameBuilder_.addAllMessages(iterable);
                }
                return this;
            }

            public Builder addName(int i, Builder builder) {
                if (this.nameBuilder_ == null) {
                    ensureNameIsMutable();
                    this.name_.add(i, builder.build());
                    onChanged();
                } else {
                    this.nameBuilder_.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addName(int i, NamePart namePart) {
                if (this.nameBuilder_ != null) {
                    this.nameBuilder_.addMessage(i, namePart);
                } else if (namePart == null) {
                    throw new NullPointerException();
                } else {
                    ensureNameIsMutable();
                    this.name_.add(i, namePart);
                    onChanged();
                }
                return this;
            }

            public Builder addName(Builder builder) {
                if (this.nameBuilder_ == null) {
                    ensureNameIsMutable();
                    this.name_.add(builder.build());
                    onChanged();
                } else {
                    this.nameBuilder_.addMessage(builder.build());
                }
                return this;
            }

            public Builder addName(NamePart namePart) {
                if (this.nameBuilder_ != null) {
                    this.nameBuilder_.addMessage(namePart);
                } else if (namePart == null) {
                    throw new NullPointerException();
                } else {
                    ensureNameIsMutable();
                    this.name_.add(namePart);
                    onChanged();
                }
                return this;
            }

            public Builder addNameBuilder() {
                return (Builder) getNameFieldBuilder().addBuilder(NamePart.getDefaultInstance());
            }

            public Builder addNameBuilder(int i) {
                return (Builder) getNameFieldBuilder().addBuilder(i, NamePart.getDefaultInstance());
            }

            public UninterpretedOption build() {
                Object buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
            }

            public UninterpretedOption buildPartial() {
                int i = 1;
                UninterpretedOption uninterpretedOption = new UninterpretedOption();
                int i2 = this.bitField0_;
                if (this.nameBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.name_ = Collections.unmodifiableList(this.name_);
                        this.bitField0_ &= -2;
                    }
                    uninterpretedOption.name_ = this.name_;
                } else {
                    uninterpretedOption.name_ = this.nameBuilder_.build();
                }
                if ((i2 & 2) != 2) {
                    i = 0;
                }
                uninterpretedOption.identifierValue_ = this.identifierValue_;
                if ((i2 & 4) == 4) {
                    i |= 2;
                }
                uninterpretedOption.positiveIntValue_ = this.positiveIntValue_;
                if ((i2 & 8) == 8) {
                    i |= 4;
                }
                uninterpretedOption.negativeIntValue_ = this.negativeIntValue_;
                if ((i2 & 16) == 16) {
                    i |= 8;
                }
                uninterpretedOption.doubleValue_ = this.doubleValue_;
                if ((i2 & 32) == 32) {
                    i |= 16;
                }
                uninterpretedOption.stringValue_ = this.stringValue_;
                if ((i2 & 64) == 64) {
                    i |= 32;
                }
                uninterpretedOption.aggregateValue_ = this.aggregateValue_;
                uninterpretedOption.bitField0_ = i;
                onBuilt();
                return uninterpretedOption;
            }

            public Builder clear() {
                super.clear();
                if (this.nameBuilder_ == null) {
                    this.name_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.nameBuilder_.clear();
                }
                this.identifierValue_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -3;
                this.positiveIntValue_ = 0;
                this.bitField0_ &= -5;
                this.negativeIntValue_ = 0;
                this.bitField0_ &= -9;
                this.doubleValue_ = 0.0d;
                this.bitField0_ &= -17;
                this.stringValue_ = ByteString.EMPTY;
                this.bitField0_ &= -33;
                this.aggregateValue_ = HttpVersions.HTTP_0_9;
                this.bitField0_ &= -65;
                return this;
            }

            public Builder clearAggregateValue() {
                this.bitField0_ &= -65;
                this.aggregateValue_ = UninterpretedOption.getDefaultInstance().getAggregateValue();
                onChanged();
                return this;
            }

            public Builder clearDoubleValue() {
                this.bitField0_ &= -17;
                this.doubleValue_ = 0.0d;
                onChanged();
                return this;
            }

            public Builder clearIdentifierValue() {
                this.bitField0_ &= -3;
                this.identifierValue_ = UninterpretedOption.getDefaultInstance().getIdentifierValue();
                onChanged();
                return this;
            }

            public Builder clearName() {
                if (this.nameBuilder_ == null) {
                    this.name_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.nameBuilder_.clear();
                }
                return this;
            }

            public Builder clearNegativeIntValue() {
                this.bitField0_ &= -9;
                this.negativeIntValue_ = 0;
                onChanged();
                return this;
            }

            public Builder clearPositiveIntValue() {
                this.bitField0_ &= -5;
                this.positiveIntValue_ = 0;
                onChanged();
                return this;
            }

            public Builder clearStringValue() {
                this.bitField0_ &= -33;
                this.stringValue_ = UninterpretedOption.getDefaultInstance().getStringValue();
                onChanged();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public String getAggregateValue() {
                Object obj = this.aggregateValue_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.aggregateValue_ = toStringUtf8;
                return toStringUtf8;
            }

            public UninterpretedOption getDefaultInstanceForType() {
                return UninterpretedOption.getDefaultInstance();
            }

            public Descriptor getDescriptorForType() {
                return UninterpretedOption.getDescriptor();
            }

            public double getDoubleValue() {
                return this.doubleValue_;
            }

            public String getIdentifierValue() {
                Object obj = this.identifierValue_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                this.identifierValue_ = toStringUtf8;
                return toStringUtf8;
            }

            public NamePart getName(int i) {
                return this.nameBuilder_ == null ? (NamePart) this.name_.get(i) : (NamePart) this.nameBuilder_.getMessage(i);
            }

            public Builder getNameBuilder(int i) {
                return (Builder) getNameFieldBuilder().getBuilder(i);
            }

            public List<Builder> getNameBuilderList() {
                return getNameFieldBuilder().getBuilderList();
            }

            public int getNameCount() {
                return this.nameBuilder_ == null ? this.name_.size() : this.nameBuilder_.getCount();
            }

            public List<NamePart> getNameList() {
                return this.nameBuilder_ == null ? Collections.unmodifiableList(this.name_) : this.nameBuilder_.getMessageList();
            }

            public NamePartOrBuilder getNameOrBuilder(int i) {
                return this.nameBuilder_ == null ? (NamePartOrBuilder) this.name_.get(i) : (NamePartOrBuilder) this.nameBuilder_.getMessageOrBuilder(i);
            }

            public List<? extends NamePartOrBuilder> getNameOrBuilderList() {
                return this.nameBuilder_ != null ? this.nameBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.name_);
            }

            public long getNegativeIntValue() {
                return this.negativeIntValue_;
            }

            public long getPositiveIntValue() {
                return this.positiveIntValue_;
            }

            public ByteString getStringValue() {
                return this.stringValue_;
            }

            public boolean hasAggregateValue() {
                return (this.bitField0_ & 64) == 64;
            }

            public boolean hasDoubleValue() {
                return (this.bitField0_ & 16) == 16;
            }

            public boolean hasIdentifierValue() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasNegativeIntValue() {
                return (this.bitField0_ & 8) == 8;
            }

            public boolean hasPositiveIntValue() {
                return (this.bitField0_ & 4) == 4;
            }

            public boolean hasStringValue() {
                return (this.bitField0_ & 32) == 32;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f296x2101041;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getNameCount(); i++) {
                    if (!getName(i).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            setUnknownFields(newBuilder.build());
                            onChanged();
                            break;
                        case 18:
                            Object newBuilder2 = NamePart.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addName(newBuilder2.buildPartial());
                            continue;
                        case 26:
                            this.bitField0_ |= 2;
                            this.identifierValue_ = codedInputStream.readBytes();
                            continue;
                        case 32:
                            this.bitField0_ |= 4;
                            this.positiveIntValue_ = codedInputStream.readUInt64();
                            continue;
                        case 40:
                            this.bitField0_ |= 8;
                            this.negativeIntValue_ = codedInputStream.readInt64();
                            continue;
                        case 49:
                            this.bitField0_ |= 16;
                            this.doubleValue_ = codedInputStream.readDouble();
                            continue;
                        case 58:
                            this.bitField0_ |= 32;
                            this.stringValue_ = codedInputStream.readBytes();
                            continue;
                        case 66:
                            this.bitField0_ |= 64;
                            this.aggregateValue_ = codedInputStream.readBytes();
                            continue;
                        default:
                            if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            }
                            continue;
                    }
                    return this;
                }
            }

            public Builder mergeFrom(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder repeatedFieldBuilder = null;
                if (uninterpretedOption != UninterpretedOption.getDefaultInstance()) {
                    if (this.nameBuilder_ == null) {
                        if (!uninterpretedOption.name_.isEmpty()) {
                            if (this.name_.isEmpty()) {
                                this.name_ = uninterpretedOption.name_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureNameIsMutable();
                                this.name_.addAll(uninterpretedOption.name_);
                            }
                            onChanged();
                        }
                    } else if (!uninterpretedOption.name_.isEmpty()) {
                        if (this.nameBuilder_.isEmpty()) {
                            this.nameBuilder_.dispose();
                            this.nameBuilder_ = null;
                            this.name_ = uninterpretedOption.name_;
                            this.bitField0_ &= -2;
                            if (GeneratedMessage.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getNameFieldBuilder();
                            }
                            this.nameBuilder_ = repeatedFieldBuilder;
                        } else {
                            this.nameBuilder_.addAllMessages(uninterpretedOption.name_);
                        }
                    }
                    if (uninterpretedOption.hasIdentifierValue()) {
                        setIdentifierValue(uninterpretedOption.getIdentifierValue());
                    }
                    if (uninterpretedOption.hasPositiveIntValue()) {
                        setPositiveIntValue(uninterpretedOption.getPositiveIntValue());
                    }
                    if (uninterpretedOption.hasNegativeIntValue()) {
                        setNegativeIntValue(uninterpretedOption.getNegativeIntValue());
                    }
                    if (uninterpretedOption.hasDoubleValue()) {
                        setDoubleValue(uninterpretedOption.getDoubleValue());
                    }
                    if (uninterpretedOption.hasStringValue()) {
                        setStringValue(uninterpretedOption.getStringValue());
                    }
                    if (uninterpretedOption.hasAggregateValue()) {
                        setAggregateValue(uninterpretedOption.getAggregateValue());
                    }
                    mergeUnknownFields(uninterpretedOption.getUnknownFields());
                }
                return this;
            }

            public Builder mergeFrom(Message message) {
                if (message instanceof UninterpretedOption) {
                    return mergeFrom((UninterpretedOption) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeName(int i) {
                if (this.nameBuilder_ == null) {
                    ensureNameIsMutable();
                    this.name_.remove(i);
                    onChanged();
                } else {
                    this.nameBuilder_.remove(i);
                }
                return this;
            }

            public Builder setAggregateValue(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 64;
                this.aggregateValue_ = str;
                onChanged();
                return this;
            }

            void setAggregateValue(ByteString byteString) {
                this.bitField0_ |= 64;
                this.aggregateValue_ = byteString;
                onChanged();
            }

            public Builder setDoubleValue(double d) {
                this.bitField0_ |= 16;
                this.doubleValue_ = d;
                onChanged();
                return this;
            }

            public Builder setIdentifierValue(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.identifierValue_ = str;
                onChanged();
                return this;
            }

            void setIdentifierValue(ByteString byteString) {
                this.bitField0_ |= 2;
                this.identifierValue_ = byteString;
                onChanged();
            }

            public Builder setName(int i, Builder builder) {
                if (this.nameBuilder_ == null) {
                    ensureNameIsMutable();
                    this.name_.set(i, builder.build());
                    onChanged();
                } else {
                    this.nameBuilder_.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder setName(int i, NamePart namePart) {
                if (this.nameBuilder_ != null) {
                    this.nameBuilder_.setMessage(i, namePart);
                } else if (namePart == null) {
                    throw new NullPointerException();
                } else {
                    ensureNameIsMutable();
                    this.name_.set(i, namePart);
                    onChanged();
                }
                return this;
            }

            public Builder setNegativeIntValue(long j) {
                this.bitField0_ |= 8;
                this.negativeIntValue_ = j;
                onChanged();
                return this;
            }

            public Builder setPositiveIntValue(long j) {
                this.bitField0_ |= 4;
                this.positiveIntValue_ = j;
                onChanged();
                return this;
            }

            public Builder setStringValue(ByteString byteString) {
                if (byteString == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 32;
                this.stringValue_ = byteString;
                onChanged();
                return this;
            }
        }

        public interface NamePartOrBuilder extends MessageOrBuilder {
            boolean getIsExtension();

            String getNamePart();

            boolean hasIsExtension();

            boolean hasNamePart();
        }

        public static final class NamePart extends GeneratedMessage implements NamePartOrBuilder {
            public static final int IS_EXTENSION_FIELD_NUMBER = 2;
            public static final int NAME_PART_FIELD_NUMBER = 1;
            private static final NamePart defaultInstance = new NamePart(true);
            private static final long serialVersionUID = 0;
            private int bitField0_;
            private boolean isExtension_;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private Object namePart_;

            public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements NamePartOrBuilder {
                private int bitField0_;
                private boolean isExtension_;
                private Object namePart_;

                private Builder() {
                    this.namePart_ = HttpVersions.HTTP_0_9;
                    maybeForceBuilderInitialization();
                }

                private Builder(BuilderParent builderParent) {
                    super(builderParent);
                    this.namePart_ = HttpVersions.HTTP_0_9;
                    maybeForceBuilderInitialization();
                }

                private NamePart buildParsed() throws InvalidProtocolBufferException {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
                }

                private static Builder create() {
                    return new Builder();
                }

                public static final Descriptor getDescriptor() {
                    return DescriptorProtos.f294xb111d23c;
                }

                private void maybeForceBuilderInitialization() {
                    if (!GeneratedMessage.alwaysUseFieldBuilders) {
                    }
                }

                public NamePart build() {
                    Object buildPartial = buildPartial();
                    if (buildPartial.isInitialized()) {
                        return buildPartial;
                    }
                    throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(buildPartial);
                }

                public NamePart buildPartial() {
                    int i = 1;
                    NamePart namePart = new NamePart();
                    int i2 = this.bitField0_;
                    if ((i2 & 1) != 1) {
                        i = 0;
                    }
                    namePart.namePart_ = this.namePart_;
                    if ((i2 & 2) == 2) {
                        i |= 2;
                    }
                    namePart.isExtension_ = this.isExtension_;
                    namePart.bitField0_ = i;
                    onBuilt();
                    return namePart;
                }

                public Builder clear() {
                    super.clear();
                    this.namePart_ = HttpVersions.HTTP_0_9;
                    this.bitField0_ &= -2;
                    this.isExtension_ = false;
                    this.bitField0_ &= -3;
                    return this;
                }

                public Builder clearIsExtension() {
                    this.bitField0_ &= -3;
                    this.isExtension_ = false;
                    onChanged();
                    return this;
                }

                public Builder clearNamePart() {
                    this.bitField0_ &= -2;
                    this.namePart_ = NamePart.getDefaultInstance().getNamePart();
                    onChanged();
                    return this;
                }

                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                public NamePart getDefaultInstanceForType() {
                    return NamePart.getDefaultInstance();
                }

                public Descriptor getDescriptorForType() {
                    return NamePart.getDescriptor();
                }

                public boolean getIsExtension() {
                    return this.isExtension_;
                }

                public String getNamePart() {
                    Object obj = this.namePart_;
                    if (obj instanceof String) {
                        return (String) obj;
                    }
                    String toStringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.namePart_ = toStringUtf8;
                    return toStringUtf8;
                }

                public boolean hasIsExtension() {
                    return (this.bitField0_ & 2) == 2;
                }

                public boolean hasNamePart() {
                    return (this.bitField0_ & 1) == 1;
                }

                protected FieldAccessorTable internalGetFieldAccessorTable() {
                    return DescriptorProtos.f295x1698fcba;
                }

                public final boolean isInitialized() {
                    return hasNamePart() && hasIsExtension();
                }

                public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    com.google.protobuf.UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder(getUnknownFields());
                    while (true) {
                        int readTag = codedInputStream.readTag();
                        switch (readTag) {
                            case 0:
                                setUnknownFields(newBuilder.build());
                                onChanged();
                                break;
                            case 10:
                                this.bitField0_ |= 1;
                                this.namePart_ = codedInputStream.readBytes();
                                continue;
                            case 16:
                                this.bitField0_ |= 2;
                                this.isExtension_ = codedInputStream.readBool();
                                continue;
                            default:
                                if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                    setUnknownFields(newBuilder.build());
                                    onChanged();
                                    break;
                                }
                                continue;
                        }
                        return this;
                    }
                }

                public Builder mergeFrom(NamePart namePart) {
                    if (namePart != NamePart.getDefaultInstance()) {
                        if (namePart.hasNamePart()) {
                            setNamePart(namePart.getNamePart());
                        }
                        if (namePart.hasIsExtension()) {
                            setIsExtension(namePart.getIsExtension());
                        }
                        mergeUnknownFields(namePart.getUnknownFields());
                    }
                    return this;
                }

                public Builder mergeFrom(Message message) {
                    if (message instanceof NamePart) {
                        return mergeFrom((NamePart) message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder setIsExtension(boolean z) {
                    this.bitField0_ |= 2;
                    this.isExtension_ = z;
                    onChanged();
                    return this;
                }

                public Builder setNamePart(String str) {
                    if (str == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 1;
                    this.namePart_ = str;
                    onChanged();
                    return this;
                }

                void setNamePart(ByteString byteString) {
                    this.bitField0_ |= 1;
                    this.namePart_ = byteString;
                    onChanged();
                }
            }

            static {
                defaultInstance.initFields();
            }

            private NamePart(Builder builder) {
                super(builder);
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            private NamePart(boolean z) {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
            }

            public static NamePart getDefaultInstance() {
                return defaultInstance;
            }

            public static final Descriptor getDescriptor() {
                return DescriptorProtos.f294xb111d23c;
            }

            private ByteString getNamePartBytes() {
                Object obj = this.namePart_;
                if (!(obj instanceof String)) {
                    return (ByteString) obj;
                }
                ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.namePart_ = copyFromUtf8;
                return copyFromUtf8;
            }

            private void initFields() {
                this.namePart_ = HttpVersions.HTTP_0_9;
                this.isExtension_ = false;
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public static Builder newBuilder(NamePart namePart) {
                return newBuilder().mergeFrom(namePart);
            }

            public static NamePart parseDelimitedFrom(InputStream inputStream) throws IOException {
                Builder newBuilder = newBuilder();
                return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
            }

            public static NamePart parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Builder newBuilder = newBuilder();
                return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
            }

            public static NamePart parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
            }

            public static NamePart parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
            }

            public static NamePart parseFrom(CodedInputStream codedInputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
            }

            public static NamePart parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
            }

            public static NamePart parseFrom(InputStream inputStream) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
            }

            public static NamePart parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
            }

            public static NamePart parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
            }

            public static NamePart parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
            }

            public NamePart getDefaultInstanceForType() {
                return defaultInstance;
            }

            public boolean getIsExtension() {
                return this.isExtension_;
            }

            public String getNamePart() {
                Object obj = this.namePart_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                ByteString byteString = (ByteString) obj;
                String toStringUtf8 = byteString.toStringUtf8();
                if (Internal.isValidUtf8(byteString)) {
                    this.namePart_ = toStringUtf8;
                }
                return toStringUtf8;
            }

            public int getSerializedSize() {
                int i = this.memoizedSerializedSize;
                if (i != -1) {
                    return i;
                }
                i = 0;
                if ((this.bitField0_ & 1) == 1) {
                    i = CodedOutputStream.computeBytesSize(1, getNamePartBytes()) + 0;
                }
                if ((this.bitField0_ & 2) == 2) {
                    i += CodedOutputStream.computeBoolSize(2, this.isExtension_);
                }
                i += getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = i;
                return i;
            }

            public boolean hasIsExtension() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean hasNamePart() {
                return (this.bitField0_ & 1) == 1;
            }

            protected FieldAccessorTable internalGetFieldAccessorTable() {
                return DescriptorProtos.f295x1698fcba;
            }

            public final boolean isInitialized() {
                byte b = this.memoizedIsInitialized;
                if (b != (byte) -1) {
                    return b == (byte) 1;
                } else {
                    if (!hasNamePart()) {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    } else if (hasIsExtension()) {
                        this.memoizedIsInitialized = (byte) 1;
                        return true;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
            }

            public Builder newBuilderForType() {
                return newBuilder();
            }

            protected Builder newBuilderForType(BuilderParent builderParent) {
                return new Builder(builderParent);
            }

            public Builder toBuilder() {
                return newBuilder(this);
            }

            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                getSerializedSize();
                if ((this.bitField0_ & 1) == 1) {
                    codedOutputStream.writeBytes(1, getNamePartBytes());
                }
                if ((this.bitField0_ & 2) == 2) {
                    codedOutputStream.writeBool(2, this.isExtension_);
                }
                getUnknownFields().writeTo(codedOutputStream);
            }
        }

        static {
            defaultInstance.initFields();
        }

        private UninterpretedOption(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private UninterpretedOption(boolean z) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
        }

        private ByteString getAggregateValueBytes() {
            Object obj = this.aggregateValue_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.aggregateValue_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public static UninterpretedOption getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
        }

        private ByteString getIdentifierValueBytes() {
            Object obj = this.identifierValue_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.identifierValue_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.name_ = Collections.emptyList();
            this.identifierValue_ = HttpVersions.HTTP_0_9;
            this.positiveIntValue_ = 0;
            this.negativeIntValue_ = 0;
            this.doubleValue_ = 0.0d;
            this.stringValue_ = ByteString.EMPTY;
            this.aggregateValue_ = HttpVersions.HTTP_0_9;
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(UninterpretedOption uninterpretedOption) {
            return newBuilder().mergeFrom(uninterpretedOption);
        }

        public static UninterpretedOption parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream) ? newBuilder.buildParsed() : null;
        }

        public static UninterpretedOption parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            return newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite) ? newBuilder.buildParsed() : null;
        }

        public static UninterpretedOption parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static UninterpretedOption parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static UninterpretedOption parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static UninterpretedOption parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static UninterpretedOption parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static UninterpretedOption parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static UninterpretedOption parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static UninterpretedOption parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public String getAggregateValue() {
            Object obj = this.aggregateValue_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.aggregateValue_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public UninterpretedOption getDefaultInstanceForType() {
            return defaultInstance;
        }

        public double getDoubleValue() {
            return this.doubleValue_;
        }

        public String getIdentifierValue() {
            Object obj = this.identifierValue_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String toStringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.identifierValue_ = toStringUtf8;
            }
            return toStringUtf8;
        }

        public NamePart getName(int i) {
            return (NamePart) this.name_.get(i);
        }

        public int getNameCount() {
            return this.name_.size();
        }

        public List<NamePart> getNameList() {
            return this.name_;
        }

        public NamePartOrBuilder getNameOrBuilder(int i) {
            return (NamePartOrBuilder) this.name_.get(i);
        }

        public List<? extends NamePartOrBuilder> getNameOrBuilderList() {
            return this.name_;
        }

        public long getNegativeIntValue() {
            return this.negativeIntValue_;
        }

        public long getPositiveIntValue() {
            return this.positiveIntValue_;
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            for (i = 0; i < this.name_.size(); i++) {
                i2 += CodedOutputStream.computeMessageSize(2, (MessageLite) this.name_.get(i));
            }
            if ((this.bitField0_ & 1) == 1) {
                i2 += CodedOutputStream.computeBytesSize(3, getIdentifierValueBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeUInt64Size(4, this.positiveIntValue_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeInt64Size(5, this.negativeIntValue_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeDoubleSize(6, this.doubleValue_);
            }
            if ((this.bitField0_ & 16) == 16) {
                i2 += CodedOutputStream.computeBytesSize(7, this.stringValue_);
            }
            if ((this.bitField0_ & 32) == 32) {
                i2 += CodedOutputStream.computeBytesSize(8, getAggregateValueBytes());
            }
            int serializedSize = getUnknownFields().getSerializedSize() + i2;
            this.memoizedSerializedSize = serializedSize;
            return serializedSize;
        }

        public ByteString getStringValue() {
            return this.stringValue_;
        }

        public boolean hasAggregateValue() {
            return (this.bitField0_ & 32) == 32;
        }

        public boolean hasDoubleValue() {
            return (this.bitField0_ & 8) == 8;
        }

        public boolean hasIdentifierValue() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean hasNegativeIntValue() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean hasPositiveIntValue() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean hasStringValue() {
            return (this.bitField0_ & 16) == 16;
        }

        protected FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.f296x2101041;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b != (byte) -1) {
                return b == (byte) 1;
            } else {
                int i = 0;
                while (i < getNameCount()) {
                    if (getName(i).isInitialized()) {
                        i++;
                    } else {
                        this.memoizedIsInitialized = (byte) 0;
                        return false;
                    }
                }
                this.memoizedIsInitialized = (byte) 1;
                return true;
            }
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        protected Builder newBuilderForType(BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.name_.size(); i++) {
                codedOutputStream.writeMessage(2, (MessageLite) this.name_.get(i));
            }
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(3, getIdentifierValueBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeUInt64(4, this.positiveIntValue_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeInt64(5, this.negativeIntValue_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeDouble(6, this.doubleValue_);
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(7, this.stringValue_);
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeBytes(8, getAggregateValueBytes());
            }
            getUnknownFields().writeTo(codedOutputStream);
        }
    }

    static {
        String[] strArr = new String[]{"\n google/protobuf/descriptor.proto\u0012\u000fgoogle.protobuf\"G\n\u0011FileDescriptorSet\u00122\n\u0004file\u0018\u0001 \u0003(\u000b2$.google.protobuf.FileDescriptorProto\"\u0003\n\u0013FileDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000f\n\u0007package\u0018\u0002 \u0001(\t\u0012\u0012\n\ndependency\u0018\u0003 \u0003(\t\u00126\n\fmessage_type\u0018\u0004 \u0003(\u000b2 .google.protobuf.DescriptorProto\u00127\n\tenum_type\u0018\u0005 \u0003(\u000b2$.google.protobuf.EnumDescriptorProto\u00128\n\u0007service\u0018\u0006 \u0003(\u000b2'.google.protobuf.ServiceDescriptorProto\u00128\n\textension\u0018\u0007 \u0003(\u000b2%.google.p", "rotobuf.FieldDescriptorProto\u0012-\n\u0007options\u0018\b \u0001(\u000b2\u001c.google.protobuf.FileOptions\u00129\n\u0010source_code_info\u0018\t \u0001(\u000b2\u001f.google.protobuf.SourceCodeInfo\"©\u0003\n\u000fDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00124\n\u0005field\u0018\u0002 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u00128\n\textension\u0018\u0006 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u00125\n\u000bnested_type\u0018\u0003 \u0003(\u000b2 .google.protobuf.DescriptorProto\u00127\n\tenum_type\u0018\u0004 \u0003(\u000b2$.google.protobuf.EnumDescriptorProto\u0012H\n\u000fexte", "nsion_range\u0018\u0005 \u0003(\u000b2/.google.protobuf.DescriptorProto.ExtensionRange\u00120\n\u0007options\u0018\u0007 \u0001(\u000b2\u001f.google.protobuf.MessageOptions\u001a,\n\u000eExtensionRange\u0012\r\n\u0005start\u0018\u0001 \u0001(\u0005\u0012\u000b\n\u0003end\u0018\u0002 \u0001(\u0005\"\u0005\n\u0014FieldDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000e\n\u0006number\u0018\u0003 \u0001(\u0005\u0012:\n\u0005label\u0018\u0004 \u0001(\u000e2+.google.protobuf.FieldDescriptorProto.Label\u00128\n\u0004type\u0018\u0005 \u0001(\u000e2*.google.protobuf.FieldDescriptorProto.Type\u0012\u0011\n\ttype_name\u0018\u0006 \u0001(\t\u0012\u0010\n\bextendee\u0018\u0002 \u0001(\t\u0012\u0015\n\rdefault_value\u0018\u0007 \u0001(\t\u0012.\n\u0007o", "ptions\u0018\b \u0001(\u000b2\u001d.google.protobuf.FieldOptions\"¶\u0002\n\u0004Type\u0012\u000f\n\u000bTYPE_DOUBLE\u0010\u0001\u0012\u000e\n\nTYPE_FLOAT\u0010\u0002\u0012\u000e\n\nTYPE_INT64\u0010\u0003\u0012\u000f\n\u000bTYPE_UINT64\u0010\u0004\u0012\u000e\n\nTYPE_INT32\u0010\u0005\u0012\u0010\n\fTYPE_FIXED64\u0010\u0006\u0012\u0010\n\fTYPE_FIXED32\u0010\u0007\u0012\r\n\tTYPE_BOOL\u0010\b\u0012\u000f\n\u000bTYPE_STRING\u0010\t\u0012\u000e\n\nTYPE_GROUP\u0010\n\u0012\u0010\n\fTYPE_MESSAGE\u0010\u000b\u0012\u000e\n\nTYPE_BYTES\u0010\f\u0012\u000f\n\u000bTYPE_UINT32\u0010\r\u0012\r\n\tTYPE_ENUM\u0010\u000e\u0012\u0011\n\rTYPE_SFIXED32\u0010\u000f\u0012\u0011\n\rTYPE_SFIXED64\u0010\u0010\u0012\u000f\n\u000bTYPE_SINT32\u0010\u0011\u0012\u000f\n\u000bTYPE_SINT64\u0010\u0012\"C\n\u0005Label\u0012\u0012\n\u000eLABEL_OPTIONAL\u0010\u0001\u0012\u0012\n\u000eLABEL_REQUI", "RED\u0010\u0002\u0012\u0012\n\u000eLABEL_REPEATED\u0010\u0003\"\u0001\n\u0013EnumDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00128\n\u0005value\u0018\u0002 \u0003(\u000b2).google.protobuf.EnumValueDescriptorProto\u0012-\n\u0007options\u0018\u0003 \u0001(\u000b2\u001c.google.protobuf.EnumOptions\"l\n\u0018EnumValueDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000e\n\u0006number\u0018\u0002 \u0001(\u0005\u00122\n\u0007options\u0018\u0003 \u0001(\u000b2!.google.protobuf.EnumValueOptions\"\u0001\n\u0016ServiceDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00126\n\u0006method\u0018\u0002 \u0003(\u000b2&.google.protobuf.MethodDescriptorProto\u00120\n\u0007options\u0018\u0003 \u0001(\u000b2\u001f.googl", "e.protobuf.ServiceOptions\"\n\u0015MethodDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u0012\n\ninput_type\u0018\u0002 \u0001(\t\u0012\u0013\n\u000boutput_type\u0018\u0003 \u0001(\t\u0012/\n\u0007options\u0018\u0004 \u0001(\u000b2\u001e.google.protobuf.MethodOptions\"Õ\u0003\n\u000bFileOptions\u0012\u0014\n\fjava_package\u0018\u0001 \u0001(\t\u0012\u001c\n\u0014java_outer_classname\u0018\b \u0001(\t\u0012\"\n\u0013java_multiple_files\u0018\n \u0001(\b:\u0005false\u0012,\n\u001djava_generate_equals_and_hash\u0018\u0014 \u0001(\b:\u0005false\u0012F\n\foptimize_for\u0018\t \u0001(\u000e2).google.protobuf.FileOptions.OptimizeMode:\u0005SPEED\u0012\"\n\u0013cc_generic_services\u0018", "\u0010 \u0001(\b:\u0005false\u0012$\n\u0015java_generic_services\u0018\u0011 \u0001(\b:\u0005false\u0012\"\n\u0013py_generic_services\u0018\u0012 \u0001(\b:\u0005false\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption\":\n\fOptimizeMode\u0012\t\n\u0005SPEED\u0010\u0001\u0012\r\n\tCODE_SIZE\u0010\u0002\u0012\u0010\n\fLITE_RUNTIME\u0010\u0003*\t\bè\u0007\u0010\u0002\"¸\u0001\n\u000eMessageOptions\u0012&\n\u0017message_set_wire_format\u0018\u0001 \u0001(\b:\u0005false\u0012.\n\u001fno_standard_descriptor_accessor\u0018\u0002 \u0001(\b:\u0005false\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOpti", "on*\t\bè\u0007\u0010\u0002\"\u0002\n\fFieldOptions\u0012:\n\u0005ctype\u0018\u0001 \u0001(\u000e2#.google.protobuf.FieldOptions.CType:\u0006STRING\u0012\u000e\n\u0006packed\u0018\u0002 \u0001(\b\u0012\u0019\n\ndeprecated\u0018\u0003 \u0001(\b:\u0005false\u0012\u001c\n\u0014experimental_map_key\u0018\t \u0001(\t\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption\"/\n\u0005CType\u0012\n\n\u0006STRING\u0010\u0000\u0012\b\n\u0004CORD\u0010\u0001\u0012\u0010\n\fSTRING_PIECE\u0010\u0002*\t\bè\u0007\u0010\u0002\"]\n\u000bEnumOptions\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0002\"b\n\u0010EnumValue", "Options\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0002\"`\n\u000eServiceOptions\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0002\"_\n\rMethodOptions\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0002\"\u0002\n\u0013UninterpretedOption\u0012;\n\u0004name\u0018\u0002 \u0003(\u000b2-.google.protobuf.UninterpretedOption.NamePart\u0012\u0018\n\u0010identifier_value\u0018\u0003 \u0001(\t\u0012\u001a\n\u0012pos", "itive_int_value\u0018\u0004 \u0001(\u0004\u0012\u001a\n\u0012negative_int_value\u0018\u0005 \u0001(\u0003\u0012\u0014\n\fdouble_value\u0018\u0006 \u0001(\u0001\u0012\u0014\n\fstring_value\u0018\u0007 \u0001(\f\u0012\u0017\n\u000faggregate_value\u0018\b \u0001(\t\u001a3\n\bNamePart\u0012\u0011\n\tname_part\u0018\u0001 \u0002(\t\u0012\u0014\n\fis_extension\u0018\u0002 \u0002(\b\"|\n\u000eSourceCodeInfo\u0012:\n\blocation\u0018\u0001 \u0003(\u000b2(.google.protobuf.SourceCodeInfo.Location\u001a.\n\bLocation\u0012\u0010\n\u0004path\u0018\u0001 \u0003(\u0005B\u0002\u0010\u0001\u0012\u0010\n\u0004span\u0018\u0002 \u0003(\u0005B\u0002\u0010\u0001B)\n\u0013com.google.protobufB\u0010DescriptorProtosH\u0001"};
        FileDescriptor.internalBuildGeneratedFileFrom(strArr, new FileDescriptor[0], new C09401());
    }

    private DescriptorProtos() {
    }

    public static FileDescriptor getDescriptor() {
        return descriptor;
    }

    public static void registerAllExtensions(ExtensionRegistry extensionRegistry) {
    }
}
