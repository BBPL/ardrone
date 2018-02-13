package com.google.protobuf;

import com.google.protobuf.GeneratedMessage.Builder;

public class SingleFieldBuilder<MType extends GeneratedMessage, BType extends Builder, IType extends MessageOrBuilder> implements BuilderParent {
    private BType builder;
    private boolean isClean;
    private MType message;
    private BuilderParent parent;

    public SingleFieldBuilder(MType mType, BuilderParent builderParent, boolean z) {
        if (mType == null) {
            throw new NullPointerException();
        }
        this.message = mType;
        this.parent = builderParent;
        this.isClean = z;
    }

    private void onChanged() {
        if (this.builder != null) {
            this.message = null;
        }
        if (this.isClean && this.parent != null) {
            this.parent.markDirty();
            this.isClean = false;
        }
    }

    public MType build() {
        this.isClean = true;
        return getMessage();
    }

    public SingleFieldBuilder<MType, BType, IType> clear() {
        this.message = (GeneratedMessage) (this.message != null ? this.message.getDefaultInstanceForType() : this.builder.getDefaultInstanceForType());
        if (this.builder != null) {
            this.builder.dispose();
            this.builder = null;
        }
        onChanged();
        return this;
    }

    public void dispose() {
        this.parent = null;
    }

    public BType getBuilder() {
        if (this.builder == null) {
            this.builder = (Builder) this.message.newBuilderForType(this);
            this.builder.mergeFrom(this.message);
            this.builder.markClean();
        }
        return this.builder;
    }

    public MType getMessage() {
        if (this.message == null) {
            this.message = (GeneratedMessage) this.builder.buildPartial();
        }
        return this.message;
    }

    public IType getMessageOrBuilder() {
        return this.builder != null ? this.builder : this.message;
    }

    public void markDirty() {
        onChanged();
    }

    public SingleFieldBuilder<MType, BType, IType> mergeFrom(MType mType) {
        if (this.builder == null && this.message == this.message.getDefaultInstanceForType()) {
            this.message = mType;
        } else {
            getBuilder().mergeFrom((Message) mType);
        }
        onChanged();
        return this;
    }

    public SingleFieldBuilder<MType, BType, IType> setMessage(MType mType) {
        if (mType == null) {
            throw new NullPointerException();
        }
        this.message = mType;
        if (this.builder != null) {
            this.builder.dispose();
            this.builder = null;
        }
        onChanged();
        return this;
    }
}
