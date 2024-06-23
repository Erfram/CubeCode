package com.cubecode.api.scripts.code.nbt;

import net.minecraft.nbt.*;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptNbtCompound implements Nbt{
    private NbtCompound nbt;

    public ScriptNbtCompound(NbtCompound nbt) {
        this.nbt = nbt == null ? new NbtCompound() : nbt;
    }

    public NbtCompound getMinecraftNbtCompound() {
        return this.nbt;
    }

    @Override
    public boolean isCompound() {
        return true;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public String stringify() {
        return this.nbt.toString();
    }

    @Override
    public boolean isEmpty() {
        return this.nbt.isEmpty();
    }

    @Override
    public int size() {
        return this.nbt.getSize();
    }

    @Override
    public Nbt copy() {
        return new ScriptNbtCompound(this.nbt.copy());
    }

    @Override
    public void combine(Nbt nbt) {
        if (nbt instanceof ScriptNbtCompound) {
            this.nbt.copyFrom(((ScriptNbtCompound) nbt).nbt);
        }
    }

    @Override
    public boolean isSame(Nbt nbt) {
        if (nbt instanceof ScriptNbtCompound) {
            return this.nbt.equals(((ScriptNbtCompound) nbt).nbt);
        }

        return false;
    }

    public boolean has(String key) {
        return this.nbt.getKeys().contains(key);
    }

    public void remove(String key) {
        this.nbt.remove(key);
    }

    public Set<String> keys() {
        return this.nbt.getKeys();
    }

    public byte getByte(String key) {
        return this.nbt.getByte(key);
    }

    public void putByte(String key, byte value) {
        this.nbt.putByte(key, value);
    }
    
    public short getShort(String key) {
        return this.nbt.getShort(key);
    }
    
    public void putShort(String key, short value) {
        this.nbt.putShort(key, value);
    }
    
    public int getInt(String key) {
        return this.nbt.getInt(key);
    }
    
    public void putInt(String key, int value) {
        this.nbt.putInt(key, value);
    }
    
    public long getLong(String key) {
        return this.nbt.getLong(key);
    }
    
    public void putLong(String key, long value) {
        this.nbt.putLong(key, value);
    }
    
    public float getFloat(String key) {
        return this.nbt.getFloat(key);
    }
    
    public void putFloat(String key, float value) {
        this.nbt.putFloat(key, value);
    }
    
    public double getDouble(String key) {
        return this.nbt.getDouble(key);
    }
    
    public void putDouble(String key, double value) {
        this.nbt.putDouble(key, value);
    }
    
    public String getString(String key) {
        return this.nbt.getString(key);
    }
    
    public void putString(String key, String value) {
        this.nbt.putString(key, value);
    }
    
    public boolean getBoolean(String key) {
        return this.nbt.getBoolean(key);
    }
    
    public void putBoolean(String key, boolean value) {
        this.nbt.putBoolean(key, value);
    }

    public ScriptNbtCompound getCompound(String key) {
        return new ScriptNbtCompound(this.nbt.getCompound(key));
    }
    
    public void setCompound(String key, ScriptNbtCompound value) {
        this.nbt.put(key, value.nbt);
    }

    
    public ScriptNbtList getList(String key) {
        NbtElement element = this.nbt.getCompound(key);

        return new ScriptNbtList(element instanceof NbtList ? (NbtList) element : new NbtList());
    }
    
    public void setList(String key, ScriptNbtList value) {
        this.nbt.put(key, value.getMinecraftNbtList());
    }
    
    public Object get(String key)
    {
        NbtElement element = this.nbt.get(key);

        if (element instanceof NbtCompound) {
            return new ScriptNbtCompound((NbtCompound) element);
        }
        else if (element instanceof NbtList) {
            return new ScriptNbtList((NbtList) element);
        }
        else if (element instanceof NbtString) {
            return this.getString(key);
        }
        else if (element instanceof NbtInt) {
            return this.getInt(key);
        }
        else if (element instanceof NbtDouble) {
            return this.getDouble(key);
        }
        else if (element instanceof NbtFloat) {
            return this.getFloat(key);
        }
        else if (element instanceof NbtLong) {
            return this.getLong(key);
        }
        else if (element instanceof NbtShort) {
            return this.getShort(key);
        }
        else if (element instanceof NbtByte) {
            return this.getByte(key);
        }

        return null;
    }

    public boolean equals(ScriptNbtCompound compound) {
        return compound != null && this.nbt.equals(compound.getMinecraftNbtCompound());
    }
    
    public void addCompound(String key) {
        this.nbt.put(key, new NbtCompound());
    }
    
    public String dumpJSON() {
        String result = stringify().replaceAll("([a-zA-Z0-9_]+):", "\"$1\":");

        Pattern pattern = Pattern.compile("([0-9]+[bLsdf])|0b|1b");
        Matcher matcher = pattern.matcher(result);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            if (matcher.group(0).equals("0b")) {
                matcher.appendReplacement(sb, "false");
            }
            else if (matcher.group(0).equals("1b")) {
                matcher.appendReplacement(sb, "true");
            }
            else {
                matcher.appendReplacement(sb, matcher.group(1).substring(0, matcher.group(1).length() - 1));
            }
        }

        matcher.appendTail(sb);

        return sb.toString();
    }
}
