package com.zyf.service.impl;

import com.zyf.service.Map;

public class HashMap<K, V> implements Map<K, V> {

    private Entry<K, V> table[] = null;
    int size = 0;

    public HashMap() {
        this.table = new Entry[16];
    }

    /**
     * 通过key hash算法 计算hash值
     * hash值进行取模找到数值下标，找到这个下表的对象
     * 判断当前对象是否为空，如果为空得情况下直接存储
     * 如果不为空得话，就发生了hash冲突了  这个时候使用next链表
     *
     * @param k
     * @param v
     * @return
     */
    @Override
    public V put(K k, V v) {
        int index = hash(k);
        Entry<K, V> entry = table[index];
        if (null == entry) {
            table[index] = new Entry<>(k, v, index, null);
            size++;
        } else {
            table[index] = new Entry<>(k, v, index, entry);
        }
        return table[index].getValue();
    }

    private int hash(K k) {
        int index = k.hashCode() % 16;  //实际上是  取模  进行移位
        return index >= 0 ? index : -index;
    }

    /**
     * 通过当前得k,进行hash算法
     * 获取index下标数组对象
     * 判断当前对象是否为空，如果不为空
     * 判断是否相当，如果不相等返回null
     * 判断next是否为空，如果不为空
     * 再判断是否相当，直到相等或者为空为止
     *
     * @param k
     * @return
     */
    @Override
    public V get(K k) {
        if (size == 0) {
            return null;
        }
        int index = hash(k);
        //Entry<K, V> entry = table[index];
        Entry<K, V> entry = findValue(table[index], k);
        return entry==null?null:entry.getValue();
    }

    private Entry<K, V> findValue(Entry<K, V> entry, K k) {
        if (k.equals(entry.getKey()) || k == entry.getKey()) {
            return entry;
        } else {
            if (entry.next != null) {
                return findValue(entry.next, k);
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size++;
    }


    class Entry<K, V> implements Map.Entry<K, V> {
        K k;
        V v;
        int hash;

        public Entry(K k, V v, int hash, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.hash = hash;
            this.next = next;
        }

        Entry<K, V> next;

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }
    }
}
