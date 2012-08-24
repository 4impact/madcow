/*
 * Copyright 2012 4impact, Brisbane, Australia
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package au.com.ps4impact.madcow.grass;

/**
 * Test class for ParseUtils.
 *
 * @author Gavin Bunney
 */
class ParseUtilsTest extends GroovyTestCase {

    public void testUnquote() {
        assertEquals('Queensland', ParseUtils.unquote("""'Queensland'"""));
        assertEquals('Queensland', ParseUtils.unquote("""\"Queensland\""""));
        assertEquals('Queensland', ParseUtils.unquote('\'Queensland\''));

        assertEquals('Queensland', ParseUtils.unquote('"""Queensland"""'));
        assertEquals('Queensland', ParseUtils.unquote('"Queensland"'));

        assertEquals('Queensland', ParseUtils.unquote("Queensland"));
    }

    public void testUnquoteNotBalanced() {
        assertEquals('\'Queensland"', ParseUtils.unquote("'Queensland\""));
        assertEquals('"Queensland\'', ParseUtils.unquote("\"Queensland'"));
        assertEquals('"""Queensland\'', ParseUtils.unquote("\"\"\"Queensland'"));
    }

    public void testEvalMeStrings() {
        assertEquals('Queensland', ParseUtils.evalMe('Queensland'));
        assertEquals('Queensland', ParseUtils.evalMe("Queensland"));
        assertEquals('Queensland', ParseUtils.evalMe("""Queensland"""));
        assertEquals('Queen\'sland', ParseUtils.evalMe("Queen'sland"));

        assertEquals('"Queensland\'', ParseUtils.evalMe("\"Queensland'"));
        assertEquals('Queensland', ParseUtils.evalMe("'Queensland'"));
    }

    public void testEvalMeLists() {
        assertEquals(['Queensland', 'Victoria'], ParseUtils.evalMe("['Queensland', 'Victoria']"));
        assertEquals(['Queen\'sland', 'Victoria'], ParseUtils.evalMe("['Queen\\'sland', 'Victoria']"));
    }

    public void testEvalMeMaps() {
        assertEquals([state: 'Queensland'], ParseUtils.evalMe("[state: 'Queensland']"));
        assertEquals([state: 'Queensland', city: 'Brisbane'], ParseUtils.evalMe("[state: 'Queensland', city: 'Brisbane']"));
    }

    public void testEvalMeListOfMap() {
        assertEquals([[state: 'Queensland'], [state: 'Victoria']], ParseUtils.evalMe("[[state: 'Queensland'], [state: 'Victoria']]"));
    }

    public void testEvalMeMapOfLists() {
        assertEquals([states: ['Queensland', 'Victoria']], ParseUtils.evalMe("[states: ['Queensland', 'Victoria']]"));
    }

    /**
     * Test to prove that not all valid groovy code will be evaluated - all treated as strings.
     */
    public void testEvalMeGroovyCode() {
        assertEquals('{ new String(\'tent\') }', ParseUtils.evalMe("{ new String('tent') }"));
        assertEquals('2 + 2', ParseUtils.evalMe("2 + 2"));
        assertEquals('2\' + \'2', ParseUtils.evalMe("'2' + '2'"));
    }
}
