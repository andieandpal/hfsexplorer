/*-
 * Copyright (C) 2006-2012 Erik Larsson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.hfs.types.hfsplus;

import java.lang.reflect.Field;
import org.catacombae.csjc.AbstractStruct;
import org.catacombae.csjc.PrintableStruct;
import org.catacombae.csjc.StructElements;
import org.catacombae.csjc.structelements.Dictionary;
import org.catacombae.csjc.structelements.IntegerFieldRepresentation;
import org.catacombae.util.Util;

public abstract class HFSPlusAttributesLeafRecordData
        implements AbstractStruct, PrintableStruct, StructElements
{

    public static final int kHFSPlusAttrInlineData = 0x10;
    public static final int kHFSPlusAttrForkData = 0x20;
    public static final int kHFSPlusAttrExtents = 0x30;

    private int recordType;

    public HFSPlusAttributesLeafRecordData(byte[] data, int offset) {
        this.recordType = Util.readIntBE(data, offset+0);
    }

    /**  */
    public final long getRecordType() { return Util.unsign(getRawRecordType()); }

    /** <b>Note that the return value from this function should be interpreted as an unsigned integer, for instance using Util.unsign(...).</b> */
    public final int getRawRecordType() { return recordType; }

    public String getRecordTypeAsString() {
        if(recordType == kHFSPlusAttrInlineData) return "kHFSPlusAttrInlineData";
        else if(recordType == kHFSPlusAttrForkData) return "kHFSPlusAttrForkData";
        else if(recordType == kHFSPlusAttrExtents) return "kHFSPlusAttrExtents";
        else return "UNKNOWN!";
    }

    public abstract int size();

    protected int getBytes(byte[] result, int offset) {
        Util.arrayPutBE(result, offset, recordType); offset += 4;

        return 4;
    }

    /* @Override */
    public Dictionary getStructElements() {
        final Class thisClass = HFSPlusAttributesLeafRecordData.class;
        DictionaryBuilder db = new DictionaryBuilder(thisClass.getSimpleName(),
                "HFS+ attributes leaf record data (abstract superclass)");

        try {
            Field recordTypeField = thisClass.getDeclaredField("recordType");

            recordTypeField.setAccessible(true);

            db.addUIntBE("recordType", recordTypeField, this, "Record type",
                    IntegerFieldRepresentation.HEXADECIMAL);

            return db.getResult();
        }
        catch(NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
