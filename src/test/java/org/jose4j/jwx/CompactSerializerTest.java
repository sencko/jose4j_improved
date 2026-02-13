/*
 * Copyright 2012-2017 Brian Campbell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jose4j.jwx;

import junit.framework.TestCase;
import org.jose4j.lang.JoseException;

/**
 */
public class CompactSerializerTest extends TestCase
{
    public void testDeserialize1() throws JoseException
    {
        String cs = "one.two.three";
        String[] parts = CompactSerializer.deserialize(cs);
        int i = 0;
        assertEquals("one", parts[i++]);
        assertEquals("two", parts[i++]);
        assertEquals("three", parts[i++]);
        assertEquals(i, parts.length);
    }

    public void testDeserialize2()  throws JoseException
    {
        String cs = "one.two.three.four";
        String[] parts = CompactSerializer.deserialize(cs);
        int i = 0;
        assertEquals("one", parts[i++]);
        assertEquals("two", parts[i++]);
        assertEquals("three", parts[i++]);
        assertEquals("four", parts[i++]);
        assertEquals(i, parts.length);
    }

    public void testDeserialize3() throws JoseException
    {
        String cs = "one.two.";
        String[] parts = CompactSerializer.deserialize(cs);
        int i = 0;
        assertEquals("one", parts[i++]);
        assertEquals("two", parts[i++]);
        assertEquals("", parts[i++]);
        assertEquals(i, parts.length);
    }

    public void testDeserialize4() throws JoseException
    {
        String cs = "one.two.three.";
        String[] parts = CompactSerializer.deserialize(cs);
        int i = 0;
        assertEquals("one", parts[i++]);
        assertEquals("two", parts[i++]);
        assertEquals("three", parts[i++]);
        assertEquals("", parts[i++]);
        assertEquals(i, parts.length);
    }

    public void testDeserialize5() throws JoseException
    {
        String cs = "one..three.four.five";
        String[] parts = CompactSerializer.deserialize(cs);
        int i = 0;
        assertEquals("one", parts[i++]);
        assertEquals("", parts[i++]);
        assertEquals("three", parts[i++]);
        assertEquals("four", parts[i++]);
        assertEquals("five", parts[i++]);
        assertEquals(i, parts.length);
    }

    public void testSerialize1() throws JoseException
    {
        String cs = CompactSerializer.serialize("one", "two", "three");
        assertEquals("one.two.three", cs);
    }

    public void testSerialize2() throws JoseException
    {
        String cs = CompactSerializer.serialize("one", "two", "three", "four");
        assertEquals("one.two.three.four", cs);
    }

    public void testSerialize3() throws JoseException
    {
        String cs = CompactSerializer.serialize("one", "two", "three", null);
        assertEquals("one.two.three.", cs);
    }

    public void testSerialize4() throws JoseException
    {
        String cs = CompactSerializer.serialize("one", "two", "three", "");
        assertEquals("one.two.three.", cs);
    }

    public void testSerialize5() throws JoseException
    {
        String cs = CompactSerializer.serialize("one", null, "three", "four", "five");
        assertEquals("one..three.four.five", cs);
    }

    public void testSerialize6() throws JoseException
    {
        String cs = CompactSerializer.serialize("one", "", "three", "four", "five");
        assertEquals("one..three.four.five", cs);
    }

    public void testCalculateCapacity1()
    {
        int capacity = CompactSerializer.calculateCapacity("one", "two", "three");
        // "one" = 3, "two" = 3, "three" = 5, separators = 2 * 1 = 2
        // Total = 3 + 3 + 5 + 2 = 13
        assertEquals(13, capacity);
        String serialized = CompactSerializer.serialize("one", "two", "three");
        assertEquals(serialized.length(), capacity);
    }

    public void testCalculateCapacity2()
    {
        int capacity = CompactSerializer.calculateCapacity("abc", "def", "ghi", "jkl");
        // "abc" = 3, "def" = 3, "ghi" = 3, "jkl" = 3, separators = 3 * 1 = 3
        // Total = 3 + 3 + 3 + 3 + 3 = 15
        assertEquals(15, capacity);
        String serialized = CompactSerializer.serialize("abc", "def", "ghi", "jkl");
        assertEquals(serialized.length(), capacity);
    }

    public void testCalculateCapacityWithNulls()
    {
        int capacity = CompactSerializer.calculateCapacity("one", null, "three");
        // "one" = 3, null = 0, "three" = 5, separators = 2 * 1 = 2
        // Total = 3 + 0 + 5 + 2 = 10
        assertEquals(10, capacity);
        String serialized = CompactSerializer.serialize("one", null, "three");
        assertEquals(serialized.length(), capacity);
    }

    public void testCalculateCapacityWithEmptyStrings()
    {
        int capacity = CompactSerializer.calculateCapacity("one", "", "three");
        // "one" = 3, "" = 0, "three" = 5, separators = 2 * 1 = 2
        // Total = 3 + 0 + 5 + 2 = 10
        assertEquals(10, capacity);
        String serialized = CompactSerializer.serialize("one", "", "three");
        assertEquals(serialized.length(), capacity);
    }

    public void testCalculateCapacitySinglePart()
    {
        int capacity = CompactSerializer.calculateCapacity("onlypart");
        // "onlypart" = 8, no separators
        // Total = 8
        assertEquals(8, capacity);
        String serialized = CompactSerializer.serialize("onlypart");
        assertEquals(serialized.length(), capacity);
    }

    public void testCalculateCapacityEmptyArray()
    {
        int capacity = CompactSerializer.calculateCapacity();
        // No parts, no separators
        // Total = 0
        assertEquals(0, capacity);
        String serialized = CompactSerializer.serialize();
        assertEquals(serialized.length(), capacity);
    }

    public void testCalculateCapacityAllNulls()
    {
        int capacity = CompactSerializer.calculateCapacity(null, null, null);
        // All nulls = 0 + 0 + 0, separators = 2 * 1 = 2
        // Total = 0 + 0 + 0 + 2 = 2
        assertEquals(2, capacity);
        String serialized = CompactSerializer.serialize(null, null, null);
        assertEquals(serialized.length(), capacity);
    }
}
