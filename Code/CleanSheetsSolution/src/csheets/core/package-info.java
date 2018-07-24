/**

<p>Provides the core classes for the application's data model, i.e. workbooks, 
spreadsheets, cells, addresses and values.

<p>A <code>Workbook</code> consists of a number of <code>Spreadsheet</code>s,
each of which consist of a grid of <code>Cell</code>s at different unique
<code>Address</code>es. Each <code>Cell</code>s has content, which can be
interpreted as a <code>Formula</code>. <code>Cell</code>s also have
<code>Value</code>s, which can be of any of a number of pre-defined types.
Listener interfaces are provided for receiving notification of important events
in the data model.

<p>In order to enable extensibility, interfaces are specified separately for
<code>Cell</code> and <code>Spreadsheet</code>.

*/
package csheets.core;