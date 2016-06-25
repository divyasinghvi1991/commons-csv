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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.RFC4180;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @version $Id$
 */
public class CSVFormatTest {

    private static void assertNotEquals(final Object right, final Object left) {
		System.out.print("CSVFormatTest - assertNotEquals");
        assertFalse(right.equals(left));
        assertFalse(left.equals(right));
    }

    private static CSVFormat copy(final CSVFormat format) {
		System.out.print("CSVFormatTest - copy");
        return format.withDelimiter(format.getDelimiter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelimiterSameAsCommentStartThrowsException() {
		System.out.print("CSVFormatTest - testDelimiterSameAsCommentStartThrowsException");
        CSVFormat.DEFAULT.withDelimiter('!').withCommentMarker('!');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelimiterSameAsEscapeThrowsException() {
		System.out.print("CSVFormatTest - testDelimiterSameAsEscapeThrowsException");
        CSVFormat.DEFAULT.withDelimiter('!').withEscape('!');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateHeaderElements() {
		System.out.print("CSVFormatTest - testDuplicateHeaderElements");
        CSVFormat.DEFAULT.withHeader("A", "A");
    }

    @Test
    public void testEquals() {
		System.out.print("CSVFormatTest - testEquals");
        final CSVFormat right = CSVFormat.DEFAULT;
        final CSVFormat left = copy(right);

        assertFalse(right.equals(null));
        assertFalse(right.equals("A String Instance"));

        assertEquals(right, right);
        assertEquals(right, left);
        assertEquals(left, right);

        assertEquals(right.hashCode(), right.hashCode());
        assertEquals(right.hashCode(), left.hashCode());
    }

    @Test
    public void testEqualsCommentStart() {
		System.out.print("CSVFormatTest - testEqualsCommentStart");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withQuote('"')
                .withCommentMarker('#')
                .withQuoteMode(QuoteMode.ALL);
        final CSVFormat left = right
                .withCommentMarker('!');

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsDelimiter() {
		System.out.print("CSVFormatTest - testEqualsDelimiter");
        final CSVFormat right = CSVFormat.newFormat('!');
        final CSVFormat left = CSVFormat.newFormat('?');

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsEscape() {
		System.out.print("CSVFormatTest - testEqualsEscape");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withQuote('"')
                .withCommentMarker('#')
                .withEscape('+')
                .withQuoteMode(QuoteMode.ALL);
        final CSVFormat left = right
                .withEscape('!');

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsHeader() {
		System.out.print("CSVFormatTest - testEqualsHeader");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withRecordSeparator(CR)
                .withCommentMarker('#')
                .withEscape('+')
                .withHeader("One", "Two", "Three")
                .withIgnoreEmptyLines()
                .withIgnoreSurroundingSpaces()
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL);
        final CSVFormat left = right
                .withHeader("Three", "Two", "One");

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsIgnoreEmptyLines() {
		System.out.print("CSVFormatTest - testEqualsIgnoreEmptyLines");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withCommentMarker('#')
                .withEscape('+')
                .withIgnoreEmptyLines()
                .withIgnoreSurroundingSpaces()
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL);
        final CSVFormat left = right
                .withIgnoreEmptyLines(false);

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsIgnoreSurroundingSpaces() {
		System.out.print("CSVFormatTest - testEqualsIgnoreSurroundingSpaces");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withCommentMarker('#')
                .withEscape('+')
                .withIgnoreSurroundingSpaces()
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL);
        final CSVFormat left = right
                .withIgnoreSurroundingSpaces(false);

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsQuoteChar() {
		System.out.print("CSVFormatTest - testEqualsQuoteChar");
        final CSVFormat right = CSVFormat.newFormat('\'').withQuote('"');
        final CSVFormat left = right.withQuote('!');

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsQuotePolicy() {
		System.out.print("CSVFormatTest - testEqualsQuotePolicy");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL);
        final CSVFormat left = right
                .withQuoteMode(QuoteMode.MINIMAL);

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsRecordSeparator() {
		System.out.print("CSVFormatTest - testEqualsRecordSeparator");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withRecordSeparator(CR)
                .withCommentMarker('#')
                .withEscape('+')
                .withIgnoreEmptyLines()
                .withIgnoreSurroundingSpaces()
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL);
        final CSVFormat left = right
                .withRecordSeparator(LF);

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsNullString() {
		System.out.print("CSVFormatTest - testEqualsNullString");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withRecordSeparator(CR)
                .withCommentMarker('#')
                .withEscape('+')
                .withIgnoreEmptyLines()
                .withIgnoreSurroundingSpaces()
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL)
                .withNullString("null");
        final CSVFormat left = right
                .withNullString("---");

        assertNotEquals(right, left);
    }

    @Test
    public void testEqualsSkipHeaderRecord() {
		System.out.print("CSVFormatTest - testEqualsSkipHeaderRecord");
        final CSVFormat right = CSVFormat.newFormat('\'')
                .withRecordSeparator(CR)
                .withCommentMarker('#')
                .withEscape('+')
                .withIgnoreEmptyLines()
                .withIgnoreSurroundingSpaces()
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL)
                .withNullString("null")
                .withSkipHeaderRecord();
        final CSVFormat left = right
                .withSkipHeaderRecord(false);

        assertNotEquals(right, left);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeSameAsCommentStartThrowsException() {
		System.out.print("CSVFormatTest - testEscapeSameAsCommentStartThrowsException");
        CSVFormat.DEFAULT.withEscape('!').withCommentMarker('!');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeSameAsCommentStartThrowsExceptionForWrapperType() {
		System.out.print("CSVFormatTest - testEscapeSameAsCommentStartThrowsExceptionForWrapperType");
        // Cannot assume that callers won't use different Character objects
        CSVFormat.DEFAULT.withEscape(new Character('!')).withCommentMarker(new Character('!'));
    }

    @Test
    public void testFormat() {
		System.out.print("CSVFormatTest - testFormat");
        final CSVFormat format = CSVFormat.DEFAULT;

        assertEquals("", format.format());
        assertEquals("a,b,c", format.format("a", "b", "c"));
        assertEquals("\"x,y\",z", format.format("x,y", "z"));
    }

    @Test
    public void testGetHeader() throws Exception {
		System.out.print("CSVFormatTest - testGetHeader");
        final String[] header = new String[]{"one", "two", "three"};
        final CSVFormat formatWithHeader = CSVFormat.DEFAULT.withHeader(header);
        // getHeader() makes a copy of the header array.
        final String[] headerCopy = formatWithHeader.getHeader();
        headerCopy[0] = "A";
        headerCopy[1] = "B";
        headerCopy[2] = "C";
        assertFalse(Arrays.equals(formatWithHeader.getHeader(), headerCopy));
        assertNotSame(formatWithHeader.getHeader(), headerCopy);
    }

    @Test
    public void testNullRecordSeparatorCsv106() {
		System.out.print("CSVFormatTest - testNullRecordSeparatorCsv106");
        final CSVFormat format = CSVFormat.newFormat(';').withSkipHeaderRecord().withHeader("H1", "H2");
        final String formatStr = format.format("A", "B");
        assertNotNull(formatStr);
        assertFalse(formatStr.endsWith("null"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQuoteCharSameAsCommentStartThrowsException() {
		System.out.print("CSVFormatTest - testQuoteCharSameAsCommentStartThrowsException");
        CSVFormat.DEFAULT.withQuote('!').withCommentMarker('!');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQuoteCharSameAsCommentStartThrowsExceptionForWrapperType() {
        // Cannot assume that callers won't use different Character objects
		System.out.print("CSVFormatTest - testQuoteCharSameAsCommentStartThrowsExceptionForWrapperType");
        CSVFormat.DEFAULT.withQuote(new Character('!')).withCommentMarker('!');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQuoteCharSameAsDelimiterThrowsException() {
		System.out.print("CSVFormatTest - testQuoteCharSameAsDelimiterThrowsException");
        CSVFormat.DEFAULT.withQuote('!').withDelimiter('!');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQuotePolicyNoneWithoutEscapeThrowsException() {
		System.out.print("CSVFormatTest - testQuotePolicyNoneWithoutEscapeThrowsException");
        CSVFormat.newFormat('!').withQuoteMode(QuoteMode.NONE);
    }

    @Test
    public void testRFC4180() {
		System.out.print("CSVFormatTest - testRFC4180");
        assertEquals(null, RFC4180.getCommentMarker());
        assertEquals(',', RFC4180.getDelimiter());
        assertEquals(null, RFC4180.getEscapeCharacter());
        assertFalse(RFC4180.getIgnoreEmptyLines());
        assertEquals(Character.valueOf('"'), RFC4180.getQuoteCharacter());
        assertEquals(null, RFC4180.getQuoteMode());
        assertEquals("\r\n", RFC4180.getRecordSeparator());
    }

    @SuppressWarnings("boxing") // no need to worry about boxing here
    @Test
    public void testSerialization() throws Exception {
		System.out.print("CSVFormatTest - testSerialization");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(CSVFormat.DEFAULT);
        oos.flush();
        oos.close();

        final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        final CSVFormat format = (CSVFormat) in.readObject();

        assertNotNull(format);
        assertEquals("delimiter", CSVFormat.DEFAULT.getDelimiter(), format.getDelimiter());
        assertEquals("encapsulator", CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals("comment start", CSVFormat.DEFAULT.getCommentMarker(), format.getCommentMarker());
        assertEquals("record separator", CSVFormat.DEFAULT.getRecordSeparator(), format.getRecordSeparator());
        assertEquals("escape", CSVFormat.DEFAULT.getEscapeCharacter(), format.getEscapeCharacter());
        assertEquals("trim", CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), format.getIgnoreSurroundingSpaces());
        assertEquals("empty lines", CSVFormat.DEFAULT.getIgnoreEmptyLines(), format.getIgnoreEmptyLines());
    }

    @Test
    public void testWithCommentStart() throws Exception {
		System.out.print("CSVFormatTest - testWithCommentStart");
        final CSVFormat formatWithCommentStart = CSVFormat.DEFAULT.withCommentMarker('#');
        assertEquals( Character.valueOf('#'), formatWithCommentStart.getCommentMarker());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithCommentStartCRThrowsException() {
		System.out.print("CSVFormatTest - testWithCommentStartCRThrowsException");
        CSVFormat.DEFAULT.withCommentMarker(CR);
    }

    @Test
    public void testWithDelimiter() throws Exception {
		System.out.print("CSVFormatTest - testWithDelimiter");
        final CSVFormat formatWithDelimiter = CSVFormat.DEFAULT.withDelimiter('!');
        assertEquals('!', formatWithDelimiter.getDelimiter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithDelimiterLFThrowsException() {
		System.out.print("CSVFormatTest - testWithDelimiterLFThrowsException");
        CSVFormat.DEFAULT.withDelimiter(LF);
    }

    @Test
    public void testWithEscape() throws Exception {
		System.out.print("CSVFormatTest - testWithEscape");
        final CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('&');
        assertEquals(Character.valueOf('&'), formatWithEscape.getEscapeCharacter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEscapeCRThrowsExceptions() {
		System.out.print("CSVFormatTest - testWithEscapeCRThrowsExceptions");
        CSVFormat.DEFAULT.withEscape(CR);
    }

    @Test
    public void testWithHeader() throws Exception {
		System.out.print("CSVFormatTest - testWithHeader");
        final String[] header = new String[]{"one", "two", "three"};
        // withHeader() makes a copy of the header array.
        final CSVFormat formatWithHeader = CSVFormat.DEFAULT.withHeader(header);
        assertArrayEquals(header, formatWithHeader.getHeader());
        assertNotSame(header, formatWithHeader.getHeader());
        header[0] = "A";
        header[1] = "B";
        header[2] = "C";
        assertFalse(Arrays.equals(formatWithHeader.getHeader(), header));
    }

    @Test
    public void testJiraCsv154_withCommentMarker() throws IOException {
		System.out.print("CSVFormatTest - testJiraCsv154_withCommentMarker");
        final String comment = "This is a header comment";
        CSVFormat format = CSVFormat.EXCEL.withHeader("H1", "H2").withCommentMarker('#').withHeaderComments(comment);
        StringBuilder out = new StringBuilder();
        final CSVPrinter printer = format.print(out);
        printer.print("A");
        printer.print("B");
        printer.close();
        String s = out.toString();
        Assert.assertTrue(s, s.contains(comment));
    }

    @Test
    public void testJiraCsv154_withHeaderComments() throws IOException {
		System.out.print("CSVFormatTest - testJiraCsv154_withHeaderComments");
        final String comment = "This is a header comment";
        CSVFormat format = CSVFormat.EXCEL.withHeader("H1", "H2").withHeaderComments(comment).withCommentMarker('#');
        StringBuilder out = new StringBuilder();
        final CSVPrinter printer = format.print(out);
        printer.print("A");
        printer.print("B");
        printer.close();
        String s = out.toString();
        Assert.assertTrue(s, s.contains(comment));
    }
    
    @Test
    public void testWithIgnoreEmptyLines() throws Exception {
		System.out.print("CSVFormatTest - testWithIgnoreEmptyLines");
        assertFalse(CSVFormat.DEFAULT.withIgnoreEmptyLines(false).getIgnoreEmptyLines());
        assertTrue(CSVFormat.DEFAULT.withIgnoreEmptyLines().getIgnoreEmptyLines());
    }

    @Test
    public void testWithIgnoreSurround() throws Exception {
		System.out.print("CSVFormatTest - testWithIgnoreSurround");
        assertFalse(CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false).getIgnoreSurroundingSpaces());
        assertTrue(CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().getIgnoreSurroundingSpaces());
    }

    @Test
    public void testWithNullString() throws Exception {
		System.out.print("CSVFormatTest - testWithNullString");
        final CSVFormat formatWithNullString = CSVFormat.DEFAULT.withNullString("null");
        assertEquals("null", formatWithNullString.getNullString());
    }

    @Test
    public void testWithQuoteChar() throws Exception {
		System.out.print("CSVFormatTest - testWithQuoteChar");
        final CSVFormat formatWithQuoteChar = CSVFormat.DEFAULT.withQuote('"');
        assertEquals(Character.valueOf('"'), formatWithQuoteChar.getQuoteCharacter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithQuoteLFThrowsException() {
		System.out.print("CSVFormatTest - testWithQuoteLFThrowsException");
        CSVFormat.DEFAULT.withQuote(LF);
    }

    @Test
    public void testWithQuotePolicy() throws Exception {
		System.out.print("CSVFormatTest - testWithQuotePolicy");
        final CSVFormat formatWithQuotePolicy = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        assertEquals(QuoteMode.ALL, formatWithQuotePolicy.getQuoteMode());
    }

    @Test
    public void testWithRecordSeparatorCR() throws Exception {
		System.out.print("CSVFormatTest - testWithRecordSeparatorCR");
        final CSVFormat formatWithRecordSeparator = CSVFormat.DEFAULT.withRecordSeparator(CR);
        assertEquals(String.valueOf(CR), formatWithRecordSeparator.getRecordSeparator());
    }

    @Test
    public void testWithRecordSeparatorLF() throws Exception {
		System.out.print("CSVFormatTest - testWithRecordSeparatorLF");
        final CSVFormat formatWithRecordSeparator = CSVFormat.DEFAULT.withRecordSeparator(LF);
        assertEquals(String.valueOf(LF), formatWithRecordSeparator.getRecordSeparator());
    }

    @Test
    public void testWithRecordSeparatorCRLF() throws Exception {
		System.out.print("CSVFormatTest - testWithRecordSeparatorCRLF");
        final CSVFormat formatWithRecordSeparator = CSVFormat.DEFAULT.withRecordSeparator(CRLF);
        assertEquals(CRLF, formatWithRecordSeparator.getRecordSeparator());
    }
}
