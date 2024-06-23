package com.cubecode.api.scripts.code.nbt;

import net.minecraft.nbt.*;

public class ScriptNbtList implements Nbt{
    private NbtList list;

    public ScriptNbtList(NbtList list) {
        this.list = list == null ? new NbtList() : list;
    }

    public NbtList getMinecraftNbtList() {
        return this.list;
    }

    @Override
    public boolean isCompound() {
        return false;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public String stringify() {
        return this.list.toString();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public Nbt copy() {
        return new ScriptNbtList(this.list.copy());
    }

    @Override
    public void combine(Nbt nbt) {
        if (nbt instanceof ScriptNbtList) {
            NbtList list = ((ScriptNbtList) nbt).getMinecraftNbtList();

            if (this.list.getNbtType() == list.getNbtType()) {
                for (NbtElement nbtElement : list) {
                    this.list.add(nbtElement.copy());
                }
            }
        }
    }

    @Override
    public boolean isSame(Nbt nbt) {
        if (nbt instanceof ScriptNbtList) {
            return this.list.equals(((ScriptNbtList) nbt).getMinecraftNbtList());
        }

        return false;
    }

    public boolean has(int index) {
        return index >= 0 && index < this.size();
    }

    public void remove(int index) {
        this.list.remove(index);
    }

    public byte getByte(int index) {
        NbtElement element = this.list.get(index);

        return element.getType() == NbtElement.BYTE_TYPE ? ((NbtByte)element.getNbtType()).byteValue() : (byte) 0;
    }

    public void setByte(int index, byte value) {
        this.list.set(index, NbtByte.of(value));
    }

    public void addByte(byte value) {
        this.list.add(NbtByte.of(value));
    }

    public short getShort(int index) {
        return this.list.getShort(index);
    }

    public void setShort(int index, short value) {
        this.list.set(index, NbtShort.of(value));
    }

    public void addShort(short value) {
        this.list.add(NbtShort.of(value));
    }

    public int getInt(int index) {
        return this.list.getInt(index);
    }

    public void setInt(int index, int value) {
        this.list.set(index, NbtInt.of(value));
    }

    public void addInt(int value) {
        this.list.add(NbtInt.of(value));
    }

    public float getFloat(int index) {
        return this.list.getFloat(index);
    }

    public void setFloat(int index, float value) {
        this.list.set(index, NbtFloat.of(value));
    }

    public void addFloat(float value) {
        this.list.add(NbtFloat.of(value));
    }

    public double getDouble(int index) {
        return this.list.getDouble(index);
    }

    public void setDouble(int index, double value) {
        this.list.set(index, NbtDouble.of(value));
    }

    public void addDouble(double value) {
        this.list.add(NbtDouble.of(value));
    }

    public String getString(int index) {
        return this.list.getString(index);
    }

    public void setString(int index, String value) {
        this.list.set(index, NbtString.of(value));
    }

    public void addString(String value) {
        this.list.add(NbtString.of(value));
    }

    public boolean getBoolean(int index) {
        NbtElement element = this.list.get(index);

        return element.getType() == NbtElement.BYTE_TYPE && ((NbtByte)element.getNbtType()).byteValue() != 0;
    }

    public void setBoolean(int index, boolean value) {
        this.list.set(index, NbtByte.of(value ? (byte) 1 : (byte) 0));
    }

    public void addBoolean(boolean value) {
        this.list.add(NbtByte.of(value ? (byte) 1 : (byte) 0));
    }

    public ScriptNbtCompound getCompound(int index) {
        return new ScriptNbtCompound(this.list.getCompound(index));
    }

    public void setCompound(int index, ScriptNbtCompound value) {
        this.list.set(index, value.getMinecraftNbtCompound());
    }

    public void addCompound(ScriptNbtCompound value) {
        this.list.add(value.getMinecraftNbtCompound());
    }

    public ScriptNbtList getList(int index) {
        return new ScriptNbtList(this.list.getList(index));
    }

    public void setList(int index, ScriptNbtList value) {
        this.list.set(index, value.getMinecraftNbtList());
    }

    public void addList(ScriptNbtList value) {
        this.list.add(value.getMinecraftNbtList());
    }

    public Object[] toArray() {
        Object[] array = new Object[this.list.size()];

        for (int i = 0; i < this.list.size(); i++) {
            array[i] = this.list.get(i);
        }

        return array;
    }
}
