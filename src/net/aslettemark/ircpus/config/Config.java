package net.aslettemark.ircpus.config;

import java.io.File;
import java.util.HashMap;

public interface Config {

    /**
     * Set a new value for a key
     * @param key key
     * @param value new value
     */
    public abstract void set(String key, Object value);

    /**
     * Get the value of a key
     * @param key key
     * @return Value matching key
     */
    public abstract Object fetch(String key);

    /**
     * Wipes all values in the config
     */
    public abstract void wipe();

    /**
     * Wipes the value of a key
     * @param key key
     * @param value value to wipe
     */
    public abstract void wipe(String key, Object value);

    /**
     * Get the values used in the config for saving
     * @return a hashmap containing all the necessary keys and values
     */
    public abstract HashMap<String, Object> getSaveValues();

    /**
     * Loads config from file. Should be called in constructor.
     */
    public abstract void load();

    public abstract void setSaveLocation(File file);

    public abstract File getSaveLocation();
}