package com.google.protobuf;

import com.google.protobuf.GeneratedMessage.Builder;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RepeatedFieldBuilder<MType extends GeneratedMessage, BType extends Builder, IType extends MessageOrBuilder> implements BuilderParent {
    private List<SingleFieldBuilder<MType, BType, IType>> builders;
    private BuilderExternalList<MType, BType, IType> externalBuilderList;
    private MessageExternalList<MType, BType, IType> externalMessageList;
    private MessageOrBuilderExternalList<MType, BType, IType> externalMessageOrBuilderList;
    private boolean isClean;
    private boolean isMessagesListMutable;
    private List<MType> messages;
    private BuilderParent parent;

    private static class BuilderExternalList<MType extends GeneratedMessage, BType extends Builder, IType extends MessageOrBuilder> extends AbstractList<BType> implements List<BType> {
        RepeatedFieldBuilder<MType, BType, IType> builder;

        BuilderExternalList(RepeatedFieldBuilder<MType, BType, IType> repeatedFieldBuilder) {
            this.builder = repeatedFieldBuilder;
        }

        public BType get(int i) {
            return this.builder.getBuilder(i);
        }

        void incrementModCount() {
            this.modCount++;
        }

        public int size() {
            return this.builder.getCount();
        }
    }

    private static class MessageExternalList<MType extends GeneratedMessage, BType extends Builder, IType extends MessageOrBuilder> extends AbstractList<MType> implements List<MType> {
        RepeatedFieldBuilder<MType, BType, IType> builder;

        MessageExternalList(RepeatedFieldBuilder<MType, BType, IType> repeatedFieldBuilder) {
            this.builder = repeatedFieldBuilder;
        }

        public MType get(int i) {
            return this.builder.getMessage(i);
        }

        void incrementModCount() {
            this.modCount++;
        }

        public int size() {
            return this.builder.getCount();
        }
    }

    private static class MessageOrBuilderExternalList<MType extends GeneratedMessage, BType extends Builder, IType extends MessageOrBuilder> extends AbstractList<IType> implements List<IType> {
        RepeatedFieldBuilder<MType, BType, IType> builder;

        MessageOrBuilderExternalList(RepeatedFieldBuilder<MType, BType, IType> repeatedFieldBuilder) {
            this.builder = repeatedFieldBuilder;
        }

        public IType get(int i) {
            return this.builder.getMessageOrBuilder(i);
        }

        void incrementModCount() {
            this.modCount++;
        }

        public int size() {
            return this.builder.getCount();
        }
    }

    public RepeatedFieldBuilder(List<MType> list, boolean z, BuilderParent builderParent, boolean z2) {
        this.messages = list;
        this.isMessagesListMutable = z;
        this.parent = builderParent;
        this.isClean = z2;
    }

    private void ensureBuilders() {
        if (this.builders == null) {
            this.builders = new ArrayList(this.messages.size());
            for (int i = 0; i < this.messages.size(); i++) {
                this.builders.add(null);
            }
        }
    }

    private void ensureMutableMessageList() {
        if (!this.isMessagesListMutable) {
            this.messages = new ArrayList(this.messages);
            this.isMessagesListMutable = true;
        }
    }

    private MType getMessage(int i, boolean z) {
        if (this.builders == null) {
            return (GeneratedMessage) this.messages.get(i);
        }
        SingleFieldBuilder singleFieldBuilder = (SingleFieldBuilder) this.builders.get(i);
        return singleFieldBuilder == null ? (GeneratedMessage) this.messages.get(i) : z ? singleFieldBuilder.build() : singleFieldBuilder.getMessage();
    }

    private void incrementModCounts() {
        if (this.externalMessageList != null) {
            this.externalMessageList.incrementModCount();
        }
        if (this.externalBuilderList != null) {
            this.externalBuilderList.incrementModCount();
        }
        if (this.externalMessageOrBuilderList != null) {
            this.externalMessageOrBuilderList.incrementModCount();
        }
    }

    private void onChanged() {
        if (this.isClean && this.parent != null) {
            this.parent.markDirty();
            this.isClean = false;
        }
    }

    public RepeatedFieldBuilder<MType, BType, IType> addAllMessages(Iterable<? extends MType> iterable) {
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            if (((GeneratedMessage) it.next()) == null) {
                throw new NullPointerException();
            }
        }
        if (!(iterable instanceof Collection)) {
            ensureMutableMessageList();
            it = iterable.iterator();
            while (it.hasNext()) {
                addMessage((GeneratedMessage) it.next());
            }
            onChanged();
            incrementModCounts();
        } else if (((Collection) iterable).size() != 0) {
            ensureMutableMessageList();
            it = iterable.iterator();
            while (it.hasNext()) {
                addMessage((GeneratedMessage) it.next());
            }
            onChanged();
            incrementModCounts();
        }
        return this;
    }

    public BType addBuilder(int i, MType mType) {
        ensureMutableMessageList();
        ensureBuilders();
        SingleFieldBuilder singleFieldBuilder = new SingleFieldBuilder(mType, this, this.isClean);
        this.messages.add(i, null);
        this.builders.add(i, singleFieldBuilder);
        onChanged();
        incrementModCounts();
        return singleFieldBuilder.getBuilder();
    }

    public BType addBuilder(MType mType) {
        ensureMutableMessageList();
        ensureBuilders();
        SingleFieldBuilder singleFieldBuilder = new SingleFieldBuilder(mType, this, this.isClean);
        this.messages.add(null);
        this.builders.add(singleFieldBuilder);
        onChanged();
        incrementModCounts();
        return singleFieldBuilder.getBuilder();
    }

    public RepeatedFieldBuilder<MType, BType, IType> addMessage(int i, MType mType) {
        if (mType == null) {
            throw new NullPointerException();
        }
        ensureMutableMessageList();
        this.messages.add(i, mType);
        if (this.builders != null) {
            this.builders.add(i, null);
        }
        onChanged();
        incrementModCounts();
        return this;
    }

    public RepeatedFieldBuilder<MType, BType, IType> addMessage(MType mType) {
        if (mType == null) {
            throw new NullPointerException();
        }
        ensureMutableMessageList();
        this.messages.add(mType);
        if (this.builders != null) {
            this.builders.add(null);
        }
        onChanged();
        incrementModCounts();
        return this;
    }

    public List<MType> build() {
        this.isClean = true;
        if (!this.isMessagesListMutable && this.builders == null) {
            return this.messages;
        }
        if (!this.isMessagesListMutable) {
            boolean z;
            for (int i = 0; i < this.messages.size(); i++) {
                Object obj = (Message) this.messages.get(i);
                SingleFieldBuilder singleFieldBuilder = (SingleFieldBuilder) this.builders.get(i);
                if (singleFieldBuilder != null && singleFieldBuilder.build() != obj) {
                    z = false;
                    break;
                }
            }
            z = true;
            if (z) {
                return this.messages;
            }
        }
        ensureMutableMessageList();
        for (int i2 = 0; i2 < this.messages.size(); i2++) {
            this.messages.set(i2, getMessage(i2, true));
        }
        this.messages = Collections.unmodifiableList(this.messages);
        this.isMessagesListMutable = false;
        return this.messages;
    }

    public void clear() {
        this.messages = Collections.emptyList();
        this.isMessagesListMutable = false;
        if (this.builders != null) {
            for (SingleFieldBuilder singleFieldBuilder : this.builders) {
                if (singleFieldBuilder != null) {
                    singleFieldBuilder.dispose();
                }
            }
            this.builders = null;
        }
        onChanged();
        incrementModCounts();
    }

    public void dispose() {
        this.parent = null;
    }

    public BType getBuilder(int i) {
        ensureBuilders();
        SingleFieldBuilder singleFieldBuilder = (SingleFieldBuilder) this.builders.get(i);
        if (singleFieldBuilder == null) {
            SingleFieldBuilder singleFieldBuilder2 = new SingleFieldBuilder((GeneratedMessage) this.messages.get(i), this, this.isClean);
            this.builders.set(i, singleFieldBuilder2);
            singleFieldBuilder = singleFieldBuilder2;
        }
        return singleFieldBuilder.getBuilder();
    }

    public List<BType> getBuilderList() {
        if (this.externalBuilderList == null) {
            this.externalBuilderList = new BuilderExternalList(this);
        }
        return this.externalBuilderList;
    }

    public int getCount() {
        return this.messages.size();
    }

    public MType getMessage(int i) {
        return getMessage(i, false);
    }

    public List<MType> getMessageList() {
        if (this.externalMessageList == null) {
            this.externalMessageList = new MessageExternalList(this);
        }
        return this.externalMessageList;
    }

    public IType getMessageOrBuilder(int i) {
        if (this.builders == null) {
            return (MessageOrBuilder) this.messages.get(i);
        }
        SingleFieldBuilder singleFieldBuilder = (SingleFieldBuilder) this.builders.get(i);
        return singleFieldBuilder == null ? (MessageOrBuilder) this.messages.get(i) : singleFieldBuilder.getMessageOrBuilder();
    }

    public List<IType> getMessageOrBuilderList() {
        if (this.externalMessageOrBuilderList == null) {
            this.externalMessageOrBuilderList = new MessageOrBuilderExternalList(this);
        }
        return this.externalMessageOrBuilderList;
    }

    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

    public void markDirty() {
        onChanged();
    }

    public void remove(int i) {
        ensureMutableMessageList();
        this.messages.remove(i);
        if (this.builders != null) {
            SingleFieldBuilder singleFieldBuilder = (SingleFieldBuilder) this.builders.remove(i);
            if (singleFieldBuilder != null) {
                singleFieldBuilder.dispose();
            }
        }
        onChanged();
        incrementModCounts();
    }

    public RepeatedFieldBuilder<MType, BType, IType> setMessage(int i, MType mType) {
        if (mType == null) {
            throw new NullPointerException();
        }
        ensureMutableMessageList();
        this.messages.set(i, mType);
        if (this.builders != null) {
            SingleFieldBuilder singleFieldBuilder = (SingleFieldBuilder) this.builders.set(i, null);
            if (singleFieldBuilder != null) {
                singleFieldBuilder.dispose();
            }
        }
        onChanged();
        incrementModCounts();
        return this;
    }
}
