package me.moree;

import java.io.IOException;

/**
 * Created by MORE-E on 3/15/2017.
 */
public interface Resource<K, V> {

    V get(K key);

    V set(K key, V value);

    V add(K key, V value);

    V remove(K key);

    boolean save() throws IOException;
}
