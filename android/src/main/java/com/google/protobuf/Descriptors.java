package com.google.protobuf;

import com.google.protobuf.DescriptorProtos.DescriptorProto;
import com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange;
import com.google.protobuf.DescriptorProtos.EnumDescriptorProto;
import com.google.protobuf.DescriptorProtos.EnumOptions;
import com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.MethodDescriptorProto;
import com.google.protobuf.DescriptorProtos.MethodOptions;
import com.google.protobuf.DescriptorProtos.ServiceDescriptorProto;
import com.google.protobuf.DescriptorProtos.ServiceOptions;
import com.google.protobuf.FieldSet.FieldDescriptorLite;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.MessageLite.Builder;
import com.google.protobuf.WireFormat.FieldType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mortbay.jetty.HttpVersions;

public final class Descriptors {

    private interface GenericDescriptor {
        FileDescriptor getFile();

        String getFullName();

        String getName();

        Message toProto();
    }

    public static final class Descriptor implements GenericDescriptor {
        private final Descriptor containingType;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final FieldDescriptor[] fields;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private final Descriptor[] nestedTypes;
        private DescriptorProto proto;

        private Descriptor(DescriptorProto descriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int i) throws DescriptorValidationException {
            int i2;
            this.index = i;
            this.proto = descriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, descriptorProto.getName());
            this.file = fileDescriptor;
            this.containingType = descriptor;
            this.nestedTypes = new Descriptor[descriptorProto.getNestedTypeCount()];
            for (int i3 = 0; i3 < descriptorProto.getNestedTypeCount(); i3++) {
                this.nestedTypes[i3] = new Descriptor(descriptorProto.getNestedType(i3), fileDescriptor, this, i3);
            }
            this.enumTypes = new EnumDescriptor[descriptorProto.getEnumTypeCount()];
            for (i2 = 0; i2 < descriptorProto.getEnumTypeCount(); i2++) {
                this.enumTypes[i2] = new EnumDescriptor(descriptorProto.getEnumType(i2), fileDescriptor, this, i2);
            }
            this.fields = new FieldDescriptor[descriptorProto.getFieldCount()];
            for (i2 = 0; i2 < descriptorProto.getFieldCount(); i2++) {
                this.fields[i2] = new FieldDescriptor(descriptorProto.getField(i2), fileDescriptor, this, i2, false);
            }
            this.extensions = new FieldDescriptor[descriptorProto.getExtensionCount()];
            for (i2 = 0; i2 < descriptorProto.getExtensionCount(); i2++) {
                this.extensions[i2] = new FieldDescriptor(descriptorProto.getExtension(i2), fileDescriptor, this, i2, true);
            }
            fileDescriptor.pool.addSymbol(this);
        }

        private void crossLink() throws DescriptorValidationException {
            int i = 0;
            for (Descriptor crossLink : this.nestedTypes) {
                crossLink.crossLink();
            }
            for (FieldDescriptor access$700 : this.fields) {
                access$700.crossLink();
            }
            FieldDescriptor[] fieldDescriptorArr = this.extensions;
            int length = fieldDescriptorArr.length;
            while (i < length) {
                fieldDescriptorArr[i].crossLink();
                i++;
            }
        }

        private void setProto(DescriptorProto descriptorProto) {
            int i;
            int i2 = 0;
            this.proto = descriptorProto;
            for (i = 0; i < this.nestedTypes.length; i++) {
                this.nestedTypes[i].setProto(descriptorProto.getNestedType(i));
            }
            for (i = 0; i < this.enumTypes.length; i++) {
                this.enumTypes[i].setProto(descriptorProto.getEnumType(i));
            }
            for (i = 0; i < this.fields.length; i++) {
                this.fields[i].setProto(descriptorProto.getField(i));
            }
            while (i2 < this.extensions.length) {
                this.extensions[i2].setProto(descriptorProto.getExtension(i2));
                i2++;
            }
        }

        public EnumDescriptor findEnumTypeByName(String str) {
            GenericDescriptor findSymbol = this.file.pool.findSymbol(this.fullName + '.' + str);
            return (findSymbol == null || !(findSymbol instanceof EnumDescriptor)) ? null : (EnumDescriptor) findSymbol;
        }

        public FieldDescriptor findFieldByName(String str) {
            GenericDescriptor findSymbol = this.file.pool.findSymbol(this.fullName + '.' + str);
            return (findSymbol == null || !(findSymbol instanceof FieldDescriptor)) ? null : (FieldDescriptor) findSymbol;
        }

        public FieldDescriptor findFieldByNumber(int i) {
            return (FieldDescriptor) this.file.pool.fieldsByNumber.get(new DescriptorIntPair(this, i));
        }

