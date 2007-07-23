package org.apache.maven.doxia.module.apt;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Stack;

import org.apache.maven.doxia.util.HtmlTools;
import org.apache.maven.doxia.sink.SinkAdapter;
import org.codehaus.plexus.util.StringUtils;

/**
 * APT Sink implementation.
 * @author eredmond
 * @plexus.component
 */
public class AptSink extends SinkAdapter
{
    private static final String EOL = System.getProperty( "line.separator" );
    private StringBuffer buffer;
    private StringBuffer tableCaptionBuffer;
    private String author;
    private String title;
    private String date;
    private boolean tableCaptionFlag;
    private boolean headerFlag;
    private boolean bufferFlag;
    private boolean itemFlag;
    private boolean verbatimFlag;
    private boolean boxed;
    private boolean gridFlag;
    private int cellCount;
    private PrintWriter writer;
    private int cellJustif[];
    private String rowLine;
    private String listNestingIndent;
    private Stack listStyles;

    /**
     * Constructor, initialize the variables.
     *
     * @param writer The writer to write the result.
     */
    public AptSink( Writer writer )
    {
        this.buffer = new StringBuffer();
        this.tableCaptionBuffer = new StringBuffer();
        this.writer = new PrintWriter( writer );
        this.listNestingIndent = "";
        this.listStyles = new Stack();
    }

    /**
     * Returns the buffer that holds the current text.
     *
     * @return A StringBuffer.
     */
    protected StringBuffer getBuffer()
    {
        return buffer;
    }

    /**
     * Used to determine whether we are in head mode.
     *
     * @param headFlag True for head mode.
     */
    protected void setHeadFlag( boolean headFlag )
    {
        this.headerFlag = headFlag;
    }

    /**
     * Reset all variables.
     */
    protected void resetState()
    {
        headerFlag = false;
        resetBuffer();
        itemFlag = false;
        verbatimFlag = false;
        cellCount = 0;
    }

    /**
     * Reset the StringBuffer.
     */
    protected void resetBuffer()
    {
        buffer = new StringBuffer();
    }

    /**
     * Reset the TableCaptionBuffer.
     */
    protected void resetTableCaptionBuffer()
    {
        tableCaptionBuffer = new StringBuffer();
    }

    /** {@inheritDoc} */
    public void head()
    {
        resetState();
        headerFlag = true;
    }

    /** {@inheritDoc} */
    public void head_()
    {
        headerFlag = false;

        write( " -----" + EOL );
        write( " " + title + EOL  );
        write( " -----" + EOL );
        write( " " + author + EOL  );
        write( " -----" + EOL );
        write( " " + date + EOL  );
        write( " -----" + EOL );
    }

    /** {@inheritDoc} */
    public void title_()
    {
        if ( buffer.length() > 0 )
        {
            title = buffer.toString();
            resetBuffer();
        }
    }

    /** {@inheritDoc} */
    public void author_()
    {
        if ( buffer.length() > 0 )
        {
            author = buffer.toString();
            resetBuffer();
        }
    }

    /** {@inheritDoc} */
    public void date_()
    {
        if ( buffer.length() > 0 )
        {
            date = buffer.toString();
            resetBuffer();
        }
    }

    /** {@inheritDoc} */
    public void section1_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void section2_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void section3_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void section4_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void section5_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void sectionTitle1()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void sectionTitle1_()
    {
        write( EOL + EOL );
    }

    /** {@inheritDoc} */
    public void sectionTitle2()
    {
        write( EOL + "*" );
    }

    /** {@inheritDoc} */
    public void sectionTitle2_()
    {
        write( EOL + EOL );
    }

    /** {@inheritDoc} */
    public void sectionTitle3()
    {
        write( EOL + "**" );
    }

    /** {@inheritDoc} */
    public void sectionTitle3_()
    {
        write( EOL + EOL );
    }

    /** {@inheritDoc} */
    public void sectionTitle4()
    {
        write( EOL + "***" );
    }

