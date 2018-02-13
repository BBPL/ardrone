package com.google.protobuf;

import com.google.common.base.Ascii;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.ExtensionRegistry.ExtensionInfo;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.UnknownFieldSet.Field;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mortbay.jetty.HttpVersions;

public final class TextFormat {
    private static final int BUFFER_SIZE = 4096;
    private static final Printer DEFAULT_PRINTER = new Printer(false);
    private static final Printer SINGLE_LINE_PRINTER = new Printer(true);

    static class InvalidEscapeSequenceException extends IOException {
        private static final long serialVersionUID = -8164033650142593304L;

        InvalidEscapeSequenceException(String str) {
            super(str);
        }
    }

    public static class ParseException extends IOException {
        private static final long serialVersionUID = 3196188060225107702L;

        public ParseException(String str) {
            super(str);
        }
    }

    private static final class Printer {
        final boolean singleLineMode;

        private Printer(boolean z) {
            this.singleLineMode = z;
        }

        private void print(Message message, TextGenerator textGenerator) throws IOException {
            for (Entry entry : message.getAllFields().entrySet()) {
                printField((FieldDescriptor) entry.getKey(), entry.getValue(), textGenerator);
            }
            printUnknownFields(message.getUnknownFields(), textGenerator);
        }

        private void printField(FieldDescriptor fieldDescriptor, Object obj, TextGenerator textGenerator) throws IOException {
            if (fieldDescriptor.isRepeated()) {
                for (Object printSingleField : (List) obj) {
                    printSingleField(fieldDescriptor, printSingleField, textGenerator);
                }
                return;
            }
            printSingleField(fieldDescriptor, obj, textGenerator);
        }

        private void printFieldValue(FieldDescriptor fieldDescriptor, Object obj, TextGenerator textGenerator) throws IOException {
            switch (fieldDescriptor.getType()) {
                case INT32:
                case SINT32:
                case SFIXED32:
                    textGenerator.print(((Integer) obj).toString());
                    return;
                case INT64:
                case SINT64:
                case SFIXED64:
                    textGenerator.print(((Long) obj).toString());
                    return;
                case BOOL:
                    textGenerator.print(((Boolean) obj).toString());
                    return;
                case FLOAT:
                    textGenerator.print(((Float) obj).toString());
                    return;
                case DOUBLE:
                    textGenerator.print(((Double) obj).toString());
                    return;
                case UINT32:
                case FIXED32:
                    textGenerator.print(TextFormat.unsignedToString(((Integer) obj).intValue()));
                    return;
                case UINT64:
                case FIXED64:
                    textGenerator.print(TextFormat.unsignedToString(((Long) obj).longValue()));
                    return;
                case STRING:
                    textGenerator.print("\"");
                    textGenerator.print(TextFormat.escapeText((String) obj));
                    textGenerator.print("\"");
                    return;
                case BYTES:
                    textGenerator.print("\"");
                    textGenerator.print(TextFormat.escapeBytes((ByteString) obj));
                    textGenerator.print("\"");
                    return;
                case ENUM:
                    textGenerator.print(((EnumValueDescriptor) obj).getName());
                    return;
                case MESSAGE:
                case GROUP:
                    print((Message) obj, textGenerator);
                    return;
                default:
                    return;
            }
        }

        private void printSingleField(FieldDescriptor fieldDescriptor, Object obj, TextGenerator textGenerator) throws IOException {
            if (fieldDescriptor.isExtension()) {
                textGenerator.print("[");
                if (fieldDescriptor.getContainingType().getOptions().getMessageSetWireFormat() && fieldDescriptor.getType() == Type.MESSAGE && fieldDescriptor.isOptional() && fieldDescriptor.getExtensionScope() == fieldDescriptor.getMessageType()) {
                    textGenerator.print(fieldDescriptor.getMessageType().getFullName());
                } else {
                    textGenerator.print(fieldDescriptor.getFullName());
                }
                textGenerator.print("]");
            } else if (fieldDescriptor.getType() == Type.GROUP) {
                textGenerator.print(fieldDescriptor.getMessageType().getName());
            } else {
                textGenerator.print(fieldDescriptor.getName());
            }
            if (fieldDescriptor.getJavaType() != JavaType.MESSAGE) {
                textGenerator.print(": ");
            } else if (this.singleLineMode) {
                textGenerator.print(" { ");
            } else {
                textGenerator.print(" {\n");
                textGenerator.indent();
            }
            printFieldValue(fieldDescriptor, obj, textGenerator);
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                if (this.singleLineMode) {
                    textGenerator.print("} ");
                    return;
                }
                textGenerator.outdent();
                textGenerator.print("}\n");
            } else if (this.singleLineMode) {
                textGenerator.print(" ");
            } else {
                textGenerator.print("\n");
            }
        }