        public Descriptor findNestedTypeByName(String str) {
            GenericDescriptor findSymbol = this.file.pool.findSymbol(this.fullName + '.' + str);
            return (findSymbol == null || !(findSymbol instanceof Descriptor)) ? null : (Descriptor) findSymbol;
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<FieldDescriptor> getFields() {
            return Collections.unmodifiableList(Arrays.asList(this.fields));
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public String getName() {
            return this.proto.getName();
        }

        public List<Descriptor> getNestedTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.nestedTypes));
        }

        public MessageOptions getOptions() {
            return this.proto.getOptions();
        }

        public boolean isExtensionNumber(int i) {
            for (ExtensionRange extensionRange : this.proto.getExtensionRangeList()) {
                if (extensionRange.getStart() <= i && i < extensionRange.getEnd()) {
                    return true;
                }
            }
            return false;
        }

        public DescriptorProto toProto() {
            return this.proto;
        }
    }

    private static final class DescriptorPool {
        static final /* synthetic */ boolean $assertionsDisabled = (!Descriptors.class.desiredAssertionStatus());
        private final DescriptorPool[] dependencies;
        private final Map<String, GenericDescriptor> descriptorsByName = new HashMap();
        private final Map<DescriptorIntPair, EnumValueDescriptor> enumValuesByNumber = new HashMap();
        private final Map<DescriptorIntPair, FieldDescriptor> fieldsByNumber = new HashMap();

        private static final class DescriptorIntPair {
            private final GenericDescriptor descriptor;
            private final int number;

            DescriptorIntPair(GenericDescriptor genericDescriptor, int i) {
                this.descriptor = genericDescriptor;
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

        private static final class PackageDescriptor implements GenericDescriptor {
            private final FileDescriptor file;
            private final String fullName;
            private final String name;

            PackageDescriptor(String str, String str2, FileDescriptor fileDescriptor) {
                this.file = fileDescriptor;
                this.fullName = str2;
                this.name = str;
            }

            public FileDescriptor getFile() {
                return this.file;
            }

            public String getFullName() {
                return this.fullName;
            }

            public String getName() {
                return this.name;
            }

            public Message toProto() {
                return this.file.toProto();
            }
        }

        DescriptorPool(FileDescriptor[] fileDescriptorArr) {
            int i;
            int i2 = 0;
            this.dependencies = new DescriptorPool[fileDescriptorArr.length];
            for (i = 0; i < fileDescriptorArr.length; i++) {
                this.dependencies[i] = fileDescriptorArr[i].pool;
            }
            i = fileDescriptorArr.length;
            while (i2 < i) {
                FileDescriptor fileDescriptor = fileDescriptorArr[i2];
                try {
                    addPackage(fileDescriptor.getPackage(), fileDescriptor);
                } catch (DescriptorValidationException e) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                }
                i2++;
            }
        }

        static void validateSymbolName(GenericDescriptor genericDescriptor) throws DescriptorValidationException {
            String name = genericDescriptor.getName();
            if (name.length() == 0) {
                throw new DescriptorValidationException(genericDescriptor, "Missing name.");
            }
            Object obj = 1;
            int i = 0;
            while (i < name.length()) {
                char charAt = name.charAt(i);
                if (charAt >= '') {
                    obj = null;
                }
                if (!(Character.isLetter(charAt) || charAt == '_' || (Character.isDigit(charAt) && i > 0))) {
                    obj = null;
                }
                i++;
            }
            if (obj == null) {
                throw new DescriptorValidationException(genericDescriptor, '\"' + name + "\" is not a valid identifier.");
            }
        }

        void addEnumValueByNumber(EnumValueDescriptor enumValueDescriptor) {
            DescriptorIntPair descriptorIntPair = new DescriptorIntPair(enumValueDescriptor.getType(), enumValueDescriptor.getNumber());
            EnumValueDescriptor enumValueDescriptor2 = (EnumValueDescriptor) this.enumValuesByNumber.put(descriptorIntPair, enumValueDescriptor);
            if (enumValueDescriptor2 != null) {
                this.enumValuesByNumber.put(descriptorIntPair, enumValueDescriptor2);
            }
        }

        void addFieldByNumber(FieldDescriptor fieldDescriptor) throws DescriptorValidationException {
            DescriptorIntPair descriptorIntPair = new DescriptorIntPair(fieldDescriptor.getContainingType(), fieldDescriptor.getNumber());
            FieldDescriptor fieldDescriptor2 = (FieldDescriptor) this.fieldsByNumber.put(descriptorIntPair, fieldDescriptor);
            if (fieldDescriptor2 != null) {
                this.fieldsByNumber.put(descriptorIntPair, fieldDescriptor2);
                throw new DescriptorValidationException((GenericDescriptor) fieldDescriptor, "Field number " + fieldDescriptor.getNumber() + "has already been used in \"" + fieldDescriptor.getContainingType().getFullName() + "\" by field \"" + fieldDescriptor2.getName() + "\".");
            }
        }

        void addPackage(String str, FileDescriptor fileDescriptor) throws DescriptorValidationException {
            String str2;
            int lastIndexOf = str.lastIndexOf(46);
            if (lastIndexOf == -1) {
                str2 = str;
            } else {
                addPackage(str.substring(0, lastIndexOf), fileDescriptor);
                str2 = str.substring(lastIndexOf + 1);
            }
            GenericDescriptor genericDescriptor = (GenericDescriptor) this.descriptorsByName.put(str, new PackageDescriptor(str2, str, fileDescriptor));
            if (genericDescriptor != null) {
                this.descriptorsByName.put(str, genericDescriptor);
                if (!(genericDescriptor instanceof PackageDescriptor)) {
                    throw new DescriptorValidationException(fileDescriptor, '\"' + str2 + "\" is already defined (as something other than a " + "package) in file \"" + genericDescriptor.getFile().getName() + "\".");
                }
            }
        }

        void addSymbol(GenericDescriptor genericDescriptor) throws DescriptorValidationException {
            validateSymbolName(genericDescriptor);
            String fullName = genericDescriptor.getFullName();
            int lastIndexOf = fullName.lastIndexOf(46);
            GenericDescriptor genericDescriptor2 = (GenericDescriptor) this.descriptorsByName.put(fullName, genericDescriptor);
            if (genericDescriptor2 != null) {
                this.descriptorsByName.put(fullName, genericDescriptor2);
                if (genericDescriptor.getFile() != genericDescriptor2.getFile()) {
                    throw new DescriptorValidationException(genericDescriptor, '\"' + fullName + "\" is already defined in file \"" + genericDescriptor2.getFile().getName() + "\".");
                } else if (lastIndexOf == -1) {
                    throw new DescriptorValidationException(genericDescriptor, '\"' + fullName + "\" is already defined.");
                } else {
                    throw new DescriptorValidationException(genericDescriptor, '\"' + fullName.substring(lastIndexOf + 1) + "\" is already defined in \"" + fullName.substring(0, lastIndexOf) + "\".");
                }
            }
        }

        GenericDescriptor findSymbol(String str) {
            GenericDescriptor genericDescriptor = (GenericDescriptor) this.descriptorsByName.get(str);
            if (genericDescriptor != null) {
                return genericDescriptor;
            }
            for (DescriptorPool descriptorPool : this.dependencies) {
                genericDescriptor = (GenericDescriptor) descriptorPool.descriptorsByName.get(str);
                if (genericDescriptor != null) {
                    return genericDescriptor;
                }
            }
            return null;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        com.google.protobuf.Descriptors.GenericDescriptor lookupSymbol(java.lang.String r7, com.google.protobuf.Descriptors.GenericDescriptor r8) throws com.google.protobuf.Descriptors.DescriptorValidationException {
            /*
            r6 = this;
            r5 = -1;
            r0 = ".";
            r0 = r7.startsWith(r0);
            if (r0 == 0) goto L_0x0034;
        L_0x0009:
            r0 = 1;
            r0 = r7.substring(r0);
            r0 = r6.findSymbol(r0);
        L_0x0012:
            if (r0 != 0) goto L_0x0082;
        L_0x0014:
            r0 = new com.google.protobuf.Descriptors$DescriptorValidationException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = 34;
            r1 = r1.append(r2);
            r1 = r1.append(r7);
            r2 = "\" is not defined.";
            r1 = r1.append(r2);
            r1 = r1.toString();
            r2 = 0;
            r0.<init>(r8, r1);
            throw r0;
        L_0x0034:
            r0 = 46;
            r2 = r7.indexOf(r0);
            if (r2 != r5) goto L_0x0053;
        L_0x003c:
            r0 = r7;
        L_0x003d:
            r3 = new java.lang.StringBuilder;
            r1 = r8.getFullName();
            r3.<init>(r1);
        L_0x0046:
            r1 = ".";
            r4 = r3.lastIndexOf(r1);
            if (r4 != r5) goto L_0x0059;
        L_0x004e:
            r0 = r6.findSymbol(r7);
            goto L_0x0012;
        L_0x0053:
            r0 = 0;
            r0 = r7.substring(r0, r2);
            goto L_0x003d;
        L_0x0059:
            r1 = r4 + 1;
            r3.setLength(r1);
            r3.append(r0);
            r1 = r3.toString();
            r1 = r6.findSymbol(r1);
            if (r1 == 0) goto L_0x007e;
        L_0x006b:
            if (r2 == r5) goto L_0x0083;
        L_0x006d:
            r0 = r4 + 1;
            r3.setLength(r0);
            r3.append(r7);
            r0 = r3.toString();
            r0 = r6.findSymbol(r0);
            goto L_0x0012;
        L_0x007e:
            r3.setLength(r4);
            goto L_0x0046;
        L_0x0082:
            return r0;
        L_0x0083:
            r0 = r1;
            goto L_0x0012;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Descriptors.DescriptorPool.lookupSymbol(java.lang.String, com.google.protobuf.Descriptors$GenericDescriptor):com.google.protobuf.Descriptors$GenericDescriptor");
        }
    }

    public static class DescriptorValidationException extends Exception {
        private static final long serialVersionUID = 5750205775490483148L;
        private final String description;
        private final String name;
        private final Message proto;

        private DescriptorValidationException(FileDescriptor fileDescriptor, String str) {
            super(fileDescriptor.getName() + ": " + str);
            this.name = fileDescriptor.getName();
            this.proto = fileDescriptor.toProto();
            this.description = str;
        }

        private DescriptorValidationException(GenericDescriptor genericDescriptor, String str) {
            super(genericDescriptor.getFullName() + ": " + str);
            this.name = genericDescriptor.getFullName();
            this.proto = genericDescriptor.toProto();
            this.description = str;
        }

        private DescriptorValidationException(GenericDescriptor genericDescriptor, String str, Throwable th) {
            this(genericDescriptor, str);
            initCause(th);
        }

        public String getDescription() {
            return this.description;
        }

        public Message getProblemProto() {
            return this.proto;
        }

        public String getProblemSymbolName() {
            return this.name;
        }
    }

    public static final class EnumDescriptor implements GenericDescriptor, EnumLiteMap<EnumValueDescriptor> {
        private final Descriptor containingType;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private EnumDescriptorProto proto;
        private EnumValueDescriptor[] values;

        private EnumDescriptor(EnumDescriptorProto enumDescriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = enumDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, enumDescriptorProto.getName());
            this.file = fileDescriptor;
            this.containingType = descriptor;
            if (enumDescriptorProto.getValueCount() == 0) {
                throw new DescriptorValidationException((GenericDescriptor) this, "Enums must contain at least one value.");
            }
            this.values = new EnumValueDescriptor[enumDescriptorProto.getValueCount()];
            for (int i2 = 0; i2 < enumDescriptorProto.getValueCount(); i2++) {
                this.values[i2] = new EnumValueDescriptor(enumDescriptorProto.getValue(i2), fileDescriptor, this, i2);
            }
            fileDescriptor.pool.addSymbol(this);
        }

        private void setProto(EnumDescriptorProto enumDescriptorProto) {
            this.proto = enumDescriptorProto;
            for (int i = 0; i < this.values.length; i++) {
                this.values[i].setProto(enumDescriptorProto.getValue(i));
            }
        }

        public EnumValueDescriptor findValueByName(String str) {
            GenericDescriptor findSymbol = this.file.pool.findSymbol(this.fullName + '.' + str);
            return (findSymbol == null || !(findSymbol instanceof EnumValueDescriptor)) ? null : (EnumValueDescriptor) findSymbol;
        }

        public EnumValueDescriptor findValueByNumber(int i) {
            return (EnumValueDescriptor) this.file.pool.enumValuesByNumber.get(new DescriptorIntPair(this, i));
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public String getName() {
            return this.proto.getName();
        }

        public EnumOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<EnumValueDescriptor> getValues() {
            return Collections.unmodifiableList(Arrays.asList(this.values));
        }

        public EnumDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class EnumValueDescriptor implements GenericDescriptor, EnumLite {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private EnumValueDescriptorProto proto;
        private final EnumDescriptor type;

        private EnumValueDescriptor(EnumValueDescriptorProto enumValueDescriptorProto, FileDescriptor fileDescriptor, EnumDescriptor enumDescriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = enumValueDescriptorProto;
            this.file = fileDescriptor;
            this.type = enumDescriptor;
            this.fullName = enumDescriptor.getFullName() + '.' + enumValueDescriptorProto.getName();
            fileDescriptor.pool.addSymbol(this);
            fileDescriptor.pool.addEnumValueByNumber(this);
        }

        private void setProto(EnumValueDescriptorProto enumValueDescriptorProto) {
            this.proto = enumValueDescriptorProto;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public String getName() {
            return this.proto.getName();
        }

        public int getNumber() {
            return this.proto.getNumber();
        }

        public EnumValueOptions getOptions() {
            return this.proto.getOptions();
        }

        public EnumDescriptor getType() {
            return this.type;
        }

        public EnumValueDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class FieldDescriptor implements GenericDescriptor, Comparable<FieldDescriptor>, FieldDescriptorLite<FieldDescriptor> {
        private static final FieldType[] table = FieldType.values();
        private Descriptor containingType;
        private Object defaultValue;
        private EnumDescriptor enumType;
        private final Descriptor extensionScope;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private Descriptor messageType;
        private FieldDescriptorProto proto;
        private Type type;

        public enum JavaType {
            INT(Integer.valueOf(0)),
            LONG(Long.valueOf(0)),
            FLOAT(Float.valueOf(0.0f)),
            DOUBLE(Double.valueOf(0.0d)),
            BOOLEAN(Boolean.valueOf(false)),
            STRING(HttpVersions.HTTP_0_9),
            BYTE_STRING(ByteString.EMPTY),
            ENUM(null),
            MESSAGE(null);
            
            private final Object defaultDefault;

            private JavaType(Object obj) {
                this.defaultDefault = obj;
            }
        }

        public enum Type {
            DOUBLE(JavaType.DOUBLE),
            FLOAT(JavaType.FLOAT),
            INT64(JavaType.LONG),
            UINT64(JavaType.LONG),
            INT32(JavaType.INT),
            FIXED64(JavaType.LONG),
            FIXED32(JavaType.INT),
            BOOL(JavaType.BOOLEAN),
            STRING(JavaType.STRING),
            GROUP(JavaType.MESSAGE),
            MESSAGE(JavaType.MESSAGE),
            BYTES(JavaType.BYTE_STRING),
            UINT32(JavaType.INT),
            ENUM(JavaType.ENUM),
            SFIXED32(JavaType.INT),
            SFIXED64(JavaType.LONG),
            SINT32(JavaType.INT),
            SINT64(JavaType.LONG);
            
            private JavaType javaType;

            private Type(JavaType javaType) {
                this.javaType = javaType;
            }

            public static Type valueOf(com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type type) {
                return values()[type.getNumber() - 1];
            }

            public JavaType getJavaType() {
                return this.javaType;
            }

            public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type toProto() {
                return com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.valueOf(ordinal() + 1);
            }
        }

        static {
            if (Type.values().length != com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.values().length) {
                throw new RuntimeException("descriptor.proto has a new declared type but Desrciptors.java wasn't updated.");
            }
        }

        private FieldDescriptor(FieldDescriptorProto fieldDescriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int i, boolean z) throws DescriptorValidationException {
            this.index = i;
            this.proto = fieldDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, fieldDescriptorProto.getName());
            this.file = fileDescriptor;
            if (fieldDescriptorProto.hasType()) {
                this.type = Type.valueOf(fieldDescriptorProto.getType());
            }
            if (getNumber() <= 0) {
                throw new DescriptorValidationException((GenericDescriptor) this, "Field numbers must be positive integers.");
            } else if (!fieldDescriptorProto.getOptions().getPacked() || isPackable()) {
                if (z) {
                    if (fieldDescriptorProto.hasExtendee()) {
                        this.containingType = null;
                        if (descriptor != null) {
                            this.extensionScope = descriptor;
                        } else {
                            this.extensionScope = null;
                        }
                    } else {
                        throw new DescriptorValidationException((GenericDescriptor) this, "FieldDescriptorProto.extendee not set for extension field.");
                    }
                } else if (fieldDescriptorProto.hasExtendee()) {
                    throw new DescriptorValidationException((GenericDescriptor) this, "FieldDescriptorProto.extendee set for non-extension field.");
                } else {
                    this.containingType = descriptor;
                    this.extensionScope = null;
                }
                fileDescriptor.pool.addSymbol(this);
            } else {
                throw new DescriptorValidationException((GenericDescriptor) this, "[packed = true] can only be specified for repeated primitive fields.");
            }
        }

        private void crossLink() throws DescriptorValidationException {
            GenericDescriptor lookupSymbol;
            if (this.proto.hasExtendee()) {
                lookupSymbol = this.file.pool.lookupSymbol(this.proto.getExtendee(), this);
                if (lookupSymbol instanceof Descriptor) {
                    this.containingType = (Descriptor) lookupSymbol;
                    if (!getContainingType().isExtensionNumber(getNumber())) {
                        throw new DescriptorValidationException((GenericDescriptor) this, '\"' + getContainingType().getFullName() + "\" does not declare " + getNumber() + " as an extension number.");
                    }
                }
                throw new DescriptorValidationException((GenericDescriptor) this, '\"' + this.proto.getExtendee() + "\" is not a message type.");
            }
            if (this.proto.hasTypeName()) {
                lookupSymbol = this.file.pool.lookupSymbol(this.proto.getTypeName(), this);
                if (!this.proto.hasType()) {
                    if (lookupSymbol instanceof Descriptor) {
                        this.type = Type.MESSAGE;
                    } else if (lookupSymbol instanceof EnumDescriptor) {
                        this.type = Type.ENUM;
                    } else {
                        throw new DescriptorValidationException((GenericDescriptor) this, '\"' + this.proto.getTypeName() + "\" is not a type.");
                    }
                }
                if (getJavaType() == JavaType.MESSAGE) {
                    if (lookupSymbol instanceof Descriptor) {
                        this.messageType = (Descriptor) lookupSymbol;
                        if (this.proto.hasDefaultValue()) {
                            throw new DescriptorValidationException((GenericDescriptor) this, "Messages can't have default values.");
                        }
                    }
                    throw new DescriptorValidationException((GenericDescriptor) this, '\"' + this.proto.getTypeName() + "\" is not a message type.");
                } else if (getJavaType() != JavaType.ENUM) {
                    throw new DescriptorValidationException((GenericDescriptor) this, "Field with primitive type has type_name.");
                } else if (lookupSymbol instanceof EnumDescriptor) {
                    this.enumType = (EnumDescriptor) lookupSymbol;
                } else {
                    throw new DescriptorValidationException((GenericDescriptor) this, '\"' + this.proto.getTypeName() + "\" is not an enum type.");
                }
            } else if (getJavaType() == JavaType.MESSAGE || getJavaType() == JavaType.ENUM) {
                throw new DescriptorValidationException((GenericDescriptor) this, "Field with message or enum type missing type_name.");
            }
            if (!this.proto.hasDefaultValue()) {
                if (!isRepeated()) {
                    switch (getJavaType()) {
                        case ENUM:
                            this.defaultValue = this.enumType.getValues().get(0);
                            break;
                        case MESSAGE:
                            this.defaultValue = null;
                            break;
                        default:
                            this.defaultValue = getJavaType().defaultDefault;
                            break;
                    }
                }
                this.defaultValue = Collections.emptyList();
            } else if (isRepeated()) {
                throw new DescriptorValidationException((GenericDescriptor) this, "Repeated fields cannot have default values.");
            } else {
                try {
                    switch (getType()) {
                        case INT32:
                        case SINT32:
                        case SFIXED32:
                            this.defaultValue = Integer.valueOf(TextFormat.parseInt32(this.proto.getDefaultValue()));
                            break;
                        case UINT32:
                        case FIXED32:
                            this.defaultValue = Integer.valueOf(TextFormat.parseUInt32(this.proto.getDefaultValue()));
                            break;
                        case INT64:
                        case SINT64:
                        case SFIXED64:
                            this.defaultValue = Long.valueOf(TextFormat.parseInt64(this.proto.getDefaultValue()));
                            break;
                        case UINT64:
                        case FIXED64:
                            this.defaultValue = Long.valueOf(TextFormat.parseUInt64(this.proto.getDefaultValue()));
                            break;
                        case FLOAT:
                            if (!this.proto.getDefaultValue().equals("inf")) {
                                if (!this.proto.getDefaultValue().equals("-inf")) {
                                    if (!this.proto.getDefaultValue().equals("nan")) {
                                        this.defaultValue = Float.valueOf(this.proto.getDefaultValue());
                                        break;
                                    } else {
                                        this.defaultValue = Float.valueOf(Float.NaN);
                                        break;
                                    }
                                }
                                this.defaultValue = Float.valueOf(Float.NEGATIVE_INFINITY);
                                break;
                            }
                            this.defaultValue = Float.valueOf(Float.POSITIVE_INFINITY);
                            break;
                        case DOUBLE:
                            if (!this.proto.getDefaultValue().equals("inf")) {
                                if (!this.proto.getDefaultValue().equals("-inf")) {
                                    if (!this.proto.getDefaultValue().equals("nan")) {
                                        this.defaultValue = Double.valueOf(this.proto.getDefaultValue());
                                        break;
                                    } else {
                                        this.defaultValue = Double.valueOf(Double.NaN);
                                        break;
                                    }
                                }
                                this.defaultValue = Double.valueOf(Double.NEGATIVE_INFINITY);
                                break;
                            }
                            this.defaultValue = Double.valueOf(Double.POSITIVE_INFINITY);
                            break;
                        case BOOL:
                            this.defaultValue = Boolean.valueOf(this.proto.getDefaultValue());
                            break;
                        case STRING:
                            this.defaultValue = this.proto.getDefaultValue();
                            break;
                        case BYTES:
                            this.defaultValue = TextFormat.unescapeBytes(this.proto.getDefaultValue());
                            break;
                        case ENUM:
                            this.defaultValue = this.enumType.findValueByName(this.proto.getDefaultValue());
                            if (this.defaultValue == null) {
                                throw new DescriptorValidationException((GenericDescriptor) this, "Unknown enum default value: \"" + this.proto.getDefaultValue() + '\"');
                            }
                            break;
                        case MESSAGE:
                        case GROUP:
                            throw new DescriptorValidationException((GenericDescriptor) this, "Message type had default value.");
                    }
                } catch (Throwable e) {
                    throw new DescriptorValidationException(this, "Couldn't parse default value: " + e.getMessage(), e);
                } catch (Throwable e2) {
                    throw new DescriptorValidationException(this, "Could not parse default value: \"" + this.proto.getDefaultValue() + '\"', e2);
                }
            }
            if (!isExtension()) {
                this.file.pool.addFieldByNumber(this);
            }
            if (this.containingType != null && this.containingType.getOptions().getMessageSetWireFormat()) {
                if (!isExtension()) {
                    throw new DescriptorValidationException((GenericDescriptor) this, "MessageSets cannot have fields, only extensions.");
                } else if (!isOptional() || getType() != Type.MESSAGE) {
                    throw new DescriptorValidationException((GenericDescriptor) this, "Extensions of MessageSets must be optional messages.");
                }
            }
        }

        private void setProto(FieldDescriptorProto fieldDescriptorProto) {
            this.proto = fieldDescriptorProto;
        }

        public int compareTo(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.containingType == this.containingType) {
                return getNumber() - fieldDescriptor.getNumber();
            }
            throw new IllegalArgumentException("FieldDescriptors can only be compared to other FieldDescriptors for fields of the same message type.");
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public Object getDefaultValue() {
            if (getJavaType() != JavaType.MESSAGE) {
                return this.defaultValue;
            }
            throw new UnsupportedOperationException("FieldDescriptor.getDefaultValue() called on an embedded message field.");
        }

        public EnumDescriptor getEnumType() {
            if (getJavaType() == JavaType.ENUM) {
                return this.enumType;
            }
            throw new UnsupportedOperationException("This field is not of enum type.");
        }

        public Descriptor getExtensionScope() {
            if (isExtension()) {
                return this.extensionScope;
            }
            throw new UnsupportedOperationException("This field is not an extension.");
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public JavaType getJavaType() {
            return this.type.getJavaType();
        }

        public com.google.protobuf.WireFormat.JavaType getLiteJavaType() {
            return getLiteType().getJavaType();
        }

        public FieldType getLiteType() {
            return table[this.type.ordinal()];
        }

        public Descriptor getMessageType() {
            if (getJavaType() == JavaType.MESSAGE) {
                return this.messageType;
            }
            throw new UnsupportedOperationException("This field is not of message type.");
        }

        public String getName() {
            return this.proto.getName();
        }

        public int getNumber() {
            return this.proto.getNumber();
        }

        public FieldOptions getOptions() {
            return this.proto.getOptions();
        }

        public Type getType() {
            return this.type;
        }

        public boolean hasDefaultValue() {
            return this.proto.hasDefaultValue();
        }

        public Builder internalMergeFrom(Builder builder, MessageLite messageLite) {
            return ((Message.Builder) builder).mergeFrom((Message) messageLite);
        }

        public boolean isExtension() {
            return this.proto.hasExtendee();
        }

        public boolean isOptional() {
            return this.proto.getLabel() == Label.LABEL_OPTIONAL;
        }

        public boolean isPackable() {
            return isRepeated() && getLiteType().isPackable();
        }

        public boolean isPacked() {
            return getOptions().getPacked();
        }

        public boolean isRepeated() {
            return this.proto.getLabel() == Label.LABEL_REPEATED;
        }

        public boolean isRequired() {
            return this.proto.getLabel() == Label.LABEL_REQUIRED;
        }

        public FieldDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class FileDescriptor {
        private final FileDescriptor[] dependencies;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final Descriptor[] messageTypes;
        private final DescriptorPool pool;
        private FileDescriptorProto proto;
        private final ServiceDescriptor[] services;

        public interface InternalDescriptorAssigner {
            ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor);
        }

        private FileDescriptor(FileDescriptorProto fileDescriptorProto, FileDescriptor[] fileDescriptorArr, DescriptorPool descriptorPool) throws DescriptorValidationException {
            int i;
            this.pool = descriptorPool;
            this.proto = fileDescriptorProto;
            this.dependencies = (FileDescriptor[]) fileDescriptorArr.clone();
            descriptorPool.addPackage(getPackage(), this);
            this.messageTypes = new Descriptor[fileDescriptorProto.getMessageTypeCount()];
            for (i = 0; i < fileDescriptorProto.getMessageTypeCount(); i++) {
                this.messageTypes[i] = new Descriptor(fileDescriptorProto.getMessageType(i), this, null, i);
            }
            this.enumTypes = new EnumDescriptor[fileDescriptorProto.getEnumTypeCount()];
            for (i = 0; i < fileDescriptorProto.getEnumTypeCount(); i++) {
                this.enumTypes[i] = new EnumDescriptor(fileDescriptorProto.getEnumType(i), this, null, i);
            }
            this.services = new ServiceDescriptor[fileDescriptorProto.getServiceCount()];
            for (int i2 = 0; i2 < fileDescriptorProto.getServiceCount(); i2++) {
                this.services[i2] = new ServiceDescriptor(fileDescriptorProto.getService(i2), this, i2);
            }
            this.extensions = new FieldDescriptor[fileDescriptorProto.getExtensionCount()];
            for (i = 0; i < fileDescriptorProto.getExtensionCount(); i++) {
                this.extensions[i] = new FieldDescriptor(fileDescriptorProto.getExtension(i), this, null, i, true);
            }
        }

        public static FileDescriptor buildFrom(FileDescriptorProto fileDescriptorProto, FileDescriptor[] fileDescriptorArr) throws DescriptorValidationException {
            FileDescriptor fileDescriptor = new FileDescriptor(fileDescriptorProto, fileDescriptorArr, new DescriptorPool(fileDescriptorArr));
            if (fileDescriptorArr.length != fileDescriptorProto.getDependencyCount()) {
                throw new DescriptorValidationException(fileDescriptor, "Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.");
            }
            int i = 0;
            while (i < fileDescriptorProto.getDependencyCount()) {
                if (fileDescriptorArr[i].getName().equals(fileDescriptorProto.getDependency(i))) {
                    i++;
                } else {
                    throw new DescriptorValidationException(fileDescriptor, "Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.");
                }
            }
            fileDescriptor.crossLink();
            return fileDescriptor;
        }

        private void crossLink() throws DescriptorValidationException {
            int i = 0;
            for (Descriptor access$500 : this.messageTypes) {
                access$500.crossLink();
            }
            for (ServiceDescriptor access$600 : this.services) {
                access$600.crossLink();
            }
            FieldDescriptor[] fieldDescriptorArr = this.extensions;
            int length = fieldDescriptorArr.length;
            while (i < length) {
                fieldDescriptorArr[i].crossLink();
                i++;
            }
        }

        public static void internalBuildGeneratedFileFrom(String[] strArr, FileDescriptor[] fileDescriptorArr, InternalDescriptorAssigner internalDescriptorAssigner) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String append : strArr) {
                stringBuilder.append(append);
            }
            try {
                byte[] bytes = stringBuilder.toString().getBytes("ISO-8859-1");
                try {
                    try {
                        FileDescriptor buildFrom = buildFrom(FileDescriptorProto.parseFrom(bytes), fileDescriptorArr);
                        ExtensionRegistryLite assignDescriptors = internalDescriptorAssigner.assignDescriptors(buildFrom);
                        if (assignDescriptors != null) {
                            try {
                                buildFrom.setProto(FileDescriptorProto.parseFrom(bytes, assignDescriptors));
                            } catch (Throwable e) {
                                throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e);
                            }
                        }
                    } catch (Throwable e2) {
                        throw new IllegalArgumentException("Invalid embedded descriptor for \"" + r1.getName() + "\".", e2);
                    }
                } catch (Throwable e22) {
                    throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e22);
                }
            } catch (Throwable e222) {
                throw new RuntimeException("Standard encoding ISO-8859-1 not supported by JVM.", e222);
            }
        }

        private void setProto(FileDescriptorProto fileDescriptorProto) {
            int i;
            int i2 = 0;
            this.proto = fileDescriptorProto;
            for (i = 0; i < this.messageTypes.length; i++) {
                this.messageTypes[i].setProto(fileDescriptorProto.getMessageType(i));
            }
            for (i = 0; i < this.enumTypes.length; i++) {
                this.enumTypes[i].setProto(fileDescriptorProto.getEnumType(i));
            }
            for (i = 0; i < this.services.length; i++) {
                this.services[i].setProto(fileDescriptorProto.getService(i));
            }
            while (i2 < this.extensions.length) {
                this.extensions[i2].setProto(fileDescriptorProto.getExtension(i2));
                i2++;
            }
        }

        public EnumDescriptor findEnumTypeByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            GenericDescriptor findSymbol = this.pool.findSymbol(str);
            return (findSymbol != null && (findSymbol instanceof EnumDescriptor) && findSymbol.getFile() == this) ? (EnumDescriptor) findSymbol : null;
        }

        public FieldDescriptor findExtensionByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            GenericDescriptor findSymbol = this.pool.findSymbol(str);
            return (findSymbol != null && (findSymbol instanceof FieldDescriptor) && findSymbol.getFile() == this) ? (FieldDescriptor) findSymbol : null;
        }

        public Descriptor findMessageTypeByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            GenericDescriptor findSymbol = this.pool.findSymbol(str);
            return (findSymbol != null && (findSymbol instanceof Descriptor) && findSymbol.getFile() == this) ? (Descriptor) findSymbol : null;
        }

        public ServiceDescriptor findServiceByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            GenericDescriptor findSymbol = this.pool.findSymbol(str);
            return (findSymbol != null && (findSymbol instanceof ServiceDescriptor) && findSymbol.getFile() == this) ? (ServiceDescriptor) findSymbol : null;
        }

        public List<FileDescriptor> getDependencies() {
            return Collections.unmodifiableList(Arrays.asList(this.dependencies));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<Descriptor> getMessageTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.messageTypes));
        }

        public String getName() {
            return this.proto.getName();
        }

        public FileOptions getOptions() {
            return this.proto.getOptions();
        }

        public String getPackage() {
            return this.proto.getPackage();
        }

        public List<ServiceDescriptor> getServices() {
            return Collections.unmodifiableList(Arrays.asList(this.services));
        }

        public FileDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class MethodDescriptor implements GenericDescriptor {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private Descriptor inputType;
        private Descriptor outputType;
        private MethodDescriptorProto proto;
        private final ServiceDescriptor service;

        private MethodDescriptor(MethodDescriptorProto methodDescriptorProto, FileDescriptor fileDescriptor, ServiceDescriptor serviceDescriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = methodDescriptorProto;
            this.file = fileDescriptor;
            this.service = serviceDescriptor;
            this.fullName = serviceDescriptor.getFullName() + '.' + methodDescriptorProto.getName();
            fileDescriptor.pool.addSymbol(this);
        }

        private void crossLink() throws DescriptorValidationException {
            GenericDescriptor lookupSymbol = this.file.pool.lookupSymbol(this.proto.getInputType(), this);
            if (lookupSymbol instanceof Descriptor) {
                this.inputType = (Descriptor) lookupSymbol;
                lookupSymbol = this.file.pool.lookupSymbol(this.proto.getOutputType(), this);
                if (lookupSymbol instanceof Descriptor) {
                    this.outputType = (Descriptor) lookupSymbol;
                    return;
                }
                throw new DescriptorValidationException((GenericDescriptor) this, '\"' + this.proto.getOutputType() + "\" is not a message type.");
            }
            throw new DescriptorValidationException((GenericDescriptor) this, '\"' + this.proto.getInputType() + "\" is not a message type.");
        }

        private void setProto(MethodDescriptorProto methodDescriptorProto) {
            this.proto = methodDescriptorProto;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public Descriptor getInputType() {
            return this.inputType;
        }

        public String getName() {
            return this.proto.getName();
        }

        public MethodOptions getOptions() {
            return this.proto.getOptions();
        }

        public Descriptor getOutputType() {
            return this.outputType;
        }

        public ServiceDescriptor getService() {
            return this.service;
        }

        public MethodDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class ServiceDescriptor implements GenericDescriptor {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private MethodDescriptor[] methods;
        private ServiceDescriptorProto proto;

        private ServiceDescriptor(ServiceDescriptorProto serviceDescriptorProto, FileDescriptor fileDescriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = serviceDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, null, serviceDescriptorProto.getName());
            this.file = fileDescriptor;
            this.methods = new MethodDescriptor[serviceDescriptorProto.getMethodCount()];
            for (int i2 = 0; i2 < serviceDescriptorProto.getMethodCount(); i2++) {
                this.methods[i2] = new MethodDescriptor(serviceDescriptorProto.getMethod(i2), fileDescriptor, this, i2);
            }
            fileDescriptor.pool.addSymbol(this);
        }

        private void crossLink() throws DescriptorValidationException {
            for (MethodDescriptor access$2200 : this.methods) {
                access$2200.crossLink();
            }
        }

        private void setProto(ServiceDescriptorProto serviceDescriptorProto) {
            this.proto = serviceDescriptorProto;
            for (int i = 0; i < this.methods.length; i++) {
                this.methods[i].setProto(serviceDescriptorProto.getMethod(i));
            }
        }

        public MethodDescriptor findMethodByName(String str) {
            GenericDescriptor findSymbol = this.file.pool.findSymbol(this.fullName + '.' + str);
            return (findSymbol == null || !(findSymbol instanceof MethodDescriptor)) ? null : (MethodDescriptor) findSymbol;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public List<MethodDescriptor> getMethods() {
            return Collections.unmodifiableList(Arrays.asList(this.methods));
        }

        public String getName() {
            return this.proto.getName();
        }

        public ServiceOptions getOptions() {
            return this.proto.getOptions();
        }

        public ServiceDescriptorProto toProto() {
            return this.proto;
        }
    }

    private static String computeFullName(FileDescriptor fileDescriptor, Descriptor descriptor, String str) {
        return descriptor != null ? descriptor.getFullName() + '.' + str : fileDescriptor.getPackage().length() > 0 ? fileDescriptor.getPackage() + '.' + str : str;
    }
}
