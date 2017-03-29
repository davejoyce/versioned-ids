/*
 *  Copyright 2017 David Joyce
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.davejoyce.id;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Unit tests of {@code NamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NamespaceIdTest extends AbstractIdTest {

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodNamespaceIdStringIterator",
          groups = "id")
    public void testFromStringGood(String nsIdString,
                                   NamespaceId<String> expected) throws Exception {
        NamespaceId<String> actual = NamespaceId.fromString(nsIdString);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public void testFromStringBad(String nsIdString) throws Exception {
        NamespaceId.fromString(nsIdString);
        fail("Expected exception on parse of: " + nsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodNamespaceIdStringIteratorWithType",
          groups = "id")
    public <T extends Comparable<T>> void testFromStringWithTypeGood(String nsIdString,
                                                                     Class<T> idType,
                                                                     NamespaceId<T> expected) throws Exception {
        NamespaceId<T> actual = NamespaceId.fromString(nsIdString, idType);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public <T extends Comparable<T>> void testFromStringWithTypeBad(String nsIdString,
                                                                    Class<T> idType) throws Exception {
        NamespaceId.fromString(nsIdString, idType);
        fail("Expected exception on parse of: " + nsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodGetterDataIterator",
          groups = "id")
    public void testGetNamespace(NamespaceId<?> nsId) throws Exception {
        assertNotNull(nsId.getNamespace());
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodGetterDataIterator",
          groups = "id")
    public void testGetId(NamespaceId<?> nsId) throws Exception {
        assertNotNull(nsId.getId());
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodToStringDataIterator",
          groups = "id")
    public void testToString(NamespaceId<?> nsId, String expected) throws Exception {
        String actual = nsId.toString();
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] compareToData() {
        final NamespaceId<Integer> nsId = new NamespaceId<>("identity", 10);
        return new Object[][] {
                new Object[]{new NamespaceId<Integer>("namespace", 1), new NamespaceId<Integer>("namespace", 2), BEFORE},
                new Object[]{new NamespaceId<Integer>("apples", 1), new NamespaceId<Integer>("bananas", 1), BEFORE},
                new Object[]{new NamespaceId<Integer>("namespace", 2), new NamespaceId<Integer>("namespace", 1), AFTER},
                new Object[]{new NamespaceId<Integer>("bananas", 1), new NamespaceId<Integer>("apples", 1), AFTER},
                new Object[]{new NamespaceId<Integer>("namespace", 2), new NamespaceId<Integer>("namespace", 2), EQUAL},
                new Object[]{nsId, nsId, EQUAL},
        };
    }

    @DataProvider
    public Object[][] equalityData() {
        final NamespaceId<Integer> nsId = new NamespaceId<>("identity", 10);
        return new Object[][] {
                new Object[]{new NamespaceId<Integer>("namespace", 1), new NamespaceId<Integer>("namespace", 1), true},
                new Object[]{new NamespaceId<Float>("namespace", 3.141592F), new NamespaceId<Float>("namespace", 3.141592F), true},
                new Object[]{new NamespaceId<String>("namespace", "id"), new NamespaceId<String>("namespace", "ID"), false},
                new Object[]{new NamespaceId<Integer>("namespace", 1), null, false},
                new Object[]{nsId, nsId, true},
        };
    }

}