        private void printUnknownField(int i, int i2, List<?> list, TextGenerator textGenerator) throws IOException {
            for (Object next : list) {
                textGenerator.print(String.valueOf(i));
                textGenerator.print(": ");
                TextFormat.printUnknownFieldValue(i2, next, textGenerator);
                textGenerator.print(this.singleLineMode ? " " : "\n");
            }
        }

        private void printUnknownFields(UnknownFieldSet unknownFieldSet, TextGenerator textGenerator) throws IOException {
            for (Entry entry : unknownFieldSet.asMap().entrySet()) {
                int intValue = ((Integer) entry.getKey()).intValue();
                Field field = (Field) entry.getValue();
                printUnknownField(intValue, 0, field.getVarintList(), textGenerator);
                printUnknownField(intValue, 5, field.getFixed32List(), textGenerator);
                printUnknownField(intValue, 1, field.getFixed64List(), textGenerator);
                printUnknownField(intValue, 2, field.getLengthDelimitedList(), textGenerator);
                for (UnknownFieldSet unknownFieldSet2 : field.getGroupList()) {
                    textGenerator.print(((Integer) entry.getKey()).toString());
                    if (this.singleLineMode) {
                        textGenerator.print(" { ");
                    } else {
                        textGenerator.print(" {\n");
                        textGenerator.indent();
                    }
                    printUnknownFields(unknownFieldSet2, textGenerator);
                    if (this.singleLineMode) {
                        textGenerator.print("} ");
                    } else {
                        textGenerator.outdent();
                        textGenerator.print("}\n");
                    }
                }
            }
        }
    }

    private static final class TextGenerator {
        private boolean atStartOfLine;
        private final StringBuilder indent;
        private final Appendable output;

        private TextGenerator(Appendable appendable) {
            this.indent = new StringBuilder();
            this.atStartOfLine = true;
            this.output = appendable;
        }

        private void write(CharSequence charSequence, int i) throws IOException {
            if (i != 0) {
                if (this.atStartOfLine) {
                    this.atStartOfLine = false;
                    this.output.append(this.indent);
                }
                this.output.append(charSequence);
            }
        }

        public void indent() {
            this.indent.append("  ");
        }

        public void outdent() {
            int length = this.indent.length();
            if (length == 0) {
                throw new IllegalArgumentException(" Outdent() without matching Indent().");
            }
            this.indent.delete(length - 2, length);
        }

        public void print(CharSequence charSequence) throws IOException {
            int i = 0;
            int length = charSequence.length();
            for (int i2 = 0; i2 < length; i2++) {
                if (charSequence.charAt(i2) == '\n') {
                    write(charSequence.subSequence(i, length), (i2 - i) + 1);
                    i = i2 + 1;
                    this.atStartOfLine = true;
                }
            }
            write(charSequence.subSequence(i, length), length - i);
        }
    }

    private static final class Tokenizer {
        private static final Pattern DOUBLE_INFINITY = Pattern.compile("-?inf(inity)?", 2);
        private static final Pattern FLOAT_INFINITY = Pattern.compile("-?inf(inity)?f?", 2);
        private static final Pattern FLOAT_NAN = Pattern.compile("nanf?", 2);
        private static final Pattern TOKEN = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_+-]*+|[.]?[0-9+-][0-9a-zA-Z_.+-]*+|\"([^\"\n\\\\]|\\\\.)*+(\"|\\\\?$)|'([^'\n\\\\]|\\\\.)*+('|\\\\?$)", 8);
        private static final Pattern WHITESPACE = Pattern.compile("(\\s|(#.*$))++", 8);
        private int column;
        private String currentToken;
        private int line;
        private final Matcher matcher;
        private int pos;
        private int previousColumn;
        private int previousLine;
        private final CharSequence text;

        private Tokenizer(CharSequence charSequence) {
            this.pos = 0;
            this.line = 0;
            this.column = 0;
            this.previousLine = 0;
            this.previousColumn = 0;
            this.text = charSequence;
            this.matcher = WHITESPACE.matcher(charSequence);
            skipWhitespace();
            nextToken();
        }

        private void consumeByteString(List<ByteString> list) throws ParseException {
            char c = '\u0000';
            if (this.currentToken.length() > 0) {
                c = this.currentToken.charAt(0);
            }
            if (c != '\"' && c != '\'') {
                throw parseException("Expected string.");
            } else if (this.currentToken.length() < 2 || this.currentToken.charAt(this.currentToken.length() - 1) != c) {
                throw parseException("String missing ending quote.");
            } else {
                try {
                    ByteString unescapeBytes = TextFormat.unescapeBytes(this.currentToken.substring(1, this.currentToken.length() - 1));
                    nextToken();
                    list.add(unescapeBytes);
                } catch (InvalidEscapeSequenceException e) {
                    throw parseException(e.getMessage());
                }
            }
        }

        private ParseException floatParseException(NumberFormatException numberFormatException) {
            return parseException("Couldn't parse number: " + numberFormatException.getMessage());
        }

        private ParseException integerParseException(NumberFormatException numberFormatException) {
            return parseException("Couldn't parse integer: " + numberFormatException.getMessage());
        }

        private void skipWhitespace() {
            this.matcher.usePattern(WHITESPACE);
            if (this.matcher.lookingAt()) {
                this.matcher.region(this.matcher.end(), this.matcher.regionEnd());
            }
        }

        public boolean atEnd() {
            return this.currentToken.length() == 0;
        }

        public void consume(String str) throws ParseException {
            if (!tryConsume(str)) {
                throw parseException("Expected \"" + str + "\".");
            }
        }

        public boolean consumeBoolean() throws ParseException {
            if (this.currentToken.equals("true") || this.currentToken.equals("t") || this.currentToken.equals("1")) {
                nextToken();
                return true;
            } else if (this.currentToken.equals("false") || this.currentToken.equals("f") || this.currentToken.equals("0")) {
                nextToken();
                return false;
            } else {
                throw parseException("Expected \"true\" or \"false\".");
            }
        }

        public ByteString consumeByteString() throws ParseException {
            List arrayList = new ArrayList();
            consumeByteString(arrayList);
            while (true) {
                if (!this.currentToken.startsWith("'") && !this.currentToken.startsWith("\"")) {
                    return ByteString.copyFrom(arrayList);
                }
                consumeByteString(arrayList);
            }
        }

        public double consumeDouble() throws ParseException {
            if (DOUBLE_INFINITY.matcher(this.currentToken).matches()) {
                boolean startsWith = this.currentToken.startsWith("-");
                nextToken();
                return startsWith ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            } else if (this.currentToken.equalsIgnoreCase("nan")) {
                nextToken();
                return Double.NaN;
            } else {
                try {
                    double parseDouble = Double.parseDouble(this.currentToken);
                    nextToken();
                    return parseDouble;
                } catch (NumberFormatException e) {
                    throw floatParseException(e);
                }
            }
        }

        public float consumeFloat() throws ParseException {
            if (FLOAT_INFINITY.matcher(this.currentToken).matches()) {
                boolean startsWith = this.currentToken.startsWith("-");
                nextToken();
                return startsWith ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
            } else if (FLOAT_NAN.matcher(this.currentToken).matches()) {
                nextToken();
                return Float.NaN;
            } else {
                try {
                    float parseFloat = Float.parseFloat(this.currentToken);
                    nextToken();
                    return parseFloat;
                } catch (NumberFormatException e) {
                    throw floatParseException(e);
                }
            }
        }

        public String consumeIdentifier() throws ParseException {
            for (int i = 0; i < this.currentToken.length(); i++) {
                char charAt = this.currentToken.charAt(i);
                if (('a' > charAt || charAt > 'z') && (('A' > charAt || charAt > 'Z') && !(('0' <= charAt && charAt <= '9') || charAt == '_' || charAt == '.'))) {
                    throw parseException("Expected identifier.");
                }
            }
            String str = this.currentToken;
            nextToken();
            return str;
        }

        public int consumeInt32() throws ParseException {
            try {
                int parseInt32 = TextFormat.parseInt32(this.currentToken);
                nextToken();
                return parseInt32;
            } catch (NumberFormatException e) {
                throw integerParseException(e);
            }
        }

        public long consumeInt64() throws ParseException {
            try {
                long parseInt64 = TextFormat.parseInt64(this.currentToken);
                nextToken();
                return parseInt64;
            } catch (NumberFormatException e) {
                throw integerParseException(e);
            }
        }

        public String consumeString() throws ParseException {
            return consumeByteString().toStringUtf8();
        }

        public int consumeUInt32() throws ParseException {
            try {
                int parseUInt32 = TextFormat.parseUInt32(this.currentToken);
                nextToken();
                return parseUInt32;
            } catch (NumberFormatException e) {
                throw integerParseException(e);
            }
        }

        public long consumeUInt64() throws ParseException {
            try {
                long parseUInt64 = TextFormat.parseUInt64(this.currentToken);
                nextToken();
                return parseUInt64;
            } catch (NumberFormatException e) {
                throw integerParseException(e);
            }
        }

        public boolean lookingAtInteger() {
            if (this.currentToken.length() == 0) {
                return false;
            }
            char charAt = this.currentToken.charAt(0);
            return ('0' <= charAt && charAt <= '9') || charAt == '-' || charAt == '+';
        }

        public void nextToken() {
            this.previousLine = this.line;
            this.previousColumn = this.column;
            while (this.pos < this.matcher.regionStart()) {
                if (this.text.charAt(this.pos) == '\n') {
                    this.line++;
                    this.column = 0;
                } else {
                    this.column++;
                }
                this.pos++;
            }
            if (this.matcher.regionStart() == this.matcher.regionEnd()) {
                this.currentToken = HttpVersions.HTTP_0_9;
                return;
            }
            this.matcher.usePattern(TOKEN);
            if (this.matcher.lookingAt()) {
                this.currentToken = this.matcher.group();
                this.matcher.region(this.matcher.end(), this.matcher.regionEnd());
            } else {
                this.currentToken = String.valueOf(this.text.charAt(this.pos));
                this.matcher.region(this.pos + 1, this.matcher.regionEnd());
            }
            skipWhitespace();
        }

        public ParseException parseException(String str) {
            return new ParseException((this.line + 1) + ":" + (this.column + 1) + ": " + str);
        }

        public ParseException parseExceptionPreviousToken(String str) {
            return new ParseException((this.previousLine + 1) + ":" + (this.previousColumn + 1) + ": " + str);
        }

        public boolean tryConsume(String str) {
            if (!this.currentToken.equals(str)) {
                return false;
            }
            nextToken();
            return true;
        }
    }

    private TextFormat() {
    }

    private static int digitValue(byte b) {
        return ((byte) 48 > b || b > (byte) 57) ? ((byte) 97 > b || b > (byte) 122) ? (b - 65) + 10 : (b - 97) + 10 : b - 48;
    }

    static String escapeBytes(ByteString byteString) {
        StringBuilder stringBuilder = new StringBuilder(byteString.size());
        for (int i = 0; i < byteString.size(); i++) {
            byte byteAt = byteString.byteAt(i);
            switch (byteAt) {
                case (byte) 7:
                    stringBuilder.append("\\a");
                    break;
                case (byte) 8:
                    stringBuilder.append("\\b");
                    break;
                case (byte) 9:
                    stringBuilder.append("\\t");
                    break;
                case (byte) 10:
                    stringBuilder.append("\\n");
                    break;
                case (byte) 11:
                    stringBuilder.append("\\v");
                    break;
                case (byte) 12:
                    stringBuilder.append("\\f");
                    break;
                case (byte) 13:
                    stringBuilder.append("\\r");
                    break;
                case (byte) 34:
                    stringBuilder.append("\\\"");
                    break;
                case (byte) 39:
                    stringBuilder.append("\\'");
                    break;
                case (byte) 92:
                    stringBuilder.append("\\\\");
                    break;
                default:
                    if (byteAt < (byte) 32) {
                        stringBuilder.append('\\');
                        stringBuilder.append((char) (((byteAt >>> 6) & 3) + 48));
                        stringBuilder.append((char) (((byteAt >>> 3) & 7) + 48));
                        stringBuilder.append((char) ((byteAt & 7) + 48));
                        break;
                    }
                    stringBuilder.append((char) byteAt);
                    break;
            }
        }
        return stringBuilder.toString();
    }

    static String escapeText(String str) {
        return escapeBytes(ByteString.copyFromUtf8(str));
    }

    private static boolean isHex(byte b) {
        return ((byte) 48 <= b && b <= (byte) 57) || (((byte) 97 <= b && b <= (byte) 102) || ((byte) 65 <= b && b <= (byte) 70));
    }

    private static boolean isOctal(byte b) {
        return (byte) 48 <= b && b <= (byte) 55;
    }

    public static void merge(CharSequence charSequence, ExtensionRegistry extensionRegistry, Builder builder) throws ParseException {
        Tokenizer tokenizer = new Tokenizer(charSequence);
        while (!tokenizer.atEnd()) {
            mergeField(tokenizer, extensionRegistry, builder);
        }
    }

    public static void merge(CharSequence charSequence, Builder builder) throws ParseException {
        merge(charSequence, ExtensionRegistry.getEmptyRegistry(), builder);
    }

    public static void merge(Readable readable, ExtensionRegistry extensionRegistry, Builder builder) throws IOException {
        merge(toStringBuilder(readable), extensionRegistry, builder);
    }

    public static void merge(Readable readable, Builder builder) throws IOException {
        merge(readable, ExtensionRegistry.getEmptyRegistry(), builder);
    }

    private static void mergeField(Tokenizer tokenizer, ExtensionRegistry extensionRegistry, Builder builder) throws ParseException {
        ExtensionInfo findExtensionByName;
        FieldDescriptor fieldDescriptor;
        String consumeIdentifier;
        Object obj = null;
        Descriptor descriptorForType = builder.getDescriptorForType();
        if (tokenizer.tryConsume("[")) {
            StringBuilder stringBuilder = new StringBuilder(tokenizer.consumeIdentifier());
            while (tokenizer.tryConsume(".")) {
                stringBuilder.append('.');
                stringBuilder.append(tokenizer.consumeIdentifier());
            }
            findExtensionByName = extensionRegistry.findExtensionByName(stringBuilder.toString());
            if (findExtensionByName == null) {
                throw tokenizer.parseExceptionPreviousToken("Extension \"" + stringBuilder + "\" not found in the ExtensionRegistry.");
            } else if (findExtensionByName.descriptor.getContainingType() != descriptorForType) {
                throw tokenizer.parseExceptionPreviousToken("Extension \"" + stringBuilder + "\" does not extend message type \"" + descriptorForType.getFullName() + "\".");
            } else {
                tokenizer.consume("]");
                fieldDescriptor = findExtensionByName.descriptor;
            }
        } else {
            consumeIdentifier = tokenizer.consumeIdentifier();
            FieldDescriptor findFieldByName = descriptorForType.findFieldByName(consumeIdentifier);
            if (findFieldByName == null) {
                findFieldByName = descriptorForType.findFieldByName(consumeIdentifier.toLowerCase(Locale.US));
                if (!(findFieldByName == null || findFieldByName.getType() == Type.GROUP)) {
                    findFieldByName = null;
                }
            }
            if (!(findFieldByName == null || findFieldByName.getType() != Type.GROUP || findFieldByName.getMessageType().getName().equals(consumeIdentifier))) {
                findFieldByName = null;
            }
            if (findFieldByName == null) {
                throw tokenizer.parseExceptionPreviousToken("Message type \"" + descriptorForType.getFullName() + "\" has no field named \"" + consumeIdentifier + "\".");
            }
            fieldDescriptor = findFieldByName;
            findExtensionByName = null;
        }
        if (fieldDescriptor.getJavaType() != JavaType.MESSAGE) {
            tokenizer.consume(":");
            switch (fieldDescriptor.getType()) {
                case INT32:
                case SINT32:
                case SFIXED32:
                    obj = Integer.valueOf(tokenizer.consumeInt32());
                    break;
                case INT64:
                case SINT64:
                case SFIXED64:
                    obj = Long.valueOf(tokenizer.consumeInt64());
                    break;
                case BOOL:
                    obj = Boolean.valueOf(tokenizer.consumeBoolean());
                    break;
                case FLOAT:
                    obj = Float.valueOf(tokenizer.consumeFloat());
                    break;
                case DOUBLE:
                    obj = Double.valueOf(tokenizer.consumeDouble());
                    break;
                case UINT32:
                case FIXED32:
                    obj = Integer.valueOf(tokenizer.consumeUInt32());
                    break;
                case UINT64:
                case FIXED64:
                    obj = Long.valueOf(tokenizer.consumeUInt64());
                    break;
                case STRING:
                    obj = tokenizer.consumeString();
                    break;
                case BYTES:
                    obj = tokenizer.consumeByteString();
                    break;
                case ENUM:
                    EnumDescriptor enumType = fieldDescriptor.getEnumType();
                    if (tokenizer.lookingAtInteger()) {
                        int consumeInt32 = tokenizer.consumeInt32();
                        obj = enumType.findValueByNumber(consumeInt32);
                        if (obj == null) {
                            throw tokenizer.parseExceptionPreviousToken("Enum type \"" + enumType.getFullName() + "\" has no value with number " + consumeInt32 + '.');
                        }
                    }
                    consumeIdentifier = tokenizer.consumeIdentifier();
                    obj = enumType.findValueByName(consumeIdentifier);
                    if (obj == null) {
                        throw tokenizer.parseExceptionPreviousToken("Enum type \"" + enumType.getFullName() + "\" has no value named \"" + consumeIdentifier + "\".");
                    }
                    break;
                case MESSAGE:
                case GROUP:
                    throw new RuntimeException("Can't get here.");
                default:
                    break;
            }
        }
        String str;
        tokenizer.tryConsume(":");
        if (tokenizer.tryConsume("<")) {
            str = ">";
        } else {
            tokenizer.consume("{");
            str = "}";
        }
        Builder newBuilderForField = findExtensionByName == null ? builder.newBuilderForField(fieldDescriptor) : findExtensionByName.defaultInstance.newBuilderForType();
        while (!tokenizer.tryConsume(str)) {
            if (tokenizer.atEnd()) {
                throw tokenizer.parseException("Expected \"" + str + "\".");
            }
            mergeField(tokenizer, extensionRegistry, newBuilderForField);
        }
        obj = newBuilderForField.build();
        if (fieldDescriptor.isRepeated()) {
            builder.addRepeatedField(fieldDescriptor, obj);
        } else {
            builder.setField(fieldDescriptor, obj);
        }
    }

    static int parseInt32(String str) throws NumberFormatException {
        return (int) parseInteger(str, true, false);
    }

    static long parseInt64(String str) throws NumberFormatException {
        return parseInteger(str, true, true);
    }

    private static long parseInteger(String str, boolean z, boolean z2) throws NumberFormatException {
        int i = 1;
        int i2 = 0;
        if (!str.startsWith("-", 0)) {
            i = 0;
        } else if (z) {
            i2 = 1;
        } else {
            throw new NumberFormatException("Number must be positive: " + str);
        }
        int i3 = 10;
        if (str.startsWith("0x", i2)) {
            i2 += 2;
            i3 = 16;
        } else if (str.startsWith("0", i2)) {
            i3 = 8;
        }
        String substring = str.substring(i2);
        if (substring.length() < 16) {
            long parseLong = Long.parseLong(substring, i3);
            long j = i != 0 ? -parseLong : parseLong;
            if (z2) {
                return j;
            }
            if (z) {
                if (j <= 2147483647L && j >= -2147483648L) {
                    return j;
                }
                throw new NumberFormatException("Number out of range for 32-bit signed integer: " + str);
            } else if (j < 4294967296L && j >= 0) {
                return j;
            } else {
                throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + str);
            }
        }
        BigInteger bigInteger = new BigInteger(substring, i3);
        BigInteger negate = i != 0 ? bigInteger.negate() : bigInteger;
        if (z2) {
            if (z) {
                if (negate.bitLength() > 63) {
                    throw new NumberFormatException("Number out of range for 64-bit signed integer: " + str);
                }
            } else if (negate.bitLength() > 64) {
                throw new NumberFormatException("Number out of range for 64-bit unsigned integer: " + str);
            }
        } else if (z) {
            if (negate.bitLength() > 31) {
                throw new NumberFormatException("Number out of range for 32-bit signed integer: " + str);
            }
        } else if (negate.bitLength() > 32) {
            throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + str);
        }
        return negate.longValue();
    }

    static int parseUInt32(String str) throws NumberFormatException {
        return (int) parseInteger(str, false, false);
    }

    static long parseUInt64(String str) throws NumberFormatException {
        return parseInteger(str, false, true);
    }

    public static void print(Message message, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.print(message, new TextGenerator(appendable));
    }

    public static void print(UnknownFieldSet unknownFieldSet, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.printUnknownFields(unknownFieldSet, new TextGenerator(appendable));
    }

    public static void printField(FieldDescriptor fieldDescriptor, Object obj, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.printField(fieldDescriptor, obj, new TextGenerator(appendable));
    }

    public static String printFieldToString(FieldDescriptor fieldDescriptor, Object obj) {
        try {
            Appendable stringBuilder = new StringBuilder();
            printField(fieldDescriptor, obj, stringBuilder);
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static void printFieldValue(FieldDescriptor fieldDescriptor, Object obj, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.printFieldValue(fieldDescriptor, obj, new TextGenerator(appendable));
    }

    public static String printToString(Message message) {
        try {
            Appendable stringBuilder = new StringBuilder();
            print(message, stringBuilder);
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static String printToString(UnknownFieldSet unknownFieldSet) {
        try {
            Appendable stringBuilder = new StringBuilder();
            print(unknownFieldSet, stringBuilder);
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    private static void printUnknownFieldValue(int i, Object obj, TextGenerator textGenerator) throws IOException {
        switch (WireFormat.getTagWireType(i)) {
            case 0:
                textGenerator.print(unsignedToString(((Long) obj).longValue()));
                return;
            case 1:
                textGenerator.print(String.format((Locale) null, "0x%016x", new Object[]{(Long) obj}));
                return;
            case 2:
                textGenerator.print("\"");
                textGenerator.print(escapeBytes((ByteString) obj));
                textGenerator.print("\"");
                return;
            case 3:
                DEFAULT_PRINTER.printUnknownFields((UnknownFieldSet) obj, textGenerator);
                return;
            case 5:
                textGenerator.print(String.format((Locale) null, "0x%08x", new Object[]{(Integer) obj}));
                return;
            default:
                throw new IllegalArgumentException("Bad tag: " + i);
        }
    }

    public static void printUnknownFieldValue(int i, Object obj, Appendable appendable) throws IOException {
        printUnknownFieldValue(i, obj, new TextGenerator(appendable));
    }

    public static String shortDebugString(Message message) {
        try {
            Appendable stringBuilder = new StringBuilder();
            SINGLE_LINE_PRINTER.print(message, new TextGenerator(stringBuilder));
            return stringBuilder.toString().trim();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static String shortDebugString(UnknownFieldSet unknownFieldSet) {
        try {
            Appendable stringBuilder = new StringBuilder();
            SINGLE_LINE_PRINTER.printUnknownFields(unknownFieldSet, new TextGenerator(stringBuilder));
            return stringBuilder.toString().trim();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    private static StringBuilder toStringBuilder(Readable readable) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        CharSequence allocate = CharBuffer.allocate(4096);
        while (true) {
            int read = readable.read(allocate);
            if (read == -1) {
                return stringBuilder;
            }
            allocate.flip();
            stringBuilder.append(allocate, 0, read);
        }
    }

    static ByteString unescapeBytes(CharSequence charSequence) throws InvalidEscapeSequenceException {
        ByteString copyFromUtf8 = ByteString.copyFromUtf8(charSequence.toString());
        byte[] bArr = new byte[copyFromUtf8.size()];
        int i = 0;
        int i2 = 0;
        while (i2 < copyFromUtf8.size()) {
            byte byteAt = copyFromUtf8.byteAt(i2);
            if (byteAt != (byte) 92) {
                bArr[i] = byteAt;
                i++;
            } else if (i2 + 1 < copyFromUtf8.size()) {
                i2++;
                byteAt = copyFromUtf8.byteAt(i2);
                int digitValue;
                if (isOctal(byteAt)) {
                    digitValue = digitValue(byteAt);
                    if (i2 + 1 < copyFromUtf8.size() && isOctal(copyFromUtf8.byteAt(i2 + 1))) {
                        i2++;
                        digitValue = (digitValue * 8) + digitValue(copyFromUtf8.byteAt(i2));
                    }
                    if (i2 + 1 < copyFromUtf8.size() && isOctal(copyFromUtf8.byteAt(i2 + 1))) {
                        i2++;
                        digitValue = (digitValue * 8) + digitValue(copyFromUtf8.byteAt(i2));
                    }
                    bArr[i] = (byte) digitValue;
                    i++;
                } else {
                    switch (byteAt) {
                        case (byte) 34:
                            bArr[i] = (byte) 34;
                            i++;
                            break;
                        case (byte) 39:
                            bArr[i] = (byte) 39;
                            i++;
                            break;
                        case (byte) 92:
                            bArr[i] = (byte) 92;
                            i++;
                            break;
                        case (byte) 97:
                            bArr[i] = (byte) 7;
                            i++;
                            break;
                        case (byte) 98:
                            bArr[i] = (byte) 8;
                            i++;
                            break;
                        case (byte) 102:
                            bArr[i] = Ascii.FF;
                            i++;
                            break;
                        case (byte) 110:
                            bArr[i] = (byte) 10;
                            i++;
                            break;
                        case (byte) 114:
                            bArr[i] = (byte) 13;
                            i++;
                            break;
                        case (byte) 116:
                            bArr[i] = (byte) 9;
                            i++;
                            break;
                        case (byte) 118:
                            bArr[i] = Ascii.VT;
                            i++;
                            break;
                        case (byte) 120:
                            if (i2 + 1 < copyFromUtf8.size() && isHex(copyFromUtf8.byteAt(i2 + 1))) {
                                i2++;
                                digitValue = digitValue(copyFromUtf8.byteAt(i2));
                                if (i2 + 1 < copyFromUtf8.size() && isHex(copyFromUtf8.byteAt(i2 + 1))) {
                                    i2++;
                                    digitValue = (digitValue * 16) + digitValue(copyFromUtf8.byteAt(i2));
                                }
                                bArr[i] = (byte) digitValue;
                                i++;
                                break;
                            }
                            throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\x' with no digits");
                        default:
                            throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\" + ((char) byteAt) + '\'');
                    }
                }
            } else {
                throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\' at end of string.");
            }
            i2++;
        }
        return ByteString.copyFrom(bArr, 0, i);
    }

    static String unescapeText(String str) throws InvalidEscapeSequenceException {
        return unescapeBytes(str).toStringUtf8();
    }

    private static String unsignedToString(int i) {
        return i >= 0 ? Integer.toString(i) : Long.toString(((long) i) & 4294967295L);
    }

    private static String unsignedToString(long j) {
        return j >= 0 ? Long.toString(j) : BigInteger.valueOf(Long.MAX_VALUE & j).setBit(63).toString();
    }
}