    /** {@inheritDoc} */
    public void sectionTitle4_()
    {
        write( EOL + EOL );
    }

    /** {@inheritDoc} */
    public void sectionTitle5()
    {
        write( EOL + "****" );
    }

    /** {@inheritDoc} */
    public void sectionTitle5_()
    {
        write( EOL + EOL );
    }

    /** {@inheritDoc} */
    public void list()
    {
        listNestingIndent += " ";
        listStyles.push( "*" );
        write( EOL );
    }

    /** {@inheritDoc} */
    public void list_()
    {
        if ( listNestingIndent.length() <= 1 )
        {
            write( EOL + listNestingIndent + "[]" + EOL );
        }
        else
        {
            write( EOL );
        }
        listNestingIndent = StringUtils.chomp( listNestingIndent, " " );
        listStyles.pop();
        itemFlag = false;
    }

    /** {@inheritDoc} */
    public void listItem()
    {
        //if ( !numberedList )
        //write( EOL + listNestingIndent + "*" );
        //else
        numberedListItem();
        itemFlag = true;
    }

    /** {@inheritDoc} */
    public void listItem_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void numberedList( int numbering )
    {
        listNestingIndent += " ";
        write( EOL );

        String style;
        switch ( numbering )
        {
            case NUMBERING_UPPER_ALPHA:
                style = "A";
                break;
            case NUMBERING_LOWER_ALPHA:
                style = "a";
                break;
            case NUMBERING_UPPER_ROMAN:
                style = "I";
                break;
            case NUMBERING_LOWER_ROMAN:
                style = "i";
                break;
            case NUMBERING_DECIMAL:
            default:
                style = "1";
        }

        listStyles.push( style );
    }

    /** {@inheritDoc} */
    public void numberedList_()
    {
        if ( listNestingIndent.length() <= 1 )
        {
            write( EOL + listNestingIndent + "[]" + EOL );
        }
        else
        {
            write( EOL );
        }
        listNestingIndent = StringUtils.chomp( listNestingIndent, " " );
        listStyles.pop();
        itemFlag = false;
    }

    /** {@inheritDoc} */
    public void numberedListItem()
    {
        String style = (String) listStyles.peek();
        if ( style == "*" )
        {
            write( EOL + listNestingIndent + "* " );
        }
        else
        {
            write( EOL + listNestingIndent + "[[" + style + "]] " );
        }
        itemFlag = true;
    }

    /** {@inheritDoc} */
    public void numberedListItem_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void definitionList()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void definitionList_()
    {
        write( EOL );
        itemFlag = false;
    }

    /** {@inheritDoc} */
    public void definedTerm()
    {
        write( EOL + " [" );
    }

    /** {@inheritDoc} */
    public void definedTerm_()
    {
        write( "]" );
    }

    /** {@inheritDoc} */
    public void definition()
    {
        itemFlag = true;
    }

    /** {@inheritDoc} */
    public void definition_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void pageBreak()
    {
        write( EOL + "\f" + EOL );
    }

    /** {@inheritDoc} */
    public void paragraph()
    {
        if ( !itemFlag )
        {
            write( EOL + " " );
        }
    }

    /** {@inheritDoc} */
    public void paragraph_()
    {
        if ( itemFlag )
        {
            itemFlag = false;
        }
        else
        {
            write( EOL + EOL );
        }
    }

    /** {@inheritDoc} */
    public void verbatim( boolean boxed )
    {
        verbatimFlag = true;
        this.boxed = boxed;
        if ( boxed )
        {
            write( "\n+------+\n" );
        }
        else
        {
            write( "\n------\n" );
        }
    }

    /** {@inheritDoc} */
    public void verbatim_()
    {
        if ( boxed )
        {
            write( "\n+------+\n" );
        }
        else
        {
            write( "\n------\n" );
        }
        boxed = false;
        verbatimFlag = false;
    }

    /** {@inheritDoc} */
    public void horizontalRule()
    {
        write( EOL + "========" + EOL );
    }

