/* ###
 * IP: GHIDRA
 * REVIEWED: YES
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.util.prop;

import ghidra.util.exception.*;

import java.io.*;

/**
 *  Handles  general storage and retrieval of long values indexed by long keys.
 * 
 */
public class LongPropertySet extends PropertySet {
    private final static long serialVersionUID = 1;
	/**
	 * Constructor for longPropertySet.
	 * @param name the name associated with this property set
	 */
	public LongPropertySet(String name) {
		super(name, null);
	}

	/**
	 * @see PropertySet#getDataSize()
	 */
	@Override
    public int getDataSize() {
		return 8;
	}

	/**
	 * Stores an long value at the given index.  Any value currently at that
	 * index will be replaced by the new value.
	 * @param index the index at which to store the long value.
	 * @param value the long value to store.
	 */
	public void putLong(long index, long value) {
		PropertyPage page = getOrCreatePage(getPageID(index));
		int n = page.getSize();
		page.addLong(getPageOffset(index), value);
		numProperties += page.getSize() - n;
	}

	/**
	 * Retrieves the long value stored at the given index.
	 * @param index the index at which to retrieve the long value.
	 * @return long the value stored at the given index.
	 * @throws NoValueException if there is no long value stored at the index.
	 */
	public long getLong(long index) throws NoValueException {
		PropertyPage page = getPage(getPageID(index));
		if (page != null) {
			return page.getLong(getPageOffset(index));
		}
		throw noValueException;
	}

	/* (non-Javadoc)
	 * @see ghidra.util.prop.PropertySet#moveIndex(long, long)
	 */
	@Override
    protected void moveIndex(long from, long to) {
		try {
			long value = getLong(from);
			remove(from);
			putLong(to, value);
		}catch(NoValueException e) {}
	}

	/**
	 * saves the property at the given index to the given output stream.
	 */
	@Override
    protected void saveProperty(ObjectOutputStream oos, long index) throws IOException {
		try {
			oos.writeLong(getLong(index));
		}
        catch(NoValueException e) {
            throw new AssertException(e.getMessage());
        }
	}
	/**
	 * restores the property from the input stream to the given index.
	 */
	@Override
    protected void restoreProperty(ObjectInputStream ois, long index) throws IOException{
		putLong(index, ois.readLong());
	}

	/* (non-Javadoc)
	 * @see ghidra.util.prop.PropertySet#applyValue(ghidra.util.prop.PropertyVisitor, long)
	 */
	@Override
    public void applyValue(PropertyVisitor visitor, long addr) {
		throw new NotYetImplementedException();
//		try {
//			visitor.visit(getLong(addr));
//		}
//		catch(NoValueException e) {
//		}
	}

}
