/*
 *  Copyright (C) 2015 Aksel H. Slettemark http://aslettemark.net/
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

    /**
     * Saves the config file
     * @return Wether the write was successful
     */
    public abstract boolean save();

    public abstract void setSaveLocation(File file);

    public abstract File getSaveLocation();
}