    /** {@inheritDoc} */
    public void table()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void table_()
    {
        if ( rowLine != null )
        {
            write( rowLine );
        }
        rowLine = null;

        if ( tableCaptionBuffer.length() > 0 )
        {
            text( tableCaptionBuffer.toString() + EOL );
        }

        resetTableCaptionBuffer();
    }

    /** {@inheritDoc} */
    public void tableRows( int justification[], boolean grid )
    {
        cellJustif = justification;
        gridFlag = grid;
    }

    /** {@inheritDoc} */
    public void tableRows_()
    {
        cellJustif = null;
        gridFlag = false;
    }

    /** {@inheritDoc} */
    public void tableRow()
    {
        bufferFlag = true;
        cellCount = 0;
    }

    /** {@inheritDoc} */
    public void tableRow_()
    {
        bufferFlag = false;

        // write out the header row first, then the data in the buffer
        buildRowLine();

        write( rowLine );

        // TODO: This will need to be more clever, for multi-line cells
        if ( gridFlag )
        {
            write( "|" );
        }

        write( buffer.toString() );

        resetBuffer();

        write( EOL );

        // only reset cell count if this is the last row
        cellCount = 0;
    }

    /** Construct a table row. */
    private void buildRowLine()
    {
        StringBuffer rowLine = new StringBuffer();
        rowLine.append( "*--" );

        for ( int i = 0; i < cellCount; i++ )
        {
            if ( cellJustif != null )
            {
                switch ( cellJustif[i] )
                {
                // TODO: use Parser constants
                case 1:
                    rowLine.append( "--+" );
                    break;
                case 2:
                    rowLine.append( "--:" );
                    break;
                default:
                    rowLine.append( "--*" );
                }
            }
            else
            {
                rowLine.append( "--*" );
            }
        }
        rowLine.append( EOL );

        this.rowLine = rowLine.toString();
    }

    /** {@inheritDoc} */
    public void tableCell_()
    {
        tableCell_( false );
    }

    /** {@inheritDoc} */
    public void tableHeaderCell_()
    {
        tableCell_( true );
    }

    /** {@inheritDoc} */
    public void tableCell_( boolean headerRow )
    {
        buffer.append( "|" );
        cellCount++;
    }

    /** {@inheritDoc} */
    public void tableCaption()
    {
        tableCaptionFlag = true;
    }

    /** {@inheritDoc} */
    public void tableCaption_()
    {
        tableCaptionFlag = false;
    }

    /** {@inheritDoc} */
    public void figureCaption_()
    {
        write( EOL );
    }

    /** {@inheritDoc} */
    public void figureGraphics( String name )
    {
        write( EOL + "[" + name + "] " );
    }

    /** {@inheritDoc} */
    public void anchor( String name )
    {
//        String id = HtmlTools.encodeId(name);
        write( "{" );
    }

    /** {@inheritDoc} */
    public void anchor_()
    {
        write( "}" );
    }

    /** {@inheritDoc} */
    public void link( String name )
    {
        if ( !headerFlag )
        {
            write( "{{{" );
            text( name );
            write( "}" );
        }
    }

    /** {@inheritDoc} */
    public void link_()
    {
        if ( !headerFlag )
        {
            write( "}}" );
        }
    }

    /** {@inheritDoc} */
    public void link( String name, String target )
    {
        if ( !headerFlag )
        {
            write( "{{{" );
            text( target );
            write( "}" );
            text( name );
        }
    }

    /** {@inheritDoc} */
    public void italic()
    {
        if ( !headerFlag )
        {
            write( "<" );
        }
    }

    /** {@inheritDoc} */
    public void italic_()
    {
        if ( !headerFlag )
        {
            write( ">" );
        }
    }

    /** {@inheritDoc} */
    public void bold()
    {
        if ( !headerFlag )
        {
            write( "<<" );
        }
    }

    /** {@inheritDoc} */
    public void bold_()
    {
        if ( !headerFlag )
        {
            write( ">>" );
        }
    }

