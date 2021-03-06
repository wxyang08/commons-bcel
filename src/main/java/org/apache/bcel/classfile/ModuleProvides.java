/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.bcel.classfile;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.Const;

/**
 * This class represents an entry in the provides table of the Module attribute.
 * Each entry describes a service implementation that the parent module provides.
 *
 * @see   Module
 * @since 6.4.0
 */
public final class ModuleProvides implements Cloneable, Node {

    private final int provides_index;  // points to CONSTANT_Class_info
    private final int provides_with_count;
    private final int[] provides_with_index;  // points to CONSTANT_Class_info


    /**
     * Construct object from file stream.
     *
     * @param file Input stream
     * @throws IOException if an I/O Exception occurs in readUnsignedShort
     */
    ModuleProvides(final DataInput file) throws IOException {
        provides_index = file.readUnsignedShort();
        provides_with_count = file.readUnsignedShort();
        provides_with_index = new int[provides_with_count];
        for (int i = 0; i < provides_with_count; i++) {
            provides_with_index[i] = file.readUnsignedShort();
        }
    }


    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( final Visitor v ) {
        v.visitModuleProvides(this);
    }

    // TODO add more getters and setters?

    /**
     * Dump table entry to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException if an I/O Exception occurs in writeShort
     */
    public void dump( final DataOutputStream file ) throws IOException {
        file.writeShort(provides_index);
        file.writeShort(provides_with_count);
        for (final int entry : provides_with_index) {
            file.writeShort(entry);
        }
    }


    /**
     * @return String representation
     */
    @Override
    public String toString() {
        return "provides(" + provides_index + ", " + provides_with_count + ", ...)";
    }


    /**
     * @return Resolved string representation
     */
    public String toString( final ConstantPool constant_pool ) {
        final StringBuilder buf = new StringBuilder();
        final String interface_name = constant_pool.constantToString(provides_index, Const.CONSTANT_Class);
        buf.append(Utility.compactClassName(interface_name, false));
        buf.append(", with(").append(provides_with_count).append("):\n");
        for (final int index : provides_with_index) {
            final String class_name = constant_pool.getConstantString(index, Const.CONSTANT_Class);
            buf.append("      ").append(Utility.compactClassName(class_name, false)).append("\n");
        }
        return buf.substring(0, buf.length()-1); // remove the last newline
    }


    /**
     * @return deep copy of this object
     */
    public ModuleProvides copy() {
        try {
            return (ModuleProvides) clone();
        } catch (final CloneNotSupportedException e) {
            // TODO should this throw?
        }
        return null;
    }
}