    /** {@inheritDoc} */
    public void monospaced()
    {
        if ( !headerFlag )
        {
            write( "<<<" );
        }
    }

    /** {@inheritDoc} */
    public void monospaced_()
    {
        if ( !headerFlag )
        {
            write( ">>>" );
        }
    }

    /** {@inheritDoc} */
    public void lineBreak()
    {
        if ( headerFlag || bufferFlag )
        {
            buffer.append( EOL );
        }
        else
        {
            write( "\\" + EOL );
        }
    }

    /** {@inheritDoc} */
    public void nonBreakingSpace()
    {
        if ( headerFlag || bufferFlag )
        {
            buffer.append( "\\ " );
        }
        else
        {
            write( "\\ " );
        }
    }

    /** {@inheritDoc} */
    public void text( String text )
    {
        if ( tableCaptionFlag )
        {
            tableCaptionBuffer.append( text );
        }
        else if ( headerFlag || bufferFlag )
        {
            buffer.append( text );
        }
        else if ( verbatimFlag )
        {
            verbatimContent( text );
        }
        else
        {
            content( text );
        }
    }

    /** {@inheritDoc} */
    public void rawText( String text )
    {
        write( text );
    }

    /**
     * Write text to output.
     *
     * @param text The text to write.
     */
    protected void write( String text )
    {
        writer.write( text );
    }

    /**
     * Write Apt escaped text to output.
     *
     * @param text The text to write.
     */
    protected void content( String text )
    {
        write( escapeAPT( text ) );
    }

    /**
     * Write Apt escaped text to output.
     *
     * @param text The text to write.
     */
    protected void verbatimContent( String text )
    {
        write( escapeAPT( text ) );
    }

    /**
     * Forward to HtmlTools.encodeFragment( text ).
     *
     * @param text the String to fragment, may be null.
     * @return the fragmented text, null if null String input.
     * @see org.apache.maven.doxia.util.HtmlTools#encodeURL(String).
     */
    public static String encodeFragment( String text )
    {
        return HtmlTools.encodeFragment( text );
    }

    /**
     * Forward to HtmlTools.encodeURL( text ).
     *
     * @param text the String to encode, may be null.
     * @return the text encoded, null if null String input.
     * @see org.apache.maven.doxia.util.HtmlTools#encodeURL(String).
     */
    public static String encodeURL( String text )
    {
        return HtmlTools.encodeURL( text );
    }

    /** {@inheritDoc} */
    public void flush()
    {
        writer.flush();
    }

    /** {@inheritDoc} */
    public void close()
    {
        writer.close();
    }

    /**
     * Escape special characters in a text in APT:
     *
     * <pre>
     * \~, \=, \-, \+, \*, \[, \], \<, \>, \{, \}, \\
     * </pre>
     *
     * @param text the String to escape, may be null
     * @return the text escaped, "" if null String input
     */
    private static String escapeAPT( String text )
    {
        if ( text == null )
        {
            return "";
        }

        int length = text.length();
        StringBuffer buffer = new StringBuffer( length );

        for ( int i = 0; i < length; ++i )
        {
            char c = text.charAt( i );
            switch ( c )
            { // 0080
                case '\\':
                case '~':
                case '=':
                case '-':
                case '+':
                case '*':
                case '[':
                case ']':
                case '<':
                case '>':
                case '{':
                case '}':
                    buffer.append( '\\' );
                    buffer.append( c );
                    break;
                default:
                    if ( c > 127 )
                    {
                        buffer.append( "\\u" );
                        String hex = Integer.toHexString( c );
                        if ( hex.length() == 2 )
                        {
                            buffer.append( "00" );
                        }
                        else if ( hex.length() == 3 )
                        {
                            buffer.append( "0" );
                        }
                        buffer.append( hex );
                    }
                    else
                    {
                        buffer.append( c );
                    }
            }
        }

        return buffer.toString();
    }
}
